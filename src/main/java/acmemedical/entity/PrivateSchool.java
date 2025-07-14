/********************************************************************************************************
 * File:  PrivateSchool.java Course Materials CST 8277
 *
 * @author Teddy Yap
 * @author Chengcheng Xiong, Group 8
 * @date modified 2025-07-14
 */
package acmemedical.entity;

import java.io.Serializable;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonTypeName;

// TODO PRSC01 - Add missing annotations, please see Week 9 slides page 15. Value 1 is public and value 0 is private.
// TODO PRSC02 - Is a JSON annotation needed here? Yes, for proper polymorphic deserialization.
@Entity
@DiscriminatorValue("0")
@JsonTypeName("private_school")
public class PrivateSchool extends MedicalSchool implements Serializable {
	private static final long serialVersionUID = 1L;

	public PrivateSchool() {
		super(false); // false = private
	}
}
