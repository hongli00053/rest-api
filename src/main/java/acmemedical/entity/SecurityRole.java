/********************************************************************************************************
 * File:  SecurityRole.java Course Materials CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author Hongli Ren, Group 8
 * @date modified 2025-07-14
 */
package acmemedical.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * Role class used for (JSR-375) Jakarta EE Security authorization/authentication
 */
// TODO SR01 - Make this into JPA entity and add all necessary annotations inside the class.
@NamedQuery(
	    name = "SecurityRole.findByName",
	    query = "SELECT r FROM SecurityRole r WHERE r.roleName = :name"
	)
@Entity
@Table(name = "security_role")
public class SecurityRole implements Serializable {
    /** Explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

    // TODO SR02 - Add annotations.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    protected int id;

    // TODO SR03 - Add annotations.
    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 45)
    protected String roleName;

    // TODO SR04 - Add annotations.
    @ManyToMany(mappedBy = "roles", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected Set<SecurityUser> users = new HashSet<>();

    public SecurityRole() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    @JsonIgnore
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    public Set<SecurityUser> getUsers() {
        return users;
    }

    public void setUsers(Set<SecurityUser> users) {
        this.users = users;
    }

    public void addUserToRole(SecurityUser user) {
        getUsers().add(user);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        return prime * result + Objects.hash(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (obj instanceof SecurityRole otherSecurityRole) {
            return Objects.equals(this.getId(), otherSecurityRole.getId());
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SecurityRole [id = ").append(id).append(", ");
        if (roleName != null)
            builder.append("roleName = ").append(roleName);
        builder.append("]");
        return builder.toString();
    }
}
