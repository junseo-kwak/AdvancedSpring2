package hello.proxy.config.v3_proxyfactory;


import hello.proxy.app.v2.OrderControllerV2;
import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ProxyFactoryConfigV2 {

    private static final String[] PATTERNS = {"request*","order*","save*"};


    @Bean
    OrderRepositoryV2 orderRepository(LogTrace logTrace){
        OrderRepositoryV2 target = new OrderRepositoryV2();
        ProxyFactory proxyFactory = new ProxyFactory(target);

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames(PATTERNS);

        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, new LogTraceAdvice(logTrace));
        proxyFactory.addAdvisor(advisor);
        OrderRepositoryV2 proxy = (OrderRepositoryV2) proxyFactory.getProxy();
        log.info("targetClass = {}, proxyClass = {}",target.getClass(),proxy.getClass());
        return proxy;
    }

    @Bean
    OrderServiceV2 orderService(LogTrace logTrace){
        OrderServiceV2 target = new OrderServiceV2(orderRepository(logTrace));
        ProxyFactory proxyFactory = new ProxyFactory(target);
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(Pointcut.TRUE, new LogTraceAdvice(logTrace));
        proxyFactory.addAdvisor(advisor);
        OrderServiceV2 proxy = (OrderServiceV2) proxyFactory.getProxy();
        log.info("targetClass = {}, proxyClass = {}",target.getClass(),proxy.getClass());
        return proxy;
    }
    @Bean
    OrderControllerV2 orderController(LogTrace logTrace){
        OrderControllerV2 target = new OrderControllerV2(orderService(logTrace));
        ProxyFactory proxyFactory = new ProxyFactory(target);
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames(PATTERNS);
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, new LogTraceAdvice(logTrace));
        proxyFactory.addAdvisor(advisor);
        OrderControllerV2 proxy = (OrderControllerV2) proxyFactory.getProxy();
        log.info("targetClass = {}, proxyClass = {}",target.getClass(),proxy.getClass());
        return proxy;
    }




}
