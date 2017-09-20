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

import java.util.Map;
import java.lang.reflect.Method;

import org.beigesoft.model.IRequestData;
import org.beigesoft.model.IHasId;
import org.beigesoft.holder.IHolderForClassByName;
import org.beigesoft.service.IEntityProcessor;
import org.beigesoft.orm.service.ASrvOrm;

/**
 * <p>Service that save entity from owned list (e.g. invoice line) into DB,
 * then return owner to edit and put owner class to additional params
 * to refresh owner list.</p>
 *
 * @param <RS> platform dependent record set type
 * @param <T> entity type
 * @param <ID> entity ID type
 * @author Yury Demidenko
 */
public class PrcEntityFolSave<RS, T extends IHasId<ID>, ID>
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
      this.srvOrm.insertEntity(pAddParam, pEntity);
      pEntity.setIsNew(false);
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
}
