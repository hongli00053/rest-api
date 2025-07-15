/********************************************************************************************************
 * File:  MedicalSchool.java Course Materials CST 8277
 *
 * @author Teddy Yap
 * @author Chengcheng Xiong, Group 8
 * @date modified 2025-07-14
 */
package acmemedical.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.NamedQuery;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * The persistent class for the medical_school database table.
 */
//TODO MS01 - Add the missing annotations.
@Entity
@Table(name = "medical_school")
//TODO MS02 - MedicalSchool has subclasses PublicSchool and PrivateSchool. Look at Week 9 slides for InheritanceType.
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "school_type", discriminatorType = DiscriminatorType.STRING)
//TODO MS03 - Do we need a mapped super class? If so, which one? Already extends PojoBase

//TODO MS04 - Add in JSON annotations to indicate different sub-classes of MedicalSchool
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "entity-type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = PublicSchool.class, name = "public_school"),
    @JsonSubTypes.Type(value = PrivateSchool.class, name = "private_school")
})
@NamedQuery(name = "MedicalSchool.findAll", query = "SELECT s FROM MedicalSchool s")
@NamedQuery(name = "MedicalSchool.isDuplicate", query = "SELECT COUNT(s) FROM MedicalSchool s WHERE s.name = :name")
@NamedQuery(name = "MedicalSchool.findById", query = "SELECT s FROM MedicalSchool s WHERE s.id = :id")
@NamedQuery(
	    name = "MedicalSchool.findWithTrainings",
	    query = "SELECT s FROM MedicalSchool s LEFT JOIN FETCH s.medicalTrainings WHERE s.id = :id"
	)
public abstract class MedicalSchool extends PojoBase implements Serializable {
    private static final long serialVersionUID = 1L;

    // TODO MS05 - Add the missing annotations.
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    // TODO MS06 - Add the 1:M annotation. What should be the cascade and fetch types?
    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<MedicalTraining> medicalTrainings = new HashSet<>();

    // TODO MS07 - Add missing annotation.
    @Column(name = "public")
    private boolean isPublic;

    // NamedQuery constants for JPA queries
    public static final String ALL_MEDICAL_SCHOOLS_QUERY = "MedicalSchool.findAll";
    public static final String IS_DUPLICATE_QUERY_NAME = "MedicalSchool.isDuplicate";
    public static final String SPECIFIC_MEDICAL_SCHOOL_QUERY_NAME = "MedicalSchool.findWithTrainings";
    public static final String FIND_BY_ID = "MedicalSchool.findById";

    public MedicalSchool() {
        super();
    }

    public MedicalSchool(boolean isPublic) {
        this();
        this.isPublic = isPublic;
    }

    // TODO MS08 - Is an annotation needed here? No, standard getter is sufficient
    @JsonIgnore
    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public Set<MedicalTraining> getMedicalTrainings() {
        return medicalTrainings;
    }

    public void setMedicalTrainings(Set<MedicalTraining> medicalTrainings) {
        this.medicalTrainings = medicalTrainings;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    //Inherited hashCode/equals is NOT sufficient for this entity class

    /**
     * Very important: Use getter's for member variables because JPA sometimes needs to intercept those calls<br/>
     * and go to the database to retrieve the value
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();

        // Only include member variables that really contribute to an object's identity
        return prime * result + Objects.hash(getId(), getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }

        if (obj instanceof MedicalSchool otherMedicalSchool) {
            return Objects.equals(this.getId(), otherMedicalSchool.getId()) &&
                   Objects.equals(this.getName(), otherMedicalSchool.getName());
        }
        return false;
    }
}
