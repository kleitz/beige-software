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

import java.math.BigDecimal;

import org.beigesoft.model.IOwned;
import org.beigesoft.persistable.AHasNameIdLongVersion;
import org.beigesoft.webstore.model.EShopItemType;

/**
 * <pre>
 * Model of CartItem.
 * </pre>
 *
 * @author Yury Demidenko
 */
public class CartItem extends AHasNameIdLongVersion
  implements IOwned<ShoppingCart> {

  /**
   * <p>Shopping Cart.</p>
   **/
  private ShoppingCart itsOwner;

  /**
   * <p>Do not show in cart, it's for performance,
   * old purchased cart emptied with this flag,
   * when buyer add new goods to cart then it's used any disabled
   * line (if exist) otherwise new line will be created.</p>
   **/
  private Boolean isDisabled;

  /**
   * <p>Shop Item Type, not null.</p>
   **/
  private EShopItemType itemType;

  /**
   * <p>Item ID, not null.</p>
   **/
  private Long itemId;

  /**
   * <p>Price, not null.</p>
   **/
  private BigDecimal itsPrice;

  /**
   * <p>Total, not null.</p>
   **/
  private BigDecimal itsTotal;

  /**
   * <p>Quantity, not null.</p>
   **/
  private Integer itsQuantity;

  /**
   * <p>Available quantity, not null.</p>
   **/
  private Integer availableQuantity;

  /**
   * <p>Getter for itsOwner.</p>
   * @return ShoppingCart
   **/
  @Override
  public final ShoppingCart getItsOwner() {
    return this.itsOwner;
  }

  /**
   * <p>Setter for itsOwner.</p>
   * @param pItsOwner reference
   **/
  @Override
  public final void setItsOwner(final ShoppingCart pItsOwner) {
    this.itsOwner = pItsOwner;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for isDisabled.</p>
   * @return Boolean
   **/
  public final Boolean getIsDisabled() {
    return this.isDisabled;
  }

  /**
   * <p>Setter for isDisabled.</p>
   * @param pIsDisabled reference
   **/
  public final void setIsDisabled(final Boolean pIsDisabled) {
    this.isDisabled = pIsDisabled;
  }

  /**
   * <p>Getter for itemType.</p>
   * @return EShopItemType
   **/
  public final EShopItemType getItemType() {
    return this.itemType;
  }

  /**
   * <p>Setter for itemType.</p>
   * @param pItemType reference
   **/
  public final void setItemType(final EShopItemType pItemType) {
    this.itemType = pItemType;
  }

  /**
   * <p>Getter for itemId.</p>
   * @return Long
   **/
  public final Long getItemId() {
    return this.itemId;
  }

  /**
   * <p>Setter for itemId.</p>
   * @param pItemId reference
   **/
  public final void setItemId(final Long pItemId) {
    this.itemId = pItemId;
  }

  /**
   * <p>Getter for itsPrice.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getItsPrice() {
    return this.itsPrice;
  }

  /**
   * <p>Setter for itsPrice.</p>
   * @param pItsPrice reference
   **/
  public final void setItsPrice(final BigDecimal pItsPrice) {
    this.itsPrice = pItsPrice;
  }

  /**
   * <p>Getter for itsTotal.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getItsTotal() {
    return this.itsTotal;
  }

  /**
   * <p>Setter for itsTotal.</p>
   * @param pItsTotal reference
   **/
  public final void setItsTotal(final BigDecimal pItsTotal) {
    this.itsTotal = pItsTotal;
  }

  /**
   * <p>Getter for itsQuantity.</p>
   * @return Integer
   **/
  public final Integer getItsQuantity() {
    return this.itsQuantity;
  }

  /**
   * <p>Setter for itsQuantity.</p>
   * @param pItsQuantity reference
   **/
  public final void setItsQuantity(final Integer pItsQuantity) {
    this.itsQuantity = pItsQuantity;
  }

  /**
   * <p>Getter for availableQuantity.</p>
   * @return Integer
   **/
  public final Integer getAvailableQuantity() {
    return this.availableQuantity;
  }

  /**
   * <p>Setter for availableQuantity.</p>
   * @param pAvailableQuantity reference
   **/
  public final void setAvailableQuantity(final Integer pAvailableQuantity) {
    this.availableQuantity = pAvailableQuantity;
  }
}
