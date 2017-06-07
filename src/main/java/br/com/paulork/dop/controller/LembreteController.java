package br.com.paulork.dop.controller;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.com.paulork.dop.model.Lembrete;
import br.com.paulork.dop.service.LembreteService;
import javax.inject.Inject;

/**
 * @author Paulo R. Kraemer <paulork10@gmail.com>
 */
@Controller
public class LembreteController {

    @Inject Result result;
    @Inject LembreteService lembServ;
    
    @Get({"/lembrete/form", "/lembrete/form/"})
    public void form() {
    }
    
    @Post({"/lembrete/salvar", "/lembrete/salvar/"})
    @Consumes("application/json")
    public void salvar(Lembrete lemb){
        try {
            lembServ.save(lemb);
            result.use(Results.json()).withoutRoot().from("ok").serialize();
        } catch(Exception ex){
            result.use(Results.json()).withoutRoot().from("erro").serialize();
        }
    }
    
    @Delete({"/lembrete/deletar", "/lembrete/deletar/{id}"})
    public void deletar(int id){
        try {
            lembServ.delete(new Lembrete(id));
            result.use(Results.json()).withoutRoot().from("ok").serialize();
        } catch(Exception ex){
            result.use(Results.json()).withoutRoot().from("erro").serialize();
        }
    }
    
    @Get({"/lembrete/by-id", "/lembrete/by-id/{id}"})
    public void byId(int id){
        result.use(Results.json()).from(lembServ.byId(id)).serialize();
    }
    
}
