package br.com.paulork.dop.manager.interceptors;

import java.util.HashMap;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import org.jboss.weld.context.bound.BoundRequestContext;

/**
 * @author Paulo R. Kraemer <paulork10@gmail.com>
 */
@Interceptor
@br.com.paulork.dop.manager.qualifiers.ComEscopo
@Priority(Interceptor.Priority.APPLICATION+50)
public class ComEscopoInterceptor {
    
    @Inject BoundRequestContext brc;

    @AroundInvoke
    public Object pega(InvocationContext ctx) throws Exception {
        brc.associate(new HashMap<>());
        brc.activate();
        Object proceed = ctx.proceed();
        brc.invalidate();
        brc.deactivate();
        return proceed;
    }

}
