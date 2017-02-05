package fr.inria.anhalytics.commons.dao.batis;

import fr.inria.anhalytics.commons.entities.Organisation;
import fr.inria.anhalytics.commons.entities.Organisation_Identifier;
import fr.inria.anhalytics.commons.entities.Organisation_Name;
import fr.inria.anhalytics.commons.entities.PART_OF;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.util.List;

/**
 * Created by lfoppiano on 18/01/17.
 */
public interface OrganisationIbatisDAO {

    //INSERTS
    long insertOrganisation(Organisation organisation);

    long insertOrganisationName(long organisationID, String name, Date lastUpdate);

    long insertMotherOrganisation(long organisationMotherID, long organisationID,
                                  Date fromDate, Date toDate);

    long insertOrganisationIdentifier(@Param("organisationID") long organisationID,
                                      @Param("ID") String ID, @Param("type") String type);


    //UPDATE
    void updateOrganisation(long organisationID, String type, String url, String status);

    void updateOrganisationNameLastUpdate(long organisationNameID, Date date);

    void updateOrganisationPartOfEndDate(long organisationID, long organisationMotherID, String newDate);

    void updateRelationEndDate(long organisationID, long organisationMotherID, Date date);

    void updatePartOfDates(Date fromDate, Date untilDate, long organisationID, long organisationMotherID);

    //SELECT

    Organisation getOrganisationByID(long organisationID);

    List<Organisation_Name> getOrganisationNamesByOrganisationId(long organisationID);

    List<Organisation_Identifier> getOrganisationIdentifiersByOrganisationID(long organisationID);

    List<Organisation> getAllOrganisations();

    List<Organisation> getOrganisationByPersonID(String personID);

    List<Organisation> getOrganisationByDocumentID(String documentID);


    List<PART_OF> getPartOfByOrganisationMotherID(String organisationMotherID);

    PART_OF getPartOfByOrgIDANDOrgMotherID(long organisationID, long organisationMotherID);

    List<Organisation> getMotherOrganisationByOrganisationID(long organisationID);

}
