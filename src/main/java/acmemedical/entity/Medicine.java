/********************************************************************************************************
 * File:  Medicine.java Course Materials CST 8277
 *
 * @author Teddy Yap
 * @author Mohammad Abdelqadiredris, Group 8
 * @date modified 2025-07-14
 */
package acmemedical.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * The persistent class for the medicine database table.
 */
@Entity
@Table(name = "medicine")
@NamedQuery(name = "Medicine.findAll", query = "SELECT m FROM Medicine m")
@NamedQuery(name = "Medicine.findById", query = "SELECT m FROM Medicine m WHERE m.id = :param1")
@AttributeOverride(name = "id", column = @Column(name = "medicine_id"))
public class Medicine extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@Column(name = "drug_name", nullable = false, length = 50)
	private String drugName;

	@Basic(optional = false)
	@Column(name = "manufacturer_name", nullable = false, length = 50)
	private String manufacturerName;

	@Basic(optional = false)
	@Column(name = "dosage_information", nullable = false, length = 100)
	private String dosageInformation;

	@Transient
	private String chemicalName;

	@Transient
	private String genericName;

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "medicine")
	@JsonIgnore
	private Set<Prescription> prescriptions = new HashSet<>();

	public Medicine() {
		super();
	}

	public Medicine(String drugName, String manufacturerName, String dosageInformation, Set<Prescription> prescriptions) {
		this();
		this.drugName = drugName;
		this.manufacturerName = manufacturerName;
		this.dosageInformation = dosageInformation;
		this.prescriptions = prescriptions;
	}

	public String getDrugName() {
		return drugName;
	}

	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public String getDosageInformation() {
		return dosageInformation;
	}

	public void setDosageInformation(String dosageInformation) {
		this.dosageInformation = dosageInformation;
	}

	public String getChemicalName() {
		return chemicalName;
	}

	public void setChemicalName(String chemicalName) {
		this.chemicalName = chemicalName;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public Set<Prescription> getPrescriptions() {
		return prescriptions;
	}

	public void setPrescriptions(Set<Prescription> prescriptions) {
		this.prescriptions = prescriptions;
	}

	public void setMedicine(String drugName, String manufacturerName, String dosageInformation) {
		setDrugName(drugName);
		setManufacturerName(manufacturerName);
		setDosageInformation(dosageInformation);
	}

	// Inherited hashCode/equals is sufficient for this entity class
}
