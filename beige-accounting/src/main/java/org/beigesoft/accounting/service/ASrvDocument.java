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

import java.util.Date;
import java.math.BigDecimal;
import java.util.Map;

import org.beigesoft.holder.IAttributes;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.accounting.persistable.IDoc;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Business service for accounting document
 * that makes accounting entries.</p>
 *
 * @param <RS> platform dependent record set type
 * @param <T> document type
 * @author Yury Demidenko
 */
public abstract class ASrvDocument<RS, T extends IDoc>
  extends ASrvAccEntitySimple<RS, T> {

  /**
   * <p>Business service for accounting entries.</p>
   **/
  private ISrvAccEntry srvAccEntry;

  /**
   * <p>minimum constructor.</p>
   * @param pEntityClass Entity Class
   **/
  public ASrvDocument(final Class<T> pEntityClass) {
    super(pEntityClass);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pEntityClass Entity Class
   * @param pSrvOrm ORM service
   * @param pSrvAccSettings AccSettings service
   * @param pSrvAccEntry Accounting entries service
   **/
  public ASrvDocument(final Class<T> pEntityClass, final ISrvOrm<RS> pSrvOrm,
    final ISrvAccSettings pSrvAccSettings,
      final ISrvAccEntry pSrvAccEntry) {
    super(pEntityClass, pSrvOrm, pSrvAccSettings);
    this.srvAccEntry = pSrvAccEntry;
  }

  /**
   * <p>Refresh entity from DB by given entity with ID.</p>
   * @param pEntity entity
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final T retrieveEntity(final Map<String, ?> pAddParam,
    final T pEntity) throws Exception {
    return retrieveEntityById(pAddParam, pEntity.getItsId());
  }

  /**
   * <p>Retrieve entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final T retrieveEntityById(final Map<String, ?> pAddParam,
    final Object pId) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    T entity = getSrvOrm().retrieveEntityById(getEntityClass(), pId);
    @SuppressWarnings("unchecked")
    Map<String, String[]> parameterMap = (Map<String, String[]>) pAddParam.
      get("parameterMap");
    if (parameterMap.get("actionAdd") != null
      && "full".equals(parameterMap.get("actionAdd")[0])) {
      IAttributes attributes = (IAttributes) pAddParam.get("attributes");
      attributes.setAttribute("accEntries", srvAccEntry
        .retrieveAccEntriesFor(pAddParam, entity));
    }
    retrieveOtherDataFor(pAddParam, entity);
    return entity;
  }

  /**
   * <p>Save entity into DB.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @param isEntityDetached ignored
   * @throws Exception - an exception
   **/
  @Override
  public final void saveEntity(final Map<String, ?> pAddParam,
    final T pEntity,
      final boolean isEntityDetached) throws Exception {
    boolean isNew = pEntity.getIsNew();
    makeFirstPrepareForSave(pAddParam, pEntity);
    @SuppressWarnings("unchecked")
    Map<String, String[]> parameterMap = (Map<String, String[]>) pAddParam.
      get("parameterMap");
    if (pEntity.getIsNew()) {
      if (pEntity.getReversedId() != null
        && pEntity.getItsTotal().doubleValue() >= 0) {
          throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
            "Reversed Total must be less than 0! " + pAddParam.get("user"));
      }
      if (pEntity.getReversedId() == null
        && pEntity.getItsTotal().doubleValue() < 0) {
          throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
            "Total must be less than 0 only in reversal! "
              + pAddParam.get("user"));
      }
      if (pEntity.getReversedId() != null) {
        String descr;
        if (pEntity.getDescription() == null) {
          descr = "";
        } else {
          descr = pEntity.getDescription();
        }
        pEntity.setDescription(descr
          + " reversed ID: " + pEntity.getReversedId());
      }
      getSrvOrm().insertEntity(pEntity);
      if (pEntity.getReversedId() != null) {
        T reversed = getSrvOrm().retrieveEntityById(
          getEntityClass(), pEntity.getReversedId());
        if (reversed.getReversedId() != null) {
          throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
            "Attempt to double reverse! " + pAddParam.get("user"));
        }
        reversed.setDescription(reversed.getDescription()
          + " reversing ID: " + pEntity.getItsId());
        reversed.setReversedId(pEntity.getItsId());
        getSrvOrm().updateEntity(reversed);
        srvAccEntry.reverseEntries(pAddParam, pEntity, reversed);
      }
    } else {
      //Prevent any changes when document has accounting entries:
      T oldEntity = getSrvOrm()
        .retrieveEntityById(getEntityClass(), pEntity.getItsId());
      if (oldEntity.getHasMadeAccEntries()) {
        throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
          "Attempt to update accounted document by " + pAddParam.get("user"));
      }
      checkOtherFraudUpdate(pAddParam, pEntity, oldEntity);
      if (parameterMap.get("actionAdd") == null
        || !"makeAccEntries".equals(parameterMap.get("actionAdd")[0])) {
        getSrvOrm().updateEntity(pEntity);
      }
    }
    if (!pEntity.getHasMadeAccEntries() && pEntity.getReversedId() == null
      && parameterMap.get("actionAdd") != null
        && "makeAccEntries".equals(parameterMap.get("actionAdd")[0])) {
      if (pEntity.getItsTotal().doubleValue() <= 0) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "total_less_or_eq_zero");
      }
      addCheckIsReadyToAccount(pAddParam, pEntity);
      srvAccEntry.makeEntries(pAddParam, pEntity); //it will update this doc
    }
    makeOtherEntries(pAddParam, pEntity, isNew);
  }

  /**
   * <p>Retrieve copy of entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final T retrieveCopyEntity(
    final Map<String, ?> pAddParam,
      final Object pId) throws Exception {
    T entity = getSrvOrm()
      .retrieveCopyEntity(getEntityClass(), pId);
    @SuppressWarnings("unchecked")
    Map<String, String[]> parameterMap = (Map<String, String[]>) pAddParam.
      get("parameterMap");
    if (parameterMap.get("actionAdd") != null
      && "reverse".equals(parameterMap.get("actionAdd")[0])) {
      if (entity.getReversedId() != null) {
        throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
          "Attempt to double reverse! " + pAddParam.get("user"));
      }
      entity.setReversedId(Long.valueOf(pId.toString()));
      entity.setItsTotal(entity.getItsTotal().negate());
    } else {
      entity.setItsTotal(new BigDecimal("0.00"));
    }
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setItsDate(new Date());
    entity.setIsNew(true);
    entity.setHasMadeAccEntries(false);
    makeAddPrepareForCopy(pAddParam, entity);
    addAccSettingsIntoAttrs(pAddParam);
    return entity;
  }

  /**
   * <p>Delete entity from DB.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final void deleteEntity(final Map<String, ?> pAddParam,
    final T pEntity) throws Exception {
    deleteEntity(pAddParam, pEntity.getItsId());
  }

  /**
   * <p>Delete entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @throws Exception - an exception
   **/
  @Override
  public final void deleteEntity(final Map<String, ?> pAddParam,
    final Object pId) throws Exception {
    T entity = retrieveEntityById(pAddParam, pId);
    if (entity.getHasMadeAccEntries()
      || entity.getItsTotal().doubleValue() > 0) {
      throw new ExceptionWithCode(ExceptionWithCode.FORBIDDEN,
        "Attempt to delete document by " + pAddParam.get("user"));
    }
    getSrvOrm().deleteEntity(getEntityClass(), pId);
  }

  //To override:
  /**
   * <p>Make save preparations before insert/update block if it's need.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  public abstract void makeFirstPrepareForSave(Map<String, ?> pAddParam,
    T pEntity) throws Exception;

  /**
   * <p>Make other entries include reversing if it's need when save.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @param pIsNew if entity was new
   * @throws Exception - an exception
   **/
  public abstract void makeOtherEntries(Map<String, ?> pAddParam,
    T pEntity, boolean pIsNew) throws Exception;

  /**
   * <p>Check other fraud update e.g. prevent change completed unaccounted
   * manufacturing process.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @param pOldEntity old saved entity
   * @throws Exception - an exception
   **/
  public abstract void checkOtherFraudUpdate(Map<String, ?> pAddParam,
    T pEntity, T pOldEntity) throws Exception;

  /**
   * <p>Make additional preparations on entity copy.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  public abstract void makeAddPrepareForCopy(Map<String, ?> pAddParam,
    T pEntity) throws Exception;

  /**
   * <p>Additional check document for ready to account (make acc.entries).</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception if don't
   **/
  public abstract void addCheckIsReadyToAccount(Map<String, ?> pAddParam,
    T pEntity) throws Exception;

  /**
   * <p>Retrieve other data of entity e.g. warehouse entries.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  public abstract void retrieveOtherDataFor(Map<String, ?> pAddParam,
    T pEntity) throws Exception;

  //Simple getters and setters:
  /**
   * <p>Getter for srvAccEntry.</p>
   * @return ISrvAccEntry
   **/
  public final ISrvAccEntry getSrvAccEntry() {
    return this.srvAccEntry;
  }

  /**
   * <p>Setter for srvAccEntry.</p>
   * @param pSrvAccEntry reference
   **/
  public final void setSrvAccEntry(final ISrvAccEntry pSrvAccEntry) {
    this.srvAccEntry = pSrvAccEntry;
  }
}
