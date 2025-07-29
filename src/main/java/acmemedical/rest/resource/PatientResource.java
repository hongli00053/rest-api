/********************************************************************************************************
 * File:  PatientResource.java Course Materials CST 8277
 *
 * @author Mohammad
 * 
 * REST endpoint for Patient entity
 */
package acmemedical.rest.resource;

import static acmemedical.utility.MyConstants.ADMIN_ROLE;
import static acmemedical.utility.MyConstants.USER_ROLE;
import static acmemedical.utility.MyConstants.PATIENT_RESOURCE_NAME;
import static acmemedical.utility.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static acmemedical.utility.MyConstants.RESOURCE_PATH_ID_PATH;

import java.util.List;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.soteria.WrappingCallerPrincipal;

import acmemedical.ejb.ACMEMedicalService;
import acmemedical.entity.Patient;
import acmemedical.entity.SecurityUser;

@Path(PATIENT_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PatientResource {

    private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMEMedicalService service;

    @Inject
    protected SecurityContext sc;

    @GET
    @RolesAllowed({ADMIN_ROLE})
    public Response getAllPatients() {
        LOG.debug("Retrieving all patients...");
        List<Patient> patients = service.getAll(Patient.class, "Patient.findAll");  // assumes named query exists
        return Response.ok(patients).build();
    }

    @GET
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response getPatientById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        LOG.debug("Retrieving patient with id {}", id);
        Patient patient = null;
        if (sc.isCallerInRole(ADMIN_ROLE)) {
            patient = service.getById(Patient.class, "Patient.findById", id);  // assumes named query exists
            return (patient == null)
                ? Response.status(Response.Status.NOT_FOUND).build()
                : Response.ok(patient).build();
        } else if (sc.isCallerInRole(USER_ROLE)) {
            WrappingCallerPrincipal wCallerPrincipal = (WrappingCallerPrincipal) sc.getCallerPrincipal();
            SecurityUser sUser = (SecurityUser) wCallerPrincipal.getWrapped();
            patient = sUser.getPatient(); // must be implemented
            if (patient != null && patient.getId() == id) {
                return Response.ok(patient).build();
            } else {
                throw new ForbiddenException("User does not own this patient record");
           }
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @POST
    @RolesAllowed({ADMIN_ROLE})
    public Response addPatient(Patient newPatient) {
        LOG.debug("Adding new patient...");
        Patient savedPatient = service.persist(newPatient);  // assumes persist() method exists or is added
        return Response.ok(savedPatient).build();
    }

    @PUT
    @RolesAllowed({ADMIN_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response updatePatient(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id, Patient patientWithUpdates) {
        LOG.debug("Updating patient with id {}", id);
        Patient updatedPatient = service.update(id, patientWithUpdates);  // assumes update() method exists or is added
        return (updatedPatient == null)
            ? Response.status(Response.Status.NOT_FOUND).build()
            : Response.ok(updatedPatient).build();
    }

    @DELETE
    @RolesAllowed({ADMIN_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response deletePatient(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        LOG.debug("Deleting patient with id {}", id);
        Patient patient = service.getById(Patient.class, "Patient.findById", id);
        if (patient == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        service.delete(patient);  // assumes delete() method exists or is added
        return Response.noContent().build();
    }
}
