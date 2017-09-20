package org.beigesoft.webstore.processor;

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

import org.beigesoft.model.IRequestData;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.service.IEntityProcessor;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.accounting.persistable.InvItem;
import org.beigesoft.accounting.service.ISrvAccSettings;
import org.beigesoft.webstore.persistable.GoodsCatalogs;
import org.beigesoft.webstore.persistable.GoodsCatalogsId;
import org.beigesoft.webstore.service.ISrvTradingSettings;

/**
 * <p>Service that save GoodsCatalogs into DB.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class PrcGoodsCatalogsSave<RS>
  implements IEntityProcessor<GoodsCatalogs, GoodsCatalogsId> {

  /**
   * <p>ORM service.</p>
   **/
  private ISrvOrm<RS> srvOrm;

  /**
   * <p>Business service for accounting settings.</p>
   **/
  private ISrvAccSettings srvAccSettings;

  /**
   * <p>Business service for trading settings.</p>
   **/
  private ISrvTradingSettings srvTradingSettings;

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
  public final GoodsCatalogs process(
    final Map<String, Object> pAddParam,
      final GoodsCatalogs pEntity,
        final IRequestData pRequestData) throws Exception {
    if (!pEntity.getIsNew()) {
      if (pAddParam.get("user") != null) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "edit_not_allowed",
            pAddParam.get("user").toString());
      } else {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "edit_not_allowed");
      }
    }
    //Beige-ORM refresh:
    pEntity.setGoods(getSrvOrm().retrieveEntity(pAddParam, pEntity.getGoods()));
    if (!(InvItem.FINISHED_PRODUCT_ID.equals(pEntity.getGoods().getItsType()
      .getItsId()) || InvItem.MERCHANDISE_ID.equals(pEntity.getGoods()
        .getItsType().getItsId()))) {
      if (pAddParam.get("user") != null) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "type_must_be_material_or_merchandise",
            pAddParam.get("user").toString());
      } else {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "type_must_be_material_or_merchandise");
      }
    }
    //Beige-ORM refresh:
    pEntity.setItsCatalog(getSrvOrm()
      .retrieveEntity(pAddParam, pEntity.getItsCatalog()));
    if (pEntity.getItsCatalog().getHasSubcatalogs()) {
      if (pAddParam.get("user") != null) {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "catalog_must_be_for_goods",
            pAddParam.get("user").toString());
      } else {
        throw new ExceptionWithCode(ExceptionWithCode.WRONG_PARAMETER,
          "catalog_must_be_for_goods");
      }
    }
    if (pEntity.getIsNew()) {
      getSrvOrm().insertEntity(pAddParam, pEntity);
    } else {
      getSrvOrm().updateEntity(pAddParam, pEntity);
    }
    pRequestData.setAttribute("tradingSettings", srvTradingSettings
      .lazyGetTradingSettings(pAddParam));
    pRequestData.setAttribute("accSettings",
      this.srvAccSettings.lazyGetAccSettings(pAddParam));
    return pEntity;
  }


  //Simple getters and setters:
  /**
   * <p>Getter for srvOrm.</p>
   * @return ISrvOrm<RS>
   **/
  public final ISrvOrm<RS> getSrvOrm() {
    return this.srvOrm;
  }

  /**
   * <p>Setter for srvOrm.</p>
   * @param pSrvOrm reference
   **/
  public final void setSrvOrm(final ISrvOrm<RS> pSrvOrm) {
    this.srvOrm = pSrvOrm;
  }

  /**
   * <p>Getter for srvAccSettings.</p>
   * @return ISrvAccSettings
   **/
  public final ISrvAccSettings getSrvAccSettings() {
    return this.srvAccSettings;
  }

  /**
   * <p>Setter for srvAccSettings.</p>
   * @param pSrvAccSettings reference
   **/
  public final void setSrvAccSettings(final ISrvAccSettings pSrvAccSettings) {
    this.srvAccSettings = pSrvAccSettings;
  }

  /**
   * <p>Getter for srvTradingSettings.</p>
   * @return ISrvTradingSettings
   **/
  public final ISrvTradingSettings getSrvTradingSettings() {
    return this.srvTradingSettings;
  }

  /**
   * <p>Setter for srvTradingSettings.</p>
   * @param pSrvTradingSettings reference
   **/
  public final void setSrvTradingSettings(
    final ISrvTradingSettings pSrvTradingSettings) {
    this.srvTradingSettings = pSrvTradingSettings;
  }
}
