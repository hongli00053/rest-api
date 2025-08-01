/********************************************************************************************************
 * File:  TestACMEMedicalSystem.java
 * Course Materials CST 8277
 * Teddy Yap
 * (Original Author) Mike Norman
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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
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
            .path("physician")  // Fixed: removed duplicate context root
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        List<Physician> physicians = response.readEntity(new GenericType<List<Physician>>() {});
        assertNotNull(physicians);
        assertThat(physicians, is(not(empty())));
    }

	/**
     * Test retrieving all patients using admin credentials.
     * Expects HTTP 200 and non-empty patient list.
     */
    @Test
    public void testGetAllPatientsWithAdminRole() {
        Response response = webTarget
            .register(adminAuth)
            .path("patient") // Fixed: removed duplicate api/v1
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        List<Patient> patients = response.readEntity(new GenericType<List<Patient>>() {});
        assertNotNull(patients);
        assertThat(patients, is(not(empty())));
    }

    /**
     * Test retrieving a specific patient by ID using admin credentials.
     * Adjusts ID as needed to match what's inserted in your DB or SQL script.
     */
    @Test
    public void testGetPatientByIdWithAdminRole() {
        int testId = 1; // Make sure ID exists
        Response response = webTarget
            .register(adminAuth)
            .path("patient/" + testId) // Fixed: removed duplicate api/v1
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        Patient patient = response.readEntity(Patient.class);
        assertNotNull(patient);
        assertEquals(testId, patient.getId());
    }

}
