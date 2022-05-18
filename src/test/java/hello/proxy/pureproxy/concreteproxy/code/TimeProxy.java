package hello.proxy.pureproxy.concreteproxy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeProxy extends ConcreteLogic{

    private final ConcreteLogic realLogic;

    public TimeProxy(ConcreteLogic realLogic) {
        this.realLogic = realLogic;
    }

    @Override
    public String operation() {
        log.info("Time Decorator 시작");
        long startTime = System.currentTimeMillis();

        String result = realLogic.operation();

        sleep(1000);

        long endTime = System.currentTimeMillis();

        long resultTime = endTime - startTime;
        log.info("Time Decorator 소요시간 : {}", resultTime);
        return result;
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
