/********************************************************************************************************
 * File:  PublicSchool.java Course materials CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author Hongli Ren, Group 8
 * @date modified 2025-07-14
 */
package acmemedical.entity;

import java.io.Serializable;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonTypeName;

// TODO PUSC01 - Add missing annotations, please see Week 9 slides page 15. Value 1 is public and value 0 is private.
// TODO PUSC02 - Is a JSON annotation needed here? Yes.
@Entity
@Table(name = "public_school")
@PrimaryKeyJoinColumn(referencedColumnName = "school_id")
@DiscriminatorValue("1")
@JsonTypeName("public_school")
public class PublicSchool extends MedicalSchool implements Serializable {
    private static final long serialVersionUID = 1L;

    public PublicSchool() {
        super(true); // true = public
    }
}
