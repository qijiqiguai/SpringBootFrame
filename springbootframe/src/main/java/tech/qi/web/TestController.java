package tech.qi.web;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tech.qi.core.SecurityContextHelper;
import tech.qi.core.ServiceException;
import tech.qi.core.security.RequestLimit;
import tech.qi.dal.repository.PermissionRepository;
import tech.qi.dal.repository.SampleRepositoryMyBatis;
import tech.qi.entity.security.Permission;
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
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    SampleRepositoryMyBatis myBatisRepo;
    @Autowired
    PermissionRepository permissionRepository;

    public AtomicInteger started = new AtomicInteger();
    public AtomicInteger ended = new AtomicInteger();

    @RequestMapping("/pub/test_mybatis")
    @ResponseBody
    public String testMyBatis() {
        return "MyBatis:" + JSONObject.toJSONString(myBatisRepo.getAllPermission());
    }

    @RequestMapping("/pub/test_paging")
    @ResponseBody
    public String testPaging() {
        Pageable pageable = new PageRequest(0, 5);
        return "Paging:" + JSONObject.toJSONString(permissionRepository.findAll(pageable));
    }

    @RequestMapping("/pub/test_jpa_sql_update")
    @ResponseBody
    public String testJpaUpdate() {
        permissionRepository.updateName(1, "newName:" + System.currentTimeMillis());
        return "test_jpa_sql_update";
    }

    @RequestMapping("/pub/test_jpa_new")
    @ResponseBody
    public String testJpaNew() {
        Permission permission = new Permission();
        permission.setName("newPermission-" + System.currentTimeMillis());
        permission.setDisplayName("newPermission:" + System.currentTimeMillis());
        permission.setParent( permissionRepository.findById(1) );
        permissionRepository.save(permission);
        return "test_jpa_new";
    }

    @RequestMapping("/pub/graceful_shutdown_test")
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
    public String exception() {
        throw new ServiceException("Test exception during request");
    }


    @RequestMapping("/api/v1/have_role")
    @ResponseBody
    public String haveRole() {
        return "Have Role:" + JSONObject.toJSONString(SecurityContextHelper.getCurrentUser().getAuthorities());
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
