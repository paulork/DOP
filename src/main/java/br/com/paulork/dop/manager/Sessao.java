package br.com.paulork.dop.manager;

import br.com.paulork.dop.model.Usuario;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;

@SessionScoped
public class Sessao implements Serializable {
    
    private boolean logged = false;
    private Usuario usuario = null;

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
