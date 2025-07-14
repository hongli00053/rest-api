/********************************************************************************************************
 * File:  PojoBase.java Course Materials CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author Chengcheng Xiong, Group 8
 * @date modified 2025-07-14
 */
package acmemedical.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

/**
 * Abstract class that is base of (class) hierarchy for all @Entity classes
 */
// TODO PB01 - Add annotation to define this class as superclass of all entities.
@MappedSuperclass
// TODO PB02 - Add annotation to place all JPA annotations on fields.
@Access(AccessType.FIELD)
// TODO PB03 - Add annotation for listener class.
@EntityListeners(PojoListener.class)
public abstract class PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	// TODO PB04 - Add missing annotations.
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected int id;

	// TODO PB05 - Add missing annotations.
	@Version
	protected int version;

	// TODO PB06 - Add missing annotations (hint, is this column on DB?).
	@Column(name = "created", updatable = false)
	protected LocalDateTime created;

	// TODO PB07 - Add missing annotations (hint, is this column on DB?).
	@Column(name = "updated")
	protected LocalDateTime updated;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
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
		if (obj instanceof PojoBase otherPojoBase) {
			return Objects.equals(this.getId(), otherPojoBase.getId());
		}
		return false;
	}
}
