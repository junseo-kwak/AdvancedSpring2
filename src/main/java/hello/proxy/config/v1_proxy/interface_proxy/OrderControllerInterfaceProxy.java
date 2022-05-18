package hello.proxy.config.v1_proxy.interface_proxy;

import hello.proxy.app.v1.OrderControllerV1;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderControllerInterfaceProxy implements OrderControllerV1 {

    private final LogTrace trace;
    private final OrderControllerV1 orderController;

    @Override
    public String request(String itemId) {
        TraceStatus status = null;
        try{
            status = trace.begin("OrderController.request()");
            orderController.request(itemId);
            trace.end(status);
            return "OK";
        }catch (Exception e){
            trace.exception(status,e);
            throw e;
        }

    }

    @Override
    public String noLog() {
        return "ok";
    }
}
