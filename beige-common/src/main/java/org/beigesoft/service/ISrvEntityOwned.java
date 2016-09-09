package org.beigesoft.service;

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
import java.util.Map;

import org.beigesoft.model.IHasId;

/**
 * <p>Abstraction of business service for persistable entity
 * that owned by an owner entity (one to one or one to many composition),
 * e.g. SalesInvoiceLine(many) for SalesInvoice(to one), and
 * owned can't exist(composition) without owner.
 * It usually delegate to ORM service for persist logic.
 * It additionally has security and other business logic.
 * It can has transaction logic.
 * It uses abstract pAddParam for additional
 * communication between business and presentation layers.</p>
 *
 * @param <T> entity type
 * @param <O> owner entity type
 * @author Yury Demidenko
 */
public interface ISrvEntityOwned<T extends IHasId<?>, O extends IHasId<?>>
  extends ISrvEntity<T> {

  /**
   * <p>Create entity with its owner e.g. invoice line
   * for invoice.</p>
   * @param pAddParam additional param
   * @param pIdEntityOwner entity owner ID
   * @return entity instance
   * @throws Exception - an exception
   **/
  T createEntityWithOwnerById(Map<String, ?> pAddParam,
    Object pIdEntityOwner) throws Exception;

  /**
   * <p>Create entity with its owner e.g. invoice line
   * for invoice.</p>
   * @param pAddParam additional param
   * @param pEntityOwner owner
   * @return entity instance
   * @throws Exception - an exception
   **/
  T createEntityWithOwner(Map<String, ?> pAddParam,
    O pEntityOwner) throws Exception;

  /**
   * <p>Retrieve owned list of entities for owner.
   * E.g. invoices lines for invoice</p>
   * @param pAddParam additional param
   * @param pIdEntityOwner ID owner
   * @return owned list of business objects
   * @throws Exception - an exception
   */
  List<T> retrieveOwnedListById(Map<String, ?> pAddParam,
    Object pIdEntityOwner) throws Exception;

  /**
   * <p>Retrieve owned list of entities for owner.
   * E.g. invoices lines for invoice</p>
   * @param pAddParam additional param
   * @param pEntityOwner owner
   * @return owned list of business objects
   * @throws Exception - an exception
   */
  List<T> retrieveOwnedList(Map<String, ?> pAddParam,
    O pEntityOwner) throws Exception;
}
