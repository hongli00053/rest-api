/********************************************************************************************************
 * File:  MedicalTraining.java Course Materials CST 8277
 *
 * @author Teddy Yap
 * @author Chengcheng Xiong, Group 8
 * @date modified 2025-07-14
 */
package acmemedical.entity;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Embedded;
import jakarta.persistence.NamedQuery;

@SuppressWarnings("unused")

/**
 * The persistent class for the medical_training database table.
 */
//TODO MT01 - Add the missing annotations.
@Entity
@Table(name = "medical_training")
@AttributeOverride(name = "id", column = @Column(name = "training_id"))
//TODO MT02 - Do we need a mapped super class? If so, which one? Already extends PojoBase
@NamedQuery(name = "MedicalTraining.findAll", query = "SELECT t FROM MedicalTraining t")
@NamedQuery(name = "MedicalTraining.findById", query = "SELECT t FROM MedicalTraining t WHERE t.id = :param1")
public class MedicalTraining extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	// TODO MT03 - Add annotations for M:1. What should be the cascade and fetch types?
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "school_id", nullable = false)
	private MedicalSchool school;

	// TODO MT04 - Add annotations for 1:1. What should be the cascade and fetch types?
	@JsonIgnore
	@OneToOne(mappedBy = "medicalTraining", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private MedicalCertificate certificate;

	@Embedded
	private DurationAndStatus durationAndStatus;

	public MedicalTraining() {
		durationAndStatus = new DurationAndStatus();
	}
	@JsonIgnore
	public MedicalSchool getMedicalSchool() {
		return school;
	}

	public void setMedicalSchool(MedicalSchool school) {
		this.school = school;
	}

	public MedicalCertificate getCertificate() {
		return certificate;
	}

	public void setCertificate(MedicalCertificate certificate) {
		this.certificate = certificate;
	}

	public DurationAndStatus getDurationAndStatus() {
		return durationAndStatus;
	}

	public void setDurationAndStatus(DurationAndStatus durationAndStatus) {
		this.durationAndStatus = durationAndStatus;
	}

	// Inherited hashCode/equals NOT sufficient for this Entity class
	/**
	 * Very important:  Use getter's for member variables because JPA sometimes needs to intercept those calls<br/>
	 * and go to the database to retrieve the value
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		return prime * result + Objects.hash(getId(), getDurationAndStatus());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof MedicalTraining otherMedicalTraining) {
			return Objects.equals(this.getId(), otherMedicalTraining.getId()) &&
				Objects.equals(this.getDurationAndStatus(), otherMedicalTraining.getDurationAndStatus());
		}
		return false;
	}

    // NamedQuery constant for finding by ID
    public static final String FIND_BY_ID = "MedicalTraining.findById";
}
