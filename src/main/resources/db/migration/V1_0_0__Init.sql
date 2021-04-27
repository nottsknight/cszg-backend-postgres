
CREATE TABLE study (
    id uuid,
    title varchar(256),
    CONSTRAINT study_pk PRIMARY KEY (id)
);

CREATE TABLE report (
    id uuid,
    title varchar(256),
    pdfData bytea
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
    report uuid,
    content varchar(512),
    precisionLabels integer[],
    recallLabels integer[],
    CONSTRAINT sentence_pk PRIMARY KEY (id),
    CONSTRAINT sentence_fk_report FOREIGN KEY (report) REFERENCES report (id)
)
