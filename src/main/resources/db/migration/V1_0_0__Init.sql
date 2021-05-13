
CREATE TABLE study (
    id uuid,
    title varchar(256) NOT NULL,
    CONSTRAINT study_pk PRIMARY KEY (id)
);

CREATE TABLE report (
    id uuid,
    title varchar(256) NOT NULL,
    pdfData bytea NOT NULL,
    CONSTRAINT report_pk PRIMARY KEY (id)
);

CREATE TABLE study_report (
    studyId uuid,
    reportId uuid,
    CONSTRAINT study_report_pk PRIMARY KEY (studyId, reportId),
    CONSTRAINT study_report_fk_study FOREIGN KEY (studyId) REFERENCES study (id),
    CONSTRAINT study_report_fk_report FOREIGN KEY (reportId) REFERENCES report (id)
);

CREATE TABLE sentence (
    id uuid,
    report uuid NOT NULL,
    content varchar(512) NOT NULL,
    precisionLabels integer[],
    recallLabels integer[],
    CONSTRAINT sentence_pk PRIMARY KEY (id),
    CONSTRAINT sentence_fk_report FOREIGN KEY (report) REFERENCES report (id)
);

CREATE TABLE fragment (
    id uuid,
    sentence uuid NOT NULL,
    pageNo integer NOT NULL,
    x1 double precision NOT NULL,
    x2 double precision NOT NULL,
    y1 double precision NOT NULL,
    y2 double precision NOT NULL,
    CONSTRAINT fragment_pk PRIMARY KEY (id),
    CONSTRAINT fragment_fk_sentence FOREIGN KEY (sentence) REFERENCES sentence (id)
);

CREATE TABLE participant (
    id uuid,
    username char(4) NOT NULL,
    age integer,
    gender char(1),
    genderDescription varchar(256),
    passwordHash char(60),
    validFrom timestamp,
    validTo timestamp,
    CONSTRAINT participant_pk PRIMARY KEY (id),
    CONSTRAINT participant_uq_username UNIQUE (username)
);

CREATE TABLE role (
    roleName varchar(8),
    CONSTRAINT role_pk PRIMARY KEY (roleName)
);

INSERT INTO role VALUES ("USER");
INSERT INTO role VALUES ("ADMIN");

CREATE TABLE participant_role (
    participantId uuid,
    roleName varchar(8),
    CONSTRAINT participant_role_pk PRIMARY KEY (participantId, roleName),
    CONSTRAINT participant_role_fk_participant FOREIGN KEY (participantId) REFERENCES participant (id),
    CONSTRAINT participant_role_fk_role FOREIGN KEY (roleName) REFERENCES role (roleName),
)

CREATE TABLE ati (
    id uuid,
    participant uuid NOT NULL,
    response1 integer NOT NULL,
    response2 integer NOT NULL,
    response3 integer NOT NULL,
    response4 integer NOT NULL,
    response5 integer NOT NULL,
    response6 integer NOT NULL,
    response7 integer NOT NULL,
    response8 integer NOT NULL,
    response9 integer NOT NULL,
    CONSTRAINT ati_pk PRIMARY KEY (id),
    CONSTRAINT ati_fk_participant FOREIGN KEY (participant) REFERENCES participant (id),
    CONSTRAINT ati_uq_participant UNIQUE (participant)
);

CREATE TABLE tlx (
    id uuid,
    participant uuid NOT NULL,
    taskNo integer NOT NULL,
    mentalDemand integer NOT NULL,
    physicalDemand integer NOT NULL,
    temporalDemand integer NOT NULL,
    performance integer NOT NULL,
    effort integer NOT NULL,
    frustration integer NOT NULL,
    CONSTRAINT tlx_pk PRIMARY KEY (id),
    CONSTRAINT tlx_fk_participant FOREIGN KEY (participant) REFERENCES participant (id),
    CONSTRAINT tlx_uq_participant_taskNo UNIQUE (participant, taskNo)
);

CREATE TABLE trust (
    id uuid,
    participant uuid NOT NULL,
    taskNo integer NOT NULL,
    response1 integer NOT NULL,
    response2 integer NOT NULL,
    response3 integer NOT NULL,
    response4 integer NOT NULL,
    response5 integer NOT NULL,
    response6 integer NOT NULL,
    response7 integer NOT NULL,
    response8 integer NOT NULL,
    response9 integer NOT NULL,
    CONSTRAINT trust_pk PRIMARY KEY (id),
    CONSTRAINT trust_fk_participant FOREIGN KEY (participant) REFERENCES participant (id),
    CONSTRAINT trust_uq_participant_taskNo UNIQUE (participant, taskNo)
);
