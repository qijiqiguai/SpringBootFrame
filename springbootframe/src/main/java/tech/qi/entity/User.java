package tech.qi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tech.qi.core.Constants;
import tech.qi.entity.security.Permission;
import tech.qi.entity.security.Role;
import tech.qi.entity.support.AbstractAuditableEntity;
import javax.persistence.*;
import java.util.*;


/**
 * @author wangqi
 */
@Entity
public class User extends AbstractAuditableEntity implements UserDetails {
    private static final long serialVersionUID = 6868025295491429059L;

    /**
     * 微信OpenID
     */
    private String wxOpenId;

    /**
     * 手机号必须是唯一的
     */
    @Column(unique = true)
    private String phone;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles = new LinkedHashSet<>();

    /**
     * 表示用户当前状态是否是因为领取优惠券等原因预注册但尚未正式注册的状态，这类用户不能直接登录.
     * 初始预注册的时候enabled为false。如果用户后期从enabled为false的状态要改为enable的话，
     * 需要检查preRegister字段，来区分是否是可以enable.
     * 正式注册的时候，需要把用户enable, 并且preRegister设为false。
     */
    private boolean preRegister;

    /**
     * 来源信息，通过推广活动来的可以记录来源。
     */
    private Long referenceId;

    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private Date lastLoginDate;
    private String lastLoginIp;


    @JsonIgnore
    public Set<Permission> getPermissions() {
        Set<Permission> perms = new HashSet<>();
        for (Role role : roles) {
            perms.addAll(role.getPermissions());
        }
        return perms;
    }

    @JsonIgnore
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.addAll(getRoles());
        authorities.addAll(getPermissions());
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
                Constants.SPRING_SECURITY_ROLE_PREFIX + Constants.SPRING_SECURITY_USER_ROLE);
        authorities.add(authority);
        return authorities;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof User) {
            return username.equals(((User) o).username);
        }
        return false;
    }

    /**
     * Returns the hashcode of the {@code username}.
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + (username != null ? username.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{id=" + getId() + ", username='" + username + "'}";
    }





    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    //  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean isPreRegister() {
        return preRegister;
    }

    public void setPreRegister(boolean preRegister) {
        this.preRegister = preRegister;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }
}
