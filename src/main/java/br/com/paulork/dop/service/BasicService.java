package br.com.paulork.dop.service;

import br.com.paulork.dop.manager.qualifiers.Transaction;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.persistence.EntityManager;

public abstract class BasicService<T extends Serializable> {

    private EntityManager em;
    private Class<T> clazz;

    public BasicService(Class<T> typeClass) {
        this.clazz = typeClass;
        BeanManager bm = javax.enterprise.inject.spi.CDI.current().getBeanManager();
        Bean<EntityManager> bean = (Bean<EntityManager>) bm.getBeans(EntityManager.class).iterator().next();
        CreationalContext<EntityManager> ctx = bm.createCreationalContext(bean);
        this.em = (EntityManager) bm.getReference(bean, EntityManager.class, ctx);
    }

    public BasicService(Class<T> typeClass, EntityManager entityManager) {
        this.clazz = typeClass;
        if (entityManager == null) {
            BeanManager bm = javax.enterprise.inject.spi.CDI.current().getBeanManager();
            Bean<EntityManager> bean = (Bean<EntityManager>) bm.getBeans(EntityManager.class).iterator().next();
            CreationalContext<EntityManager> ctx = bm.createCreationalContext(bean);
            this.em = (EntityManager) bm.getReference(bean, EntityManager.class, ctx);
        } else {
            this.em = entityManager;
        }
    }

    @Transaction
    public T save(T object) {
        return em.merge(object);
    }
    
    @Transaction
    public void save(List<T> list) {
        list.forEach(l -> save(l));
    }

    public T byId(Integer id) {
        return (T) em.find(clazz, id);
    }

    @Transaction
    public void delete(List<T> list) {
        list.forEach(l -> delete(l));
    }

    @Transaction
    public void delete(T object) {
        if (em.contains(object)) {
            em.remove(object);
        } else {
            try {
                Field idField = object.getClass().getDeclaredField("id");
                idField.setAccessible(true);
                Integer id = (Integer) idField.get(object);

                Object obj = em.getReference(clazz, id);
                em.remove(obj);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                throw new RuntimeException("Erro ao excluir objeto.", ex);
            }
        }
    }

    public EntityManager getEntityManager() {
        return em;
    }
}
