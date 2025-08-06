/********************************************************************************************************
 * File:  MedicalSchoolSerializer.java Course Materials CST 8277
 *
 * @author Manaf Akil
 * @author Chengcheng Xiong, Group 8
 * @date modified 2025-07-14
 */
package acmemedical.rest.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import acmemedical.entity.MedicalSchool;

public class MedicalSchoolSerializer extends StdSerializer<MedicalSchool> {

    private static final long serialVersionUID = 1L;

    public MedicalSchoolSerializer() {
        this(null);
    }

    public MedicalSchoolSerializer(Class<MedicalSchool> t) {
        super(t);
    }

    /**
     * This is to prevent back and forth serialization between Many to Many relations.<br>
     * This is done by setting the relation to null.
     */
    @Override
    public void serialize(MedicalSchool originalMedicalSchool, JsonGenerator generator, SerializerProvider provider)
            throws IOException {

        generator.writeStartObject();
        generator.writeNumberField("id", originalMedicalSchool.getId());
        generator.writeStringField("name", originalMedicalSchool.getName());
        generator.writeBooleanField("public", originalMedicalSchool.isPublic());
        generator.writeStringField("created", originalMedicalSchool.getCreated().toString());
        generator.writeStringField("updated", originalMedicalSchool.getUpdated().toString());
        generator.writeNumberField("version", originalMedicalSchool.getVersion());
        
        // Show count of medical trainings instead of full training details
        generator.writeNumberField("medicalTrainingCount", originalMedicalSchool.getMedicalTrainings().size());
        
        generator.writeEndObject();
    }
}
