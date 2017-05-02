package org.beigesoft.orm.factory;

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
import java.util.Hashtable;
import java.lang.reflect.Method;

import org.beigesoft.model.IHasId;
import org.beigesoft.persistable.IPersistableBase;
import org.beigesoft.service.ISrvDate;
import org.beigesoft.properties.UtlProperties;
import org.beigesoft.factory.IFactorySimple;
import org.beigesoft.factory.IFactoryAppBeansByClass;
import org.beigesoft.factory.IFactoryAppBeansByName;
import org.beigesoft.converter.IConverterToFromString;
import org.beigesoft.holder.IHolderForClassByName;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.service.IEntityProcessor;
import org.beigesoft.service.IFillerObjectFields;
import org.beigesoft.service.IMailSender;
import org.beigesoft.settings.IMngSettings;
import org.beigesoft.orm.processor.PrcEntityPbDelete;
import org.beigesoft.orm.processor.PrcEntityDelete;
import org.beigesoft.orm.processor.PrcEntityFfolDelete;
import org.beigesoft.orm.processor.PrcEntityFfolSave;
import org.beigesoft.orm.processor.PrcEntityFolDelete;
import org.beigesoft.orm.processor.PrcEntityFolSave;
import org.beigesoft.orm.processor.PrcEntityPbCopy;
import org.beigesoft.orm.processor.PrcEntityCopy;
import org.beigesoft.orm.processor.PrcEntityPbSave;
import org.beigesoft.orm.processor.PrcEmailMsgSave;
import org.beigesoft.orm.processor.PrcEntitySave;
import org.beigesoft.orm.processor.PrcEntityRetrieve;
import org.beigesoft.orm.processor.PrcEntityPbEditDelete;
import org.beigesoft.orm.processor.PrcEntityCreate;
import org.beigesoft.orm.service.ASrvOrm;

/**
 * <p>ORM entities processors factory.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FctBnEntitiesProcessors<RS>
  implements IFactoryAppBeansByName<IEntityProcessor> {

  /**
   * <p>Converters map "converter name"-"object' s converter".</p>
   **/
  private final Map<String, IEntityProcessor>
    processorsMap =
      new Hashtable<String, IEntityProcessor>();

  /**
   * <p>ORM service.</p>
   **/
  private ASrvOrm<RS> srvOrm;

  /**
   * <p>Manager UVD settings.</p>
   **/
  private IMngSettings mngUvdSettings;

  /**
   * <p>Fillers fields factory.</p>
   */
  private IFactoryAppBeansByClass<IFillerObjectFields<?>> fillersFieldsFactory;

  /**
   * <p>Entities factories factory.</p>
   **/
  private IFactoryAppBeansByClass<IFactorySimple<?>> entitiesFactoriesFatory;

  /**
   * <p>Properties service.</p>
   **/
  private UtlProperties utlProperties;

  /**
   * <p>Date service.</p>
   **/
  private ISrvDate srvDate;

  /**
   * <p>Fields converters factory.</p>
   **/
  private IFactoryAppBeansByName<IConverterToFromString<?>>
    convertersFieldsFatory;

  /**
   * <p>Field converter names holder.</p>
   **/
  private IHolderForClassByName<String> fieldConverterNamesHolder;

  /**
   * <p>Fields getters RAPI holder.</p>
   **/
  private IHolderForClassByName<Method> gettersRapiHolder;

  /**
   * <p>Fields setters RAPI holder.</p>
   **/
  private IHolderForClassByName<Method> settersRapiHolder;

  /**
   * <p>Upload directory relative to WEB-APP path
   * without start and end separator, e.g. "static/uploads".</p>
   **/
  private String uploadDirectory;

  /**
   * <p>Full WEB-APP path without end separator,
   * revealed from servlet context and used for upload files.</p>
   **/
  private String webAppPath;

  /**
   * <p>Email sender.</p>
   **/
  private IMailSender emailSender;

  /**
   * <p>Get bean in lazy mode (if bean is null then initialize it).</p>
   * @param pAddParam additional param
   * @param pBeanName - bean name
   * @return requested bean
   * @throws Exception - an exception
   */
  @Override
  public final IEntityProcessor lazyGet(
    final Map<String, Object> pAddParam,
      final String pBeanName) throws Exception {
    IEntityProcessor proc =
      this.processorsMap.get(pBeanName);
    if (proc == null) {
      // locking:
      synchronized (this.processorsMap) {
        // make sure again whether it's null after locking:
        proc = this.processorsMap.get(pBeanName);
        if (proc == null) {
          if (pBeanName
            .equals(PrcEntityRetrieve.class.getSimpleName())) {
            proc = createPutPrcEntityRetrieve();
          } else if (pBeanName
            .equals(PrcEntityCreate.class.getSimpleName())) {
            proc = createPutPrcEntityCreate();
          } else if (pBeanName
            .equals(PrcEntityDelete.class.getSimpleName())) {
            proc = createPutPrcEntityDelete();
          } else if (pBeanName
            .equals(PrcEntityPbDelete.class.getSimpleName())) {
            proc = createPutPrcEntityPbDelete();
          } else if (pBeanName
            .equals(PrcEntityFfolDelete.class.getSimpleName())) {
            proc = createPutPrcEntityFfolDelete();
          } else if (pBeanName
            .equals(PrcEntityFfolSave.class.getSimpleName())) {
            proc = createPutPrcEntityFfolSave();
          } else if (pBeanName
            .equals(PrcEntityFolDelete.class.getSimpleName())) {
            proc = createPutPrcEntityFolDelete();
          } else if (pBeanName
            .equals(PrcEntityFolSave.class.getSimpleName())) {
            proc = createPutPrcEntityFolSave();
          } else if (pBeanName
            .equals(PrcEntityPbCopy.class.getSimpleName())) {
            proc = createPutPrcEntityPbCopy();
          } else if (pBeanName
            .equals(PrcEntityCopy.class.getSimpleName())) {
            proc = createPutPrcEntityCopy();
          } else if (pBeanName
            .equals(PrcEntityPbSave.class.getSimpleName())) {
            proc = createPutPrcEntityPbSave();
          } else if (pBeanName
            .equals(PrcEntityPbEditDelete.class.getSimpleName())) {
            proc = createPutPrcEntityPbEditDelete();
          } else if (pBeanName
            .equals(PrcEmailMsgSave.class.getSimpleName())) {
            proc = createPutPrcEmailMsgSave();
          } else if (pBeanName
            .equals(PrcEntitySave.class.getSimpleName())) {
            proc = createPutPrcEntitySave();
          }
        }
      }
    }
    if (proc == null) {
      throw new ExceptionWithCode(ExceptionWithCode.CONFIGURATION_MISTAKE,
        "There is no entity processor with name " + pBeanName);
    }
    return proc;
  }

  /**
   * <p>Set bean.</p>
   * @param pBeanName - bean name
   * @param pBean bean
   * @throws Exception - an exception
   */
  @Override
  public final synchronized void set(final String pBeanName,
    final IEntityProcessor pBean) throws Exception {
    this.processorsMap.put(pBeanName, pBean);
  }

  /**
   * <p>Get PrcEntityCreate (create and put into map).</p>
   * @return requested PrcEntityCreate
   * @throws Exception - an exception
   */
  protected final PrcEntityCreate<RS, IHasId<Object>, Object>
    createPutPrcEntityCreate() throws Exception {
    PrcEntityCreate<RS, IHasId<Object>, Object> proc =
      new PrcEntityCreate<RS, IHasId<Object>, Object>();
    proc.setSrvOrm(getSrvOrm());
    proc.setMngUvdSettings(getMngUvdSettings());
    proc.setSrvDate(getSrvDate());
    proc.setConvertersFieldsFatory(getConvertersFieldsFatory());
    proc.setFieldConverterNamesHolder(getFieldConverterNamesHolder());
    //assigning fully initialized object:
    this.processorsMap
      .put(PrcEntityCreate.class.getSimpleName(), proc);
    return proc;
  }

  /**
   * <p>Get PrcEntityPbDelete (create and put into map).</p>
   * @return requested PrcEntityPbDelete
   * @throws Exception - an exception
   */
  protected final PrcEntityPbDelete<RS, IPersistableBase>
    createPutPrcEntityPbDelete() throws Exception {
    PrcEntityPbDelete<RS, IPersistableBase> proc =
      new PrcEntityPbDelete<RS, IPersistableBase>();
    proc.setSrvOrm(getSrvOrm());
    //assigning fully initialized object:
    this.processorsMap
      .put(PrcEntityPbDelete.class.getSimpleName(), proc);
    return proc;
  }

  /**
   * <p>Get PrcEntityDelete (create and put into map).</p>
   * @return requested PrcEntityDelete
   * @throws Exception - an exception
   */
  protected final PrcEntityDelete<RS, IHasId<Object>, Object>
    createPutPrcEntityDelete() throws Exception {
    PrcEntityDelete<RS, IHasId<Object>, Object> proc =
      new PrcEntityDelete<RS, IHasId<Object>, Object>();
    proc.setSrvOrm(getSrvOrm());
    //assigning fully initialized object:
    this.processorsMap
      .put(PrcEntityDelete.class.getSimpleName(), proc);
    return proc;
  }

  /**
   * <p>Get PrcEntityFfolDelete (create and put into map).</p>
   * @return requested PrcEntityFfolDelete
   * @throws Exception - an exception
   */
  protected final PrcEntityFfolDelete<RS, IHasId<Object>, Object>
    createPutPrcEntityFfolDelete() throws Exception {
    PrcEntityFfolDelete<RS, IHasId<Object>, Object> proc =
      new PrcEntityFfolDelete<RS, IHasId<Object>, Object>();
    proc.setSrvOrm(getSrvOrm());
    proc.setGettersRapiHolder(getGettersRapiHolder());
    //assigning fully initialized object:
    this.processorsMap
      .put(PrcEntityFfolDelete.class.getSimpleName(), proc);
    return proc;
  }

  /**
   * <p>Get PrcEntityFfolSave (create and put into map).</p>
   * @return requested PrcEntityFfolSave
   * @throws Exception - an exception
   */
  protected final PrcEntityFfolSave<RS, IHasId<Object>, Object>
    createPutPrcEntityFfolSave() throws Exception {
    PrcEntityFfolSave<RS, IHasId<Object>, Object> proc =
      new PrcEntityFfolSave<RS, IHasId<Object>, Object>();
    proc.setSrvOrm(getSrvOrm());
    proc.setGettersRapiHolder(getGettersRapiHolder());
    proc.setSettersRapiHolder(getSettersRapiHolder());
    proc.setUploadDirectory(getUploadDirectory());
    proc.setWebAppPath(getWebAppPath());
    //assigning fully initialized object:
    this.processorsMap
      .put(PrcEntityFfolSave.class.getSimpleName(), proc);
    return proc;
  }

  /**
   * <p>Get PrcEntityFolDelete (create and put into map).</p>
   * @return requested PrcEntityFolDelete
   * @throws Exception - an exception
   */
  protected final PrcEntityFolDelete<RS, IHasId<Object>, Object>
    createPutPrcEntityFolDelete() throws Exception {
    PrcEntityFolDelete<RS, IHasId<Object>, Object> proc =
      new PrcEntityFolDelete<RS, IHasId<Object>, Object>();
    proc.setSrvOrm(getSrvOrm());
    proc.setGettersRapiHolder(getGettersRapiHolder());
    //assigning fully initialized object:
    this.processorsMap
      .put(PrcEntityFolDelete.class.getSimpleName(), proc);
    return proc;
  }

  /**
   * <p>Get PrcEntityFolSave (create and put into map).</p>
   * @return requested PrcEntityFolSave
   * @throws Exception - an exception
   */
  protected final PrcEntityFolSave<RS, IHasId<Object>, Object>
    createPutPrcEntityFolSave() throws Exception {
    PrcEntityFolSave<RS, IHasId<Object>, Object> proc =
      new PrcEntityFolSave<RS, IHasId<Object>, Object>();
    proc.setSrvOrm(getSrvOrm());
    proc.setGettersRapiHolder(getGettersRapiHolder());
    //assigning fully initialized object:
    this.processorsMap
      .put(PrcEntityFolSave.class.getSimpleName(), proc);
    return proc;
  }

  /**
   * <p>Get PrcEntityPbCopy (create and put into map).</p>
   * @return requested PrcEntityPbCopy
   * @throws Exception - an exception
   */
  protected final PrcEntityPbCopy<RS, IPersistableBase>
    createPutPrcEntityPbCopy() throws Exception {
    PrcEntityPbCopy<RS, IPersistableBase> proc =
      new PrcEntityPbCopy<RS, IPersistableBase>();
    proc.setSrvOrm(getSrvOrm());
    proc.setMngUvdSettings(getMngUvdSettings());
    proc.setFillersFieldsFactory(getFillersFieldsFactory());
    proc.setEntitiesFactoriesFatory(getEntitiesFactoriesFatory());
    proc.setUtlProperties(getUtlProperties());
    proc.setSrvDate(getSrvDate());
    proc.setConvertersFieldsFatory(getConvertersFieldsFatory());
    proc.setFieldConverterNamesHolder(getFieldConverterNamesHolder());
    //assigning fully initialized object:
    this.processorsMap
      .put(PrcEntityPbCopy.class.getSimpleName(), proc);
    return proc;
  }

  /**
   * <p>Get PrcEntityCopy (create and put into map).</p>
   * @return requested PrcEntityCopy
   * @throws Exception - an exception
   */
  protected final PrcEntityCopy<RS, IHasId<Object>, Object>
    createPutPrcEntityCopy() throws Exception {
    PrcEntityCopy<RS, IHasId<Object>, Object> proc =
      new PrcEntityCopy<RS, IHasId<Object>, Object>();
    proc.setSrvOrm(getSrvOrm());
    proc.setMngUvdSettings(getMngUvdSettings());
    proc.setFillersFieldsFactory(getFillersFieldsFactory());
    proc.setEntitiesFactoriesFatory(getEntitiesFactoriesFatory());
    proc.setUtlProperties(getUtlProperties());
    proc.setSrvDate(getSrvDate());
    proc.setConvertersFieldsFatory(getConvertersFieldsFatory());
    proc.setFieldConverterNamesHolder(getFieldConverterNamesHolder());
    //assigning fully initialized object:
    this.processorsMap
      .put(PrcEntityCopy.class.getSimpleName(), proc);
    return proc;
  }

  /**
   * <p>Get PrcEntityPbSave (create and put into map).</p>
   * @return requested PrcEntityPbSave
   * @throws Exception - an exception
   */
  protected final PrcEntityPbSave<RS, IPersistableBase>
    createPutPrcEntityPbSave() throws Exception {
    PrcEntityPbSave<RS, IPersistableBase> proc =
      new PrcEntityPbSave<RS, IPersistableBase>();
    proc.setSrvOrm(getSrvOrm());
    //assigning fully initialized object:
    this.processorsMap
      .put(PrcEntityPbSave.class.getSimpleName(), proc);
    return proc;
  }

  /**
   * <p>Get PrcEmailMsgSave (create and put into map).</p>
   * @return requested PrcEmailMsgSave
   * @throws Exception - an exception
   */
  protected final PrcEmailMsgSave<RS>
    createPutPrcEmailMsgSave() throws Exception {
    PrcEmailMsgSave<RS> proc = new PrcEmailMsgSave<RS>();
    proc.setSrvOrm(getSrvOrm());
    proc.setEmailSender(getEmailSender());
    //assigning fully initialized object:
    this.processorsMap
      .put(PrcEmailMsgSave.class.getSimpleName(), proc);
    return proc;
  }

  /**
   * <p>Get PrcEntitySave (create and put into map).</p>
   * @return requested PrcEntitySave
   * @throws Exception - an exception
   */
  protected final PrcEntitySave<RS, IHasId<Object>, Object>
    createPutPrcEntitySave() throws Exception {
    PrcEntitySave<RS, IHasId<Object>, Object> proc =
      new PrcEntitySave<RS, IHasId<Object>, Object>();
    proc.setSrvOrm(getSrvOrm());
    //assigning fully initialized object:
    this.processorsMap
      .put(PrcEntitySave.class.getSimpleName(), proc);
    return proc;
  }

  /**
   * <p>Get PrcEntityPbEditDelete (create and put into map).</p>
   * @return requested PrcEntityPbEditDelete
   * @throws Exception - an exception
   */
  protected final PrcEntityPbEditDelete<RS, IPersistableBase>
    createPutPrcEntityPbEditDelete() throws Exception {
    PrcEntityPbEditDelete<RS, IPersistableBase> proc =
      new PrcEntityPbEditDelete<RS, IPersistableBase>();
    proc.setSrvOrm(getSrvOrm());
    proc.setMngUvdSettings(getMngUvdSettings());
    proc.setFillersFieldsFactory(getFillersFieldsFactory());
    proc.setEntitiesFactoriesFatory(getEntitiesFactoriesFatory());
    proc.setUtlProperties(getUtlProperties());
    proc.setSrvDate(getSrvDate());
    proc.setConvertersFieldsFatory(getConvertersFieldsFatory());
    proc.setFieldConverterNamesHolder(getFieldConverterNamesHolder());
    //assigning fully initialized object:
    this.processorsMap
      .put(PrcEntityPbEditDelete.class.getSimpleName(), proc);
    return proc;
  }

  /**
   * <p>Get PrcEntityRetrieve (create and put into map).</p>
   * @return requested PrcEntityRetrieve
   * @throws Exception - an exception
   */
  protected final PrcEntityRetrieve<RS, IHasId<Object>, Object>
    createPutPrcEntityRetrieve() throws Exception {
    PrcEntityRetrieve<RS, IHasId<Object>, Object> proc =
      new PrcEntityRetrieve<RS, IHasId<Object>, Object>();
    proc.setSrvOrm(getSrvOrm());
    proc.setMngUvdSettings(getMngUvdSettings());
    proc.setFillersFieldsFactory(getFillersFieldsFactory());
    proc.setEntitiesFactoriesFatory(getEntitiesFactoriesFatory());
    proc.setUtlProperties(getUtlProperties());
    proc.setSrvDate(getSrvDate());
    proc.setConvertersFieldsFatory(getConvertersFieldsFatory());
    proc.setFieldConverterNamesHolder(getFieldConverterNamesHolder());
    //assigning fully initialized object:
    this.processorsMap
      .put(PrcEntityRetrieve.class.getSimpleName(), proc);
    return proc;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for srvOrm.</p>
   * @return ASrvOrm<RS>
   **/
  public final ASrvOrm<RS> getSrvOrm() {
    return this.srvOrm;
  }

  /**
   * <p>Setter for srvOrm.</p>
   * @param pSrvOrm reference
   **/
  public final void setSrvOrm(final ASrvOrm<RS> pSrvOrm) {
    this.srvOrm = pSrvOrm;
  }

  /**
   * <p>Getter for mngUvdSettings.</p>
   * @return IMngSettings
   **/
  public final IMngSettings getMngUvdSettings() {
    return this.mngUvdSettings;
  }

  /**
   * <p>Setter for mngUvdSettings.</p>
   * @param pMngUvdSettings reference
   **/
  public final void setMngUvdSettings(final IMngSettings pMngUvdSettings) {
    this.mngUvdSettings = pMngUvdSettings;
  }

  /**
   * <p>Getter for fillersFieldsFactory.</p>
   * @return IFactoryAppBeansByClass<IFillerObjectFields<?>>
   **/
  public final IFactoryAppBeansByClass<IFillerObjectFields<?>>
    getFillersFieldsFactory() {
    return this.fillersFieldsFactory;
  }

  /**
   * <p>Setter for fillersFieldsFactory.</p>
   * @param pFillersFieldsFactory reference
   **/
  public final void setFillersFieldsFactory(
    final IFactoryAppBeansByClass<IFillerObjectFields<?>>
      pFillersFieldsFactory) {
    this.fillersFieldsFactory = pFillersFieldsFactory;
  }

  /**
   * <p>Getter for entitiesFactoriesFatory.</p>
   * @return IFactoryAppBeansByClass<IFactorySimple<?>>
   **/
  public final IFactoryAppBeansByClass<IFactorySimple<?>>
    getEntitiesFactoriesFatory() {
    return this.entitiesFactoriesFatory;
  }

  /**
   * <p>Setter for entitiesFactoriesFatory.</p>
   * @param pEntitiesFactoriesFatory reference
   **/
  public final void setEntitiesFactoriesFatory(
    final IFactoryAppBeansByClass<IFactorySimple<?>> pEntitiesFactoriesFatory) {
    this.entitiesFactoriesFatory = pEntitiesFactoriesFatory;
  }

  /**
   * <p>Geter for utlProperties.</p>
   * @return UtlProperties
   **/
  public final UtlProperties getUtlProperties() {
    return this.utlProperties;
  }

  /**
   * <p>Setter for utlProperties.</p>
   * @param pUtlProperties reference
   **/
  public final void setUtlProperties(final UtlProperties pUtlProperties) {
    this.utlProperties = pUtlProperties;
  }

  /**
   * <p>Getter for srvDate.</p>
   * @return ISrvDate
   **/
  public final ISrvDate getSrvDate() {
    return this.srvDate;
  }

  /**
   * <p>Setter for srvDate.</p>
   * @param pSrvDate reference
   **/
  public final void setSrvDate(final ISrvDate pSrvDate) {
    this.srvDate = pSrvDate;
  }

  /**
   * <p>Getter for convertersFieldsFatory.</p>
   * @return IFactoryAppBeansByName<IConverterToFromString<?>>
   **/
  public final IFactoryAppBeansByName<IConverterToFromString<?>>
    getConvertersFieldsFatory() {
    return this.convertersFieldsFatory;
  }

  /**
   * <p>Setter for convertersFieldsFatory.</p>
   * @param pConvertersFieldsFatory reference
   **/
  public final void setConvertersFieldsFatory(
    final IFactoryAppBeansByName<IConverterToFromString<?>>
      pConvertersFieldsFatory) {
    this.convertersFieldsFatory = pConvertersFieldsFatory;
  }

  /**
   * <p>Getter for fieldConverterNamesHolder.</p>
   * @return IHolderForClassByName<String>
   **/
  public final IHolderForClassByName<String> getFieldConverterNamesHolder() {
    return this.fieldConverterNamesHolder;
  }

  /**
   * <p>Setter for fieldConverterNamesHolder.</p>
   * @param pFieldConverterNamesHolder reference
   **/
  public final void setFieldConverterNamesHolder(
    final IHolderForClassByName<String> pFieldConverterNamesHolder) {
    this.fieldConverterNamesHolder = pFieldConverterNamesHolder;
  }

  /**
   * <p>Getter for gettersRapiHolder.</p>
   * @return IHolderForClassByName<Method>
   **/
  public final IHolderForClassByName<Method> getGettersRapiHolder() {
    return this.gettersRapiHolder;
  }

  /**
   * <p>Setter for gettersRapiHolder.</p>
   * @param pGettersRapiHolder reference
   **/
  public final void setGettersRapiHolder(
    final IHolderForClassByName<Method> pGettersRapiHolder) {
    this.gettersRapiHolder = pGettersRapiHolder;
  }


  /**
   * <p>Getter for settersRapiHolder.</p>
   * @return IHolderForClassByName<Method>
   **/
  public final IHolderForClassByName<Method> getSettersRapiHolder() {
    return this.settersRapiHolder;
  }

  /**
   * <p>Setter for settersRapiHolder.</p>
   * @param pSettersRapiHolder reference
   **/
  public final void setSettersRapiHolder(
    final IHolderForClassByName<Method> pSettersRapiHolder) {
    this.settersRapiHolder = pSettersRapiHolder;
  }
  /**
   * <p>Getter for uploadDirectory.</p>
   * @return String
   **/
  public final String getUploadDirectory() {
    return this.uploadDirectory;
  }

  /**
   * <p>Setter for uploadDirectory.</p>
   * @param pUploadDirectory reference
   **/
  public final void setUploadDirectory(final String pUploadDirectory) {
    this.uploadDirectory = pUploadDirectory;
  }

  /**
   * <p>Getter for webAppPath.</p>
   * @return String
   **/
  public final String getWebAppPath() {
    return this.webAppPath;
  }

  /**
   * <p>Setter for webAppPath.</p>
   * @param pWebAppPath reference
   **/
  public final void setWebAppPath(final String pWebAppPath) {
    this.webAppPath = pWebAppPath;
  }

  /**
   * <p>Getter for emailSender.</p>
   * @return IMailSender
   **/
  public final IMailSender getEmailSender() {
    return this.emailSender;
  }

  /**
   * <p>Setter for emailSender.</p>
   * @param pEmailSender reference
   **/
  public final void setEmailSender(final IMailSender pEmailSender) {
    this.emailSender = pEmailSender;
  }
}
