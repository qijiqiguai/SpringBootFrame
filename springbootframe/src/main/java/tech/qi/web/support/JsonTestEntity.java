package tech.qi.web.support;

import com.fasterxml.jackson.annotation.JsonView;
import tech.qi.core.SecurityContextHelper;
import tech.qi.entity.User;
import tech.qi.entity.support.JsonId;
import tech.qi.entity.support.View;
import tech.qi.util.Util;

/**
 *
 * @author wangqi
 * @date 2017/12/28 下午4:36
 */
public class JsonTestEntity {
    public static final String HASH_ID_PREFIX = "test-";

    public JsonTestEntity() {
        this.user = SecurityContextHelper.getCurrentUser();
        this.adminSummary = "adminSummary";
        this.adminDetail = "adminDetail";
        this.commonSummary = "commonSummary";
        this.commonDetail = "commonDetail";
        this.hidden = "hidden";
    }

    @JsonView({View.Admin.Summary.class})
    @JsonId
    private User user;

    /**
     * 在Controller上启动JsonView之后，不注JsonView的默认是不显示的。
     */
    private String hidden;

    @JsonView({View.Admin.Summary.class})
    private String adminSummary;

    @JsonView({View.Admin.Detail.class})
    private String adminDetail;

    @JsonView(View.Common.Summary.class)
    private String commonSummary;

    @JsonView(View.Common.Detail.class)
    private String commonDetail;

    @JsonView({View.Common.Detail.class, View.Admin.Summary.class})
    public String getHashid() {
        return HASH_ID_PREFIX + Util.alphabetHashidEncode(user.getId().longValue());
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAdminSummary() {
        return adminSummary;
    }

    public void setAdminSummary(String adminSummary) {
        this.adminSummary = adminSummary;
    }

    public String getAdminDetail() {
        return adminDetail;
    }

    public void setAdminDetail(String adminDetail) {
        this.adminDetail = adminDetail;
    }

    public String getCommonSummary() {
        return commonSummary;
    }

    public void setCommonSummary(String commonSummary) {
        this.commonSummary = commonSummary;
    }

    public String getCommonDetail() {
        return commonDetail;
    }

    public void setCommonDetail(String commonDetail) {
        this.commonDetail = commonDetail;
    }

    public String getHidden() {
        return hidden;
    }

    public void setHidden(String hidden) {
        this.hidden = hidden;
    }
}
