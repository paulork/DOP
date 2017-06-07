package br.com.paulork.dop.manager.interceptors;

import br.com.paulork.dop.manager.qualifiers.Transaction;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

@Interceptor
@Transaction
@Priority(Interceptor.Priority.APPLICATION)
public class TransactionInterceptor {

    @Inject private EntityManager em;

    @AroundInvoke
    public Object intercept(InvocationContext context) throws Exception {
        Object obj = null;
        EntityTransaction tx = em.getTransaction();
        boolean criador = false;
        try {
            if (!tx.isActive()) {
                tx.begin();
                criador = true;
            }
            obj = context.proceed();
            if (tx != null && criador) {
                tx.commit();
            }
        } catch (Exception e) {
            if (tx != null && criador) {
                tx.rollback();
            }
            throw e;
        }
        return obj;
    }

}
