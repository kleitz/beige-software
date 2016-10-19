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
import org.beigesoft.accounting.persistable.UseMaterialEntry;
import org.beigesoft.accounting.persistable.CogsEntry;
import org.beigesoft.orm.service.ISrvOrm;

/**
 * <p>Business service for accounting document
 * which or its lines makes accounting, warehouse,
 * use material and COGS entries.</p>
 *
 * @param <RS> platform dependent record set type
 * @param <T> document type
 * @author Yury Demidenko
 */
public abstract class ASrvDocumentFull<RS, T extends IDocWarehouse>
  extends ASrvDocument<RS, T> {

  /**
   * <p>Business service for warehouse.</p>
   **/
  private ISrvWarehouseEntry srvWarehouseEntry;

  /**
   * <p>Business service for draw material.</p>
   **/
  private ISrvDrawItemEntry<UseMaterialEntry> srvUseMaterialEntry;

  /**
   * <p>Business service for draw material.</p>
   **/
  private ISrvDrawItemEntry<CogsEntry> srvCogsEntry;

  /**
   * <p>minimum constructor.</p>
   * @param pEntityClass Entity Class
   **/
  public ASrvDocumentFull(final Class<T> pEntityClass) {
    super(pEntityClass);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pEntityClass Entity Class
   * @param pSrvOrm ORM service
   * @param pSrvAccSettings AccSettings service
   * @param pSrvAccEntry Accounting entries service
   * @param pSrvWarehouseEntry Warehouse service
   * @param pSrvUseMaterialEntry Draw material service
   * @param pSrvCogsEntry Draw material service
   **/
  public ASrvDocumentFull(final Class<T> pEntityClass,
    final ISrvOrm<RS> pSrvOrm, final ISrvAccSettings pSrvAccSettings,
      final ISrvAccEntry pSrvAccEntry,
        final ISrvWarehouseEntry pSrvWarehouseEntry,
          final ISrvDrawItemEntry<UseMaterialEntry> pSrvUseMaterialEntry,
            final ISrvDrawItemEntry<CogsEntry> pSrvCogsEntry) {
    super(pEntityClass, pSrvOrm, pSrvAccSettings, pSrvAccEntry);
    this.srvWarehouseEntry = pSrvWarehouseEntry;
    this.srvUseMaterialEntry = pSrvUseMaterialEntry;
    this.srvCogsEntry = pSrvCogsEntry;
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
    attributes.setAttribute("useMaterialEntries", srvUseMaterialEntry
      .retrieveEntriesFor(pAddParam, pEntity));
    attributes.setAttribute("cogsEntries", srvCogsEntry
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

  /**
   * <p>Getter for srvUseMaterialEntry.</p>
   * @return ISrvDrawItemEntry<UseMaterialEntry>
   **/
  public final ISrvDrawItemEntry<UseMaterialEntry> getSrvUseMaterialEntry() {
    return this.srvUseMaterialEntry;
  }

  /**
   * <p>Setter for srvUseMaterialEntry.</p>
   * @param pSrvUseMaterialEntry reference
   **/
  public final void setSrvUseMaterialEntry(
    final ISrvDrawItemEntry<UseMaterialEntry> pSrvUseMaterialEntry) {
    this.srvUseMaterialEntry = pSrvUseMaterialEntry;
  }

  /**
   * <p>Getter for srvCogsEntry.</p>
   * @return ISrvDrawItemEntry<CogsEntry>
   **/
  public final ISrvDrawItemEntry<CogsEntry> getSrvCogsEntry() {
    return this.srvCogsEntry;
  }

  /**
   * <p>Setter for srvCogsEntry.</p>
   * @param pSrvCogsEntry reference
   **/
  public final void setSrvCogsEntry(
    final ISrvDrawItemEntry<CogsEntry> pSrvCogsEntry) {
    this.srvCogsEntry = pSrvCogsEntry;
  }
}
