package br.com.paulork.dop.manager.observers;

import br.com.caelum.vraptor.events.VRaptorInitialized;
import br.com.paulork.dop.manager.ejb.NotificadorTimer;
import br.com.paulork.dop.manager.qualifiers.ComEscopo;
import br.com.paulork.dop.utils.ConnectionUtils;
import br.com.paulork.dop.utils.xml.XMLConfig;
import java.sql.Connection;
import java.util.HashMap;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

@ApplicationScoped
public class SysInitObserver {

    @Inject private EntityManagerFactory emf;
    @Inject private NotificadorTimer notificador;
    
    @ComEscopo
    public void init(@Observes VRaptorInitialized evento) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("...").append("\n");
        sb.append("------------------------------------------").append("\n");
        sb.append("|                                        |").append("\n");
        sb.append("|      VRaptor acabou de inicializar!    |").append("\n");
        sb.append("|                                        |").append("\n");
        sb.append("------------------------------------------");
        System.out.println(sb.toString());
        
        // Carrega XML de configuração
        // Xml contém os dados para envio de mail e dados de configuração do Hibernate
        try {
            if (!XMLConfig.isLoaded()) {
                XMLConfig.loadXML();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        // Conecta no banco de dados, verifica a existencia do SCHEMA, caso contrário
        // cria-o, e alimenta a base com alguns dados de login, atividade e lembrete
        HashMap<String, String> configBanco = XMLConfig.getConfigBanco();
        System.out.println("----> Verificando conexão com a base de dados...");
        Connection connection = ConnectionUtils.getConnection(configBanco.get("host"), configBanco.get("porta"), configBanco.get("db"), configBanco.get("owner"), configBanco.get("senha"));
        System.out.println("----> Conexão: "+(connection.isClosed() ? "FALHOU!" : "OK!"));
        if(!connection.isClosed()){
            System.out.println("----> Verificando se o SCHEMA existe...");
            boolean existeSchema = ConnectionUtils.existeSchema(configBanco.get("schema"), connection);
            System.out.println("----> SCHEMA: "+(existeSchema ? "EXISTE!" : "NÃO EXISTE! CRIANDO..."));
            if(!existeSchema){
                boolean criaSchema = ConnectionUtils.criaSchema(configBanco.get("schema"), "postgres", connection);
                if(criaSchema){
                    ConnectionUtils.criaTabelas(configBanco.get("schema"), connection);
                    System.out.println("----> Tabelas criadas...");
                } else {
                    throw new Exception("Falha ao criar SCHEMA, verifique o arquivo de configuração (commons/config.xml).");
                }
            }
        } else {
            throw new Exception("Falha ao conectar a base de dados, verifique o arquivo de configuração (commons/config.xml).");
        }
        
        // Aciona o producer (JPAProducer.java) para montar o EntityManagerFactory
        // É feito isso aqui para não demorar (tempo de criação do EntityManagerFactory)
        // no primeiro login.
        emf.isOpen();
        
        // Libera o notificador para iniciar o monitoramento das atividades
        notificador.setSystemInitiated();
    }
    
}
