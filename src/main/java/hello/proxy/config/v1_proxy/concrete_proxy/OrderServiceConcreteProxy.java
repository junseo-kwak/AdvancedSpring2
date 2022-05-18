package hello.proxy.config.v1_proxy.concrete_proxy;


import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;

public class OrderServiceConcreteProxy extends OrderServiceV2 {

    private final LogTrace trace;
    private final OrderServiceV2 orderService;

    public OrderServiceConcreteProxy(OrderRepositoryV2 orderRepository, LogTrace trace, OrderServiceV2 orderService) {
        super(orderRepository);
        this.trace = trace;
        this.orderService = orderService;
    }

    @Override
    public void orderItem(String itemId) {
        TraceStatus status = null;
        try{
            status = trace.begin("OrderServiceConcreteProxy.orderItem()");

            orderService.orderItem(itemId);

            trace.end(status);

        }catch(Exception e){
            trace.exception(status,e);
            throw e;
        }

    }
}
