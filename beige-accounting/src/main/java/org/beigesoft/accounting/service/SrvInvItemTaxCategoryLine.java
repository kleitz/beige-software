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

import java.util.List;
import java.util.Map;

import org.beigesoft.accounting.model.ETaxType;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.accounting.persistable.InvItemTaxCategory;
import org.beigesoft.accounting.persistable.InvItemTaxCategoryLine;
import org.beigesoft.service.ISrvEntityOwned;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Business service for invItem tax Line.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvInvItemTaxCategoryLine<RS>
  extends ASrvAccEntitySimple<RS, InvItemTaxCategoryLine>
    implements ISrvEntityOwned<InvItemTaxCategoryLine, InvItemTaxCategory> {

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvInvItemTaxCategoryLine() {
    super(InvItemTaxCategoryLine.class);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvAccSettings AccSettings service
   **/
  public SrvInvItemTaxCategoryLine(final ISrvOrm<RS> pSrvOrm,
    final ISrvAccSettings pSrvAccSettings) {
    super(InvItemTaxCategoryLine.class, pSrvOrm, pSrvAccSettings);
  }

  /**
   * <p>Create entity.</p>
   * @param pAddParam additional param
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final InvItemTaxCategoryLine createEntity(
    final Map<String, Object> pAddParam) throws Exception {
    InvItemTaxCategoryLine entity = new InvItemTaxCategoryLine();
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setIsNew(true);
    InvItemTaxCategory itsOwner = new InvItemTaxCategory();
    entity.setItsOwner(itsOwner);
    addAccSettingsIntoAttrs(pAddParam);
    return entity;
  }

  /**
   * <p>Retrieve entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final InvItemTaxCategoryLine retrieveEntityById(
    final Map<String, Object> pAddParam,
      final Object pId) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityById(getEntityClass(), pId);
  }

  /**
   * <p>Refresh entity from DB by given entity with ID.</p>
   * @param pEntity entity
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final InvItemTaxCategoryLine retrieveEntity(
    final Map<String, Object> pAddParam,
      final InvItemTaxCategoryLine pEntity) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityById(getEntityClass(), pEntity.getItsId());
  }

  /**
   * <p>Retrieve copy of entity from DB by given ID.</p>
   * @param pAddParam additional param
   * @param pId ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final InvItemTaxCategoryLine retrieveCopyEntity(
    final Map<String, Object> pAddParam,
      final Object pId) throws Exception {
    InvItemTaxCategoryLine entity = getSrvOrm().retrieveCopyEntity(
      InvItemTaxCategoryLine.class, pId);
    entity.setTax(null);
    entity.setIsNew(true);
    addAccSettingsIntoAttrs(pAddParam);
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
  public final void saveEntity(final Map<String, Object> pAddParam,
    final InvItemTaxCategoryLine pEntity,
      final boolean isEntityDetached) throws Exception {
    if (pEntity.getItsPercentage().doubleValue() <= 0) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "percentage_wrong");
    }
    if (pEntity.getTax() == null) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "tax_wrong");
    }
    pEntity.setTax(getSrvOrm().retrieveEntity(pEntity.getTax())); //SQL refresh
    if (!(ETaxType.SALES_TAX_INITEM.equals(pEntity.getTax().getItsType())
      || ETaxType.SALES_TAX_OUTITEM.equals(pEntity.getTax().getItsType()))) {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "tax_wrong");
    }
    if (pEntity.getIsNew()) {
      getSrvOrm().insertEntity(pEntity);
    } else {
      getSrvOrm().updateEntity(pEntity);
    }
    updateInvItemTaxCategory(pAddParam, pEntity.getItsOwner().getItsId());
  }

  /**
   * <p>Delete entity from DB.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final void deleteEntity(final Map<String, Object> pAddParam,
    final InvItemTaxCategoryLine pEntity) throws Exception {
    getSrvOrm().deleteEntity(getEntityClass(), pEntity.getItsId());
    updateInvItemTaxCategory(pAddParam, pEntity.getItsOwner().getItsId());
    addAccSettingsIntoAttrs(pAddParam);
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
    InvItemTaxCategoryLine entity = getSrvOrm()
      .retrieveEntityById(getEntityClass(), pId);
    deleteEntity(pAddParam, entity);
  }

  /**
   * <p>Create entity with its itsOwner e.g. invoice line
   * for invoice.</p>
   * @param pAddParam additional param
   * @param pIdEntityItsOwner entity itsOwner ID
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final InvItemTaxCategoryLine createEntityWithOwnerById(
    final Map<String, Object> pAddParam,
      final Object pIdEntityItsOwner) throws Exception {
    InvItemTaxCategoryLine entity = new InvItemTaxCategoryLine();
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setIsNew(true);
    InvItemTaxCategory itsOwner = new InvItemTaxCategory();
    itsOwner.setItsId(Long.valueOf(pIdEntityItsOwner.toString()));
    entity.setItsOwner(itsOwner);
    addAccSettingsIntoAttrs(pAddParam);
    return entity;
  }

  /**
   * <p>Create entity with its itsOwner e.g. invoice line
   * for invoice.</p>
   * @param pAddParam additional param
   * @param pEntityItsOwner itsOwner
   * @return entity instance
   * @throws Exception - an exception
   **/
  @Override
  public final InvItemTaxCategoryLine createEntityWithOwner(
    final Map<String, Object> pAddParam,
      final InvItemTaxCategory pEntityItsOwner) throws Exception {
    InvItemTaxCategoryLine entity = new InvItemTaxCategoryLine();
    entity.setIsNew(true);
    entity.setIdDatabaseBirth(getSrvOrm().getIdDatabase());
    entity.setItsOwner(pEntityItsOwner);
    addAccSettingsIntoAttrs(pAddParam);
    return entity;
  }

  /**
   * <p>Retrieve owned list of entities for itsOwner.
   * E.g. invoices lines for invoice</p>
   * @param pAddParam additional param
   * @param pIdEntityItsOwner ID itsOwner
   * @return owned list of business objects
   * @throws Exception - an exception
   */
  @Override
  public final List<InvItemTaxCategoryLine> retrieveOwnedListById(
    final Map<String, Object> pAddParam,
      final Object pIdEntityItsOwner) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityOwnedlist(InvItemTaxCategoryLine.class,
      InvItemTaxCategory.class, pIdEntityItsOwner);
  }

  /**
   * <p>Retrieve owned list of entities for itsOwner.
   * E.g. invoices lines for invoice</p>
   * @param pAddParam additional param
   * @param pEntityItsOwner itsOwner
   * @return owned list of business objects
   * @throws Exception - an exception
   */
  @Override
  public final List<InvItemTaxCategoryLine> retrieveOwnedList(
    final Map<String, Object> pAddParam,
      final InvItemTaxCategory pEntityItsOwner) throws Exception {
    addAccSettingsIntoAttrs(pAddParam);
    return getSrvOrm().retrieveEntityOwnedlist(InvItemTaxCategoryLine.class,
      InvItemTaxCategory.class, pEntityItsOwner.getItsId());
  }

  //Utils:
  /**
   * <p>Update InvItemTaxCategory.</p>
   * @param pAddParam additional param
   * @param pId InvItemTaxCategory ID
   * @throws Exception - an exception
   **/
  public final void updateInvItemTaxCategory(
    final Map<String, Object> pAddParam, final Long pId) throws Exception {
    List<InvItemTaxCategoryLine> ptl = getSrvOrm()
      .retrieveEntityOwnedlist(InvItemTaxCategoryLine.class,
        InvItemTaxCategory.class, pId);
    StringBuffer sb = new StringBuffer("");
    int i = 0;
    for (InvItemTaxCategoryLine pt : ptl) {
      if (i++ > 0) {
        sb.append(", ");
      }
      sb.append(pt.getTax().getItsName() + " "
        + pt.getItsPercentage() + "%");
    }
    InvItemTaxCategory itsOwner = getSrvOrm()
      .retrieveEntityById(InvItemTaxCategory.class, pId);
    itsOwner.setTaxesDescription(sb.toString());
    getSrvOrm().updateEntity(itsOwner);
  }
}
