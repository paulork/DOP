package br.com.paulork.dop.service;

import br.com.paulork.dop.model.Lembrete;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * @author Paulo R. Kraemer <paulork10@gmail.com>
 */
public class LembreteService extends BasicService<Lembrete> {
    
    public LembreteService() {
        super(Lembrete.class);
    }

    public LembreteService(EntityManager entityManager) {
        super(Lembrete.class, entityManager);
    }
    
    public List<Lembrete> buscaIntervalo(Date start, Date end){
        TypedQuery<Lembrete> query = getEntityManager().createQuery("SELECT l FROM Lembrete l "
                + (start != null && end != null ? " WHERE (l.data BETWEEN :ini AND :fim)" : ""), Lembrete.class);
        if(start != null){
            query.setParameter("ini", start);
        }
        if(end != null){
            query.setParameter("fim", end);
        }
        return query.getResultList();
    }

    public List<Lembrete> buscaPassados(){
        TypedQuery<Lembrete> query = getEntityManager().createQuery("SELECT l FROM Lembrete l WHERE l.data < :agora ORDER BY l.id, l.data", Lembrete.class);
        query.setParameter("agora", new Date());
        return query.getResultList();
    }
    
    public List<Lembrete> buscaFuturos(){
        TypedQuery<Lembrete> query = getEntityManager().createQuery("SELECT l FROM Lembrete l WHERE l.data > :agora ORDER BY l.id, l.data", Lembrete.class);
        query.setParameter("agora", new Date());
        return query.getResultList();
    }
    
}
