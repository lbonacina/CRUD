package CRUD.util;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: eluibon
 * Date: 22/03/13
 * Time: 21.16
 *
 * simple CDI interception to track creation & destruction of CDI beans
 * enable it by declaring it on beans.xml & using annotation on beans to be tracked
 *
 * <beans>
 *     <interceptors>
 *         <class>CRUD.util.BeanLifeCycleTrackerInterceptor</class>
 *     </interceptors>
 * </beans>
 *
 * NB: interception must be serializable if the tracked bean is declaring a passivating scope (ie not requestscoped)
 *     bean must be of course itself serializable
 */
@Interceptor
@BeanLifecycleTracker
public class BeanLifeCycleTrackerInterceptor implements Serializable {


    private static final long serialVersionUID = 7024786313073838978L;
    @Inject Logger log ;

    @PostConstruct
    public void postConstructTracker(InvocationContext ctx) throws Exception {
        log.info("\npostConstructTracker : " + ctx.getTarget() + "\n");
        ctx.proceed();
    }

    @PreDestroy
    public void preDestroyTracker(InvocationContext ctx) throws Exception {
        log.info("\npreDestroyTracker : " + ctx.getTarget() + "\n");
        ctx.proceed() ;
    }

}
