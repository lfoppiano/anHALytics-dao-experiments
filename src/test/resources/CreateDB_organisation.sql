-- -----------------------------------------------------
-- Table ORGANISATION
-- -----------------------------------------------------
CREATE TABLE ORGANISATION (
  organisationID INT IDENTITY,
  TYPE           VARCHAR(10) DEFAULT NULL  NULL,
  url            VARCHAR(255) DEFAULT NULL NULL,
  status         VARCHAR(45) DEFAULT NULL  NULL,
  PRIMARY KEY (organisationID)
);

-- -----------------------------------------------------
-- Table ORGANISATION_IDENTIFIER
-- -----------------------------------------------------
CREATE TABLE ORGANISATION_IDENTIFIER (
  organisation_identifierID IDENTITY,
  organisationID INT                       NOT NULL,
  ID             VARCHAR(150) DEFAULT NULL NULL,
  TYPE           VARCHAR(55) DEFAULT NULL  NULL,

  CONSTRAINT fk_IDENTIFIERS_ORGANISATION1
  FOREIGN KEY (organisationID)
  REFERENCES ORGANISATION (organisationID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE UNIQUE INDEX indexOrganisationIdentifier
  ON ORGANISATION_IDENTIFIER (organisationID ASC, ID ASC, TYPE ASC);
CREATE INDEX fk_IDENTIFIERS_DOCUMENT1
  ON ORGANISATION_IDENTIFIER (organisationID ASC);

-- -----------------------------------------------------
-- Table ORGANISATION_NAME
-- -----------------------------------------------------
CREATE TABLE ORGANISATION_NAME (
  organisation_nameID IDENTITY,
  organisationID  INT                          NOT NULL,
  NAME            VARCHAR(150) DEFAULT NULL    NULL,
  lastupdate_date DATE DEFAULT NULL            NULL,

  CONSTRAINT fk_ORGANISATION_NAME_ORGANISATION1
  FOREIGN KEY (organisationID)
  REFERENCES ORGANISATION (organisationID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE UNIQUE INDEX index3
  ON ORGANISATION_NAME (organisationID ASC, NAME ASC, lastupdate_date ASC);
CREATE INDEX fk_ORGANISATION_NAME_ORGANISATION1_idx
  ON ORGANISATION_NAME (organisationID ASC);

-- -----------------------------------------------------
-- Table PART_OF
-- -----------------------------------------------------
CREATE TABLE PART_OF (
  organisation_motherID INT               NOT NULL,
  organisationID        INT               NOT NULL,
  from_date             DATE DEFAULT NULL NULL,
  until_date            DATE DEFAULT NULL NULL,

  CONSTRAINT fk_incorporation_structure1
  FOREIGN KEY (organisation_motherID
  )
  REFERENCES ORGANISATION (organisationID
  )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_incorporation_structure2
  FOREIGN KEY (organisationID
  )
  REFERENCES ORGANISATION (organisationID
  )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE UNIQUE INDEX index4
  ON PART_OF (organisationID ASC, from_date ASC, organisation_motherID ASC);
CREATE INDEX fk_incorporation_structure1_idx
  ON PART_OF (organisation_motherID ASC);
CREATE INDEX fk_incorporation_structure2_idx
  ON PART_OF (organisationID ASC);