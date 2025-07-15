/********************************************************************************************************
 * File:  Physician.java Course Materials CST 8277
 *
 * @author Teddy Yap
 * @author Chengcheng Xiong, Group 8
 * @date modified 2025-07-14
 */
package acmemedical.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.NamedQuery;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

/**
 * The persistent class for the physician database table.
 */
@SuppressWarnings("unused")
// TODO PH01 - Add the missing annotations.
@Entity
@Table(name = "physician")
// TODO PH02 - Do we need a mapped super class? If so, which one? Already extends PojoBase
@NamedQuery(
	    name = "Physician.findWithPrescriptionsAndCertificates",
	    query = "SELECT p FROM Physician p LEFT JOIN FETCH p.prescriptions LEFT JOIN FETCH p.medicalCertificates WHERE p.id = :id"
	)
@NamedQuery(name = "Physician.findAll", query = "SELECT p FROM Physician p")
@NamedQuery(name = "Physician.findById", query = "SELECT p FROM Physician p WHERE p.id = :id")
public class Physician extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	public Physician() {
		super();
	}

	// TODO PH03 - Add annotations.
	@Basic(optional = false)
	@Column(name = "first_name", nullable = false, length = 50)
	private String firstName;

	// TODO PH04 - Add annotations.
	@Basic(optional = false)
	@Column(name = "last_name", nullable = false, length = 50)
	private String lastName;

	// TODO PH05 - Add annotations for 1:M relation. What should be the cascade and fetch types?
	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<MedicalCertificate> medicalCertificates = new HashSet<>();

	// TODO PH06 - Add annotations for 1:M relation. What should be the cascade and fetch types?
	@OneToMany(mappedBy = "physician", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Prescription> prescriptions = new HashSet<>();

	// One-to-one mapping with SecurityUser. Physician is the inverse side, the foreign key is maintained by SecurityUser.
	// This allows you to get the associated SecurityUser via physician.getSecurityUser().
	@OneToOne(mappedBy = "physician", fetch = FetchType.LAZY)
	@JsonIgnore // Prevent recursive serialization
	private SecurityUser securityUser;

	public SecurityUser getSecurityUser() {
		return securityUser;
	}

	public void setSecurityUser(SecurityUser securityUser) {
		this.securityUser = securityUser;
	}

	// TODO PH07 - Is an annotation needed here? No — standard getter.
	@JsonIgnore
	@OneToMany(mappedBy = "physician", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public Set<MedicalCertificate> getMedicalCertificates() {
		return medicalCertificates;
	}

	public void setMedicalCertificates(Set<MedicalCertificate> medicalCertificates) {
		this.medicalCertificates = medicalCertificates;
	}

	// TODO PH08 - Is an annotation needed here? No — standard getter.

	@JsonIgnore
	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public Set<Prescription> getPrescriptions() {
		return prescriptions;
	}

	public void setPrescriptions(Set<Prescription> prescriptions) {
		this.prescriptions = prescriptions;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setFullName(String firstName, String lastName) {
		setFirstName(firstName);
		setLastName(lastName);
	}

	// Inherited hashCode/equals is sufficient for this entity class
}
