/********************************************************************************************************
 * File:  MedicalTrainingResource.java Course Materials CST 8277
 *
 * @author Mohammad AbdElQadir Edris
 *Implemented CRUD resource for MedicalTraining with security
 *******************************************************************************************************/
package acmemedical.rest.resource;

import static acmemedical.utility.MyConstants.ADMIN_ROLE;
import static acmemedical.utility.MyConstants.USER_ROLE;
import static acmemedical.utility.MyConstants.MEDICAL_TRAINING_RESOURCE_NAME;
import static acmemedical.utility.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static acmemedical.utility.MyConstants.RESOURCE_PATH_ID_PATH;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import acmemedical.ejb.ACMEMedicalService;
import acmemedical.entity.MedicalTraining;

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

@Path(MEDICAL_TRAINING_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MedicalTrainingResource {

    private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMEMedicalService service;

    @GET
    @RolesAllowed({ ADMIN_ROLE, USER_ROLE })
    public Response getAllMedicalTrainings() {
        LOG.debug("Retrieving all medical trainings");
        List<MedicalTraining> list = service.getAll(MedicalTraining.class, "MedicalTraining.findAll");
        return Response.ok(list).build();
    }

    @GET
    @RolesAllowed({ ADMIN_ROLE, USER_ROLE })
    @Path(RESOURCE_PATH_ID_PATH)
    public Response getMedicalTrainingById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        LOG.debug("Retrieving medical training by ID: {}", id);
        MedicalTraining mt = service.getMedicalTrainingById(id);
        return (mt != null)
            ? Response.ok(mt).build()
            : Response.status(Status.NOT_FOUND).build();
    }

    @POST
    @RolesAllowed(ADMIN_ROLE)
    public Response createMedicalTraining(MedicalTraining newTraining) {
        LOG.debug("Creating new medical training");
        MedicalTraining created = service.persistMedicalTraining(newTraining);
        return Response.status(Status.CREATED).entity(created).build();
    }

    @PUT
    @RolesAllowed(ADMIN_ROLE)
    @Path(RESOURCE_PATH_ID_PATH)
    public Response updateMedicalTraining(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id, MedicalTraining updated) {
        LOG.debug("Updating medical training with ID {}", id);
        MedicalTraining modified = service.updateMedicalTraining(id, updated);
        return (modified != null)
            ? Response.ok(modified).build()
            : Response.status(Status.NOT_FOUND).build();
    }

    @DELETE
    @RolesAllowed(ADMIN_ROLE)
    @Path(RESOURCE_PATH_ID_PATH)
    public Response deleteMedicalTraining(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        LOG.debug("Deleting medical training with ID {}", id);
        // You may implement deletion logic in service if needed
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }
}
