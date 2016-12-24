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
import java.text.DateFormat;

import org.beigesoft.holder.IAttributes;
import org.beigesoft.accounting.persistable.IDocWarehouse;
import org.beigesoft.accounting.persistable.CogsEntry;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.service.ISrvI18n;

/**
 * <p>Business service for accounting document
 * which or its lines makes accounting and warehouse entries.</p>
 *
 * @param <RS> platform dependent record set type
 * @param <T> document type
 * @author Yury Demidenko
 */
public abstract class ASrvDocumentCogs<RS, T extends IDocWarehouse>
  extends ASrvDocument<RS, T> {

  /**
   * <p>Business service for warehouse.</p>
   **/
  private ISrvWarehouseEntry srvWarehouseEntry;

  /**
   * <p>Business service for draw any item to sale/loss/stole.</p>
   **/
  private ISrvDrawItemEntry<CogsEntry> srvCogsEntry;

  /**
   * <p>minimum constructor.</p>
   * @param pEntityClass Entity Class
   **/
  public ASrvDocumentCogs(final Class<T> pEntityClass) {
    super(pEntityClass);
  }

  /**
   * <p>Useful constructor.</p>
   * @param pEntityClass Entity Class
   * @param pSrvOrm ORM service
   * @param pSrvAccSettings AccSettings service
   * @param pSrvAccEntry Accounting entries service
   * @param pSrvI18n I18N service
   * @param pDateFormatter for description
   * @param pSrvWarehouseEntry Warehouse service
   * @param pSrvCogsEntry Draw material service
   **/
  public ASrvDocumentCogs(final Class<T> pEntityClass,
    final ISrvOrm<RS> pSrvOrm, final ISrvAccSettings pSrvAccSettings,
      final ISrvAccEntry pSrvAccEntry,
        final ISrvI18n pSrvI18n, final DateFormat pDateFormatter,
          final ISrvWarehouseEntry pSrvWarehouseEntry,
            final ISrvDrawItemEntry<CogsEntry> pSrvCogsEntry) {
    super(pEntityClass, pSrvOrm, pSrvAccSettings, pSrvAccEntry,
      pSrvI18n, pDateFormatter);
    this.srvWarehouseEntry = pSrvWarehouseEntry;
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
