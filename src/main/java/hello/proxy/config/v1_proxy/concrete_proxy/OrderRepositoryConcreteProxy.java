package hello.proxy.config.v1_proxy.concrete_proxy;

import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;


public class OrderRepositoryConcreteProxy extends OrderRepositoryV2 {

    private final LogTrace trace;
    private final OrderRepositoryV2 orderRepository;

    public OrderRepositoryConcreteProxy(LogTrace trace, OrderRepositoryV2 orderRepository) {
        this.trace = trace;
        this.orderRepository = orderRepository;
    }

    @Override
    public void save(String itemId) {
        TraceStatus status = null;
        try{
            status = trace.begin("OrderRepositoryConcreteProxy.save()");

            orderRepository.save(itemId);

            trace.end(status);

        }catch(Exception e){
            trace.exception(status,e);
            throw e;
        }

    }
}
