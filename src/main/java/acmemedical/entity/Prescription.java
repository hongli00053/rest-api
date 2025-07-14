/********************************************************************************************************
 * File:  Prescription.java Course Materials CST 8277
 *
 * @author Teddy Yap
 * @author Chengcheng Xiong, Group 8
 * @date modified 2025-07-14
 */
package acmemedical.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * The persistent class for the prescription database table.
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "prescription")
@Access(AccessType.FIELD)
@NamedQuery(name = "Prescription.findAll", query = "SELECT p FROM Prescription p")
public class Prescription extends PojoBaseCompositeKey<PrescriptionPK> implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PrescriptionPK id;

	@MapsId("physicianId")
	@ManyToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "physician_id", referencedColumnName = "id", nullable = false)
	private Physician physician;

	// TODO PR01 - Add missing annotations.
	@MapsId("patientId")
	@ManyToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", referencedColumnName = "id", nullable = false)
	private Patient patient;

	// TODO PR02 - Add missing annotations.
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "medicine_id", referencedColumnName = "id")
	private Medicine medicine;

	@Column(name = "number_of_refills")
	private int numberOfRefills;

	@Column(name = "prescription_information", length = 100)
	private String prescriptionInformation;

	public Prescription() {
		id = new PrescriptionPK();
	}

	@Override
	public PrescriptionPK getId() {
		return id;
	}

	@Override
	public void setId(PrescriptionPK id) {
		this.id = id;
	}
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "physician_id")
	public Physician getPhysician() {
		return physician;
	}

	public void setPhysician(Physician physician) {
		id.setPhysicianId(physician.getId());
		this.physician = physician;
	}
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id")
	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		id.setPatientId(patient.getId());
		this.patient = patient;
	}

	public Medicine getMedicine() {
		return medicine;
	}

	public void setMedicine(Medicine medicine) {
		this.medicine = medicine;
	}

	public int getNumberOfRefills() {
		return numberOfRefills;
	}

	public void setNumberOfRefills(int numberOfRefills) {
		this.numberOfRefills = numberOfRefills;
	}

	public String getPrescriptionInformation() {
		return prescriptionInformation;
	}

	public void setPrescriptionInformation(String prescriptionInformation) {
		this.prescriptionInformation = prescriptionInformation;
	}

	// Inherited hashCode/equals is sufficient for this entity class
}
