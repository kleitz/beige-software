package org.beigesoft.accounting.service;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.util.Map;

import org.beigesoft.persistable.APersistableBase;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.model.IHasId;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Simple business service for persistable entity.
 * It just delegate to ORM service for persist logic.
 * It uses abstract pAddParam for additional
 * communication between business and presentation layers.</p>
 *
 * @param <T> entity type
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public abstract class ASrvAccEntityExt<RS, T extends IHasId<?>>
  extends ASrvAccEntitySimple<RS, T> {

  /**
   * <p>minimum constructor.</p>
   * @param pEntityClass Entity Class
   **/
  public ASrvAccEntityExt(final Class<T> pEntityClass) {
    super(pEntityClass);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pEntityClass Entity Class
   * @param pSrvOrm ORM service
   * @param pSrvAccSettings AccSettings service
   **/
  public ASrvAccEntityExt(final Class<T> pEntityClass,
    final ISrvOrm<RS> pSrvOrm,
        final ISrvAccSettings pSrvAccSettings) {
    super(pEntityClass, pSrvOrm, pSrvAccSettings);
  }

  /**
   * <p>Refresh entity from DB by given entity with ID.</p>
   * @param pEntity entity
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final T retrieveEntity(final Map<String, Object> pAddParam,
    final T pEntity) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityById(getEntityClass(), pEntity.getItsId());
  }

  /**
   * <p>Retrieve entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final T retrieveEntityById(final Map<String, Object> pAddParam,
    final Object pId) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityById(getEntityClass(), pId);
  }

  /**
   * <p>Delete entity from DB.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final void deleteEntity(final Map<String, Object> pAddParam,
    final T pEntity) throws Exception {
    if (APersistableBase.class.isAssignableFrom(pEntity.getClass())
      && !((APersistableBase) pEntity).getIdDatabaseBirth()
        .equals(getSrvOrm().getIdDatabase())) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "can_not_delete_foreign_src");
    }
    getSrvOrm().deleteEntity(getEntityClass(), pEntity.getItsId());
  }

  /**
   * <p>Delete entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @throws Exception - an exception
   **/
  @Override
  public final void deleteEntity(final Map<String, Object> pAddParam,
    final Object pId) throws Exception {
    T entity = retrieveEntityById(pAddParam, pId);
    deleteEntity(pAddParam, entity);
  }
}
