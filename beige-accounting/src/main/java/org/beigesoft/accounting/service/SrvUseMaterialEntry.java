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
import java.text.DateFormat;

import org.beigesoft.service.ISrvI18n;
import org.beigesoft.accounting.persistable.base.ADrawItemSourcesLine;
import org.beigesoft.accounting.persistable.UseMaterialEntry;
import org.beigesoft.orm.service.ISrvOrm;
import org.beigesoft.orm.service.ISrvDatabase;

/**
 * <p>Business service for draw material for manufacture product
 * or another material.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrvUseMaterialEntry<RS>
  extends ASrvDrawItemEntry<UseMaterialEntry, RS> {

  /**
   * <p>minimum constructor.</p>
   **/
  public SrvUseMaterialEntry() {
  }

  /**
   * <p>Useful constructor.</p>
   * @param pSrvOrm ORM service
   * @param pSrvDatabase Database service
   * @param pSrvTypeCode service for code - java type map of material holders
   * @param pSrvAccSettings AccSettings service
   * @param pSrvI18n I18N service
   * @param pDateFormatter for description
   **/
  public SrvUseMaterialEntry(final ISrvOrm<RS> pSrvOrm,
    final ISrvDatabase<RS> pSrvDatabase, final ISrvTypeCode pSrvTypeCode,
      final ISrvAccSettings pSrvAccSettings,
        final ISrvI18n pSrvI18n, final DateFormat pDateFormatter) {
    super(pSrvOrm, pSrvDatabase, pSrvTypeCode, pSrvAccSettings, pSrvI18n,
      pDateFormatter);
  }

  /**
   * <p>Get draw item entry class.</p>
   * @return draw item entry class
   **/
  @Override
  public final Class<UseMaterialEntry> getDrawItemEntryClass() {
    return UseMaterialEntry.class;
  }

  /**
   * <p>Create draw item entry.</p>
   * @return draw item entry
   **/
  @Override
  public final UseMaterialEntry createDrawItemEntry() {
    return new UseMaterialEntry();
  }

  /**
   * <p>Get draw item sources.</p>
   * @return draw item sources
   * @throws Exception - an exception
   **/
  @Override
  public final List<? extends ADrawItemSourcesLine>
    getDrawItemSources() throws Exception {
    return getSrvAccSettings().lazyGetAccSettings()
      .getDrawMaterialSources();
  }
}
