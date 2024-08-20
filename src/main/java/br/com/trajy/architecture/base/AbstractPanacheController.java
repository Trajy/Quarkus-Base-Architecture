package br.com.trajy.architecture.base;

import static jakarta.transaction.Transactional.TxType.NEVER;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.noContent;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * Abstract Class that contains boilerplate code to implement GET, CREATE, UPDATE and DELETE rest end-points
 * using Hibernate panache entity reactive
 * @author Trajy
 * @param <D> Data Transfer Object Type (DTO)
 * @param <E> Entity Type
 * @param <S> Service Type
 * @param <A> Assembly Type
 */
public abstract class AbstractPanacheController<D, E extends PanacheEntity, S extends AbstractPanacheService<E>, A extends AbstractPanacheAssembly<D, E>> {

    @Inject
    protected S service;

    @Inject
    protected A assembly;

    /**
     * Boilerplate code implementation to handle GET requests
     * @author Trajy
     * @return List of found objects
     */
    @GET
    @Produces(APPLICATION_JSON)
    @Transactional(NEVER)
    public List<D> find() {
        this.beforeFind();
        List<D> ds = service.find().stream().map(e -> assembly.assembly(e)).toList();
        this.afterFind(ds);
        return ds;
    }

    /**
     * Boilerplate code implementation to handle POST requests
     * @author Trajy
     * @param d data from request body
     * @return no content 204 response
     */
    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Transactional
    public Response create(D d) {
        this.beforeCreate(d);
        service.create(assembly.assembly(d));
        this.afterCreate();
        return noContent().build();
    }

    /**
     * Boilerplate code implementation to handle PUT requests
     * @author Trajy
     * @param id id from rul path param
     * @param d data from request body
     * @return no content 204 response
     */
    @PUT
    @Path("/{id}")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Transactional
    public Response update(@PathParam("id") Long id, D d) {
        this.beforeUpdate(id, d);
        service.update(id, assembly.fillToAttachedEntity(service.findById(id), assembly.assembly(d)));
        this.afterUpdate();
        return noContent().build();
    }

    /**
     * Boilerplate code implementation to handle DELETE requests
     * @author Trajy
     * @param id id from request path param
     * @return no content 204 response
     */
    @DELETE
    @Path("/{id}")
    @Produces(APPLICATION_JSON)
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        this.beforeDelete(id);
        service.delete(id);
        this.afterDelete();
        return noContent().build();
    }

    /**
     * @author Trajy
     * Override this method to handle request before find
     */
    protected void beforeFind() { }

    /**
     * Override this method to handle request after find
     * @author Trajy
     * @param es List of found results
     */
    protected void afterFind(List<D> es) { }

    /**
     * Override this method to handle request before create
     * @author Trajy
     * @param d data received from request body
     */
    protected void beforeCreate(D d) { }

    /**
     * Override this method to handle request after create
     * @author Trajy
     */
    protected void afterCreate() { }

    /**
     * Override this method to handle request before update
     * @author Trajy
     * @param id received from path url
     * @param d data received from request body
     */
    protected void beforeUpdate(Long id, D d) { }

    /**
     * Override this method to handle request after update
     * @author Trajy
     */
    protected void afterUpdate() { }

    /**
     * Override this method to handle request before delete
     * @author Trajy
     * @param id id received from path url
     */
    protected void beforeDelete(Long id) { }

    /**
     * Override this method to handle request after delete
     * @author Trajy
     */
    protected void afterDelete() { }

}
