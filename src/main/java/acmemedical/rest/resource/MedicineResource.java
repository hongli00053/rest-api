/********************************************************************************************************
 * File:  MedicineResource.java Course Materials CST 8277
 *
 * @author Mohammad AbdElQadir Edris
 * RESTful CRUD implementation for Medicine entity
 *******************************************************************************************************/
package acmemedical.rest.resource;

import static acmemedical.utility.MyConstants.ADMIN_ROLE;
import static acmemedical.utility.MyConstants.MEDICINE_RESOURCE_NAME;
import static acmemedical.utility.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static acmemedical.utility.MyConstants.RESOURCE_PATH_ID_PATH;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import acmemedical.ejb.ACMEMedicalService;
import acmemedical.entity.Medicine;

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

@Path(MEDICINE_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MedicineResource {

    private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMEMedicalService service;

    @GET
    @RolesAllowed(ADMIN_ROLE)
    public Response getAllMedicines() {
        LOG.debug("Retrieving all medicines");
        List<Medicine> meds = service.getAll(Medicine.class, "Medicine.findAll");
        return Response.ok(meds).build();
    }

    @GET
    @RolesAllowed(ADMIN_ROLE)
    @Path(RESOURCE_PATH_ID_PATH)
    public Response getMedicineById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        LOG.debug("Getting medicine with id = {}", id);
        Medicine med = service.getById(Medicine.class, "Medicine.findById", id);
        return med != null ? Response.ok(med).build() : Response.status(Status.NOT_FOUND).build();
    }

    @POST
    @RolesAllowed(ADMIN_ROLE)
    public Response createMedicine(Medicine med) {
        LOG.debug("Creating new medicine");
        service.persist(med);
        return Response.status(Status.CREATED).entity(med).build();
    }

    @PUT
    @RolesAllowed(ADMIN_ROLE)
    public Response updateMedicine(Medicine med) {
        LOG.debug("Updating medicine");
        if (med.getId() == 0) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        Medicine updated = service.update(med);
        return updated != null ? Response.ok(updated).build() : Response.status(Status.NOT_FOUND).build();
    }

    @PUT
    @RolesAllowed(ADMIN_ROLE)
    @Path(RESOURCE_PATH_ID_PATH)
    public Response updateMedicineById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id, Medicine med) {
        LOG.debug("Updating medicine with id = {}", id);
        med.setId(id); // Ensure the ID is set correctly
        Medicine updated = service.update(med);
        return updated != null ? Response.ok(updated).build() : Response.status(Status.NOT_FOUND).build();
    }

    @DELETE
    @RolesAllowed(ADMIN_ROLE)
    @Path(RESOURCE_PATH_ID_PATH)
    public Response deleteMedicine(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        LOG.debug("Deleting medicine with id = {}", id);
        Medicine med = service.getById(Medicine.class, "Medicine.findById", id);
        if (med != null) {
            service.delete(med);
            return Response.noContent().build();
        }
        return Response.status(Status.NOT_FOUND).build();
    }
}
