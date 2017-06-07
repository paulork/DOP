package br.com.paulork.dop.manager.ejb;

import br.com.paulork.dop.model.Atividade;
import br.com.paulork.dop.model.Lembrete;
import br.com.paulork.dop.service.AtividadeService;
import br.com.paulork.dop.service.LembreteService;
import br.com.paulork.dop.utils.UtilDate;
import br.com.paulork.dop.utils.xml.XMLConfig;
import br.com.paulork.dop.utils.mail.EMail;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

/**
 * @author Paulo R. Kraemer <paulork10@gmail.com>
 */
@Singleton
public class NotificadorTimer {
    
    @Inject AtividadeService ativServ;
    @Inject LembreteService lembServ;
    @Inject @Any private Event<EMail> mailEvent;
    private boolean systemInitiated = false;
    
    @Schedule(hour = "*", minute = "*", second = "0", info = "NotificaAtividades")
    public void notificaAtividades() throws Exception {
        System.out.println("EJB - notificaAtividades - systemInitiated: "+systemInitiated);
        System.out.println("EJB - notificaAtividades - XMLConfig.isLoaded(): "+XMLConfig.isLoaded());
        if(systemInitiated && XMLConfig.isLoaded()){
            // Notifica 30 minutos antes da atividade começar
            List<Atividade> buscaRecentes = ativServ.buscaRecentes(UtilDate.localDateTimeToDate(LocalDateTime.now().plusMinutes(30)));
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
            if(!buscaRecentes.isEmpty()){
                StringBuilder sb = new StringBuilder();
                sb.append("<p>Olá, este email é para lembrar você do(s) seguinte(s) compromisso(s), daqui a pouco:<p>");
                for (Atividade ativ : buscaRecentes) {
                    sb.append("<ul>");
                    sb.append("<li><b>Título:</b> ").append(ativ.getTitulo()).append("</li>");
                    sb.append("<li><b>Descrição:</b> ").append(ativ.getDescricao()).append("</li>");
                    sb.append("<li><b>Começa em:</b> ").append(sdf.format(ativ.getDataIni())).append("</li>");
                    sb.append("<li><b>Termina em:</b> ").append(sdf.format(ativ.getDataFim())).append("</li>");
                    sb.append("</ul>");
                    sb.append("---------------------------------------------------");
                    ativ.setNotificado(true); // Marca Atividade como notificada
                }
                sb.append("<p>Até mais...<p>");
                
                HashMap<String, String> smtp = XMLConfig.getConfigEmail();
                EMail mail = new EMail(
                        smtp.get("mail_smtp"), 
                        Integer.parseInt(smtp.get("mail_port")), 
                        smtp.get("mail_user"), 
                        smtp.get("mail_pass"), 
                        Boolean.valueOf(smtp.get("mail_tls")), 
                        Boolean.valueOf(smtp.get("mail_ssl"))
                );
                mail.setAssunto("Notificação de compromisso");
                mail.setMensagem(sb.toString());
                mail.addDestinatario(smtp.get("mail_to"));
                
                System.out.println("------> DISPARANDO O EVENTO DE ENVIO... ");
                mailEvent.fire(mail); // Disparo de Evento CDI - Observado em SendMailObserver.java
                
                ativServ.save(buscaRecentes); //Salva os que já foram notificados
            }
        }
    }
    
    @Schedule(hour = "0", minute = "1", second = "0", info = "NotificacaoDiaria")
    public void notificacaoDiaria() throws Exception{
        System.out.println("EJB - notificacaoDiaria - VERIFICANDO...");
        // Envia email a após a meia noite sobre os compromisso do dia
        Date ini = Date.from(LocalDateTime.of(LocalDate.now(), LocalTime.MIN).toInstant(ZoneOffset.UTC));
        Date fim = Date.from(LocalDateTime.of(LocalDate.now(), LocalTime.MAX).toInstant(ZoneOffset.UTC));
        
        List<Atividade> buscaAtiv = ativServ.buscaIntervalo(ini, fim);
        System.out.println("EJB - notificacaoDiaria - ATIVIDADES: "+buscaAtiv.size());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
        StringBuilder sb = new StringBuilder();
        sb.append("<p>Olá, este email é para lembrar você do(s) seguinte(s) compromisso(s), daqui a pouco:<p>");
        sb.append("<p><b>ATIVIDADES:</b><p>");
        for (Atividade ativ : buscaAtiv) {
            sb.append("<ul>");
            sb.append("<li><b>Título:</b> ").append(ativ.getTitulo()).append("</li>");
            sb.append("<li><b>Descrição:</b> ").append(ativ.getDescricao()).append("</li>");
            sb.append("<li><b>Começa em:</b> ").append(sdf.format(ativ.getDataIni())).append("</li>");
            sb.append("<li><b>Termina em:</b> ").append(sdf.format(ativ.getDataFim())).append("</li>");
            sb.append("</ul>");
            sb.append("---------------------------------------------------");
            ativ.setNotificado(true); // Marca Atividade como notificada
        }
        //----------------------------------------------------------------------
        sb.append("<p><b>LEMBRETE:</b><p>");
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        List<Lembrete> buscaLemb = lembServ.buscaIntervalo(ini, fim);
        System.out.println("EJB - notificacaoDiaria - LEMBRETES: "+buscaLemb.size());
        for (Lembrete lemb : buscaLemb) {
            sb.append("<ul>");
            sb.append("<li><b>Título:</b> ").append(lemb.getTitulo()).append("</li>");
            sb.append("<li><b>Descrição:</b> ").append(lemb.getDescricao()).append("</li>");
            sb.append("<li><b>Data:</b> ").append(sdf.format(lemb.getData())).append("</li>");
            sb.append("</ul>");
            sb.append("---------------------------------------------------");
        }
        sb.append("<p>Até mais...<p>");
        
        HashMap<String, String> smtp = XMLConfig.getConfigEmail();
        EMail mail = new EMail(
                smtp.get("mail_smtp"), 
                Integer.parseInt(smtp.get("mail_port")), 
                smtp.get("mail_user"), 
                smtp.get("mail_pass"), 
                Boolean.valueOf(smtp.get("mail_tls")), 
                Boolean.valueOf(smtp.get("mail_ssl"))
        );
        mail.setAssunto("Compromissos de hoje ("+sdf.format(ini)+")");
        mail.setMensagem(sb.toString());
        mail.addDestinatario(smtp.get("mail_to"));
        
        mailEvent.fire(mail); // Disparo de Evento CDI - Observado em SendMailObserver.java
    }
    
    public void setSystemInitiated(){
        this.systemInitiated = true;
    }
    
}