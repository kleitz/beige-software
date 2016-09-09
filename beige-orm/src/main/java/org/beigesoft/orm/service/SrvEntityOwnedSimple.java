package org.beigesoft.orm.service;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.util.List;
import java.util.Map;

import org.beigesoft.model.IHasId;
import org.beigesoft.service.ISrvEntityOwned;

/**
 * <p>Simple business service for persistable entity
 * that owned by an owner entity (one to one or one to many composition),
 * e.g. SalesInvoiceLine(many) for SalesInvoice(to one), and
 * owned can't exist(composition) without owner.
 * It just delegate to ORM service for persist logic.
 * It uses abstract pAddParam for additional
 * communication between business and presentation layers.</p>
 *
 * @param <T> entity type
 * @param <O> owner entity type
 * @author Yury Demidenko
 */
public class SrvEntityOwnedSimple<T extends IHasId<?>, O extends IHasId<?>>
 extends SrvEntitySimple<T> implements ISrvEntityOwned<T, O> {

  /**
   * <p>Entity owner class.</p>
   **/
  private final Class<T> ownerClass;

  /**
   * <p>minimum constructor.</p>
   * @param pEntityClass Entity Class
   * @param pOwnerClass Owner Class
   **/
  public SrvEntityOwnedSimple(final Class<T> pEntityClass,
    final Class<T> pOwnerClass) {
    super(pEntityClass);
    this.ownerClass = pOwnerClass;
  }

  /**
   * <p>Useful constructor.</p>
   * @param pEntityClass Entity Class
   * @param pSrvOrm ORM service
   * @param pOwnerClass Owner Class
   **/
  public SrvEntityOwnedSimple(final Class<T> pEntityClass,
    final Class<T> pOwnerClass, final ISrvOrm<?> pSrvOrm) {
    super(pEntityClass, pSrvOrm);
    this.ownerClass = pOwnerClass;
  }

  /**
   * <p>Create entity with its owner e.g. invoice line
   * for invoice.</p>
   * @param pAddParam additional param
   * @param pIdEntityOwner entity owner ID
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final T createEntityWithOwnerById(final Map<String, ?> pAddParam,
    final Object pIdEntityOwner) throws Exception {
    T entity = getSrvOrm().createEntityWithOwner(getEntityClass(),
      this.ownerClass, pIdEntityOwner);
    entity.setIsNew(true);
    return entity;
  }

  /**
   * <p>Create entity with its owner e.g. invoice line
   * for invoice.</p>
   * @param pAddParam additional param
   * @param pEntityOwner owner
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final T createEntityWithOwner(final Map<String, ?> pAddParam,
    final O pEntityOwner) throws Exception {
    T entity = getSrvOrm().createEntityWithOwner(getEntityClass(),
      this.ownerClass, pEntityOwner.getItsId());
    entity.setIsNew(true);
    return entity;
  }

  /**
   * <p>Retrieve owned list of entities for owner.
   * E.g. invoices lines for invoice</p>
   * @param pAddParam additional param
   * @param pIdEntityOwner ID owner
   * @return owned list of business objects
   * @throws Exception - an exception
   */
  @Override
  public final List<T> retrieveOwnedListById(final Map<String, ?> pAddParam,
    final Object pIdEntityOwner) throws Exception {
    return getSrvOrm().retrieveEntityOwnedlist(getEntityClass(),
      this.ownerClass, pIdEntityOwner);
  }

  /**
   * <p>Retrieve owned list of entities for owner.
   * E.g. invoices lines for invoice</p>
   * @param pAddParam additional param
   * @param pEntityOwner owner
   * @return owned list of business objects
   * @throws Exception - an exception
   */
  @Override
  public final List<T> retrieveOwnedList(final Map<String, ?> pAddParam,
    final O pEntityOwner) throws Exception {
    return getSrvOrm().retrieveEntityOwnedlist(getEntityClass(),
      this.ownerClass, pEntityOwner.getItsId());
  }

  //Simple getters and setters:
  /**
   * <p>Geter for ownerClass.</p>
   * @return final Class<T>
   **/
  public final Class<T> getOwnerClass() {
    return this.ownerClass;
  }
}
