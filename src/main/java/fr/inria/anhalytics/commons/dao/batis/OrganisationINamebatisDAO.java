package fr.inria.anhalytics.commons.dao.batis;

import fr.inria.anhalytics.commons.entities.Organisation;
import fr.inria.anhalytics.commons.entities.Organisation_Identifier;
import fr.inria.anhalytics.commons.entities.Organisation_Name;
import fr.inria.anhalytics.commons.entities.PART_OF;

import java.util.List;

/**
 * Created by lfoppiano on 18/01/17.
 */
public interface OrganisationINamebatisDAO {

    //INSERTS
    long insertOrganisation(String type, String url, String status);

    long insertOrganisationName(String organisationID, String name, String lastUpdate);

    long insertMotherOrganisation(String organisationMotherID, String organisationID,
                                  String fromDate, String toDate);

    long insertOrganisationIdentifier(String organisationID, String ID, String type);


    //UPDATE

    void updateOrganisationPartOfEndDate(String organisationID, String organisationMotherID, String newDate);

    //SELECT

    Organisation getOrganisationById(long organisationID);

    List<Organisation_Name> getOrganisationNamesByOrganisationId(long organisationID);

    List<Organisation_Identifier> getOrganisationIdentifiersByOrganisationId(long organisationID);

    List<Organisation> getAllOrganisations();

    List<Organisation> getOrganisationByPersonID(String personID);

    List<Organisation> getOrganisationByDocumentID(String documentID);

    List<PART_OF> getPartOfByOrganisationMotherID(String organisationMotherID);

    
}
