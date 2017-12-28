package tech.qi.web;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tech.qi.core.ServiceException;
import tech.qi.core.security.RequestLimit;
import tech.qi.entity.support.View;
import tech.qi.web.support.JsonTestEntity;

import java.util.HashMap;
import java.util.Map;
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


    @JsonView(View.Common.Summary.class)
    @RequestMapping(value = "/pub/json_common_summary", method = RequestMethod.GET)
    public Map<String, Object> jsonCommonSummary() {
        Map<String, Object> map = new HashMap<>(1);
        map.put("json", new JsonTestEntity());
        return map;
    }

    @JsonView({View.Common.Detail.class})
    @RequestMapping(value = "/pub/json_common_detail", method = RequestMethod.GET)
    public Map<String, Object> jsonCommonDetail() {
        Map<String, Object> map = new HashMap<>(1);
        map.put("json", new JsonTestEntity());
        return map;
    }

    @JsonView(View.Admin.Summary.class)
    @RequestMapping(value = "/pub/json_admin_summary", method = RequestMethod.GET)
    public Map<String, Object> jsonAdminSummary() {
        Map<String, Object> map = new HashMap<>(1);
        map.put("json", new JsonTestEntity());
        return map;
    }

    @JsonView(View.Admin.Detail.class)
    @RequestMapping(value = "/pub/json_admin_detail", method = RequestMethod.GET)
    public Map<String, Object> jsonAdminDetail() {
        Map<String, Object> map = new HashMap<>(1);
        map.put("json", new JsonTestEntity());
        return map;
    }
}
