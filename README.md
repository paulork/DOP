# Desafio Organizador Pessoal
Projeto simples, demonstrando um pouco das tecnologias abordadas (vide sessão "Tecnologias abordadas").

O Organizador Pessoal tem o propósito de organizar e lembrar o usuário de seus compromissos diários, e relembrá-lo minutos antes das horas agendadas.
## Requisitos
- Permitir o registro de lembretes para o dia, sem uma hora específica para ser executado, e por este motivo, não deve bloquear o registro de outras atividades.
- Permitir o registro de atividades com local, data e hora de início e final, descrição e um nome
- Listar todas as atividades de um dia, de uma semana ou de um mês
- Listar eventos já finalizados
- Enviar um e-mail diariamente para o usuário com uma lista de atividades e lembretes
- Enviar um e-mail para o usuário antes do início do evento.
## Restrições e arquitetura
- Separação de camadas, back-end e front-end.
- Utilizar o conceito REST (qualquer framework Java).
- Persistência de dados com ORM.
- Servidor de aplicação opensource.
- Banco de dados Postgres SQL.
## Tecnologias abordadas
- Java 8 - https://www.java.com/pt_BR/download/
- VRaptor - http://www.vraptor.org/pt/
- CDI (Interceptors, Observers, Producers, Events, etc) - http://cdi-spec.org/
- EJB Timer - https://jcp.org/en/jsr/detail?id=345
- Hibernate - http://hibernate.org/orm/
- JPA - https://jcp.org/en/jsr/detail?id=338
- Fullcalendar - https://fullcalendar.io/
- Postgres - https://www.postgresql.org/
- Payara - http://www.payara.fish
## Requisitos para funcionamento da aplicação:
- Java 8 - https://www.java.com/pt_BR/download/
- Payara 172 (4.1.2.172) - http://www.payara.fish/downloads
- PostgreSQL 9.4+
## Configurações necessárias (ex: criação de banco, definição de senha de acesso ao banco, etc);
- Configuração de conexão com o banco, email e hibernate/c3p0 em "resources/commons/config.xml":
```xml
<dop>
    <banco>
        <host>localhost</host>
        <porta>5432</porta>
        <owner>postgres</owner> <!-- usuário de login no banco -->
        <senha>123</senha>
        <db>dop</db> 
        <schema>dop</schema> <!-- ORIENTAÇÃO: especifique um SCHEMA que não exista, ele será criado, bem como as tabelas e alguns dados iniciais! -->
    </banco>
    
    <email>
        <mail_to>teste@teste.com</mail_to> <!-- email que receberá as notificações -->
        <mail_user>teste</mail_user> <!-- usuario ou email para login -->
        <mail_pass>senha</mail_pass>
        <mail_smtp>mail.teste.com.br</mail_smtp> 
        <mail_port>587</mail_port>
        <mail_tls>false</mail_tls>
        <mail_ssl>true</mail_ssl>
    </email>
		
    <hibernate>
    ...
    </hibernate>
</dop>
```
- Script para geração das tabelas (caso queira fazer manual, caso contrário, configure a sessão "banco" no XML acima, seguindo a orientação para a tag "schema") em "resources/commons/schema.sql":
```sql
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

ALTER TABLE ONLY atividade ALTER COLUMN id SET DEFAULT nextval('atividade_id_seq'::regclass);
ALTER TABLE ONLY atividade ALTER COLUMN notificado SET DEFAULT false;
ALTER TABLE ONLY atividade ALTER COLUMN notificado SET NOT NULL;
ALTER TABLE ONLY lembrete ALTER COLUMN id SET DEFAULT nextval('lembrete_id_seq'::regclass);
ALTER TABLE ONLY usuario ALTER COLUMN id SET DEFAULT nextval('usuario_id_seq'::regclass);

INSERT INTO atividade (titulo, descricao, data_ini, data_fim, local) VALUES ('Atividade 1', 'Descrição para Atividade 1', '2017-06-01 01:00:00', '2017-06-01 02:00:00', 'São Paulo');
INSERT INTO atividade (titulo, descricao, data_ini, data_fim, local) VALUES ('Atividade 2', 'Descrição para Atividade 2', '2017-06-06 05:00:00', '2017-06-06 06:00:00', 'São Paulo');
INSERT INTO atividade (titulo, descricao, data_ini, data_fim, local) VALUES ('Atividade 3', 'Descrição para Atividade 3', '2017-06-08 11:00:00', '2017-06-08 12:00:00', 'São Paulo');
INSERT INTO atividade (titulo, descricao, data_ini, data_fim, local) VALUES ('Atividade 4', 'Descrição para Atividade 4', '2017-06-18 22:30:00', '2017-06-18 23:30:00', 'São Paulo');

INSERT INTO lembrete (data, titulo, descricao) VALUES ('2017-06-06', 'Lembrete 1', 'Descrição para Lembrete 1');
INSERT INTO lembrete (data, titulo, descricao) VALUES ('2017-06-06', 'Lembrete 2', 'Descrição para Lembrete 2');
INSERT INTO lembrete (data, titulo, descricao) VALUES ('2017-06-06', 'Lembrete 3', 'Descrição para Lembrete 3');

INSERT INTO usuario (email, senha, nome) VALUES ('paulo', '12', 'Paulo');

ALTER TABLE ONLY atividade ADD CONSTRAINT atividade_pkey PRIMARY KEY (id);
ALTER TABLE ONLY usuario ADD CONSTRAINT fk_usuario PRIMARY KEY (id);
ALTER TABLE ONLY lembrete ADD CONSTRAINT lembrete_pkey PRIMARY KEY (id);
ALTER TABLE ONLY usuario ADD CONSTRAINT unk_usuario_email UNIQUE (email);
```
# Considerações finais
De olho nos requisitos, o sistema foi pensado para ser o mais simples possivel de usar. Por esse motivo optou-se por utilizar uma visualização em forma de calendário para visualização dos compromissos (atividades e lembretes) futuros e passados, por mês, semana e dia. Como auxiliar, foi criada duas listagens para comprmissos passados e futuros, com possibilidade de excluir compromissos. Para tal foi utilizado o FullCalendar, além de um pouco de JQuery, e Bootstrap. 

Como é um projeto simples e de baixa complexidade, optou-se por uma aplicação web clássica (monolito), com apenas uma separação lógica entre front-end e back-end. Como resultante temos um único projeto, com build em 1 etapa, resultando em um arquivo WAR ao final do mesmo. Caso fosse uma aplicação mais complexa, com um front-end mais complexo, possivelmente optariamos por colocá-lo em um outro projeto, principalmente no caso de usar um framework de front-end como Angular. Levando em conta essa simplicidade e baixa complexidade, utilizou-se JSP para compor as "views".

Quanto ao back-end, foi utilizado o framework MVC Java VRaptor devido a ser simples, leve, e totalmente integrado a especificação CDI. Além de ser muito usado aqui no Brasil e ter uma comunidade de usuários bem ativa. Também é um framework de facil extensão, devido ao CDI. Exemplo1: Caso alguns dos conversores implementados pelo VRaptor não esteja atendendo as necessidades do nosso projeto, podemos simplesmente criar um outra classe que atenda e especificar sua prioridade como superior a implementação padrão. Exemplo2: Caso quiséssemos mudar uma implementação (de qualquer natureza) dada pelo VRaptor, usamos o qualificador @Specializes, tornando a nossa implementação a padrão.

Para framework ORM utilizamos o Hibernate, que é o padrão de mercado.

A separação lógica do back-end se deu em: controllers, manager, service, model e util. Segue explanação sobre cada um:
- No primeiro, estão agrupados todos os controladores (interfaceamento back<->front). São os controllers que recebem as requisições provindas do front (claro que o JSON mandado a partir do front já chega para os métodos do controller no back em forma de objeto (já popúlado)). É nos controllers que definimos as URLs, e como se dará a troca de dados entre front e back (post, get, delete, put, xml, json, etc).
- No segundo, estão localizadas as lógicas de gerenciamento da aplicação web, os interceptadores, observadores, conversores, produtores, qualificadores, ejb timer para monitoramente de atividades e lembretes a notificar, etc.
- No terceiro, estão as consultas e as lógicas de acesso a dados. É aqui que se dá o interfaceamento entre a aplicação, o hibernate e o banco de dados. É aqui que desenvolvemos a consultas, e persistimos as alterações para a base de dados.
- No quarto, estão as classes de entidades (modelos) que representam as tabelas no banco de dados.
- No quinto, estão as classes utilitárias usadas para manipulação de XML, de arquivos, data, e conexão JDBC. Contém algumas classes utilitárias criadas para uso nesse projeto específico e outras desenvolvidas anteriormente por mim.

Como podemos ver, trata-se de uma projeto bem simples, mas com um apanhado grande de tecnologias, afim de demonstrar um pouco do conhecimento adquirido com cada uma delas.

Caso fosse em um projeto mais complexo, estaríamos usando multi-tenancy (por schema ou por banco, com uma base/schema para as coisas do sistema: login, log de erros, parametros, clientes, etc; e as demais bases/schema para cada cliente.), gerenciamento de sessão por token e com controle via banco de dados, também usaríamos algo mais robusto no front, como React, Vue, Angular, etc em um projeto separado (no mínimo para desenvolvimento), usaríamos também versionamento da base de dado, definiríamos politicas de upload, download e armazenamento de arquivos. Dependendo do caráter da aplicação talvez até um base de dados No-SQL (MongoDB, Neo4J, etc).
