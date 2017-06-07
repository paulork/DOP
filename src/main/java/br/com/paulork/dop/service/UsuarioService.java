package br.com.paulork.dop.service;

import br.com.paulork.dop.model.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * @author Paulo R. Kraemer <paulork10@gmail.com>
 */
public class UsuarioService extends BasicService<Usuario> {
    
    public UsuarioService() {
        super(Usuario.class);
    }

    public UsuarioService(EntityManager entityManager) {
        super(Usuario.class, entityManager);
    }
    
    public Usuario buscaLogin(String email, String senha){
        TypedQuery<Usuario> query = getEntityManager().createQuery("SELECT u FROM Usuario u WHERE u.email = :email AND u.senha = :senha", Usuario.class);
        query.setParameter("email", email);
        query.setParameter("senha", senha);
        try {
            return query.getSingleResult();
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
