package br.com.paulork.dop.service;

import br.com.paulork.dop.model.Atividade;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * @author Paulo R. Kraemer <paulork10@gmail.com>
 */
public class AtividadeService extends BasicService<Atividade> {
    
    public AtividadeService() {
        super(Atividade.class);
    }

    public AtividadeService(EntityManager entityManager) {
        super(Atividade.class, entityManager);
    }
    
    public List<Atividade> buscaIntervalo(Date start, Date end){
        TypedQuery<Atividade> query = getEntityManager().createQuery("SELECT a FROM Atividade a "
                + (start != null && end != null ? " WHERE (a.dataIni BETWEEN :ini AND :fim) OR (a.dataFim BETWEEN :ini AND :fim)" : ""), Atividade.class);
        if(start != null){
            query.setParameter("ini", start);
        }
        if(end != null){
            query.setParameter("fim", end);
        }
        return query.getResultList();
    }
    
    public List<Atividade> buscaRecentes(Date agora){
        TypedQuery<Atividade> query = getEntityManager().createQuery("SELECT a FROM Atividade a WHERE (:agora BETWEEN a.dataIni AND a.dataFim) AND (a.notificado = false)", Atividade.class);
        query.setParameter("agora", agora);
        return query.getResultList();
    }
    
    public List<Atividade> buscaPassados(){
        TypedQuery<Atividade> query = getEntityManager().createQuery("SELECT a FROM Atividade a WHERE a.dataFim < :agora ORDER BY a.id, a.dataIni, a.dataFim", Atividade.class);
        query.setParameter("agora", new Date());
        return query.getResultList();
    }
    
    public List<Atividade> buscaFuturos(){
        TypedQuery<Atividade> query = getEntityManager().createQuery("SELECT a FROM Atividade a WHERE a.dataIni > :agora ORDER BY a.id, a.dataIni, a.dataFim", Atividade.class);
        query.setParameter("agora", new Date());
        return query.getResultList();
    }
    
}
