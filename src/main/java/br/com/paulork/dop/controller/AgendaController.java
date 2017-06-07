package br.com.paulork.dop.controller;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.com.paulork.dop.model.Atividade;
import br.com.paulork.dop.model.Lembrete;
import br.com.paulork.dop.service.AtividadeService;
import br.com.paulork.dop.service.LembreteService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Paulo R. Kraemer <paulork10@gmail.com>
 */
@Controller
public class AgendaController {

    @Inject Result result;
    @Inject AtividadeService ativServ;
    @Inject LembreteService lembServ;

    @Get({"/agenda/listar-passados", "/agenda/listar-passados/"})
    public void listarPassados() {
        List<Atividade> ativs = ativServ.buscaPassados();
        List<Lembrete> lembs = lembServ.buscaPassados();
        result.include("atividades", ativs);
        result.include("lembretes", lembs);
    }
    
    @Get({"/agenda/listar-futuros", "/agenda/listar-futuros/"})
    public void listarFuturos() {
        List<Atividade> ativs = ativServ.buscaFuturos();
        List<Lembrete> lembs = lembServ.buscaFuturos();
        result.include("atividades", ativs);
        result.include("lembretes", lembs);
    }
    
    @Get({"/agenda/buscar", "/agenda/buscar/"})
    public void buscar(Date start, Date end) {
        List<Atividade> ativs = ativServ.buscaIntervalo(start, end);
        List<Lembrete> lembs = lembServ.buscaIntervalo(start, end);
        
        List<Dto> dtos = new ArrayList<>();
        for (Atividade ativ : ativs) {
            Dto dto = new Dto(
                    ativ.getTitulo(), 
                    ativ.getDescricao(), 
                    ativ.getDataIni(),
                    ativ.getDataFim(),
                    false
            );
            dtos.add(dto);
        }
        
        for (Lembrete lemb : lembs) {
            Dto dto = new Dto(
                    lemb.getTitulo(),
                    lemb.getDescricao(),
                    lemb.getData(), 
                    null,
                    true
            );
            dtos.add(dto);
        }
        
        result.use(Results.json()).withoutRoot().from(dtos).serialize();
    }
    
    private class Dto {
        private String title;
        private String description;
        private Date start;
        private Date end;
        private Boolean allDay;

        public Dto(String title, String description, Date start, Date end, boolean allDay) {
            this.title = title;
            this.description = description;
            this.start = start;
            this.end = end;
            this.allDay = allDay;
        }
        
    }
    
}
