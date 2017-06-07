package br.com.paulork.dop.controller;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.paulork.dop.model.Usuario;
import br.com.paulork.dop.service.UsuarioService;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Paulo R. Kraemer <paulork10@gmail.com>
 */
@Controller
public class UsuarioController {

    @Inject Result result;
    @Inject UsuarioService prodServ;

    @Get({"/usuario/form", "/usuario/form/"})
    public void form() {
    }

    @Post({"/usuario/salvar", "/usuario/salvar/"})
    @Consumes("application/json")
    public void salvar(Usuario usuario) {
        prodServ.save(usuario);
        result.redirectTo(this).listar(null);
    }

    @Get({"/usuario/editar", "/usuario/editar/"})
    public void editar(int id) {
        Usuario usuario = prodServ.byId(id);
        result.include("usuario", usuario).redirectTo("/usuario/form");
    }

    @Get({"/usuario/deletar", "/usuario/deletar/"})
    public void deletar(int id) {
        prodServ.delete(new Usuario(id));
        result.redirectTo(this).listar(null);
    }

    @Get({"/usuario/listar", "/usuario/listar/"})
    public void listar(String ordenarPor) {
        List<Usuario> usuarios = null;
        result.include("usuarios", usuarios);
    }
    
    @Get({"/usuario/buscar", "/usuario/buscar/"})
    public void buscar(Usuario ordenarPor) {
        List<Usuario> usuarios = null;
        //result.include("usuarios", usuarios).redirectTo("/usuario/lista");
    }
    
    @Get({"/usuario/info", "/usuario/info/"})
    public void info(int id) {
        Usuario usuario = prodServ.byId(id);
        result.include("usuario", usuario);
    }
    
}
