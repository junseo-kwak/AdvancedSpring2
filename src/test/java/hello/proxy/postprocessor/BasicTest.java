package hello.proxy.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
public class BasicTest {

    @Test
    void test(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(BasicConfig.class);
        // A는 빈으로 등록된다.
        A beanA = ac.getBean("beanA", A.class);

        beanA.helloA();

        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> ac.getBean(B.class));


    }


    @Configuration
    static class BasicConfig{

        @Bean(name = "beanA")
        public A a(){
            return new A();
        }

        public B b(){
            return new B();
        }

    }

    static class A{
        public void helloA(){
            log.info("helloA");
        }
    }

    static class B{
        public void helloB(){
            log.info("helloB");
        }
    }




}
