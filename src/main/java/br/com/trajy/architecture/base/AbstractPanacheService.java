package br.com.trajy.architecture.base;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import java.util.List;

/**
 * Abstract Class that contains boilerplate code to implement find, create, update and delete operations with your
 * respective business rules using Hibernate panache entity reactive
 * @author Trajy
 * @param <E> Entity type
 */
public abstract class AbstractPanacheService<E extends PanacheEntity> {

    /**
     * Boilerplate code implementation to handle find feature
     * @author Trajy
     * @return found results
     */
    public List<E> find() {
        this.beforeFind();
        List<E> es = this.findStaticMethod();
        this.afterFind(es);
        return es;
    }

    /**
     * Boilerplate code implementation to handle find by id feature
     * @author Trajy
     * @param id id to search
     * @return found entity
     */
    public E findById(Long id) {
        this.beforeFindById(id);
        E entity = this.findByIdStatidMethod(id);
        this.afterFindById(entity);
        return entity;
    }

    /**
     * Boilerplate code implementation to handle create feature
     * @author Trajy
     * @param e entity with data to persist
     */
    public void create(E e) {
        this.beforeCreate(e);
        e.persist();
    }

    /**
     * Boilerplate code implementation to handle update feature
     * @author Trajy
     * @param id id of object to update
     * @param e entity with data to update
     */
    public void update(Long id, E e) {
        this.beforeUpdate(id, e);
        e.persist();
        this.afterUpdate(id, e);
    }

    /**
     * Boilerplate code implementation to handle delete feature
     * @author Trajy
     * @param id id to delete
     */
    public void delete(Long id) {
        this.beforeDelete(id);
        this.deleteByIdStaticMethod(id);
        this.afterDelete(id);
    }

    /**
     * Override this method to call static implementation of find method from Entity that extends PanacheEntity
     * @author Trajy
     * @return results from static implementation
     */
    protected abstract List<E> findStaticMethod();

    /**
     * Override this method to call static implementation of find by id method from Entity that extends PanacheEntity
     * @author Trajy
     * @param id id to search
     * @return result from static implementation
     */
    protected abstract E findByIdStatidMethod(Long id);

    /**
     * Override this method to call static implementation of delete method from Entity that extends PanacheEntity
     * @author Trajy
     * @param id id to delete
     */
    protected abstract void deleteByIdStaticMethod(Long id);

    /**
     * Override this method to handle business logic before find feature
     * @author Trajy
     */
    protected void beforeFind() { }

    /**
     * Override this method to handle business logic after find feature
     * @author Trajy
     * @param es found entities
     */
    protected void afterFind(List<E> es) { }

    /**
     * Override this method to handle business logic before find by id feature
     * @author Trajy
     * @param id id that will be searched
     */
    protected void beforeFindById(Long id) { }

    /**
     * Override this method to handle business logic after find by id feature
     * @author Trajy
     * @param e found entity
     */
    protected void afterFindById(E e) { }

    /**
     * Override this method to handle business logic before create feature
     * @param e entity that will be persisted
     */
    protected void beforeCreate(E e) { }

    /**
     * Override this method to handle business logic before update feature
     * @author Trajy
     * @param id id that will be updated
     * @param e entity with new data to update
     */
    protected void beforeUpdate(Long id, E e) { }

    /**
     * Override this method to handle business logic after update feature
     * @author Trajy
     * @param id id that has searched
     * @param e entity that has persisted
     */
    protected void afterUpdate(Long id, E e) { }

    /**
     * Override this method to handle business logic before delete feature
     * @author Trajy
     * @param id id ta will be deleted
     */
    protected void beforeDelete(Long id) { }

    /**
     * Override this method to handle business logic after delete feature
     * @author Trajy
     * @param id id that has deleted
     */
    protected void afterDelete(Long id) { }

}
