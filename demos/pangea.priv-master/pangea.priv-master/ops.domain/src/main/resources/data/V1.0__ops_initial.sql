CREATE TABLE application (
    id bigint NOT NULL
);

CREATE SEQUENCE application_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE ONLY application
    ADD CONSTRAINT application_pkey PRIMARY KEY (id);
