package tech.qi.entity.security;

import org.springframework.security.core.GrantedAuthority;
import tech.qi.entity.support.AbstractAuditableEntity;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Role extends AbstractAuditableEntity implements GrantedAuthority {

    private static final long serialVersionUID = -8012512398520535511L;

    @Column(nullable = false, unique = true)
    private String name;
    private String displayName;
    private boolean enabled;
    private String description;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permission",
            joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id")})
    private Set<Permission> permissions = new LinkedHashSet<Permission>();


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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }


//	public Set<User> getUsers() { return users; }
//
//	public void setUsers(Set<User> users) {
//		this.users = users;
//	}

    /* (non-Javadoc)
     * @see org.springframework.security.core.GrantedAuthority#getAuthority()
     */
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
//		return getAuthority().hashCode();
        int result = 17;
        result = 37 * result + (getAuthority() != null ? this.getName().hashCode() : 0);
        return result;
    }
}
