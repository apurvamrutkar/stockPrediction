/**
 * 
 */

package com.apurv.stockpredictor.dao;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaQuery;

import com.apurv.stockpredictor.exceptions.DatabaseException;
import com.apurv.stockpredictor.exceptions.StaleDataException;

/**
 * @author Apurv Amrutkar
 *
 */
@Stateless
public abstract class AbstractDaoImpl<T> {

    /** The entity class. */
    private Class<T> entityClass;

    /**
     * Instantiates a new TaskMgmtApp abstract dao impl.
     *
     * @param entityClass the entity class
     */
    public AbstractDaoImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Gets the entity manager.
     *
     * @return the entity manager
     */
    protected abstract EntityManager getEntityManager();

    /**
     * Creates new record for Entity T in Database
     *
     * @param entity the entity
     * @throws DatabaseException when there is connection error or SQL Persistence exception
     */
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public T create(T entity) throws DatabaseException {
        getEntityManager().persist(entity);
        try {

            getEntityManager().flush();
            return entity;
        }
        catch (PersistenceException persistenceException) {
            Logger.getLogger(AbstractDaoImpl.class.getName()).log(java.util.logging.Level.SEVERE, "Error in edit {0}",
                    new Object[] { persistenceException.getMessage() });
            throw new DatabaseException("Error while creating "+entity.getClass());
        }
    }

    /**
     * Edits the entity T and merges the changes in database
     *
     * @param entity the entity
     * @return the t
     * @throws DatabaseException when there is a connection error or there is SWL exception while merging
     */
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public T edit(T entity) throws DatabaseException {
        try {
            entity = getEntityManager().merge(entity);
            getEntityManager().flush();
            return entity;
        }
        catch (PersistenceException persistenceException) {
            Logger.getLogger(AbstractDaoImpl.class.getName()).log(java.util.logging.Level.SEVERE, "Error in edit {0}",
                    new Object[] { persistenceException.getMessage() });
            throw new DatabaseException("Error while editing "+entity.getClass());
        }
    }

    /**
     * Removes the entity record from the Database.
     *
     * @param entity the entity
     */
    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
        getEntityManager().flush();
    }

    /**
     * Flush- commits all the actions done on the database and are in Queue currently
     *
     * @throws DatabaseException the database exception when there is persistence exception with database
     * mostly due to connection error.
     * @throws StaleDataException when there is verion error(Optimistic lock) due to old version being commited
     * in Database
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void flush() throws DatabaseException, StaleDataException {
        try {
            getEntityManager().flush();
        }
        catch (PersistenceException persistenceException) {
            Logger.getLogger(AbstractDaoImpl.class.getName()).log(java.util.logging.Level.SEVERE, "Error in flushing {0}",
                    new Object[] { persistenceException.getMessage() });
            if (persistenceException instanceof OptimisticLockException) {
                throw new StaleDataException("The Entity was outdated");
            }
            throw new DatabaseException("Error while flushing(commiting new updates) to "+entityClass);
        }
    }

    /**
     * Refresh entity to refresh entity and cache all new records/updated records
     *
     * @param entity the entity
     */
    public void refreshEntity(T entity) {
        getEntityManager().refresh(entity);
    }

    /**
     * Find the Entity record with the Primary Key id provided as input
     *
     * @param id the id
     * @return the t
     * @throws IllegalArgumentException if the primary key passed to this method is not
     * of the right type
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public T find(Long id) {
        return getEntityManager().find(entityClass, id);
    }

    /**
     * Find all the records of the entity in Database
     *
     * @return the list
     * @throws DatabaseException when there is connection error
     */
    @SuppressWarnings("unchecked")
    public List<T> findAll() throws DatabaseException {
        try {
            CriteriaQuery<Object> cq = getEntityManager().getCriteriaBuilder().createQuery();
            cq.select(cq.from(entityClass));
            cq.orderBy(getEntityManager().getCriteriaBuilder().asc(
                    cq.from(entityClass).get("id")));

            return (List<T>) getEntityManager().createQuery(cq).getResultList();
        }
        catch (Exception e) {
            throw new DatabaseException("Error while findingAll "+entityClass);
        }
    }

}
