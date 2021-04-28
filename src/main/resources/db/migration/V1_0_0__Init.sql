
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
    username char(4),
    age integer,
    gender char(1),
    genderDescription varchar(256),
    passwordHash char(60),
    validFrom timestamp,
    validTo timestamp,
    CONSTRAINT participant_pk PRIMARY KEY (id),
    CONSTRAINT participant_uq_username UNIQUE (username)
);
