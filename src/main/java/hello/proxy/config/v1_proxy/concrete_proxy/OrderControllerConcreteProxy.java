package hello.proxy.config.v1_proxy.concrete_proxy;

import hello.proxy.app.v2.OrderControllerV2;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;

public class OrderControllerConcreteProxy extends OrderControllerV2 {

    private final LogTrace trace;
    private final OrderControllerV2 target;

    public OrderControllerConcreteProxy(LogTrace trace, OrderControllerV2 orderController) {
        super(null);
        this.trace = trace;
        this.target = orderController;
    }

    @Override
    public String request(String itemId) {
        TraceStatus status = null;
        String result = "";
        try {
            status = trace.begin("orderController.request()");
            result = target.request(itemId);

        }catch(Exception e){
            trace.exception(status,e);
            throw e;
        }

        return result;
    }

    @Override
    public String noLog() {
        TraceStatus status = null;
        String result = "";
        try {
            status = trace.begin("orderController.noLog()");
            result = target.noLog();

        }catch(Exception e){
            trace.exception(status,e);
            throw e;
        }

        return result;
    }
}
