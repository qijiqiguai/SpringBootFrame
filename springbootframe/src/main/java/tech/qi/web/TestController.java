package tech.qi.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tech.qi.core.ServiceException;
import tech.qi.security.RequestLimit;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author wangqi
 */
@RestController
public class TestController {
    public AtomicInteger started = new AtomicInteger();
    public AtomicInteger ended = new AtomicInteger();

    @RequestMapping("/pub/graceful_shutdown_test")
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

    @RequestMapping("/pub/exception")
    @ResponseBody
    public String exception() {
        throw new ServiceException("Test exception during request");
    }

    @RequestMapping("/api/v1/have_role")
    @ResponseBody
    public String haveRole() {
        return "have_role";
    }

    @RequestLimit(count=2, time=10000)
    @RequestMapping("/common_api")
    @ResponseBody
    public String commonApi() {
        return "Common API";
    }

}
