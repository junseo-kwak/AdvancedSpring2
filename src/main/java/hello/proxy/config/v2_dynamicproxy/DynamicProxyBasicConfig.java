package hello.proxy.config.v2_dynamicproxy;

import hello.proxy.app.v1.*;
import hello.proxy.config.v2_dynamicproxy.handler.LogTraceBasicHandler;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;

@Configuration
public class DynamicProxyBasicConfig {

    @Bean
    OrderRepositoryV1 orderRepository(LogTrace trace){
        OrderRepositoryV1 orderRepositoryImpl = new OrderRepositoryV1Impl();

        OrderRepositoryV1 proxy = (OrderRepositoryV1) Proxy.newProxyInstance(OrderRepositoryV1.class.getClassLoader(),
                new Class[]{OrderRepositoryV1.class},
                new LogTraceBasicHandler(trace, orderRepositoryImpl));

        return proxy;
    }

    @Bean
    OrderServiceV1 orderService(LogTrace trace){
        OrderServiceV1 orderServiceV1Impl = new OrderServiceV1Impl(orderRepository(trace));

        OrderServiceV1 proxy = (OrderServiceV1) Proxy.newProxyInstance(OrderServiceV1.class.getClassLoader(),
                new Class[]{OrderServiceV1.class},
                new LogTraceBasicHandler(trace, orderServiceV1Impl));
        return proxy;
    }

    @Bean
    OrderControllerV1 orderController(LogTrace trace){
        OrderControllerV1 orderControllerV1Impl = new OrderControllerV1Impl(orderService(trace));

        OrderControllerV1 proxy = (OrderControllerV1) Proxy.newProxyInstance(OrderControllerV1.class.getClassLoader(),
                new Class[]{OrderControllerV1.class},
                new LogTraceBasicHandler(trace, orderControllerV1Impl));

        return proxy;
    }

}
