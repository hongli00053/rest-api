/********************************************************************************************************
 * File:  MedicalCertificateResource.java Course Materials CST 8277
 *
 * @author Mohammad AbdElQadir Edris
 * Full CRUD resource for MedicalCertificate with role-based access control
 *******************************************************************************************************/
package acmemedical.rest.resource;

import static acmemedical.utility.MyConstants.ADMIN_ROLE;
import static acmemedical.utility.MyConstants.USER_ROLE;
import static acmemedical.utility.MyConstants.MEDICAL_CERTIFICATE_RESOURCE_NAME;
import static acmemedical.utility.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static acmemedical.utility.MyConstants.RESOURCE_PATH_ID_PATH;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import acmemedical.ejb.ACMEMedicalService;
import acmemedical.entity.MedicalCertificate;

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

@Path(MEDICAL_CERTIFICATE_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MedicalCertificateResource {

    private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMEMedicalService service;

    @GET
    @RolesAllowed(ADMIN_ROLE)
    public Response getAllCertificates() {
        LOG.debug("Retrieving all medical certificates");
        List<MedicalCertificate> list = service.getAll(MedicalCertificate.class, "MedicalCertificate.findAll");
        return Response.ok(list).build();
    }

    @GET
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response getCertificateById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        LOG.debug("Retrieving certificate with ID {}", id);
        MedicalCertificate cert = service.getById(MedicalCertificate.class, "MedicalCertificate.findById", id);
        return (cert != null)
            ? Response.ok(cert).build()
            : Response.status(Status.NOT_FOUND).build();
    }

    @POST
    @RolesAllowed(ADMIN_ROLE)
    public Response createCertificate(MedicalCertificate newCert) {
        LOG.debug("Creating new medical certificate");
        service.persist(newCert);
        return Response.status(Status.CREATED).entity(newCert).build();
    }

    @PUT
    @RolesAllowed(ADMIN_ROLE)
    @Path(RESOURCE_PATH_ID_PATH)
    public Response updateCertificate(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id, MedicalCertificate updatedCert) {
        LOG.debug("Updating certificate with ID {}", id);
        MedicalCertificate modified = service.update(id, updatedCert);
        return (modified != null)
            ? Response.ok(modified).build()
            : Response.status(Status.NOT_FOUND).build();
    }

    @DELETE
    @RolesAllowed(ADMIN_ROLE)
    @Path(RESOURCE_PATH_ID_PATH)
    public Response deleteCertificate(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        LOG.debug("Deleting certificate with ID {}", id);
        MedicalCertificate cert = service.getById(MedicalCertificate.class, "MedicalCertificate.findById", id);
        if (cert != null) {
            service.delete(cert);
            return Response.noContent().build();
        }
        return Response.status(Status.NOT_FOUND).build();
    }
}

