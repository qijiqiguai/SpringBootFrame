package tech.qi.entity.security;

import tech.qi.entity.User;
import tech.qi.entity.support.AbstractAuditableEntity;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;



/**
 * @author wangqi
 */
@Entity
public class UserLogin extends AbstractAuditableEntity {
    private String lastLoginIp;
    private String clientId;
    private String deviceToken;
    private String deviceType;
    private int userType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
