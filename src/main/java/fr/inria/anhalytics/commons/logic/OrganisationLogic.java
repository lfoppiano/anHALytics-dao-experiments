package fr.inria.anhalytics.commons.logic;

import fr.inria.anhalytics.commons.dao.DAO;
import fr.inria.anhalytics.commons.dao.batis.OrganisationIbatisDAO;
import fr.inria.anhalytics.commons.entities.Organisation;
import fr.inria.anhalytics.commons.entities.Organisation_Identifier;
import fr.inria.anhalytics.commons.entities.Organisation_Name;
import fr.inria.anhalytics.commons.entities.PART_OF;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by lfoppiano on 27/01/17.
 */
public class OrganisationLogic extends DAO<Organisation, Long> {
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    SqlSessionFactory sqlSessionFactory = null;

    public OrganisationLogic() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    @Override
    public boolean create(Organisation obj) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            OrganisationIbatisDAO organisationMapper = session.getMapper(OrganisationIbatisDAO.class);

            if(organisationMapper.getOrganisationByID(obj.getOrganisationId()) != null) {
                throw new RuntimeException("The organisation already exists");
            }
            organisationMapper.insertOrganisation(obj);

            for (Organisation_Name name : obj.getNames()) {
                java.sql.Date updateDate = getDate(name.getLastupdate_date());
                organisationMapper.insertOrganisationName(obj.getOrganisationId(), name.getName(), updateDate);
            }

            for (PART_OF relation : obj.getRels()) {
                final Long organisationMotherID = relation.getOrganisation_mother().getOrganisationId();
                PART_OF relationOppositeDirection = organisationMapper.getPartOfByOrgIDANDOrgMotherID(
                        organisationMotherID,
                        obj.getOrganisationId());

                if (relationOppositeDirection == null) {
                    organisationMapper.insertMotherOrganisation(
                            organisationMotherID, obj.getOrganisationId(),
                            getDate(relation.getFromDate()), getDate(relation.getUntilDate()));
                }
            }

            for (Organisation_Identifier oi : obj.getOrganisation_identifiers()) {
                organisationMapper.insertOrganisationIdentifier(
                        obj.getOrganisationId(), oi.getId(), oi.getType()
                );
            }

        } finally {
            session.close();
        }

        return true;
    }

    private java.sql.Date getDate(Date date) {
        return date == null ?
                new java.sql.Date(00000000L) :
                new java.sql.Date(date.getTime());
    }

    private Date getEarlierDate(Date dateBefore, Date dateAfter) {
        if (dateAfter.before(dateBefore)) {
            return dateBefore;
        }
        return dateAfter;
    }

    private Date getLatestDate(Date dateBefore, Date dateAfter) {
        final java.sql.Date before = getDate(dateBefore);
        final java.sql.Date after = getDate(dateAfter);
        if (before.after(after)) {
            return before;
        }
        return after;
    }

    @Override
    public boolean delete(Organisation obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean update(Organisation organisation) {
        SqlSession session = sqlSessionFactory.openSession();
        boolean result = false;
        try {
            OrganisationIbatisDAO organisationMapper = session.getMapper(OrganisationIbatisDAO.class);

            //update organisation
            final Long organisationID = organisation.getOrganisationId();
            organisationMapper.updateOrganisation(organisationID, organisation.getType(), organisation.getUrl(), organisation.getStatus());
            List<Organisation_Name> names = organisationMapper.getOrganisationNamesByOrganisationId(organisationID);

            //update names
            for (Organisation_Name newName : organisation.getNames()) {
                if (names.contains(newName)) {  //Update the date of the name if database coming after the new record
                    Organisation_Name existingName = names.get(names.indexOf(newName));
                    if (newName.getLastupdate_date().after(existingName.getLastupdate_date())) {
                        organisationMapper.updateOrganisationNameLastUpdate(existingName.getOrganisation_nameid(),
                                getDate(newName.getLastupdate_date()));
                    }
                } else {
                    organisationMapper.insertOrganisationName(organisationID, newName.getName(), getDate(newName.getLastupdate_date()));
                }
            }

            //update relationships
            for (PART_OF relationship : organisation.getRels()) {
                PART_OF partOF = organisationMapper.getPartOfByOrgIDANDOrgMotherID(organisationID, relationship.getOrganisation_mother().getOrganisationId());

                if (partOF != null) {
                    partOF.setFromDate(getEarlierDate(partOF.getFromDate(), relationship.getFromDate()));
                    partOF.setUntilDate(getLatestDate(partOF.getUntilDate(), relationship.getUntilDate()));

                    organisationMapper.updatePartOfDates(new java.sql.Date(partOF.getFromDate().getTime()), new java.sql.Date(partOF.getUntilDate().getTime()), organisationID, partOF.getOrganisation_mother().getOrganisationId());
                } else {
//                    PART_OF partOFInverted = organisationMapper.getPartOfByOrgIDANDOrgMotherID(relationship.getOrganisation_mother().getOrganisationId(), organisationID);
//                    if(partOFInverted != null) {
//
//                    }
                    organisationMapper.insertMotherOrganisation(
                            relationship.getOrganisation_mother().getOrganisationId(),
                            organisationID,
                            getDate(relationship.getFromDate()),
                            getDate(relationship.getUntilDate()));
                }
            }

            for (Organisation_Identifier identifier : organisation.getOrganisation_identifiers()) {
                organisationMapper.insertOrganisationIdentifier(organisationID, identifier.getId(), identifier.getType());
            }

        } finally {
            session.close();
        }

        result = true;

        return result;
    }

    @Override
    public Organisation find(Long id) {
        SqlSession session = sqlSessionFactory.openSession();
        Organisation organisation = null;
        try {
            OrganisationIbatisDAO organisationMapper = session.getMapper(OrganisationIbatisDAO.class);

            // This can be done with a single query and three JOIN
            organisation = organisationMapper.getOrganisationByID(id);
            organisation.getNames().addAll(organisationMapper.getOrganisationNamesByOrganisationId(id));
            organisation.getOrganisation_identifiers().addAll(organisationMapper.getOrganisationIdentifiersByOrganisationId(id));
            List<Organisation> mothers = organisationMapper.getMotherOrganisationByOrganisationID(id);

            for (Organisation mother : mothers) {
                PART_OF partOf = new PART_OF();
                partOf.setOrganisation_mother(mother);
                organisation.addRel(partOf);
            }
        } finally {
            session.close();
        }

        return organisation;
    }
}
