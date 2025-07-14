/********************************************************************************************************
 * File:  SecurityUser.java Course Materials CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author Chengcheng Xiong, Group 8
 * @date modified 2025-07-14
 */
package acmemedical.entity;

import java.io.Serializable;
import java.security.Principal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.NamedQuery;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * User class used for (JSR-375) Jakarta EE Security authorization/authentication
 */
// TODO SU01 - Make this into JPA entity and add all the necessary annotations inside the class.
@Entity
@Table(name = "security_user")
@NamedQuery(
	    name = "SecurityUser.findWithRolesAndPhysician",
	    query = "SELECT u FROM SecurityUser u LEFT JOIN FETCH u.roles LEFT JOIN FETCH u.physician WHERE u.username = :username"
	)
public class SecurityUser implements Serializable, Principal {
    private static final long serialVersionUID = 1L;

    // TODO SU02 - Add annotations.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    protected int id;

    // TODO SU03 - Add annotations.
    @Basic(optional = false)
    @Column(name = "username", nullable = false, length = 100)
    protected String username;

    // TODO SU04 - Add annotations.
    @Basic(optional = false)
    @Column(name = "password_hash", nullable = false, length = 256)
    @JsonIgnore
    protected String pwHash;

    // TODO SU05 - Add annotations.
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "physician_id")
    protected Physician physician;

    // TODO SU06 - Add annotations.
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
        name = "user_has_role",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    )
    protected Set<SecurityRole> roles = new HashSet<>();

    public SecurityUser() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwHash() {
        return pwHash;
    }
    @JsonIgnore
    public void setPwHash(String pwHash) {
        this.pwHash = pwHash;
    }

    // TODO SU07 - Setup custom JSON serializer
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_has_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    public Set<SecurityRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<SecurityRole> roles) {
        this.roles = roles;
    }
    
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "physician_id")
    public Physician getPhysician() {
        return physician;
    }

    public void setPhysician(Physician physician) {
        this.physician = physician;
    }

    // Principal
    @Override
    public String getName() {
        return getUsername();
    }
}