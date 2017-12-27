package tech.qi.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author wangqi
 */
@Controller
public class TestController {
    public AtomicInteger started = new AtomicInteger();
    public AtomicInteger ended = new AtomicInteger();

    @RequestMapping("/graceful_shutdown_test")
    @ResponseBody
    public String shutdown() {

        System.out.println( Thread.currentThread().getName()
                + " -> " + this + " Get one, got: " + started.addAndGet(1) );
        try {
            Thread.sleep( 1000*1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println( Thread.currentThread().getName()
                + " -> " + this + "  Finish one, finished: " + ended.addAndGet(1) );
        return "hello world";
    }

}
