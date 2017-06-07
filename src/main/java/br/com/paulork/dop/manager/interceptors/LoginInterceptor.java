package br.com.paulork.dop.manager.interceptors;

import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.com.caelum.vraptor.view.Results;
import br.com.paulork.dop.manager.Sessao;
import br.com.paulork.dop.manager.qualifiers.Public;
import java.lang.annotation.Annotation;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@Intercepts
public class LoginInterceptor {

    @Inject private Result result;
    @Inject private ControllerMethod controllerMethod;
    @Inject private Sessao userSession;
    
    @Inject private HttpServletRequest request;

    @Accepts
    public boolean accepts() {
        boolean exist = true;
        Annotation[] annotations = controllerMethod.getController().getAnnotations();
        for (Annotation annotation : annotations) {
            if(annotation.annotationType().equals(Public.class)){
                return false;
            }
        }
        return exist;
    }

    @AroundCall
    public void intercept(SimpleInterceptorStack stack) {
        boolean isAjax = (request.getHeader("Ajax") == null ? false : Boolean.valueOf(request.getHeader("Ajax")));
        
        if(userSession.isLogged()){
            stack.next();
        } else {
            if(isAjax){
                result.use(Results.http()).body("Sua sessão expirou! Redirecionando para o login...").setStatusCode(403);
                return;
            } else {
                result.redirectTo("/login");
                return;
            }
        }
    }
    
}
