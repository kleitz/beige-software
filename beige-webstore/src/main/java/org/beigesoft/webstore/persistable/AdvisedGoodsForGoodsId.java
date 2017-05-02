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
 * Model of ID for Advised Goods For Goods.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class AdvisedGoodsForGoodsId {

  /**
   * <p>Not null, for forAdviseCategory advise category,
   * e.g. "smartphones 5inch 2*3".</p>
   **/
  private AdviseCategoryOfGs forAdviseCategory;

  /**
   * <p>Not null, forAdviseCategory advised category,
   * e.g. "covers for smartphones 5inch 2*3".</p>
   **/
  private AdviseCategoryOfGs advisedCategory;

  /**
   * <p>Minimal constructor.</p>
   **/
  public AdvisedGoodsForGoodsId() {
  }

  /**
   * <p>Useful constructor.</p>
   * @param pForAdviseCategory reference
   * @param pAdviseCategory reference
   **/
  public AdvisedGoodsForGoodsId(final AdviseCategoryOfGs pAdviseCategory,
    final AdviseCategoryOfGs pForAdviseCategory) {
    this.forAdviseCategory = pForAdviseCategory;
    this.advisedCategory = pAdviseCategory;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for forAdviseCategory.</p>
   * @return AdviseCategoryOfGs
   **/
  public final AdviseCategoryOfGs getForAdviseCategory() {
    return this.forAdviseCategory;
  }

  /**
   * <p>Setter for forAdviseCategory.</p>
   * @param pForAdviseCategory reference
   **/
  public final void setForAdviseCategory(
    final AdviseCategoryOfGs pForAdviseCategory) {
    this.forAdviseCategory = pForAdviseCategory;
  }

  /**
   * <p>Getter for advisedCategory.</p>
   * @return AdviseCategoryOfGs
   **/
  public final AdviseCategoryOfGs getAdvisedCategory() {
    return this.advisedCategory;
  }

  /**
   * <p>Setter for advisedCategory.</p>
   * @param pAdvisedCategory reference
   **/
  public final void setAdvisedCategory(
    final AdviseCategoryOfGs pAdvisedCategory) {
    this.advisedCategory = pAdvisedCategory;
  }
}
