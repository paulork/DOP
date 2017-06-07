package br.com.paulork.dop.manager.producers;

import br.com.paulork.dop.utils.xml.XMLConfig;
import java.util.HashMap;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAProducer {
    
    @Produces
    @ApplicationScoped
    public EntityManagerFactory createEntityManagerFactory() throws Exception {
        HashMap<String, String> map = XMLConfig.getConfigHibernate();
        map.put("packagesToScan", "br.com.paulork.dop.model");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hbn-provider", map);
        emf.getCache().evictAll();
        return emf;
    }

    @Produces
    @RequestScoped
    public EntityManager createEntityManager(EntityManagerFactory factory) throws Exception {
        EntityManager em = factory.createEntityManager();
        return em;
    }

    public void destroyEntityManager(@Disposes EntityManager em) {
        if (em.isOpen()) {
            em.close();
        }
    }

    public void destroyEntityManagerFactory(@Disposes EntityManagerFactory emf) {
        if (emf.isOpen()) {
            emf.close();
        }
    }
    
}
