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

import org.beigesoft.holder.IAttributes;
import org.beigesoft.accounting.persistable.IDocWarehouse;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Business service for accounting document
 * which or its lines makes accounting and warehouse entries.</p>
 *
 * @param <RS> platform dependent record set type
 * @param <T> document type
 * @author Yury Demidenko
 */
public abstract class ASrvDocumentWarehouse<RS, T extends IDocWarehouse>
  extends ASrvDocument<RS, T> {

  /**
   * <p>Business service for warehouse.</p>
   **/
  private ISrvWarehouseEntry srvWarehouseEntry;

  /**
   * <p>minimum constructor.</p>
   * @param pEntityClass Entity Class
   **/
  public ASrvDocumentWarehouse(final Class<T> pEntityClass) {
    super(pEntityClass);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pEntityClass Entity Class
   * @param pSrvOrm ORM service
   * @param pSrvAccSettings AccSettings service
   * @param pSrvAccEntry Accounting entries service
   * @param pSrvWarehouseEntry Warehouse service
   **/
  public ASrvDocumentWarehouse(final Class<T> pEntityClass,
    final ISrvOrm<RS> pSrvOrm, final ISrvAccSettings pSrvAccSettings,
      final ISrvAccEntry pSrvAccEntry,
        final ISrvWarehouseEntry pSrvWarehouseEntry) {
    super(pEntityClass, pSrvOrm, pSrvAccSettings, pSrvAccEntry);
    this.srvWarehouseEntry = pSrvWarehouseEntry;
  }


  /**
   * <p>Retrieve other data of entity e.g. warehouse entries.</p>
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  @Override
  public final void retrieveOtherDataFor(final Map<String, Object> pAddParam,
    final T pEntity) throws Exception {
    IAttributes attributes = (IAttributes) pAddParam.get("attributes");
    attributes.setAttribute("warehouseEntries", srvWarehouseEntry
      .retrieveEntriesFor(pAddParam, pEntity));
  }

  //Simple getters and setters:
  /**
   * <p>Geter for srvWarehouseEntry.</p>
   * @return ISrvWarehouseEntry
   **/
  public final ISrvWarehouseEntry getSrvWarehouseEntry() {
    return this.srvWarehouseEntry;
  }

  /**
   * <p>Setter for srvWarehouseEntry.</p>
   * @param pSrvWarehouseEntry reference
   **/
  public final void setSrvWarehouseEntry(
    final ISrvWarehouseEntry pSrvWarehouseEntry) {
    this.srvWarehouseEntry = pSrvWarehouseEntry;
  }
}
