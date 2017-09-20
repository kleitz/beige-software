package org.beigesoft.orm.processor;

/*
 * Copyright (c) 2015-2017 Beigesoft ™
 *
 * Licensed under the GNU General Public License (GPL), Version 2.0
 * (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

import java.util.Date;
import java.util.Map;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Method;

import org.beigesoft.model.IRequestData;
import org.beigesoft.model.IHasId;
import org.beigesoft.holder.IHolderForClassByName;
import org.beigesoft.service.IEntityProcessor;
import org.beigesoft.orm.service.ASrvOrm;

/**
 * <p>Service that save entity from owned list with file into DB,
 * then return owner to edit and put owner class to additional params
 * to refresh owner list.</p>
 *
 * @param <RS> platform dependent record set type
 * @param <T> entity type
 * @param <ID> entity ID type
 * @author Yury Demidenko
 */
public class PrcEntityFfolSave<RS, T extends IHasId<ID>, ID>
  implements IEntityProcessor<T, ID> {

  /**
   * <p>ORM service.</p>
   **/
  private ASrvOrm<RS> srvOrm;

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
   * <p>Process entity request.</p>
   * @param pAddParam additional param, e.g. return this line's
   * document in "nextEntity" for farther process
   * @param pRequestData Request Data
   * @param pEntity Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final T process(
    final Map<String, Object> pAddParam,
      final T pEntity, final IRequestData pRequestData) throws Exception {
    if (pEntity.getIsNew()) {
      OutputStream outs = null;
      InputStream ins = null;
      try {
        //fill file name:
        String fileToUploadName = (String) pRequestData
          .getAttribute("fileToUploadName");
        String fieldNameFileName = pRequestData
          .getParameter("fieldNameFileName");
        if (fieldNameFileName != null) {
          Method setterFileName = this.settersRapiHolder
            .getFor(pEntity.getClass(), fieldNameFileName);
          setterFileName.invoke(pEntity, fileToUploadName);
        }
        //fill file and filePath field:
        String filePath = this.webAppPath + File.separator
          + this.uploadDirectory + File.separator
            + new Date().getTime() + fileToUploadName;
        ins = (InputStream) pRequestData
          .getAttribute("fileToUploadInputStream");
        outs = new BufferedOutputStream(
          new FileOutputStream(filePath));
        byte[] data = new byte[1024];
        int count;
        while ((count = ins.read(data)) != -1) {
          outs.write(data, 0, count);
        }
        outs.flush();
        String fieldPathName = pRequestData
          .getParameter("paramNameFileToUpload");
        fieldPathName = fieldPathName.substring(fieldPathName.indexOf(".") + 1);
        Method setterFieldPath = this.settersRapiHolder
          .getFor(pEntity.getClass(), fieldPathName);
        setterFieldPath.invoke(pEntity, filePath);
        this.srvOrm.insertEntity(pAddParam, pEntity);
        pEntity.setIsNew(false);
      } finally {
        if (ins != null) {
          ins.close();
        }
        if (outs != null) {
          outs.close();
        }
      }
    } else {
      this.srvOrm.updateEntity(pAddParam, pEntity);
    }
    String ownerFieldName = this.srvOrm.getTablesMap()
      .get(pEntity.getClass().getSimpleName()).getOwnerFieldName();
    Method getter = this.gettersRapiHolder
      .getFor(pEntity.getClass(), ownerFieldName);
    @SuppressWarnings("unchecked")
    T owner = (T) getter.invoke(pEntity);
    pAddParam.put("nameOwnerEntity", owner.getClass().getSimpleName());
    pAddParam.put("nextEntity", owner);
    return null;
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

}
