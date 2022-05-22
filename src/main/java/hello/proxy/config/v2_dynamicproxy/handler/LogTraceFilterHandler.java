package hello.proxy.config.v2_dynamicproxy.handler;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
public class LogTraceFilterHandler implements InvocationHandler {

    private final LogTrace logTrace;
    private final Object target;
    private final String[] PATTERNS;

    public LogTraceFilterHandler(LogTrace logTrace, Object target, String[] PATTERNS) {
        this.logTrace = logTrace;
        this.target = target;
        this.PATTERNS = PATTERNS;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if(!PatternMatchUtils.simpleMatch(PATTERNS, method.getName())){
            return method.invoke(target,args);
        }

        String message = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()";
        TraceStatus status = null;
        try{
            status = logTrace.begin(message);
            Object result = method.invoke(target, args);
            logTrace.end(status);
            return result;
        }catch(Exception e){
            logTrace.exception(status,e);
            throw e;
        }
    }
}
