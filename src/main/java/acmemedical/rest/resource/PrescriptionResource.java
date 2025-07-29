/********************************************************************************************************
 * File:  PrescriptionResource.java Course Materials CST 8277
 *
 * @author Mohammad AbdElQadir Edris
 * RESTful CRUD for Prescription with composite key support and role security
 *******************************************************************************************************/
package acmemedical.rest.resource;

import static acmemedical.utility.MyConstants.ADMIN_ROLE;
import static acmemedical.utility.MyConstants.PRESCRIPTION_RESOURCE_NAME;
import static acmemedical.utility.MyConstants.RESOURCE_PATH_ID_PATH;
import static acmemedical.utility.MyConstants.RESOURCE_PATH_ID_ELEMENT;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import acmemedical.ejb.ACMEMedicalService;
import acmemedical.entity.Prescription;
import acmemedical.entity.PrescriptionPK;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path(PRESCRIPTION_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PrescriptionResource {

    private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMEMedicalService service;

    @GET
    @RolesAllowed(ADMIN_ROLE)
    public Response getAllPrescriptions() {
        LOG.debug("Retrieving all prescriptions");
        List<Prescription> list = service.getAll(Prescription.class, "Prescription.findAll");
        return Response.ok(list).build();
    }

    @GET
    @RolesAllowed(ADMIN_ROLE)
    @Path("/{physicianId}/{patientId}")
    public Response getPrescriptionById(@PathParam("physicianId") int physicianId,
                                        @PathParam("patientId") int patientId) {
        LOG.debug("Retrieving prescription for physicianId = {}, patientId = {}", physicianId, patientId);
        PrescriptionPK pk = new PrescriptionPK(physicianId, patientId);
        Prescription presc = service.findByCompositeId(Prescription.class, pk);
        return (presc != null)
            ? Response.ok(presc).build()
            : Response.status(Status.NOT_FOUND).build();
    }

    @POST
    @RolesAllowed(ADMIN_ROLE)
    public Response createPrescription(Prescription newPresc) {
        LOG.debug("Creating new prescription");
        service.persist(newPresc);
        return Response.status(Status.CREATED).entity(newPresc).build();
    }

    @PUT
    @RolesAllowed(ADMIN_ROLE)
    public Response updatePrescription(Prescription updatedPresc) {
        LOG.debug("Updating prescription");
        Prescription merged = service.update(updatedPresc);
        return Response.ok(merged).build();
    }

    @DELETE
    @RolesAllowed(ADMIN_ROLE)
    @Path("/{physicianId}/{patientId}")
    public Response deletePrescription(@PathParam("physicianId") int physicianId,
                                       @PathParam("patientId") int patientId) {
        LOG.debug("Deleting prescription for physicianId = {}, patientId = {}", physicianId, patientId);
        PrescriptionPK pk = new PrescriptionPK(physicianId, patientId);
        Prescription presc = service.findByCompositeId(Prescription.class, pk);
        if (presc != null) {
            service.delete(presc);
            return Response.noContent().build();
        }
        return Response.status(Status.NOT_FOUND).build();
    }
}
