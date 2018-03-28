package fr.inria.anhalytics.commons.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Organisation implements Serializable {

    private Long organisationId;
    private String type = "";
    private String url = "";
    private String status = "";
    private List<Organisation_Name> names = null;
    private List<PART_OF> rels = null;
    private List<Organisation_Identifier> organisation_identifiers = null;
    private String source = "";

    public Organisation() {
    }

    public Organisation(Long organisationId, String type, String status, String url, List<Organisation_Name> names, List<PART_OF> rels, List<Organisation_Identifier> organisation_identifiers, String source) {
        this.organisationId = organisationId;
        this.type = type;
        this.url = url;
        this.names = names;
        this.status = status;
        this.rels = rels;
        this.organisation_identifiers = organisation_identifiers;
        this.source = source;
    }

    public Long getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(Long organisationId) {
        this.organisationId = organisationId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (type.length() > 45) {
            type = type.substring(0, 44);
        }
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        if (url.length() > 255) {
            url = url.substring(0, 254);
        }
        this.url = url;
    }

//    /**
//     * @return the structure
//     */
//    public String getStructure() {
//        return structure;
//    }
//
//    /**
//     * @param structure the structure to set
//     */
//    public void setStructure(String structure) {
//        if (structure.length() > 45) {
//            structure = structure.substring(0, 44);
//        }
//        this.structure = structure;
//    }

    public List<Organisation_Name> getNames() {
        if (this.names == null) {
            this.names = new ArrayList<Organisation_Name>();
        }
        return names;
    }

    public void addName(Organisation_Name name) {
        if (this.names == null) {
            this.names = new ArrayList<Organisation_Name>();
        }
        if (name.getName().length() > 150) {
            name.setName(name.getName().substring(0, 149));
        }
        this.names.add(name);
    }

    public List<PART_OF> getRels() {
        if (this.rels == null) {
            this.rels = new ArrayList<PART_OF>();
        }
        return rels;
    }

    public void addRel(PART_OF rel) {
        if (this.rels == null) {
            this.rels = new ArrayList<PART_OF>();
        }
        this.rels.add(rel);
    }

    public Map<String, Object> getOrganisationDocument() {
        Map<String, Object> organisationDocument = new HashMap<String, Object>();
        List<Map<String, Object>> organisationNamesDocument = new ArrayList<Map<String, Object>>();
        organisationDocument.put("organisationId", this.getOrganisationId());
        List<Map<String, Object>> organisationIdentifiersDocument = new ArrayList<Map<String, Object>>();
        for (Organisation_Name name : getNames()) {
            organisationNamesDocument.add(name.getOrganisationNameDocument());
        }
        organisationDocument.put("names", organisationNamesDocument);
        organisationDocument.put("type", this.getType());
        organisationDocument.put("status", this.getStatus());
//        organisationDocument.put("structId", this.getStructure());
        organisationDocument.put("url", this.getUrl());
        for (Organisation_Identifier oi : this.getOrganisation_identifiers()) {
            Map<String, Object> id = new HashMap<String, Object>();
            id.put("type", oi.getType());
            id.put("id", oi.getId());
            organisationIdentifiersDocument.add(id);
        }
        organisationDocument.put("identifers", organisationIdentifiersDocument);
        //organisationDocument.put("orgs", this.getRels());
        return organisationDocument;

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Organisation_Identifier> getOrganisation_identifiers() {
        if (this.organisation_identifiers == null) {
            this.organisation_identifiers = new ArrayList<Organisation_Identifier>();
        }
        return organisation_identifiers;
    }

    public void setOrganisation_identifiers(List<Organisation_Identifier> organisation_identifiers) {
        this.organisation_identifiers = organisation_identifiers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Organisation)) return false;

        Organisation that = (Organisation) o;

        return getOrganisationId() != null ? getOrganisationId().equals(that.getOrganisationId()) : that.getOrganisationId() == null;
    }

    @Override
    public int hashCode() {
        return getOrganisationId() != null ? getOrganisationId().hashCode() : 0;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
