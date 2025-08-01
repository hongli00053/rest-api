/********************************************************************************************************
 * File:  MedicalCertificate.java Course Materials CST 8277
 *
 * @author Teddy Yap
 * @author Chengcheng Xiong, Group 8
 * @date modified 2025-07-14
 */
package acmemedical.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * The persistent class for the medical_certificate database table.
 */
//TODO MC01 - Add the missing annotations.
@Entity
@Table(name = "medical_certificate")
@AttributeOverride(name = "id", column = @Column(name = "certificate_id"))
//TODO MC02 - Do we need a mapped super class?  If so, which one?
// Already extends PojoBase
@NamedQueries({
    @NamedQuery(name = "MedicalCertificate.findAll", query = "SELECT c FROM MedicalCertificate c"),
    @NamedQuery(name = "MedicalCertificate.findByPhysicianId", query = "SELECT c FROM MedicalCertificate c WHERE c.owner.id = :physicianId"),
    @NamedQuery(name = "MedicalCertificate.findById", query = "SELECT c FROM MedicalCertificate c WHERE c.id = :param1")
})
public class MedicalCertificate extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	// TODO MC03 - Add annotations for 1:1 mapping.  What should be the cascade and fetch types?
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "training_id", unique = true, nullable = true)
	private MedicalTraining medicalTraining;

	// TODO MC04 - Add annotations for M:1 mapping.  What should be the cascade and fetch types?
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "physician_id", nullable = true)
	private Physician owner;
	
	// TODO MC05 - Add annotations.
	@Column(name = "signed")
	private byte signed;

	// NamedQuery constant for finding by ID card
	public static final String ID_CARD_QUERY_NAME = "MedicalCertificate.findByIdCard";

	public MedicalCertificate() {
		super();
	}

	public MedicalCertificate(MedicalTraining medicalTraining, Physician owner, byte signed) {
		this();
		this.medicalTraining = medicalTraining;
		this.owner = owner;
		this.signed = signed;
	}

	public MedicalTraining getMedicalTraining() {
		return medicalTraining;
	}

	public void setMedicalTraining(MedicalTraining medicalTraining) {
		this.medicalTraining = medicalTraining;
	}
	

	public Physician getOwner() {
		return owner;
	}

	public void setOwner(Physician owner) {
		this.owner = owner;
	}

	public byte getSigned() {
		return signed;
	}

	public void setSigned(byte signed) {
		this.signed = signed;
	}
	@JsonIgnore
	public void setSigned(boolean signed) {
		this.signed = (byte) (signed ? 0b0001 : 0b0000);
	}

	// Inherited hashCode/equals is sufficient for this entity class
}
