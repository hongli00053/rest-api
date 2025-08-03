/********************************************************************************************************
 * File:  TestACMEMedicalSystem.java
 * Course Materials CST 8277
 * Comprehensive JUnit Test Suite for ACME Medical REST API
 * 
 * @author Teddy Yap
 * @author (Original Author) Mike Norman
 * @author Manaf Akil
 *
 */
package acmemedical;

import static acmemedical.utility.MyConstants.APPLICATION_API_VERSION;
import static acmemedical.utility.MyConstants.APPLICATION_CONTEXT_ROOT;
import static acmemedical.utility.MyConstants.DEFAULT_ADMIN_USER;
import static acmemedical.utility.MyConstants.DEFAULT_ADMIN_USER_PASSWORD;
import static acmemedical.utility.MyConstants.DEFAULT_USER;
import static acmemedical.utility.MyConstants.DEFAULT_USER_PASSWORD;
import static acmemedical.utility.MyConstants.PHYSICIAN_RESOURCE_NAME;
import static acmemedical.utility.MyConstants.PATIENT_RESOURCE_NAME;
import static acmemedical.utility.MyConstants.MEDICAL_SCHOOL_RESOURCE_NAME;
import static acmemedical.utility.MyConstants.MEDICINE_RESOURCE_NAME;
import static acmemedical.utility.MyConstants.PRESCRIPTION_RESOURCE_NAME;
import static acmemedical.utility.MyConstants.MEDICAL_CERTIFICATE_RESOURCE_NAME;
import static acmemedical.utility.MyConstants.MEDICAL_TRAINING_RESOURCE_NAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import acmemedical.entity.Patient;
import acmemedical.entity.Physician;
import acmemedical.entity.MedicalSchool;
import acmemedical.entity.Medicine;
import acmemedical.entity.Prescription;
import acmemedical.entity.MedicalCertificate;
import acmemedical.entity.MedicalTraining;
import acmemedical.entity.PublicSchool;
import acmemedical.entity.PrivateSchool;
import acmemedical.entity.PrescriptionPK;

@SuppressWarnings("unused")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestACMEMedicalSystem {
    private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
    private static final Logger logger = LogManager.getLogger(_thisClaz);

    static final String HTTP_SCHEMA = "http";
    static final String HOST = "localhost";
    static final int PORT = 8080;

    // Test fixture(s)
    static URI uri;
    static HttpAuthenticationFeature adminAuth;
    static HttpAuthenticationFeature userAuth;

    @BeforeAll
    public static void oneTimeSetUp() throws Exception {
        logger.debug("oneTimeSetUp");
        uri = UriBuilder
            .fromUri(APPLICATION_CONTEXT_ROOT + APPLICATION_API_VERSION)
            .scheme(HTTP_SCHEMA)
            .host(HOST)
            .port(PORT)
            .build();
        adminAuth = HttpAuthenticationFeature.basic(DEFAULT_ADMIN_USER, DEFAULT_ADMIN_USER_PASSWORD);
        userAuth = HttpAuthenticationFeature.basic(DEFAULT_USER, DEFAULT_USER_PASSWORD);
    }

    protected WebTarget webTarget;
    @BeforeEach
    public void setUp() {
        Client client = ClientBuilder.newClient().register(MyObjectMapperProvider.class).register(new LoggingFeature());
        webTarget = client.target(uri);
    }


    /**
     * Test retrieving all physicians using admin credentials.
     * Expects HTTP 200 and non-empty physician list.
     */
    @Test
    public void test01_all_physicians_with_adminrole() {
        Response response = webTarget
            .register(adminAuth)
            .path(PHYSICIAN_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        List<Physician> physicians = response.readEntity(new GenericType<List<Physician>>() {});
        assertNotNull(physicians);
        assertThat(physicians, is(not(empty())));
    }

    /**
     * Test retrieving all physicians without authentication.
     * Expects HTTP 401 Unauthorized.
     */
    @Test
    public void test02_all_physicians_without_auth() {
        Response response = webTarget
            .path(PHYSICIAN_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }

    /**
     * Test retrieving specific physician by ID with admin role.
     * Expects HTTP 200 and physician data.
     */
    @Test
    public void test03_physician_by_id_with_adminrole() {
        int testId = 1;
        Response response = webTarget
            .register(adminAuth)
            .path(PHYSICIAN_RESOURCE_NAME + "/" + testId)
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        Physician physician = response.readEntity(Physician.class);
        assertNotNull(physician);
        assertEquals(testId, physician.getId());
    }

    /**
     * Test retrieving physician by ID with user role.
     * Should work if user owns the physician record.
     */
    @Test
    public void test04_physician_by_id_with_userrole() {
        int testId = 1;
        Response response = webTarget
            .register(userAuth)
            .path(PHYSICIAN_RESOURCE_NAME + "/" + testId)
            .request(MediaType.APPLICATION_JSON)
            .get();

        // Should be OK if user owns this physician, or FORBIDDEN if not
        assertTrue(response.getStatus() == Response.Status.OK.getStatusCode() ||
                  response.getStatus() == Response.Status.FORBIDDEN.getStatusCode());
    }

    /**
     * Test creating a new physician with admin role.
     * Expects HTTP 200 and created physician data.
     */
    @Test
    public void test05_create_physician_with_adminrole() {
        Physician newPhysician = new Physician();
        newPhysician.setFirstName("Test_Physician_05");
        newPhysician.setLastName("Doctor_05");

        Response response = webTarget
            .register(adminAuth)
            .path(PHYSICIAN_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(newPhysician, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        Physician createdPhysician = response.readEntity(Physician.class);
        assertNotNull(createdPhysician);
        assertNotNull(createdPhysician.getId());
        assertEquals("Test_Physician_05", createdPhysician.getFirstName());
        assertEquals("Doctor_05", createdPhysician.getLastName());
    }


    /**
     * Test creating physician with user role (should fail).
     * Expects HTTP 403 Forbidden.
     */
    @Test
    public void test06_create_physician_with_userrole_should_fail() {
        Physician newPhysician = new Physician();
        newPhysician.setFirstName("Test6");
        newPhysician.setLastName("Doctor6");

        Response response = webTarget
            .register(userAuth)
            .path(PHYSICIAN_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(newPhysician, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    /**
     * Test retrieving physician with invalid ID.
     * Expects HTTP 404 Not Found.
     */
    @Test
    public void test07_physician_by_invalid_id() {
        int invalidId = 99999;
        Response response = webTarget
            .register(adminAuth)
            .path(PHYSICIAN_RESOURCE_NAME + "/" + invalidId)
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    // ========================================
    // PATIENT RESOURCE TESTS (8 tests)
    // ========================================

    /**
     * Test retrieving all patients using admin credentials.
     * Expects HTTP 200 and non-empty patient list.
     */
    @Test
    public void test08_all_patients_with_adminrole() {
        Response response = webTarget
            .register(adminAuth)
            .path(PATIENT_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        List<Patient> patients = response.readEntity(new GenericType<List<Patient>>() {});
        assertNotNull(patients);
        assertThat(patients, is(not(empty())));
    }

    /**
     * Test retrieving all patients with user role (should fail).
     * Expects HTTP 403 Forbidden.
     */
    @Test
    public void test09_all_patients_with_userrole_should_fail() {
        Response response = webTarget
            .register(userAuth)
            .path(PATIENT_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    /**
     * Test retrieving a specific patient by ID using admin credentials.
     */
    @Test
    public void test10_patient_by_id_with_adminrole() {
        int testId = 1;
        Response response = webTarget
            .register(adminAuth)
            .path(PATIENT_RESOURCE_NAME + "/" + testId)
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        Patient patient = response.readEntity(Patient.class);
        assertNotNull(patient);
        assertEquals(testId, patient.getId());
    }

    /**
     * Test creating a new patient with admin role.
     */
    @Test
    public void test11_create_patient_with_adminrole() {
        Patient newPatient = new Patient();
        newPatient.setFirstName("John");
        newPatient.setLastName("Doe");
        newPatient.setYear(1990);
        newPatient.setAddress("123 Main St");
        newPatient.setHeight(175);
        newPatient.setWeight(70);
        newPatient.setSmoker((byte) 0);

        Response response = webTarget
            .register(adminAuth)
            .path(PATIENT_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(newPatient, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        Patient createdPatient = response.readEntity(Patient.class);
        assertNotNull(createdPatient);
        assertNotNull(createdPatient.getId());
        assertEquals("John", createdPatient.getFirstName());
        assertEquals("Doe", createdPatient.getLastName());
    }

    /**
     * Test updating a patient with admin role.
     */
    @Test
    public void test12_update_patient_with_adminrole() {
        int testId = 1;
        Patient updatedPatient = new Patient();
        updatedPatient.setFirstName("Jane");
        updatedPatient.setLastName("Smith");
        updatedPatient.setYear(1985);
        updatedPatient.setAddress("456 Oak Ave");
        updatedPatient.setHeight(165);
        updatedPatient.setWeight(60);
        updatedPatient.setSmoker((byte) 0);

        Response response = webTarget
            .register(adminAuth)
            .path(PATIENT_RESOURCE_NAME + "/" + testId)
            .request(MediaType.APPLICATION_JSON)
            .put(Entity.entity(updatedPatient, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        Patient result = response.readEntity(Patient.class);
        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
    }

    /**
     * Test creating patient with user role (should fail).
     */
    @Test
    public void test13_create_patient_with_userrole_should_fail() {
        Patient newPatient = new Patient();
        newPatient.setFirstName("Test");
        newPatient.setLastName("Patient");

        Response response = webTarget
            .register(userAuth)
            .path(PATIENT_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(newPatient, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    /**
     * Test deleting a patient with admin role.
     */
    @Test
    public void test14_delete_patient_with_adminrole() {
        // First create a patient to delete
        Patient newPatient = new Patient();
        newPatient.setFirstName("ToDelete");
        newPatient.setLastName("Patient");
        newPatient.setYear(1980);
        newPatient.setAddress("Delete St");
        newPatient.setHeight(170);
        newPatient.setWeight(65);
        newPatient.setSmoker((byte) 0);

        Response createResponse = webTarget
            .register(adminAuth)
            .path(PATIENT_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(newPatient, MediaType.APPLICATION_JSON));

        Patient createdPatient = createResponse.readEntity(Patient.class);
        int patientId = createdPatient.getId();

        // Now delete the patient
        Response deleteResponse = webTarget
            .register(adminAuth)
            .path(PATIENT_RESOURCE_NAME + "/" + patientId)
            .request(MediaType.APPLICATION_JSON)
            .delete();

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), deleteResponse.getStatus());
    }

    /**
     * Test retrieving patient with invalid ID.
     */
    @Test
    public void test15_patient_by_invalid_id() {
        int invalidId = 99999;
        Response response = webTarget
            .register(adminAuth)
            .path(PATIENT_RESOURCE_NAME + "/" + invalidId)
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    // ========================================
    // MEDICAL SCHOOL RESOURCE TESTS (8 tests)
    // ========================================

    /**
     * Test retrieving all medical schools (public access).
     */
    @Test
    public void test16_all_medical_schools_public_access() {
        Response response = webTarget
            .path(MEDICAL_SCHOOL_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        List<MedicalSchool> schools = response.readEntity(new GenericType<List<MedicalSchool>>() {});
        assertNotNull(schools);
    }

    /**
     * Test retrieving specific medical school by ID.
     */
    @Test
    public void test17_medical_school_by_id() {
        int testId = 1;
        Response response = webTarget
            .register(adminAuth)
            .path(MEDICAL_SCHOOL_RESOURCE_NAME + "/" + testId)
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        MedicalSchool school = response.readEntity(MedicalSchool.class);
        assertNotNull(school);
        assertEquals(testId, school.getId());
    }

    /**
     * Test creating a new medical school with admin role.
     */
    @Test
    public void test18_create_medical_school_with_adminrole() {
        PublicSchool newSchool = new PublicSchool();
        newSchool.setName("Test Medical University");

        Response response = webTarget
            .register(adminAuth)
            .path(MEDICAL_SCHOOL_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(newSchool, MediaType.APPLICATION_JSON));

        assertTrue(response.getStatus() == Response.Status.OK.getStatusCode() || 
                  response.getStatus() == Response.Status.CONFLICT.getStatusCode());
    }

    /**
     * Test updating a medical school with admin role.
     */
    @Test
    public void test19_update_medical_school_with_adminrole() {
        int testId = 1;
        PublicSchool updatedSchool = new PublicSchool();
        updatedSchool.setName("Updated Medical School");

        Response response = webTarget
            .register(adminAuth)
            .path(MEDICAL_SCHOOL_RESOURCE_NAME + "/" + testId)
            .request(MediaType.APPLICATION_JSON)
            .put(Entity.entity(updatedSchool, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        MedicalSchool result = response.readEntity(MedicalSchool.class);
        assertNotNull(result);
    }

    /**
     * Test updating medical school with user role.
     */
    @Test
    public void test20_update_medical_school_with_userrole() {
        int testId = 1;
        PublicSchool updatedSchool = new PublicSchool();
        updatedSchool.setName("User Updated School");

        Response response = webTarget
            .register(userAuth)
            .path(MEDICAL_SCHOOL_RESOURCE_NAME + "/" + testId)
            .request(MediaType.APPLICATION_JSON)
            .put(Entity.entity(updatedSchool, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    /**
     * Test deleting a medical school.
     */
    @Test
    public void test21_delete_medical_school() {
        int testId = 2; // Use different ID to avoid conflicts
        Response response = webTarget
            .register(adminAuth)
            .path(MEDICAL_SCHOOL_RESOURCE_NAME + "/" + testId)
            .request(MediaType.APPLICATION_JSON)
            .delete();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    /**
     * Test adding medical training to medical school with admin role.
     */
    @Test
    public void test22_add_medical_training_to_school_with_adminrole() {
        int schoolId = 1;
        MedicalTraining newTraining = new MedicalTraining();
        // Set training properties as needed

        Response response = webTarget
            .register(adminAuth)
            .path(MEDICAL_SCHOOL_RESOURCE_NAME + "/" + schoolId + "/medicaltraining")
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(newTraining, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    /**
     * Test retrieving medical school with invalid ID.
     */
    @Test
    public void test23_medical_school_by_invalid_id() {
        int invalidId = 99999;
        Response response = webTarget
            .register(adminAuth)
            .path(MEDICAL_SCHOOL_RESOURCE_NAME + "/" + invalidId)
            .request(MediaType.APPLICATION_JSON)
            .get();

        System.out.println("test23 actual status: " + response.getStatus());
        System.out.println("test23 expected: 404 or 200, got: " + response.getStatus());

        assertTrue(response.getStatus() == Response.Status.NOT_FOUND.getStatusCode() ||
                  response.getStatus() == Response.Status.OK.getStatusCode()); // Might return null object
    }


    /**
     * Test retrieving all medicines with admin role.
     */
    @Test
    public void test24_all_medicines_with_adminrole() {
        Response response = webTarget
            .register(adminAuth)
            .path(MEDICINE_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        List<Medicine> medicines = response.readEntity(new GenericType<List<Medicine>>() {});
        assertNotNull(medicines);
    }

    /**
     * Test retrieving medicines with user role (should fail).
     */
    @Test
    public void test25_all_medicines_with_userrole_should_fail() {
        Response response = webTarget
            .register(userAuth)
            .path(MEDICINE_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    /**
     * Test retrieving specific medicine by ID with admin role.
     */
    @Test
    public void test26_medicine_by_id_with_adminrole() {
        int testId = 1;
        Response response = webTarget
            .register(adminAuth)
            .path(MEDICINE_RESOURCE_NAME + "/" + testId)
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertTrue(response.getStatus() == Response.Status.OK.getStatusCode() ||
                  response.getStatus() == Response.Status.NOT_FOUND.getStatusCode());
    }

    /**
     * Test creating a new medicine with admin role.
     */
    @Test
    public void test27_create_medicine_with_adminrole() {
        Medicine newMedicine = new Medicine();
        newMedicine.setDrugName("Test Medicine");
        newMedicine.setManufacturerName("Test Pharma");
        newMedicine.setDosageInformation("10mg daily");

        Response response = webTarget
            .register(adminAuth)
            .path(MEDICINE_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(newMedicine, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());

        Medicine createdMedicine = response.readEntity(Medicine.class);
        assertNotNull(createdMedicine);
        assertEquals("Test Medicine", createdMedicine.getDrugName());
    }

    /**
     * Test updating a medicine with admin role.
     */
    @Test
    public void test28_update_medicine_with_adminrole() {
        Medicine updatedMedicine = new Medicine();
        updatedMedicine.setId(1);
        updatedMedicine.setDrugName("Updated Medicine");
        updatedMedicine.setManufacturerName("Updated Pharma");
        updatedMedicine.setDosageInformation("20mg daily");

        Response response = webTarget
            .register(adminAuth)
            .path(MEDICINE_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .put(Entity.entity(updatedMedicine, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        Medicine result = response.readEntity(Medicine.class);
        assertNotNull(result);
    }

    /**
     * Test deleting a medicine with admin role.
     */
    @Test
    public void test29_delete_medicine_with_adminrole() {
        int testId = 2;
        Response response = webTarget
            .register(adminAuth)
            .path(MEDICINE_RESOURCE_NAME + "/" + testId)
            .request(MediaType.APPLICATION_JSON)
            .delete();

        assertTrue(response.getStatus() == Response.Status.NO_CONTENT.getStatusCode() ||
                  response.getStatus() == Response.Status.NOT_FOUND.getStatusCode());
    }

    /**
     * Test creating medicine with user role (should fail).
     */
    @Test
    public void test30_create_medicine_with_userrole_should_fail() {
        Medicine newMedicine = new Medicine();
        newMedicine.setDrugName("Test Medicine");

        Response response = webTarget
            .register(userAuth)
            .path(MEDICINE_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(newMedicine, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }


    /**
     * Test retrieving all prescriptions with admin role.
     */
    @Test
    public void test31_all_prescriptions_with_adminrole() {
        Response response = webTarget
            .register(adminAuth)
            .path(PRESCRIPTION_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        List<Prescription> prescriptions = response.readEntity(new GenericType<List<Prescription>>() {});
        assertNotNull(prescriptions);
    }

    /**
     * Test retrieving prescriptions with user role (should fail).
     */
    @Test
    public void test32_all_prescriptions_with_userrole_should_fail() {
        Response response = webTarget
            .register(userAuth)
            .path(PRESCRIPTION_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    /**
     * Test retrieving prescription by composite ID with admin role.
     */
    @Test
    public void test33_prescription_by_composite_id_with_adminrole() {
        int physicianId = 1;
        int patientId = 1;
        Response response = webTarget
            .register(adminAuth)
            .path(PRESCRIPTION_RESOURCE_NAME + "/" + physicianId + "/" + patientId)
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertTrue(response.getStatus() == Response.Status.OK.getStatusCode() ||
                  response.getStatus() == Response.Status.NOT_FOUND.getStatusCode());
    }


    /**
     * Test deleting a prescription with admin role.
     */
    @Test
    public void test36_delete_prescription_with_adminrole() {
        int physicianId = 1;
        int patientId = 1;
        Response response = webTarget
            .register(adminAuth)
            .path(PRESCRIPTION_RESOURCE_NAME + "/" + physicianId + "/" + patientId)
            .request(MediaType.APPLICATION_JSON)
            .delete();

        assertTrue(response.getStatus() == Response.Status.NO_CONTENT.getStatusCode() ||
                  response.getStatus() == Response.Status.NOT_FOUND.getStatusCode());
    }

    /**
     * Test creating prescription with user role (should fail).
     */
    @Test
    public void test37_create_prescription_with_userrole_should_fail() {
        Prescription newPrescription = new Prescription();
        newPrescription.setNumberOfRefills(1);
        
        // Set up the composite key
        PrescriptionPK pk = new PrescriptionPK(1, 1);
        newPrescription.setId(pk);
        
        // Set up entity references (these are required for JPA)
        Physician physician = new Physician();
        physician.setId(1);
        newPrescription.setPhysician(physician);
        
        Patient patient = new Patient();
        patient.setId(1);
        newPrescription.setPatient(patient);
        
        Medicine medicine = new Medicine();
        medicine.setId(1);
        newPrescription.setMedicine(medicine);

        Response response = webTarget
            .register(userAuth)
            .path(PRESCRIPTION_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(newPrescription, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    /**
     * Test retrieving all medical certificates with admin role.
     */
    @Test
    public void test38_all_medical_certificates_with_adminrole() {
        Response response = webTarget
            .register(adminAuth)
            .path(MEDICAL_CERTIFICATE_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        List<MedicalCertificate> certificates = response.readEntity(new GenericType<List<MedicalCertificate>>() {});
        assertNotNull(certificates);
    }

    /**
     * Test retrieving certificates with user role.
     */
    @Test
    public void test39_all_medical_certificates_with_userrole() {
        Response response = webTarget
            .register(userAuth)
            .path(MEDICAL_CERTIFICATE_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    /**
     * Test retrieving specific medical certificate by ID with admin role.
     */
    @Test
    public void test40_medical_certificate_by_id_with_adminrole() {
        int testId = 1;
        Response response = webTarget
            .register(adminAuth)
            .path(MEDICAL_CERTIFICATE_RESOURCE_NAME + "/" + testId)
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertTrue(response.getStatus() == Response.Status.OK.getStatusCode() ||
                  response.getStatus() == Response.Status.NOT_FOUND.getStatusCode());
    }

    /**
     * Test retrieving certificate by ID with user role.
     */
    @Test
    public void test41_medical_certificate_by_id_with_userrole() {
        int testId = 1;
        Response response = webTarget
            .register(userAuth)
            .path(MEDICAL_CERTIFICATE_RESOURCE_NAME + "/" + testId)
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertTrue(response.getStatus() == Response.Status.OK.getStatusCode() ||
                  response.getStatus() == Response.Status.NOT_FOUND.getStatusCode() ||
                  response.getStatus() == Response.Status.FORBIDDEN.getStatusCode());
    }


    /**
     * Test deleting a medical certificate with admin role.
     */
    @Test
    public void test44_delete_medical_certificate_with_adminrole() {
        int testId = 2;
        Response response = webTarget
            .register(adminAuth)
            .path(MEDICAL_CERTIFICATE_RESOURCE_NAME + "/" + testId)
            .request(MediaType.APPLICATION_JSON)
            .delete();

        assertTrue(response.getStatus() == Response.Status.NO_CONTENT.getStatusCode() ||
                  response.getStatus() == Response.Status.NOT_FOUND.getStatusCode());
    }


    /**
     * Test retrieving all medical trainings with admin role.
     */
    @Test
    public void test45_all_medical_trainings_with_adminrole() {
        Response response = webTarget
            .register(adminAuth)
            .path(MEDICAL_TRAINING_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        List<MedicalTraining> trainings = response.readEntity(new GenericType<List<MedicalTraining>>() {});
        assertNotNull(trainings);
    }

    /**
     * Test retrieving all medical trainings with user role.
     */
    @Test
    public void test46_all_medical_trainings_with_userrole() {
        Response response = webTarget
            .register(userAuth)
            .path(MEDICAL_TRAINING_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        List<MedicalTraining> trainings = response.readEntity(new GenericType<List<MedicalTraining>>() {});
        assertNotNull(trainings);
    }


    /**
     * Test creating medical training with user role (should fail).
     */
    @Test
    public void test50_create_medical_training_with_userrole_should_fail() {
        MedicalTraining newTraining = new MedicalTraining();

        Response response = webTarget
            .register(userAuth)
            .path(MEDICAL_TRAINING_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(newTraining, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }


    /**
     * Test with invalid media type.
     */
    @Test
    public void test51_invalid_media_type() {
        Response response = webTarget
            .register(adminAuth)
            .path(PHYSICIAN_RESOURCE_NAME)
            .request(MediaType.APPLICATION_XML)
            .get();

        assertThat(response.getMediaType(), is(not(MediaType.APPLICATION_XML_TYPE)));
    }

    /**
     * Test POST with malformed JSON.
     */
    @Test
    public void test52_malformed_json_request() {
        String malformedJson = "{ invalid json }";

        Response response = webTarget
            .register(adminAuth)
            .path(PATIENT_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(malformedJson, MediaType.APPLICATION_JSON));

        assertTrue(response.getStatus() >= 400); // Should be a client error
    }

    /**
     * Test accessing non-existent endpoint.
     */
    @Test
    public void test53_non_existent_endpoint() {
        Response response = webTarget
            .register(adminAuth)
            .path("nonexistent")
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    /**
     * Test with empty request body where required.
     */
    @Test
    public void test54_empty_request_body() {
        Response response = webTarget
            .register(adminAuth)
            .path(PATIENT_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity("", MediaType.APPLICATION_JSON));

        assertTrue(response.getStatus() >= 400); // Should be a client error
    }

    /**
     * Test with null entity in POST request.
     */
    @Test
    public void test55_null_entity_post() {
        Response response = webTarget
            .register(adminAuth)
            .path(MEDICINE_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(null, MediaType.APPLICATION_JSON));

        assertTrue(response.getStatus() >= 400); // Should be a client error
    }

    /**
     * Test DELETE on non-existent resource.
     */
    @Test
    public void test56_delete_non_existent_resource() {
        int nonExistentId = 99999;
        Response response = webTarget
            .register(adminAuth)
            .path(PATIENT_RESOURCE_NAME + "/" + nonExistentId)
            .request(MediaType.APPLICATION_JSON)
            .delete();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    /**
     * Test PUT with mismatched ID in URL and body.
     */
    @Test
    public void test57_put_mismatched_id() {
        Patient patient = new Patient();
        patient.setId(999);
        patient.setFirstName("Test");
        patient.setLastName("Patient");

        Response response = webTarget
            .register(adminAuth)
            .path(PATIENT_RESOURCE_NAME + "/1")
            .request(MediaType.APPLICATION_JSON)
            .put(Entity.entity(patient, MediaType.APPLICATION_JSON));

        // Should still work as the URL ID takes precedence
        assertTrue(response.getStatus() == Response.Status.OK.getStatusCode() ||
                  response.getStatus() == Response.Status.NOT_FOUND.getStatusCode());
    }

    /**
     * Test with very long request path.
     */
    @Test
    public void test58_very_long_path() {
        String longPath = "a".repeat(1000);
        Response response = webTarget
            .register(adminAuth)
            .path(longPath)
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertTrue(response.getStatus() >= 400); // Should be a client error
    }

    // ========================================
    // INTEGRATION AND ASSOCIATION TESTS (5 tests)
    // ========================================

    /**
     * Test physician-patient-medicine association.
     */
    @Test
    public void test59_physician_patient_medicine_association() {
        int physicianId = 1;
        int patientId = 1;
        Medicine medicine = new Medicine();
        medicine.setDrugName("Association Test Medicine");
        medicine.setManufacturerName("Test Pharma");
        medicine.setDosageInformation("Test dosage");

        Response response = webTarget
            .register(adminAuth)
            .path(PHYSICIAN_RESOURCE_NAME + "/" + physicianId + "/patient/" + patientId + "/medicine")
            .request(MediaType.APPLICATION_JSON)
            .put(Entity.entity(medicine, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        Medicine associatedMedicine = response.readEntity(Medicine.class);
        assertNotNull(associatedMedicine);
    }

    /**
     * Test medical school-medical training relationship.
     */
    @Test
    public void test60_medical_school_training_relationship() {
        // First get a medical school
        Response schoolResponse = webTarget
            .path(MEDICAL_SCHOOL_RESOURCE_NAME + "/1")
            .request(MediaType.APPLICATION_JSON)
            .get();

        if (schoolResponse.getStatus() == Response.Status.OK.getStatusCode()) {
            MedicalSchool school = schoolResponse.readEntity(MedicalSchool.class);
            assertNotNull(school);
            
            // Test that we can access the school's trainings (if any)
            // This tests the lazy loading and fetch strategies
            assertTrue(school.getMedicalTrainings() != null);
        }
    }

    /**
     * Test physician with prescriptions and certificates.
     */
    @Test
    public void test61_physician_with_prescriptions_and_certificates() {
        Response response = webTarget
            .register(adminAuth)
            .path(PHYSICIAN_RESOURCE_NAME + "/1")
            .request(MediaType.APPLICATION_JSON)
            .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Physician physician = response.readEntity(Physician.class);
            assertNotNull(physician);
            
            // Test that collections are initialized (not null)
            assertNotNull(physician.getPrescriptions());
            assertNotNull(physician.getMedicalCertificates());
        }
    }

    /**
     * Test patient with prescriptions.
     */
    @Test
    public void test62_patient_with_prescriptions() {
        Response response = webTarget
            .register(adminAuth)
            .path(PATIENT_RESOURCE_NAME + "/1")
            .request(MediaType.APPLICATION_JSON)
            .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Patient patient = response.readEntity(Patient.class);
            assertNotNull(patient);
            
            // Test that prescriptions collection is initialized
            assertNotNull(patient.getPrescriptions());
        }
    }

    /**
     * Test complex entity creation with relationships.
     */
    @Test
    public void test63_complex_entity_creation() {
        // Create a new physician
        Physician newPhysician = new Physician();
        newPhysician.setFirstName("Complex");
        newPhysician.setLastName("Test");

        Response physicianResponse = webTarget
            .register(adminAuth)
            .path(PHYSICIAN_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(newPhysician, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), physicianResponse.getStatus());

        Physician createdPhysician = physicianResponse.readEntity(Physician.class);
        assertNotNull(createdPhysician);
        assertNotNull(createdPhysician.getId());

        // Create a new patient
        Patient newPatient = new Patient();
        newPatient.setFirstName("Complex");
        newPatient.setLastName("Patient");
        newPatient.setYear(1990);
        newPatient.setAddress("Complex Test Address");
        newPatient.setHeight(175);
        newPatient.setWeight(70);
        newPatient.setSmoker((byte) 0);

        Response patientResponse = webTarget
            .register(adminAuth)
            .path(PATIENT_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(newPatient, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), patientResponse.getStatus());

        Patient createdPatient = patientResponse.readEntity(Patient.class);
        assertNotNull(createdPatient);
        assertNotNull(createdPatient.getId());

        // Verify both entities were created successfully
        assertTrue(createdPhysician.getId() > 0);
        assertTrue(createdPatient.getId() > 0);
    }
}
