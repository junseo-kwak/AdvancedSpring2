package hello.proxy.config.v1_proxy.interface_proxy;

import hello.proxy.app.v1.OrderRepositoryV1;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderRepositoryInterfaceProxy implements OrderRepositoryV1{

    private final LogTrace trace;
    private final OrderRepositoryV1 orderRepository;

    @Override
    public void save(String itemId) {
        TraceStatus status = null;

        try{
             status = trace.begin("OrderRepository.save()");

            orderRepository.save(itemId);
            trace.end(status);
        }catch(Exception e){
            trace.exception(status,e);
            throw e;
        }

    }
}
