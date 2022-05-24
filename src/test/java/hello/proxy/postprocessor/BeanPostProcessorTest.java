package hello.proxy.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
public class BeanPostProcessorTest {

    @Test
    void test(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(BeanPostProcessorConfig.class);
        // A이름으로 B가 등록된다.

        B beanB = ac.getBean("beanA", B.class);

        beanB.helloB();

        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> ac.getBean(A.class));


    }


    @Configuration
    static class BeanPostProcessorConfig {

        @Bean(name = "beanA")
        public A a(){
            return new A();
        }

        @Bean
        public AToBPostProcessor aToBPostProcessor(){
            return new AToBPostProcessor();
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

    static class AToBPostProcessor implements BeanPostProcessor{
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

            log.info("beanName = {}, beanType ={}",beanName,bean);

            if(bean instanceof A){
                return new B();
            }
            return bean;
        }
    }




}
