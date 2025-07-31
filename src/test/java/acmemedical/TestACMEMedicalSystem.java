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
     * Test retrieving all physicians using admin role.
     * Expected: HTTP 200 OK and non-empty list of physicians.
     * @author manaf
     */
    @Test
    public void test01_all_physicians_with_adminrole() {
        Response response = webTarget
            .register(adminAuth)
            .path("api/v1/physician")
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        List<Physician> physicians = response.readEntity(new GenericType<List<Physician>>() {});
        assertNotNull(physicians);
        assertThat(physicians, is(not(empty())));
    }

    /**
     * Test retrieving all patients using admin role.
     * Expected: HTTP 200 OK and list of patients returned.
     */
    @Test
    public void testGetAllPatientsWithAdminRole() {
        Response response = webTarget
            .register(adminAuth)
            .path("api/v1/patient")
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        List<Patient> patients = response.readEntity(new GenericType<List<Patient>>() {});
        assertNotNull(patients);
        assertThat(patients, is(not(empty())));
    }

    /**
     * Test retrieving a patient by ID using admin role.
     * Expected: HTTP 200 OK and correct patient returned.
     */
    @Test
    public void testGetPatientByIdWithAdminRole() {
        int testId = 1; // Make sure a patient with ID 1 exists
        Response response = webTarget
            .register(adminAuth)
            .path("api/v1/patient/" + testId)
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        Patient patient = response.readEntity(Patient.class);
        assertNotNull(patient);
        assertEquals(testId, patient.getId());
    }
}
