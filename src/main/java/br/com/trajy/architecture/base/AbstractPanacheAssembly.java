package br.com.trajy.architecture.base;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

/**
 * Abstract Class that contains boilerplate code to implement fundamental assembly operations
 * @author Trajy
 * @param <D> Data Transfer Object Type (DTO)
 * @param <E> Entity Type
 */
public abstract class AbstractPanacheAssembly<D, E extends PanacheEntity> {

    /**
     * Override this method with implementation to assembly Data Transfer Object (DTO) type into Entity type
     * @author Trajy
     * @param d Data Transfer Object (DTO)
     * @return Entity filled with same DTO data
     */
    public abstract E assembly(D d);

    /**
     * Override this method with implementation to assembly Entity type into Data Transfer Object (DTO) type
     * @author Trajy
     * @param e Entity object
     * @return Data Transfer Object (DTO) filled with same Entity data
     */
    public abstract D assembly(E e);

    /**
     * Copy id from another entity
     * @author Trajy
     * @param attached Entity atteched that will receive new Data
     * @param detached Entity detached that contains new data
     * @return Same ateched object in e param filled with new data from detached object
     */
    public abstract E fillToAttachedEntity(E attached, E detached);

}
