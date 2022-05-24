package hello.proxy.config.v3_proxyfactory;


import hello.proxy.app.v1.*;
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
public class ProxyFactoryConfigV1 {

    private static final String[] PATTERNS = {"request*","order*","save*"};


    @Bean
    OrderRepositoryV1 orderRepository(LogTrace logTrace){
        OrderRepositoryV1 target = new OrderRepositoryV1Impl();
        ProxyFactory proxyFactory = new ProxyFactory(target);

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames(PATTERNS);

        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, new LogTraceAdvice(logTrace));
        proxyFactory.addAdvisor(advisor);
        OrderRepositoryV1 proxy = (OrderRepositoryV1) proxyFactory.getProxy();
        log.info("targetClass = {}, proxyClass = {}",target.getClass(),proxy.getClass());
        return proxy;
    }

    @Bean
    OrderServiceV1 orderService(LogTrace logTrace){
        OrderServiceV1 target = new OrderServiceV1Impl(orderRepository(logTrace));
        ProxyFactory proxyFactory = new ProxyFactory(target);
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(Pointcut.TRUE, new LogTraceAdvice(logTrace));
        proxyFactory.addAdvisor(advisor);
        OrderServiceV1 proxy = (OrderServiceV1) proxyFactory.getProxy();
        log.info("targetClass = {}, proxyClass = {}",target.getClass(),proxy.getClass());
        return proxy;
    }
    @Bean
    OrderControllerV1 orderController(LogTrace logTrace){
        OrderControllerV1 target = new OrderControllerV1Impl(orderService(logTrace));
        ProxyFactory proxyFactory = new ProxyFactory(target);
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames(PATTERNS);
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, new LogTraceAdvice(logTrace));
        proxyFactory.addAdvisor(advisor);
        OrderControllerV1 proxy = (OrderControllerV1) proxyFactory.getProxy();
        log.info("targetClass = {}, proxyClass = {}",target.getClass(),proxy.getClass());
        return proxy;
    }




}
