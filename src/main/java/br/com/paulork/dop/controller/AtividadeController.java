package br.com.paulork.dop.controller;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.com.paulork.dop.model.Atividade;
import br.com.paulork.dop.service.AtividadeService;
import javax.inject.Inject;

/**
 * @author Paulo R. Kraemer <paulork10@gmail.com>
 */
@Controller
public class AtividadeController {
    
    @Inject Result result;
    @Inject AtividadeService ativServ;
    
    @Get({"/atividade/form", "/atividade/form/"})
    public void form() {
    }
    
    @Post({"/atividade/salvar", "/atividade/salvar/"})
    @Consumes("application/json")
    public void salvar(Atividade ativ){
        try {
            ativServ.save(ativ);
            result.use(Results.json()).withoutRoot().from("ok").serialize();
        } catch(Exception ex){
            result.use(Results.json()).withoutRoot().from("erro").serialize();
        }
    }
    
    @Delete({"/atividade/deletar", "/atividade/deletar/{id}"})
    public void deletar(int id){
        try {
            ativServ.delete(new Atividade(id));
            result.use(Results.json()).withoutRoot().from("ok").serialize();
        } catch(Exception ex){
            result.use(Results.json()).withoutRoot().from("erro").serialize();
        }
    }
    
    @Get({"/atividade/by-id", "/atividade/by-id/{id}"})
    public void byId(int id){
        result.use(Results.json()).from(ativServ.byId(id))
                .include("dataIni")
                .include("dataFim")
                .serialize();
    }

}
