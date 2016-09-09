package org.beigesoft.web.service;

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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import javax.servlet.http.HttpServletRequest;

import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.factory.IFactoryAppBeans;
import org.beigesoft.service.UtlReflection;
import org.beigesoft.properties.UtlProperties;
import org.beigesoft.service.ISrvI18n;
import org.beigesoft.service.ISrvEntity;
import org.beigesoft.service.ISrvEntityOwned;
import org.beigesoft.orm.service.ISrvDatabase;
import org.beigesoft.settings.MngSettings;
import org.beigesoft.web.model.Page;
import org.beigesoft.web.model.RequestAttrs;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.model.IHasId;
import org.beigesoft.log.ILogger;

/**
 * <p>Business (transactional) generic service for any Entity (persistable).
 * Do not use it in other business (transactional) service.
 * It must return(attach) full loaded models (non-lazy).
 * It's designed to be used in a servlet.
 * It's made according Beigesoft WEB interface specification version #1.</p>
 *
 * @author Yury Demidenko
 */
public class SrvWebEntity implements ISrvWebEntity {

  /**
   * <p>Logger.</p>
   **/
  private ILogger logger;

  /**
   * <p>ORM service.</p>
   **/
  private ISrvOrm<?> srvOrm;

  /**
   * <p>I18N service cause JSTL I18N doesn't works on Android.</p>
   **/
  private ISrvI18n srvI18n;

  /**
   * <p>JSP utils.</p>
   */
  private UtlJsp utlJsp;

  /**
   * <p>Reflection service.</p>
   **/
  private UtlReflection utlReflection;

  /**
   * <p>Page service.</p>
   */
  private ISrvPage srvPage;

  /**
   * <p>Database service.</p>
   */
  private ISrvDatabase<?> srvDatabase;

  /**
   * <p>Manager UVD settings.</p>
   **/
  private MngSettings mngUvdSettings;

  /**
   * <p>Service that fill entity from pequest.</p>
   **/
  private ISrvWebMvc srvWebMvc;

  /**
   * <p>Entities map "EntitySimpleName"-"Class".</p>
   **/
  private Map<String, Class<?>> entitiesMap;

    /**
   * <p>Factory of entities services.</p>
   **/
  private IFactoryAppBeans factoryEntityServices;

  /**
   * <p>Properties service.</p>
   **/
  private final UtlProperties utlProperties = new UtlProperties();

  /**
   * <p>Create entity.</p>
   * @param pReq - servlet request
   * @throws Exception - an exception
   */
  @Override
  public final void create(final HttpServletRequest pReq) throws Exception {
    String nameEntity = pReq.getParameter("nameEntity");
    String nameSrvEntity = pReq.getParameter("nameSrvEntity");
    if (nameSrvEntity == null || nameSrvEntity.trim().length() < 3) {
      nameSrvEntity = "srv" + nameEntity;
    }
    String userName = null;
    if (pReq.getUserPrincipal() != null) {
      userName = pReq.getUserPrincipal().getName();
    }
    Map<String, Object> addParams = new HashMap<String, Object>();
    addParams.put("user", userName);
    addParams.put("attributes", new RequestAttrs(pReq));
    addParams.put("parameterMap", pReq.getParameterMap());
    @SuppressWarnings("unchecked")
    ISrvEntity<IHasId<?>> srvEntity = (ISrvEntity<IHasId<?>>)
      this.factoryEntityServices.lazyGet(nameSrvEntity);
    IHasId<?> entity = srvEntity.createEntity(addParams);
    putEntityIntoRequest(pReq, entity);
  }

  /**
   * <p>Create entity transactional (use RDBMS).</p>
   * @param pReq - servlet request
   * @throws Exception - an exception
   */
  @Override
  public final void createTransactional(
    final HttpServletRequest pReq) throws Exception {
    String nameEntity = pReq.getParameter("nameEntity");
    String nameSrvEntity = pReq.getParameter("nameSrvEntity");
    if (nameSrvEntity == null || nameSrvEntity.trim().length() < 3) {
      nameSrvEntity = "srv" + nameEntity;
    }
    String userName = null;
    if (pReq.getUserPrincipal() != null) {
      userName = pReq.getUserPrincipal().getName();
    }
    Map<String, Object> addParams = new HashMap<String, Object>();
    addParams.put("user", userName);
    addParams.put("attributes", new RequestAttrs(pReq));
    addParams.put("parameterMap", pReq.getParameterMap());
    @SuppressWarnings("unchecked")
    ISrvEntity<IHasId<?>> srvEntity = (ISrvEntity<IHasId<?>>)
      this.factoryEntityServices.lazyGet(nameSrvEntity);
    try {
      this.srvDatabase.setIsAutocommit(false);
      this.srvDatabase.
        setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
      this.srvDatabase.beginTransaction();
      IHasId<?> entity = srvEntity.createEntity(addParams);
      putEntityIntoRequest(pReq, entity);
      this.srvDatabase.commitTransaction();
    } catch (Exception ex) {
      this.srvDatabase.rollBackTransaction();
      throw ex;
    } finally {
      this.srvDatabase.releaseResources();
    }
  }

  /**
   * <p>View entity.</p>
   * @param pReq - servlet request
   * @throws Exception - an exception
   */
  @Override
  public final void view(final HttpServletRequest pReq) throws Exception {
    try {
      this.srvDatabase.setIsAutocommit(false);
      this.srvDatabase.
        setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
      this.srvDatabase.beginTransaction();
      retrieveEntityAndPutIntoRequest(pReq);
      this.srvDatabase.commitTransaction();
    } catch (Exception ex) {
      this.srvDatabase.rollBackTransaction();
      throw ex;
    } finally {
      this.srvDatabase.releaseResources();
    }
  }

  /**
   * <p>Print entity.</p>
   * @param pReq - servlet request
   * @throws Exception - an exception
   */
  @Override
  public final void print(final HttpServletRequest pReq) throws Exception {
    try {
      this.srvDatabase.setIsAutocommit(false);
      this.srvDatabase.
        setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
      this.srvDatabase.beginTransaction();
      retrieveEntityAndPutIntoRequest(pReq);
      this.srvDatabase.commitTransaction();
    } catch (Exception ex) {
      this.srvDatabase.rollBackTransaction();
      throw ex;
    } finally {
      this.srvDatabase.releaseResources();
    }
  }

  /**
   * <p>Copy entity.</p>
   * @param pReq - servlet request
   * @throws Exception - an exception
   */
  @Override
  public final void copy(final HttpServletRequest pReq) throws Exception {
    String nameEntity = pReq.getParameter("nameEntity");
    String nameSrvEntity = pReq.getParameter("nameSrvEntity");
    if (nameSrvEntity == null || nameSrvEntity.trim().length() < 3) {
      nameSrvEntity = "srv" + nameEntity;
    }
    String idEntity = pReq.getParameter("idEntity");
    String userName = null;
    if (pReq.getUserPrincipal() != null) {
      userName = pReq.getUserPrincipal().getName();
    }
    Map<String, Object> addParams = new HashMap<String, Object>();
    addParams.put("user", userName);
    addParams.put("attributes", new RequestAttrs(pReq));
    addParams.put("parameterMap", pReq.getParameterMap());
    try {
      this.srvDatabase.setIsAutocommit(false);
      this.srvDatabase.setTransactionIsolation(ISrvDatabase
        .TRANSACTION_READ_UNCOMMITTED);
      this.srvDatabase.beginTransaction();
      @SuppressWarnings("unchecked")
      ISrvEntity<IHasId<?>> srvEntity = (ISrvEntity<IHasId<?>>)
        this.factoryEntityServices.lazyGet(nameSrvEntity);
      IHasId<?> entity = srvEntity.retrieveCopyEntity(addParams, idEntity);
      putEntityIntoRequest(pReq, entity);
      this.srvDatabase.commitTransaction();
    } catch (Exception ex) {
      this.srvDatabase.rollBackTransaction();
      throw ex;
    } finally {
      this.srvDatabase.releaseResources();
    }
  }

  /**
   * <p>Edit entity.</p>
   * @param pReq - servlet request
   * @throws Exception - an exception
   */
  @Override
  public final void edit(final HttpServletRequest pReq) throws Exception {
    try {
      this.srvDatabase.setIsAutocommit(false);
      this.srvDatabase.
        setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
      this.srvDatabase.beginTransaction();
      retrieveEntityAndPutIntoRequest(pReq);
      this.srvDatabase.commitTransaction();
    } catch (Exception ex) {
      this.srvDatabase.rollBackTransaction();
      throw ex;
    } finally {
      this.srvDatabase.releaseResources();
    }
  }

  /**
   * <p>Confirm delete entity.</p>
   * @param pReq - servlet request
   * @throws Exception - an exception
   */
  @Override
  public final void confirmDelete(
    final HttpServletRequest pReq) throws Exception {
    try {
      this.srvDatabase.setIsAutocommit(false);
      this.srvDatabase.
        setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
      this.srvDatabase.beginTransaction();
      retrieveEntityAndPutIntoRequest(pReq);
      this.srvDatabase.commitTransaction();
    } catch (Exception ex) {
      this.srvDatabase.rollBackTransaction();
      throw ex;
    } finally {
      this.srvDatabase.releaseResources();
    }
  }

  /**
   * <p>Save entity.</p>
   * @param pReq - servlet request
   * @throws Exception - an exception
   */
  @Override
  public final void save(final HttpServletRequest pReq) throws Exception {
    String nameEntity = pReq.getParameter("nameEntity");
    String nameSrvEntity = pReq.getParameter("nameSrvEntity");
    if (nameSrvEntity == null || nameSrvEntity.trim().length() < 3) {
      nameSrvEntity = "srv" + nameEntity;
    }
    @SuppressWarnings("unchecked")
    ISrvEntity<IHasId<?>> srvEntity = (ISrvEntity<IHasId<?>>)
      this.factoryEntityServices.lazyGet(nameSrvEntity);
    String userName = null;
    if (pReq.getUserPrincipal() != null) {
      userName = pReq.getUserPrincipal().getName();
    }
    Map<String, Object> addParams = new HashMap<String, Object>();
    addParams.put("user", userName);
    addParams.put("attributes", new RequestAttrs(pReq));
    addParams.put("parameterMap", pReq.getParameterMap());
    IHasId<?> entity = srvEntity.createEntity(addParams);
    this.srvWebMvc.fillEntity(entity, pReq);
    try {
      this.srvDatabase.setIsAutocommit(false);
      this.srvDatabase.
        setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
      this.srvDatabase.beginTransaction();
      srvEntity.saveEntity(addParams, entity, false);
      retrieveEntityAndPutIntoRequest(pReq, entity);
      retrievePageUntransactinal(pReq);
      this.srvDatabase.commitTransaction();
    } catch (Exception ex) {
      this.srvDatabase.rollBackTransaction();
      throw ex;
    } finally {
      this.srvDatabase.releaseResources();
    }
  }

  /**
   * <p>Delete entity.</p>
   * @param pReq - servlet request
   * @throws Exception - an exception
   */
  @Override
  public final void delete(
    final HttpServletRequest pReq) throws Exception {
    String nameEntity = pReq.getParameter("nameEntity");
    String nameSrvEntity = pReq.getParameter("nameSrvEntity");
    if (nameSrvEntity == null || nameSrvEntity.trim().length() < 3) {
      nameSrvEntity = "srv" + nameEntity;
    }
    @SuppressWarnings("unchecked")
    ISrvEntity<IHasId<?>> srvEntity = (ISrvEntity<IHasId<?>>)
      this.factoryEntityServices.lazyGet(nameSrvEntity);
    String userName = null;
    if (pReq.getUserPrincipal() != null) {
      userName = pReq.getUserPrincipal().getName();
    }
    Map<String, Object> addParams = new HashMap<String, Object>();
    addParams.put("user", userName);
    addParams.put("attributes", new RequestAttrs(pReq));
    addParams.put("parameterMap", pReq.getParameterMap());
    IHasId<?> entity = srvEntity.createEntity(addParams);
    this.srvWebMvc.fillEntity(entity, pReq);
    try {
      this.srvDatabase.setIsAutocommit(false);
      this.srvDatabase.
        setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
      this.srvDatabase.beginTransaction();
      srvEntity.deleteEntity(addParams, entity);
      retrievePageUntransactinal(pReq);
      this.srvDatabase.commitTransaction();
    } catch (Exception ex) {
      this.srvDatabase.rollBackTransaction();
      throw ex;
    } finally {
      this.srvDatabase.releaseResources();
    }
  }

  /**
   * <p>Create entity from owned list.</p>
   * @param pReq - servlet request,
   * @throws Exception - an exception
   */
  @Override
  public final void createFromOwnedList(
    final HttpServletRequest pReq) throws Exception {
    String nameEntity = pReq.getParameter("nameEntityFromOwnedList");
    String nameSrvEntity = pReq.getParameter("nameSrvEntity");
    if (nameSrvEntity == null || nameSrvEntity.trim().length() < 3) {
      nameSrvEntity = "srv" + nameEntity;
    }
    @SuppressWarnings("unchecked")
    ISrvEntityOwned<IHasId<?>, IHasId<?>> srvEntity =
      (ISrvEntityOwned<IHasId<?>, IHasId<?>>)
        this.factoryEntityServices.lazyGet(nameSrvEntity);
    String idEntityOwner = pReq.getParameter("idEntity");
    String userName = null;
    if (pReq.getUserPrincipal() != null) {
      userName = pReq.getUserPrincipal().getName();
    }
    Map<String, Object> addParams = new HashMap<String, Object>();
    addParams.put("user", userName);
    addParams.put("attributes", new RequestAttrs(pReq));
    addParams.put("parameterMap", pReq.getParameterMap());
    IHasId<?> entity = srvEntity
      .createEntityWithOwnerById(addParams, idEntityOwner);
    putEntityIntoRequest(pReq, entity);
  }

  /**
   * <p>Create entity from owned list with using RDBMS.</p>
   * @param pReq - servlet request,
   * @throws Exception - an exception
   */
  @Override
  public final void createFromOwnedListTransactional(
    final HttpServletRequest pReq) throws Exception {
    String nameEntity = pReq.getParameter("nameEntityFromOwnedList");
    String nameSrvEntity = pReq.getParameter("nameSrvEntity");
    if (nameSrvEntity == null || nameSrvEntity.trim().length() < 3) {
      nameSrvEntity = "srv" + nameEntity;
    }
    @SuppressWarnings("unchecked")
    ISrvEntityOwned<IHasId<?>, IHasId<?>> srvEntity =
      (ISrvEntityOwned<IHasId<?>, IHasId<?>>)
        this.factoryEntityServices.lazyGet(nameSrvEntity);
    String idEntityOwner = pReq.getParameter("idEntity");
    String userName = null;
    if (pReq.getUserPrincipal() != null) {
      userName = pReq.getUserPrincipal().getName();
    }
    Map<String, Object> addParams = new HashMap<String, Object>();
    addParams.put("user", userName);
    addParams.put("attributes", new RequestAttrs(pReq));
    addParams.put("parameterMap", pReq.getParameterMap());
    try {
      this.srvDatabase.setIsAutocommit(false);
      this.srvDatabase.setTransactionIsolation(ISrvDatabase
        .TRANSACTION_READ_UNCOMMITTED);
      this.srvDatabase.beginTransaction();
      IHasId<?> entity = srvEntity
        .createEntityWithOwnerById(addParams, idEntityOwner);
      putEntityIntoRequest(pReq, entity);
      this.srvDatabase.commitTransaction();
    } catch (Exception ex) {
      this.srvDatabase.rollBackTransaction();
      throw ex;
    } finally {
      this.srvDatabase.releaseResources();
    }
  }

  /**
   * <p>Copy entity from owned list.</p>
   * @param pReq - servlet request,
   * @throws Exception - an exception
   */
  @Override
  public final void copyFromOwnedList(
    final HttpServletRequest pReq) throws Exception {
    String nameEntity = pReq.getParameter("nameEntityFromOwnedList");
    String nameSrvEntity = pReq.getParameter("nameSrvEntity");
    if (nameSrvEntity == null || nameSrvEntity.trim().length() < 3) {
      nameSrvEntity = "srv" + nameEntity;
    }
    String idEntity = pReq.getParameter("idEntityFromOwnedList");
    String userName = null;
    if (pReq.getUserPrincipal() != null) {
      userName = pReq.getUserPrincipal().getName();
    }
    Map<String, Object> addParams = new HashMap<String, Object>();
    addParams.put("user", userName);
    addParams.put("attributes", new RequestAttrs(pReq));
    addParams.put("parameterMap", pReq.getParameterMap());
    @SuppressWarnings("unchecked")
    ISrvEntityOwned<IHasId<?>, IHasId<?>> srvEntity =
      (ISrvEntityOwned<IHasId<?>, IHasId<?>>)
        this.factoryEntityServices.lazyGet(nameSrvEntity);
    try {
      this.srvDatabase.setIsAutocommit(false);
      this.srvDatabase.setTransactionIsolation(ISrvDatabase
        .TRANSACTION_READ_UNCOMMITTED);
      this.srvDatabase.beginTransaction();
      IHasId<?> entity = srvEntity
        .retrieveCopyEntity(addParams, idEntity);
      putEntityIntoRequest(pReq, entity);
      this.srvDatabase.commitTransaction();
    } catch (Exception ex) {
      this.srvDatabase.rollBackTransaction();
      throw ex;
    } finally {
      this.srvDatabase.releaseResources();
    }
  }

  /**
   * <p>Edit entity from owned list.</p>
   * @param pReq - servlet request,
   * @throws Exception - an exception
   */
  @Override
  public final void editFromOwnedList(
    final HttpServletRequest pReq) throws Exception {
    try {
      this.srvDatabase.setIsAutocommit(false);
      this.srvDatabase.
        setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
      this.srvDatabase.beginTransaction();
      retrieveEntityFromOwnedListAndPutIntoRequest(pReq);
      this.srvDatabase.commitTransaction();
    } catch (Exception ex) {
      this.srvDatabase.rollBackTransaction();
      throw ex;
    } finally {
      this.srvDatabase.releaseResources();
    }
  }

  /**
   * <p>Confirm delete entity from owned list.</p>
   * @param pReq - servlet request,
   * @throws Exception - an exception
   */
  @Override
  public final void confirmDeleteFromOwnedList(
    final HttpServletRequest pReq) throws Exception {
    try {
      this.srvDatabase.setIsAutocommit(false);
      this.srvDatabase.
        setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
      this.srvDatabase.beginTransaction();
      retrieveEntityFromOwnedListAndPutIntoRequest(pReq);
      this.srvDatabase.commitTransaction();
    } catch (Exception ex) {
      this.srvDatabase.rollBackTransaction();
      throw ex;
    } finally {
      this.srvDatabase.releaseResources();
    }
  }

  /**
   * <p>Save entity from owned list.</p>
   * @param pReq - servlet request,
   * @throws Exception - an exception
   */
  @Override
  public final void saveFromOwnedList(
    final HttpServletRequest pReq) throws Exception {
    String nameEntity = pReq.getParameter("nameEntityFromOwnedList");
    String nameSrvEntity = pReq.getParameter("nameSrvEntity");
    if (nameSrvEntity == null || nameSrvEntity.trim().length() < 3) {
      nameSrvEntity = "srv" + nameEntity;
    }
    @SuppressWarnings("unchecked")
    ISrvEntityOwned<IHasId<?>, IHasId<?>> srvEntity =
      (ISrvEntityOwned<IHasId<?>, IHasId<?>>)
        this.factoryEntityServices.lazyGet(nameSrvEntity);
    String userName = null;
    if (pReq.getUserPrincipal() != null) {
      userName = pReq.getUserPrincipal().getName();
    }
    Map<String, Object> addParams = new HashMap<String, Object>();
    addParams.put("user", userName);
    addParams.put("attributes", new RequestAttrs(pReq));
    addParams.put("parameterMap", pReq.getParameterMap());
    IHasId<?> entity = srvEntity.createEntity(addParams);
    this.srvWebMvc.fillEntity(entity, pReq);
    try {
      this.srvDatabase.setIsAutocommit(false);
      this.srvDatabase.
        setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
      this.srvDatabase.beginTransaction();
      srvEntity.saveEntity(addParams, entity,
        false);
      retrieveEntityAndPutIntoRequest(pReq);
      retrievePageUntransactinal(pReq);
      this.srvDatabase.commitTransaction();
    } catch (Exception ex) {
      this.srvDatabase.rollBackTransaction();
      throw ex;
    } finally {
      this.srvDatabase.releaseResources();
    }
  }

  /**
   * <p>Delete entity from owned list.</p>
   * @param pReq - servlet request,
   * @throws Exception - an exception
   */
  @Override
  public final void deleteFromOwnedList(
    final HttpServletRequest pReq) throws Exception {
    String nameEntity = pReq.getParameter("nameEntityFromOwnedList");
    String nameSrvEntity = pReq.getParameter("nameSrvEntity");
    if (nameSrvEntity == null || nameSrvEntity.trim().length() < 3) {
      nameSrvEntity = "srv" + nameEntity;
    }
    @SuppressWarnings("unchecked")
    ISrvEntityOwned<IHasId<?>, IHasId<?>> srvEntity =
      (ISrvEntityOwned<IHasId<?>, IHasId<?>>)
        this.factoryEntityServices.lazyGet(nameSrvEntity);
    String userName = null;
    if (pReq.getUserPrincipal() != null) {
      userName = pReq.getUserPrincipal().getName();
    }
    Map<String, Object> addParams = new HashMap<String, Object>();
    addParams.put("user", userName);
    addParams.put("attributes", new RequestAttrs(pReq));
    addParams.put("parameterMap", pReq.getParameterMap());
    IHasId<?> entity = srvEntity.createEntity(addParams);
    this.srvWebMvc.fillEntity(entity, pReq);
    try {
      this.srvDatabase.setIsAutocommit(false);
      this.srvDatabase.
        setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
      this.srvDatabase.beginTransaction();
      srvEntity.deleteEntity(addParams, entity);
      retrieveEntityAndPutIntoRequest(pReq);
      retrievePageUntransactinal(pReq);
      this.srvDatabase.commitTransaction();
    } catch (Exception ex) {
      this.srvDatabase.rollBackTransaction();
      throw ex;
    } finally {
      this.srvDatabase.releaseResources();
    }
  }

  /**
   * <p>Retrieve entity page.</p>
   * @param pReq - servlet request
   * @param pUser - pUser
   * @throws Exception - an exception
   */
  @Override
  public final void retrievePage(
    final HttpServletRequest pReq) throws Exception {
    try {
      this.srvDatabase.setIsAutocommit(false);
      this.srvDatabase.
        setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
      this.srvDatabase.beginTransaction();
      retrievePageUntransactinal(pReq);
      this.srvDatabase.commitTransaction();
    } catch (Exception ex) {
      this.srvDatabase.rollBackTransaction();
      throw ex;
    } finally {
      this.srvDatabase.releaseResources();
    }
  }

  //Utils:
  /**
   * <p>Retrieve entity page untransactional.</p>
   * @param pReq - servlet request
   * @throws Exception - an exception
   */
  public final void retrievePageUntransactinal(
    final HttpServletRequest pReq) throws Exception {
    String nameEntity = pReq.getParameter("nameEntity");
    String nameSrvEntity = pReq.getParameter("nameSrvEntity");
    if (nameSrvEntity == null || nameSrvEntity.trim().length() < 3) {
      nameSrvEntity = "srv" + nameEntity;
    }
    getLogger().debug(SrvWebEntity.class,
      "SrvWebEntity: Try to retrieve page for : " + nameEntity);
    Class clazz = getEntitiesMap().get(nameEntity);
    String userName = null;
    if (pReq.getUserPrincipal() != null) {
      userName = pReq.getUserPrincipal().getName();
    }
    Map<String, Object> addParams = new HashMap<String, Object>();
    addParams.put("user", userName);
    addParams.put("attributes", new RequestAttrs(pReq));
    addParams.put("parameterMap", pReq.getParameterMap());
    List<Map.Entry<String, Map<String, String>>> fields =
      this.mngUvdSettings.makeFldPropLst(clazz.getCanonicalName(),
        "orderPrintfullList"); //cause use hidden field for filter
    StringBuffer queryWhere = new StringBuffer();
    Map<String, Object> filterMap = new HashMap<String, Object>();
    for (Map.Entry<String, Map<String, String>> entry : fields) {
      if ("filterEntity".equals(entry.getValue().get("wdgFilter"))) {
        tryMakeWhereEntity(queryWhere, pReq, nameEntity, entry.getKey(),
          filterMap);
      } else if ("filterEntityIdString".equals(entry.getValue()
        .get("wdgFilter"))) {
        tryMakeWhereEntityIdString(queryWhere, pReq, nameEntity, entry.getKey(),
          filterMap);
      } else if ("filterEnum".equals(entry.getValue().get("wdgFilter"))) {
        tryMakeWhereEnum(queryWhere, pReq, clazz, entry.getKey(),
          filterMap);
      } else if ("filterBoolean".equals(entry.getValue().get("wdgFilter"))) {
        tryMakeWhereBoolean(queryWhere, pReq, clazz, entry.getKey(),
          filterMap);
      } else {
        tryMakeWhereStd(queryWhere, pReq, nameEntity, entry.getKey(), "1",
          filterMap);
        tryMakeWhereStd(queryWhere, pReq, nameEntity, entry.getKey(), "2",
          filterMap);
      }
    }
    //cause settled either from request or from settings
    Map<String, String> orderMap = new HashMap<String, String>();
    String queryOrderBy;
    String fltOrdPrefix;
    String nameRenderer = pReq.getParameter("nameRenderer");
    if (nameRenderer.contains("pickerDub")) {
      fltOrdPrefix = "fltordPD";
    } else if (nameRenderer.contains("picker")) {
      fltOrdPrefix = "fltordP";
    } else {
      fltOrdPrefix = "fltordM";
    }
    String orderBy = pReq.getParameter(fltOrdPrefix + "orderBy");
    if (orderBy != null && !orderBy.equals("disabled")) {
      orderMap.put(fltOrdPrefix + "orderBy", orderBy);
      String desc = "";
      if (pReq.getParameter(fltOrdPrefix + "orderByDesc").equals("on")) {
        desc = " desc";
        orderMap.put(fltOrdPrefix + "orderByDesc", "on");
      } else {
        orderMap.put(fltOrdPrefix + "orderByDesc", "off");
      }
      queryOrderBy = " order by " + nameEntity.toUpperCase() + "."
        + orderBy.toUpperCase() + desc;
    } else {
      orderMap.put(fltOrdPrefix + "orderBy", this.mngUvdSettings
        .getClassesSettings().get(clazz.getCanonicalName())
          .get("orderByDefault"));
      String orderByDesc = this.mngUvdSettings.getClassesSettings()
        .get(clazz.getCanonicalName()).get("orderByDescDefault");
      String orderByDescStr = " desc";
      if (!(orderByDesc != null && orderByDesc.equals("on"))) {
        orderByDesc = "off";
        orderByDescStr = "";
      }
      orderMap.put(fltOrdPrefix + "orderByDesc", orderByDesc);
      queryOrderBy = " order by " + nameEntity.toUpperCase() + "."
        + orderMap.get(fltOrdPrefix + "orderBy").toUpperCase() + orderByDescStr;
    }
    @SuppressWarnings("unchecked")
    ISrvEntity<IHasId<?>> srvEntity = (ISrvEntity<IHasId<?>>)
      this.factoryEntityServices.lazyGet(nameSrvEntity);
    Integer rowCount;
    if (queryWhere.length() > 3) {
      rowCount = srvEntity.evalRowCountWhere(addParams,
        queryWhere.toString());
    } else {
      rowCount = srvEntity.evalRowCount(addParams);
    }
    Integer page = Integer.valueOf(pReq.getParameter("page"));
    Integer itemsPerPage = Integer.valueOf(mngUvdSettings.getAppSettings()
      .get("itemsPerPage"));
    int totalPages = srvPage.evalPageCount(rowCount, itemsPerPage);
    if (page > totalPages) {
      page = totalPages;
    }
    int firstResult = (page - 1) * itemsPerPage; //0-20,20-40
    List entities;
    if (queryWhere.length() > 3 || queryOrderBy.length() > 3) {
      if (queryWhere.length() > 3) {
        entities = srvEntity.retrievePageWithConditions(addParams, "where "
          + queryWhere.toString() + queryOrderBy, firstResult, itemsPerPage);
      } else {
        entities = srvEntity.retrievePageWithConditions(addParams,
          queryOrderBy, firstResult, itemsPerPage);
      }
    } else {
      entities = srvEntity.retrievePage(addParams, firstResult, itemsPerPage);
    }
    Integer paginationTail = Integer.valueOf(mngUvdSettings.getAppSettings()
      .get("paginationTail"));
    List<Page> pages = srvPage.evalPages(page, totalPages, paginationTail);
    pReq.setAttribute("pages", pages);
    pReq.setAttribute("orderMap", orderMap);
    pReq.setAttribute("filterMap", filterMap);
    pReq.setAttribute("entities", entities);
    pReq.setAttribute("classEntity", clazz);
    pReq.setAttribute("mngUvds", this.mngUvdSettings);
    pReq.setAttribute("srvOrm", this.srvOrm);
    pReq.setAttribute("srvI18n", this.srvI18n);
    pReq.setAttribute("utlJsp", this.utlJsp);
    pReq.setAttribute("utlReflection", this.utlReflection);
  }

  /**
   * <p>Make SQL WHERE clause if need.</p>
   * @param pSbWhere result clause
   * @param pReq - servlet request
   * @param pNameEntity - entity name
   * @param pFldNm - field name
   * @param pParSuffix - parameter suffix
   * @param pFilterMap - map to store current filter
   * @throws Exception - an Exception
   **/
  public final void tryMakeWhereStd(final StringBuffer pSbWhere,
    final HttpServletRequest pReq, final String pNameEntity,
      final String pFldNm, final String pParSuffix,
        final Map<String, Object> pFilterMap) throws Exception {
    String nameRenderer = pReq.getParameter("nameRenderer");
    String fltOrdPrefix;
    if (nameRenderer != null && nameRenderer.contains("pickerDub")) {
      fltOrdPrefix = "fltordPD";
    } else if (nameRenderer != null && nameRenderer.contains("picker")) {
      fltOrdPrefix = "fltordP";
    } else {
      fltOrdPrefix = "fltordM";
    }
    String fltforcedName = fltOrdPrefix + "forcedFor";
    String fltforced = pReq.getParameter(fltforcedName);
    if (fltforced != null) {
      pFilterMap.put(fltforcedName, fltforced);
    }
    String nmFldVal = fltOrdPrefix + pFldNm + "Val" + pParSuffix;
    String fltVal = pReq.getParameter(nmFldVal);
    String nmFldOpr = fltOrdPrefix + pFldNm
      + "Opr" + pParSuffix;
    String valFldOpr = pReq.getParameter(nmFldOpr);
    String cond = null;
    if ("isnotnull".equals(valFldOpr) || "isnull".equals(valFldOpr)) {
        cond = pNameEntity.toUpperCase()
            + "." + pFldNm.toUpperCase() + " "
            + toSqlOperator(valFldOpr);
    } else if (fltVal != null && valFldOpr != null
      && !valFldOpr.equals("disabled") && !valFldOpr.equals("")) {
      cond = pNameEntity.toUpperCase()
          + "." + pFldNm.toUpperCase() + " "
          + toSqlOperator(valFldOpr)
          + " " + fltVal;
    }
    if (cond != null) {
      pFilterMap.put(nmFldVal, fltVal);
      pFilterMap.put(nmFldOpr, valFldOpr);
      if (pSbWhere.toString().length() == 0) {
        pSbWhere.append(cond);
      } else {
        pSbWhere.append(" and " + cond);
      }
    }
  }

  /**
   * <p>Make SQL operator e.g. 'eq'-&gt;'&gt;'.</p>
   * @param pOper operator - eq, gt, lt
   * @return SQL operator
   * @throws ExceptionWithCode - code 1003 WRONG_PARAMETER
   **/
  public final String toSqlOperator(
    final String pOper) throws ExceptionWithCode {
    if ("eq".equals(pOper)) {
      return "=";
    } else if ("gt".equals(pOper)) {
      return ">";
    } else if ("gteq".equals(pOper)) {
      return ">=";
    } else if ("in".equals(pOper)) {
      return "in";
    } else if ("lt".equals(pOper)) {
      return "<";
    } else if ("lteq".equals(pOper)) {
      return "<=";
    } else if ("isnull".equals(pOper)) {
      return "is null";
    } else if ("isnotnull".equals(pOper)) {
      return "is not null";
    } else if ("like".equals(pOper)) {
      return "like";
    } else {
      throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
        "can't match SQL operator: " + pOper);
    }
  }

  /**
   * <p>Make SQL WHERE clause for entity if need.</p>
   * @param pSbWhere result clause
   * @param pReq - servlet request
   * @param pNameEntity - entity name
   * @param pFldNm - field name
   * @param pFilterMap - map to store current filter
   * @throws Exception - an Exception
   **/
  public final void tryMakeWhereEntity(final StringBuffer pSbWhere,
    final HttpServletRequest pReq, final String pNameEntity,
      final String pFldNm,
        final Map<String, Object> pFilterMap) throws Exception {
    String nameRenderer = pReq.getParameter("nameRenderer");
    String fltOrdPrefix;
    if (nameRenderer.contains("pickerDub")) {
      fltOrdPrefix = "fltordPD";
    } else if (nameRenderer.contains("picker")) {
      fltOrdPrefix = "fltordP";
    } else {
      fltOrdPrefix = "fltordM";
    }
    String nmFldValId = fltOrdPrefix + pFldNm + "ValId";
    String fltValId = pReq.getParameter(nmFldValId);
    String nmFldOpr = fltOrdPrefix + pFldNm + "Opr";
    String valFldOpr = pReq.getParameter(nmFldOpr);
    if (valFldOpr != null) {
      pFilterMap.put(nmFldOpr, valFldOpr);
      String fltforcedName = fltOrdPrefix + "forcedFor";
      String fltforced = pReq.getParameter(fltforcedName);
      if (fltforced != null) {
        pFilterMap.put(fltforcedName, fltforced);
      }
      if (valFldOpr.equals("isnull")) {
        String cond = pNameEntity.toUpperCase()
          + "." + pFldNm.toUpperCase() + " "
            + toSqlOperator(valFldOpr);
        if (pSbWhere.toString().length() == 0) {
          pSbWhere.append(cond);
        } else {
          pSbWhere.append(" and " + cond);
        }
      } else if (!valFldOpr.equals("disabled") && !valFldOpr.equals("")
        && fltValId != null && fltValId.length() > 0) {
        pFilterMap.put(nmFldValId, fltValId);
        String nmFldValAppearance = fltOrdPrefix + pFldNm
          + "ValAppearance";
        String fltValAppearance = pReq.getParameter(nmFldValAppearance);
        pFilterMap.put(nmFldValAppearance, fltValAppearance);
        String valId = fltValId;
        if (valFldOpr.equals("in")) {
          valId = "(" + valId + ")";
        }
        String cond = pNameEntity.toUpperCase()
            + "." + pFldNm.toUpperCase() + " "
              + toSqlOperator(valFldOpr)
                + " " + valId;
        if (pSbWhere.toString().length() == 0) {
          pSbWhere.append(cond);
        } else {
          pSbWhere.append(" and " + cond);
        }
      }
    }
  }


  /**
   * <p>Make SQL WHERE clause for entity if need.</p>
   * @param pSbWhere result clause
   * @param pReq - servlet request
   * @param pNameEntity - entity name
   * @param pFldNm - field name
   * @param pFilterMap - map to store current filter
   * @throws Exception - an Exception
   **/
  public final void tryMakeWhereEntityIdString(final StringBuffer pSbWhere,
    final HttpServletRequest pReq, final String pNameEntity,
      final String pFldNm,
        final Map<String, Object> pFilterMap) throws Exception {
    String nameRenderer = pReq.getParameter("nameRenderer");
    String fltOrdPrefix;
    if (nameRenderer.contains("pickerDub")) {
      fltOrdPrefix = "fltordPD";
    } else if (nameRenderer.contains("picker")) {
      fltOrdPrefix = "fltordP";
    } else {
      fltOrdPrefix = "fltordM";
    }
    String nmFldValId = fltOrdPrefix + pFldNm + "ValId";
    String fltValId = pReq.getParameter(nmFldValId);
    String nmFldOpr = fltOrdPrefix + pFldNm + "Opr";
    String valFldOpr = pReq.getParameter(nmFldOpr);
    if (valFldOpr != null) {
      pFilterMap.put(nmFldOpr, valFldOpr);
      String fltforcedName = fltOrdPrefix + "forcedFor";
      String fltforced = pReq.getParameter(fltforcedName);
      if (fltforced != null) {
        pFilterMap.put(fltforcedName, fltforced);
      }
      if (valFldOpr.equals("isnull")) {
        String cond = pNameEntity.toUpperCase()
          + "." + pFldNm.toUpperCase() + " "
            + toSqlOperator(valFldOpr);
        if (pSbWhere.toString().length() == 0) {
          pSbWhere.append(cond);
        } else {
          pSbWhere.append(" and " + cond);
        }
      } else if (!valFldOpr.equals("disabled") && !valFldOpr.equals("")
        && fltValId != null && fltValId.length() > 0) {
        pFilterMap.put(nmFldValId, fltValId);
        String nmFldValAppearance = fltOrdPrefix + pFldNm
          + "ValAppearance";
        String fltValAppearance = pReq.getParameter(nmFldValAppearance);
        pFilterMap.put(nmFldValAppearance, fltValAppearance);
        String valId = fltValId;
        if (valFldOpr.equals("in")) {
          valId = "(" + valId + ")"; //usually made by hand forced in widget
        } else {
          valId = "'" + valId + "'";
        }
        String cond = pNameEntity.toUpperCase()
            + "." + pFldNm.toUpperCase() + " "
              + toSqlOperator(valFldOpr)
                + " " + valId;
        if (pSbWhere.toString().length() == 0) {
          pSbWhere.append(cond);
        } else {
          pSbWhere.append(" and " + cond);
        }
      }
    }
  }

  /**
   * <p>Make SQL WHERE clause for enum if need.</p>
   * @param pSbWhere result clause
   * @param pReq - servlet request
   * @param pEntityClass - entity class
   * @param pFldNm - field name
   * @param pFilterMap - map to store current filter
   * @throws Exception - an Exception
   **/
  public final void tryMakeWhereEnum(final StringBuffer pSbWhere,
    final HttpServletRequest pReq, final Class<?> pEntityClass,
      final String pFldNm,
        final Map<String, Object> pFilterMap) throws Exception {
    String nameRenderer = pReq.getParameter("nameRenderer");
    String fltOrdPrefix;
    if (nameRenderer.contains("pickerDub")) {
      fltOrdPrefix = "fltordPD";
    } else if (nameRenderer.contains("picker")) {
      fltOrdPrefix = "fltordP";
    } else {
      fltOrdPrefix = "fltordM";
    }
    String fltforcedName = fltOrdPrefix + "forcedFor";
    String fltforced = pReq.getParameter(fltforcedName);
    if (fltforced != null) {
      pFilterMap.put(fltforcedName, fltforced);
    }
    String nmFldVal = fltOrdPrefix + pFldNm + "Val";
    String fltVal = pReq.getParameter(nmFldVal);
    String nmFldOpr = fltOrdPrefix + pFldNm + "Opr";
    String valFldOpr = pReq.getParameter(nmFldOpr);
    if (fltVal != null && fltVal.length() > 0 && valFldOpr != null
      && !valFldOpr.equals("disabled") && !valFldOpr.equals("")) {
      pFilterMap.put(nmFldVal, fltVal);
      pFilterMap.put(nmFldOpr, valFldOpr);
      pFilterMap.put(nmFldVal, fltVal);
      String nmFldValAppearance = fltOrdPrefix + pFldNm
        + "ValAppearance";
      String fltValAppearance = pReq.getParameter(nmFldValAppearance);
      pFilterMap.put(nmFldValAppearance, fltValAppearance);
      String val = fltVal;
      if (valFldOpr.equals("in")) {
        val = "(" + val + ")";
      }
      String cond = pEntityClass.getSimpleName().toUpperCase()
          + "." + pFldNm.toUpperCase() + " "
          + toSqlOperator(valFldOpr)
          + " " + val;
      if (pSbWhere.toString().length() == 0) {
        pSbWhere.append(cond);
      } else {
        pSbWhere.append(" and " + cond);
      }
    }
  }

  /**
   * <p>Make SQL WHERE clause for boolean if need.</p>
   * @param pSbWhere result clause
   * @param pReq - servlet request
   * @param pEntityClass - entity class
   * @param pFldNm - field name
   * @param pFilterMap - map to store current filter
   * @throws Exception - an Exception
   **/
  public final void tryMakeWhereBoolean(final StringBuffer pSbWhere,
    final HttpServletRequest pReq, final Class<?> pEntityClass,
      final String pFldNm,
        final Map<String, Object> pFilterMap) throws Exception {
    String nameRenderer = pReq.getParameter("nameRenderer");
    String fltOrdPrefix;
    if (nameRenderer.contains("pickerDub")) {
      fltOrdPrefix = "fltordPD";
    } else if (nameRenderer.contains("picker")) {
      fltOrdPrefix = "fltordP";
    } else {
      fltOrdPrefix = "fltordM";
    }
    String fltforcedName = fltOrdPrefix + "forcedFor";
    String fltforced = pReq.getParameter(fltforcedName);
    if (fltforced != null) {
      pFilterMap.put(fltforcedName, fltforced);
    }
    String nmFldVal = fltOrdPrefix + pFldNm + "Val";
    String fltVal = pReq.getParameter(nmFldVal);
    if (fltVal != null && (fltVal.length() == 0
      || "null".equals(fltVal))) {
      fltVal = null;
    }
    pFilterMap.put(nmFldVal, fltVal);
    if (fltVal != null) {
      int intVal = 0;
      if (fltVal.equals("true")) {
        intVal = 1;
      }
      String cond = pEntityClass.getSimpleName().toUpperCase()
          + "." + pFldNm.toUpperCase() + " = " + intVal;
      if (pSbWhere.toString().length() == 0) {
        pSbWhere.append(cond);
      } else {
        pSbWhere.append(" and " + cond);
      }
    }
  }

  /**
   * <p>Retrieve entity from owned list and put models to request.</p>
   * @param pReq - servlet request
   * @throws Exception - an exception
   */
  public final void retrieveEntityFromOwnedListAndPutIntoRequest(
    final HttpServletRequest pReq) throws Exception {
    String nameEntity = pReq.getParameter("nameEntityFromOwnedList");
    String nameSrvEntity = pReq.getParameter("nameSrvEntity");
    if (nameSrvEntity == null || nameSrvEntity.trim().length() < 3) {
      nameSrvEntity = "srv" + nameEntity;
    }
    String idEntity = pReq.getParameter("idEntityFromOwnedList");
    String userName = null;
    if (pReq.getUserPrincipal() != null) {
      userName = pReq.getUserPrincipal().getName();
    }
    Map<String, Object> addParams = new HashMap<String, Object>();
    addParams.put("user", userName);
    addParams.put("attributes", new RequestAttrs(pReq));
    addParams.put("parameterMap", pReq.getParameterMap());
    @SuppressWarnings("unchecked")
    ISrvEntityOwned<IHasId<?>, IHasId<?>> srvEntity =
      (ISrvEntityOwned<IHasId<?>, IHasId<?>>)
        this.factoryEntityServices.lazyGet(nameSrvEntity);
    IHasId<?> entity = srvEntity.retrieveEntityById(addParams, idEntity);
    putEntityIntoRequest(pReq, entity);
  }

  /**
   * <p>Retrieve entity and put models to request.</p>
   * @param pReq - servlet request
   * @throws Exception - an exception
   */
  public final void retrieveEntityAndPutIntoRequest(
    final HttpServletRequest pReq) throws Exception {
    String nameEntity = pReq.getParameter("nameEntity");
    String nameSrvEntity = pReq.getParameter("nameSrvEntity");
    if (nameSrvEntity == null || nameSrvEntity.trim().length() < 3) {
      nameSrvEntity = "srv" + nameEntity;
    }
    String idEntity = pReq.getParameter("idEntity");
    @SuppressWarnings("unchecked")
    ISrvEntity<IHasId<?>> srvEntity = (ISrvEntity<IHasId<?>>)
      this.factoryEntityServices.lazyGet(nameSrvEntity);
    String userName = null;
    if (pReq.getUserPrincipal() != null) {
      userName = pReq.getUserPrincipal().getName();
    }
    Map<String, Object> addParams = new HashMap<String, Object>();
    addParams.put("user", userName);
    addParams.put("attributes", new RequestAttrs(pReq));
    addParams.put("parameterMap", pReq.getParameterMap());
    IHasId<?> entity = srvEntity
      .retrieveEntityById(addParams, idEntity);
    retrieveEntityOwnedlistsAndPutIntoRequest(pReq, entity);
    putEntityIntoRequest(pReq, entity);
  }

  /**
   * <p>Retrieve entity and put models to request.</p>
   * @param pReq - servlet request
   * @param pEntity - entity
   * @throws Exception - an exception
   */
  public final void retrieveEntityAndPutIntoRequest(
    final HttpServletRequest pReq, final IHasId<?> pEntity) throws Exception {
    String userName = null;
    if (pReq.getUserPrincipal() != null) {
      userName = pReq.getUserPrincipal().getName();
    }
    @SuppressWarnings("unchecked")
    ISrvEntity<IHasId<?>> srvEntity = (ISrvEntity<IHasId<?>>)
      this.factoryEntityServices.lazyGet("srv"
        + pEntity.getClass().getSimpleName());
    Map<String, Object> addParams = new HashMap<String, Object>();
    addParams.put("user", userName);
    addParams.put("attributes", new RequestAttrs(pReq));
    addParams.put("parameterMap", pReq.getParameterMap());
    IHasId<?> entity = srvEntity.retrieveEntity(addParams, pEntity);
    retrieveEntityOwnedlistsAndPutIntoRequest(pReq, entity);
    putEntityIntoRequest(pReq, entity);
  }

  /**
   * <p>Retrieve entity's sublists (e.g. invoice lines for invoice)
   * if they exist and put them into request.</p>
   * @param pReq - servlet request
   * @param pEntityOwner - Entity Owner e.g. an invoice
   * @throws Exception - an exception
   */
  public final void retrieveEntityOwnedlistsAndPutIntoRequest(
    final HttpServletRequest pReq,
      final IHasId<?> pEntityOwner) throws Exception {
    String ownedLists = mngUvdSettings.getClassesSettings()
      .get(pEntityOwner.getClass().getCanonicalName()).get("ownedLists");
    String userName = null;
    if (pReq.getUserPrincipal() != null) {
      userName = pReq.getUserPrincipal().getName();
    }
    Map<String, Object> addParams = new HashMap<String, Object>();
    addParams.put("user", userName);
    addParams.put("attributes", new RequestAttrs(pReq));
    addParams.put("parameterMap", pReq.getParameterMap());
    if (ownedLists != null) {
      Map<Class<?>, List<IHasId<?>>> ownedListsMap =
        new LinkedHashMap<Class<?>, List<IHasId<?>>>();
      pReq.setAttribute("ownedListsMap", ownedListsMap);
      LinkedHashSet<String> ownedListsSet = utlProperties
        .evalPropsStringsSet(ownedLists);
      for (String className : ownedListsSet) {
        Class<?> clazz = Class.forName(className);
      @SuppressWarnings("unchecked")
        ISrvEntityOwned<IHasId<?>, IHasId<?>> srvEntity =
          (ISrvEntityOwned<IHasId<?>, IHasId<?>>)
            this.factoryEntityServices.lazyGet("srv" + clazz.getSimpleName());
        List<IHasId<?>> entities = srvEntity
          .retrieveOwnedList(addParams, pEntityOwner);
        ownedListsMap.put(clazz, entities);
      }
    }
  }

  /**
   * <p>Put models into request.</p>
   * @param pReq - servlet request
   * @param pEntity - entity
   * @throws Exception - an exception
   */
  public final void putEntityIntoRequest(
    final HttpServletRequest pReq,
      final IHasId<?> pEntity) throws Exception {
    pReq.setAttribute("entity", pEntity);
    pReq.setAttribute("mngUvds", this.mngUvdSettings);
    pReq.setAttribute("srvOrm", this.srvOrm);
    pReq.setAttribute("srvI18n", this.srvI18n);
    pReq.setAttribute("utlJsp", this.utlJsp);
  }

  //Simple getters and setters:
  /**
   * <p>Geter for srvOrm.</p>
   * @return ISrvOrm
   **/
  public final ISrvOrm<?> getSrvOrm() {
    return this.srvOrm;
  }

  /**
   * <p>Setter for srvOrm.</p>
   * @param pSrvOrm reference
   **/
  public final void setSrvOrm(final ISrvOrm<?> pSrvOrm) {
    this.srvOrm = pSrvOrm;
  }

  /**
   * <p>Geter for srvPage.</p>
   * @return ISrvPage
   **/
  public final ISrvPage getSrvPage() {
    return this.srvPage;
  }

  /**
   * <p>Setter for srvPage.</p>
   * @param pSrvPage reference
   **/
  public final void setSrvPage(final ISrvPage pSrvPage) {
    this.srvPage = pSrvPage;
  }

  /**
   * <p>Geter for srvDatabase.</p>
   * @return ISrvDatabase<?>
   **/
  public final ISrvDatabase<?> getSrvDatabase() {
    return this.srvDatabase;
  }

  /**
   * <p>Setter for srvDatabase.</p>
   * @param pSrvDatabase reference
   **/
  public final void setSrvDatabase(final ISrvDatabase<?> pSrvDatabase) {
    this.srvDatabase = pSrvDatabase;
  }

  /**
   * <p>Geter for mngUvdSettings.</p>
   * @return MngSettings
   **/
  public final MngSettings getMngUvdSettings() {
    return this.mngUvdSettings;
  }

  /**
   * <p>Setter for mngUvdSettings.</p>
   * @param pMngSettings reference
   **/
  public final void setMngUvdSettings(
    final MngSettings pMngSettings) {
    this.mngUvdSettings = pMngSettings;
  }

  /**
   * <p>Geter for srvWebMvc.</p>
   * @return ISrvWebMvc
   **/
  public final ISrvWebMvc getSrvWebMvc() {
    return this.srvWebMvc;
  }

  /**
   * <p>Setter for srvWebMvc.</p>
   * @param pSrvWebMvc reference/value
   **/
  public final void setSrvWebMvc(final ISrvWebMvc pSrvWebMvc) {
    this.srvWebMvc = pSrvWebMvc;
  }

  /**
   * <p>Geter for entitiesMap.</p>
   * @return Map<String, Class<?>>
   **/
  public final Map<String, Class<?>> getEntitiesMap() {
    return this.entitiesMap;
  }

  /**
   * <p>Setter for entitiesMap.</p>
   * @param pEntitiesMap reference
   **/
  public final void setEntitiesMap(final Map<String, Class<?>> pEntitiesMap) {
    this.entitiesMap = pEntitiesMap;
  }

  /**
   * <p>Geter for utlProperties.</p>
   * @return UtlProperties
   **/
  public final UtlProperties getUtlProperties() {
    return this.utlProperties;
  }

  /**
   * <p>Geter for factoryEntityServices.</p>
   * @return IFactoryAppBeans
   **/
  public final IFactoryAppBeans getFactoryEntityServices() {
    return this.factoryEntityServices;
  }

  /**
   * <p>Setter for factoryEntityServices.</p>
   * @param pFactoryEntityServices reference
   **/
  public final void setFactoryEntityServices(
    final IFactoryAppBeans pFactoryEntityServices) {
    this.factoryEntityServices = pFactoryEntityServices;
  }

  /**
   * <p>Geter for srvI18n.</p>
   * @return ISrvI18n
   **/
  public final ISrvI18n getSrvI18n() {
    return this.srvI18n;
  }

  /**
   * <p>Setter for srvI18n.</p>
   * @param pSrvI18n reference
   **/
  public final void setSrvI18n(final ISrvI18n pSrvI18n) {
    this.srvI18n = pSrvI18n;
  }

  /**
   * <p>Geter for logger.</p>
   * @return ILogger
   **/
  public final ILogger getLogger() {
    return this.logger;
  }

  /**
   * <p>Setter for logger.</p>
   * @param pLogger reference
   **/
  public final void setLogger(final ILogger pLogger) {
    this.logger = pLogger;
  }

  /**
   * <p>Getter for utlJsp.</p>
   * @return UtlJsp
   **/
  public final UtlJsp getUtlJsp() {
    return this.utlJsp;
  }

  /**
   * <p>Setter for utlJsp.</p>
   * @param pUtlJsp reference
   **/
  public final void setUtlJsp(final UtlJsp pUtlJsp) {
    this.utlJsp = pUtlJsp;
  }

  /**
   * <p>Getter for utlReflection.</p>
   * @return UtlReflection
   **/
  public final UtlReflection getUtlReflection() {
    return this.utlReflection;
  }

  /**
   * <p>Setter for utlReflection.</p>
   * @param pUtlReflection reference
   **/
  public final void setUtlReflection(final UtlReflection pUtlReflection) {
    this.utlReflection = pUtlReflection;
  }
}
