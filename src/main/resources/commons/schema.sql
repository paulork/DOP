/**
 * Author:  Paulo R. K. <paulork10@gmail.com>
 * Created: 02/06/2017
 */

CREATE TABLE atividade (
    id integer NOT NULL,
    titulo character varying(60),
    descricao character varying(255),
    data_ini timestamp without time zone,
    data_fim timestamp without time zone,
    local character varying(255),
    notificado boolean
);
ALTER TABLE atividade OWNER TO postgres;

CREATE SEQUENCE atividade_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE atividade_id_seq OWNER TO postgres;
ALTER SEQUENCE atividade_id_seq OWNED BY atividade.id;

--------------------------------------------------------------------------------

CREATE TABLE lembrete (
    id integer NOT NULL,
    data date,
    titulo character varying(60),
    descricao character varying(255)
);
ALTER TABLE lembrete OWNER TO postgres;

CREATE SEQUENCE lembrete_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE lembrete_id_seq OWNER TO postgres;
ALTER SEQUENCE lembrete_id_seq OWNED BY lembrete.id;

--------------------------------------------------------------------------------

CREATE TABLE usuario (
    email character varying(100),
    senha character varying(22),
    nome character varying(50),
    id integer NOT NULL
);
ALTER TABLE usuario OWNER TO postgres;
CREATE SEQUENCE usuario_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE usuario_id_seq OWNER TO postgres;
ALTER SEQUENCE usuario_id_seq OWNED BY usuario.id;

--------------------------------------------------------------------------------

ALTER TABLE ONLY atividade ALTER COLUMN id SET DEFAULT nextval('atividade_id_seq'::regclass);
ALTER TABLE ONLY atividade ALTER COLUMN notificado SET DEFAULT false;
ALTER TABLE ONLY atividade ALTER COLUMN notificado SET NOT NULL;
ALTER TABLE ONLY lembrete ALTER COLUMN id SET DEFAULT nextval('lembrete_id_seq'::regclass);
ALTER TABLE ONLY usuario ALTER COLUMN id SET DEFAULT nextval('usuario_id_seq'::regclass);

INSERT INTO atividade (titulo, descricao, data_ini, data_fim, local) VALUES ('Atividade 1', 'Descrição para Atividade 1', '2017-06-01 01:00:00', '2017-06-01 02:00:00', 'São Paulo');
INSERT INTO atividade (titulo, descricao, data_ini, data_fim, local) VALUES ('Atividade 2', 'Descrição para Atividade 2', '2017-06-06 05:00:00', '2017-06-06 06:00:00', 'São Paulo');
INSERT INTO atividade (titulo, descricao, data_ini, data_fim, local) VALUES ('Atividade 3', 'Descrição para Atividade 3', '2017-06-08 11:00:00', '2017-06-08 12:00:00', 'São Paulo');
INSERT INTO atividade (titulo, descricao, data_ini, data_fim, local) VALUES ('Atividade 4', 'Descrição para Atividade 4', '2017-06-18 22:30:00', '2017-06-18 23:30:00', 'São Paulo');
-- SELECT pg_catalog.setval('atividade_id_seq', 4, true);

INSERT INTO lembrete (data, titulo, descricao) VALUES ('2017-06-06', 'Lembrete 1', 'Descrição para Lembrete 1');
INSERT INTO lembrete (data, titulo, descricao) VALUES ('2017-06-06', 'Lembrete 2', 'Descrição para Lembrete 2');
INSERT INTO lembrete (data, titulo, descricao) VALUES ('2017-06-06', 'Lembrete 3', 'Descrição para Lembrete 3');
-- SELECT pg_catalog.setval('lembrete_id_seq', 3, true);
-- 
INSERT INTO usuario (email, senha, nome) VALUES ('paulo', '12', 'Paulo');
-- SELECT pg_catalog.setval('usuario_id_seq', 1, false);

ALTER TABLE ONLY atividade ADD CONSTRAINT atividade_pkey PRIMARY KEY (id);
ALTER TABLE ONLY usuario ADD CONSTRAINT fk_usuario PRIMARY KEY (id);
ALTER TABLE ONLY lembrete ADD CONSTRAINT lembrete_pkey PRIMARY KEY (id);
ALTER TABLE ONLY usuario ADD CONSTRAINT unk_usuario_email UNIQUE (email);