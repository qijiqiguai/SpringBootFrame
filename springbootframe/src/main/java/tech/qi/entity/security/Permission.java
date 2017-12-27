package tech.qi.entity.security;

import org.springframework.security.core.GrantedAuthority;
import tech.qi.entity.support.AbstractAuditableEntity;
import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * @author wangqi
 */
@Entity
public class Permission extends AbstractAuditableEntity implements GrantedAuthority, Comparable<Permission> {

    private static final long serialVersionUID = 1632844349169744054L;

    @Column(nullable = false, unique = true)
    private String name;
    private String displayName;
    private String description;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Permission parent;
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "parent", fetch = FetchType.LAZY)
    private Set<Permission> children = new LinkedHashSet<Permission>();
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<Role> roles = new LinkedHashSet<Role>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Permission getParent() {
        return parent;
    }

    public void setParent(Permission parent) {
        this.parent = parent;
    }


    public Set<Permission> getChildren() {
        return children;
    }

    public void setChildren(Set<Permission> children) {
        this.children = children;
    }


    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public int compareTo(Permission p) {
        return Long.compare(getId(), p.getId());
    }

    @Override
    public String getAuthority() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GrantedAuthority) {
            return (getAuthority().equals(((GrantedAuthority) o).getAuthority()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getAuthority().hashCode();
    }
}
