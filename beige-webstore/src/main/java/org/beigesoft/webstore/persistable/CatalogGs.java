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

import org.beigesoft.persistable.AHasNameIdLongVersion;

/**
 * <pre>
 * Model of catalog of goods/services.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class CatalogGs extends AHasNameIdLongVersion {

  /**
   * <p>If has subcatalogs, not null, false default.</p>
   **/
  private Boolean hasSubcatalogs = false;

  /**
   * <p>Description.</p>
   **/
  private String description;

  /**
   * <p>Ordering.</p>
   **/
  private Integer itsIndex;

  /**
   * <p>Is it in the menu, default true, to quick switch on/off from menu
   * or for catalog that shows only on start.</p>
   **/
  private Boolean isInMenu = true;

  //Simple getters and setters:
  /**
   * <p>Getter for description.</p>
   * @return String
   **/
  public final String getDescription() {
    return this.description;
  }

  /**
   * <p>Setter for description.</p>
   * @param pDescription reference
   **/
  public final void setDescription(final String pDescription) {
    this.description = pDescription;
  }

  /**
   * <p>Getter for hasSubcatalogs.</p>
   * @return Boolean
   **/
  public final Boolean getHasSubcatalogs() {
    return this.hasSubcatalogs;
  }

  /**
   * <p>Setter for hasSubcatalogs.</p>
   * @param pHasSubcatalogs reference
   **/
  public final void setHasSubcatalogs(final Boolean pHasSubcatalogs) {
    this.hasSubcatalogs = pHasSubcatalogs;
  }

  /**
   * <p>Getter for itsIndex.</p>
   * @return Integer
   **/
  public final Integer getItsIndex() {
    return this.itsIndex;
  }

  /**
   * <p>Setter for itsIndex.</p>
   * @param pItsIndex reference
   **/
  public final void setItsIndex(final Integer pItsIndex) {
    this.itsIndex = pItsIndex;
  }

  /**
   * <p>Getter for isInMenu.</p>
   * @return Boolean
   **/
  public final Boolean getIsInMenu() {
    return this.isInMenu;
  }

  /**
   * <p>Setter for isInMenu.</p>
   * @param pIsInMenu reference
   **/
  public final void setIsInMenu(final Boolean pIsInMenu) {
    this.isInMenu = pIsInMenu;
  }
}
