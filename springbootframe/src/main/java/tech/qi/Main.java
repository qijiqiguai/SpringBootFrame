package tech.qi;

import org.apache.catalina.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.annotation.AsyncConfigurer;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author wangqi
 * @date 2017/12/11 下午2:35
 */
@ComponentScan(basePackages = {"tech.qi"})
@SpringBootApplication
public class Main implements AsyncConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(GracefulShutdown.class);
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Main.class, args);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                logger.info("Shutdown @ " + System.currentTimeMillis());
            }
        }));
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadFactory namedFactory = new ThreadFactory() {
            AtomicInteger counter = new AtomicInteger();
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("Async-Runner-" + counter.addAndGet(1));
                return thread;
            }
        };
        ExecutorService executorService = new ThreadPoolExecutor(
                10, Runtime.getRuntime().availableProcessors() * 2, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(200), namedFactory, new ThreadPoolExecutor.AbortPolicy());
        return executorService;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }

    /**
     * 优雅停机：ApplicationListener，用于接受shutdown事件
     * @return
     */
    @Bean
    public GracefulShutdown gracefulShutdown() {
        return new GracefulShutdown();
    }

    /**
     * 优雅停机：用于注入 connector
     * @return
     */
    @Bean
    public EmbeddedServletContainerCustomizer tomcatCustomizer() {
        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                if (container instanceof TomcatEmbeddedServletContainerFactory) {
                    ((TomcatEmbeddedServletContainerFactory) container).addConnectorCustomizers(gracefulShutdown());
                }
            }
        };
    }

    /**
     * 优雅停机：核心类
     */
    private static class GracefulShutdown implements TomcatConnectorCustomizer,
            ApplicationListener<ContextClosedEvent>
    {
        private static final Logger log = LoggerFactory.getLogger(GracefulShutdown.class);
        private volatile Connector connector;
        private final int waitTimeS = 100;

        @Override
        public void customize(Connector connector) {
            this.connector = connector;
        }

        @Override
        public void onApplicationEvent(ContextClosedEvent event) {
            this.connector.pause();
            Executor executor = this.connector.getProtocolHandler().getExecutor();
            if (executor instanceof ThreadPoolExecutor) {
                try {
                    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
                    threadPoolExecutor.shutdown();
                    if (!threadPoolExecutor.awaitTermination(waitTimeS, TimeUnit.SECONDS)) {
                        log.warn("Tomcat thread pool did not shut down gracefully within "
                                + waitTimeS + " seconds. Proceeding with forceful shutdown");
                    }
                }
                catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
