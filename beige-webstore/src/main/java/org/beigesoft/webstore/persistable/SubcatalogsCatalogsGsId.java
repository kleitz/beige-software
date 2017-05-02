package org.beigesoft.webstore.persistable;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

/**
 * <pre>
 * Model of Catalog that contains of Subcatalogs of Goods/Services.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class SubcatalogsCatalogsGsId {

  /**
   * <p>Subcatalogs Catalog, not null, its hasSubcatalogs=true.</p>
   **/
  private CatalogGs itsCatalog;

  /**
   * <p>Subatalog, not null.</p>
   **/
  private CatalogGs subcatalog;

  /**
   * <p>Minimal constructor.</p>
   **/
  public SubcatalogsCatalogsGsId() {
  }

  /**
   * <p>Useful constructor.</p>
   * @param pCatalog reference
   * @param pSubcatalog reference
   **/
  public SubcatalogsCatalogsGsId(final CatalogGs pCatalog,
    final CatalogGs pSubcatalog) {
    this.subcatalog = pSubcatalog;
    this.itsCatalog = pCatalog;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for itsCatalog.</p>
   * @return CatalogGs
   **/
  public final CatalogGs getItsCatalog() {
    return this.itsCatalog;
  }

  /**
   * <p>Setter for itsCatalog.</p>
   * @param pCatalog reference
   **/
  public final void setItsCatalog(final CatalogGs pCatalog) {
    this.itsCatalog = pCatalog;
  }

  /**
   * <p>Getter for subcatalog.</p>
   * @return CatalogGs
   **/
  public final CatalogGs getSubcatalog() {
    return this.subcatalog;
  }

  /**
   * <p>Setter for subcatalog.</p>
   * @param pSubcatalog reference
   **/
  public final void setSubcatalog(final CatalogGs pSubcatalog) {
    this.subcatalog = pSubcatalog;
  }
}
