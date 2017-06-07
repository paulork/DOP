package br.com.paulork.dop.controller;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.com.paulork.dop.manager.Sessao;
import br.com.paulork.dop.manager.qualifiers.Public;
import br.com.paulork.dop.model.Usuario;
import br.com.paulork.dop.service.UsuarioService;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Paulo R. Kraemer <paulork10@gmail.com>
 */
@Controller
@Public
public class LoginController {
    
    @Inject private Result result;
    @Inject private Sessao userSession;
    @Inject private HttpServletRequest request;
    @Inject private UsuarioService userServ;
    
    @Get("/login")
    public void form(){}
    
    @Post("/login/check")
    public void login(String usuario, String senha){
        Usuario user = userServ.buscaLogin(usuario, senha);
        if(user != null){
            userSession.setLogged(true);
            userSession.setUsuario(user);
            result.use(Results.json()).withoutRoot().from("ok").serialize();
        } else {
            result.use(Results.json()).withoutRoot().from("erro").serialize();
        }
    }
    
    @Get("/logout")
    public void logout(){
        userSession.setLogged(false);
        request.getSession().invalidate();
        result.redirectTo(this).form();
    }

}
