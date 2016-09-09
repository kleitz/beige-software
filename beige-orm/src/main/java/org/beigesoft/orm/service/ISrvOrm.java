package org.beigesoft.orm.service;

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

/**
 * <p>Abstraction of ORM service.
 * According specification Beige-ORM version #2.
 * It evaluate tables descriptors
 * from properties files. It generates DDL and DML queries.
 * It perform SQL queries through database service.
 * It has no transaction logic.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public interface ISrvOrm<RS> {

  /**
   * <p>ORM properties directory.</p>
   **/
  String ORM_PROP_DIRECTORY = "beige-orm";

  /**
   * <p>Name of file with SQL query to chech table existence name.</p>
   **/
  String TABLE_EXISTENCE_QUERY_FILE_NAME = "checkTableExist.sql";

  /**
   * <p>Name of table parameter in SQL query to chech table existence name.</p>
   **/
  String TABLE_PARAM_NAME_IN_EXISTENCE_QUERY = ":tableName";

  /**
   * <p>Version name.</p>
   **/
  String VERSION_NAME = "itsVersion";

  /**
   * <p>Unpersistable old version name for WHERE.</p>
   **/
  String VERSIONOLD_NAME = "itsVersionOld";

  /**
   * <p>Word that point to current dir System.getProperty("user.dir").</p>
   **/
  String WORD_CURRENT_DIR = "#currentDir#";

  /**
   * <p>Word that point to current parent dir
   * System.getProperty("user.dir").parent.</p>
   **/
  String WORD_CURRENT_PARENT_DIR = "#currentParentDir#";

  /**
   * <p>Version algorithm = increment.</p>
   **/
  Integer VERSION_ALG_INCREMENT = 0;

  /**
   * <p>Version algorithm <b>version=time when entity has been changed</b>.</p>
   **/
  Integer VERSION_ALG_CHANGED_TIME = 1;

  /**
   * <p>Create entity.</p>
   * @param <T> entity type
   * @param pEntityClass entity class
   * @return entity instance
   * @throws Exception - an exception
   **/
  <T> T createEntity(Class<T> pEntityClass) throws Exception;

    /**
   * <p>Create entity with NON-COMPLEX ID with its owner e.g. invoice line
   * for invoice.</p>
   * @param <T> entity type
   * @param pEntityClass entity class
   * @param pEntityOwnerClass entity owner class
   * @param idEntityOwner entity owner ID
   * @return entity instance
   * @throws Exception - an exception
   **/
  <T> T createEntityWithOwner(Class<T> pEntityClass,
    Class<?> pEntityOwnerClass, Object idEntityOwner) throws Exception;

  /**
   * <p>Refresh entity from DB by given entity with NON-COMPLEX ID.</p>
   * @param <T> entity type
   * @param pEntity entity
   * @return entity or null
   * @throws Exception - an exception
   **/
  <T> T retrieveEntity(T pEntity) throws Exception;

  /**
   * <p>Retrieve entity with NON-COMPLEX ID from DB by given ID.</p>
   * @param <T> entity type
   * @param pEntityClass entity class
   * @param pId ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  <T> T retrieveEntityById(Class<T> pEntityClass,
    Object pId) throws Exception;

  /**
   * <p>Retrieve copy of entity with NON-COMPLEX ID from DB by given ID.</p>
   * @param <T> entity type
   * @param pEntityClass entity class
   * @param pId ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  <T> T retrieveCopyEntity(Class<T> pEntityClass,
    Object pId) throws Exception;

  /**
   * <p>Retrieve entity from DB.</p>
   * @param <T> entity type
   * @param pEntityClass entity class
   * @param pQuery SELECT statement
   * @return entity or null
   * @throws Exception - an exception
   **/
  <T> T retrieveEntity(
    Class<T> pEntityClass, String pQuery) throws Exception;

  /**
   * <p>Retrieve entity from DB by given query conditions.
   * The first record in recordset will be used.</p>
   * @param <T> entity type
   * @param pEntityClass entity class
   * @param pQueryConditions Not NULL e.g. "where name='U1' ORDER BY id"
   * @return entity or null
   * @throws Exception - an exception
   **/
  <T> T retrieveEntityWithConditions(
    Class<T> pEntityClass, String pQueryConditions) throws Exception;

  /**
   * <p>Insert entity into DB.</p>
   * @param <T> entity type
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  <T> void insertEntity(T pEntity) throws Exception;

  /**
   * <p>Update entity with NON-COMPLEX ID in DB.</p>
   * @param <T> entity type
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  <T> void updateEntity(T pEntity) throws Exception;

  /**
   * <p>Update entity with condition, e.g. complex ID.</p>
   * @param <T> entity type
   * @param pEntity entity
   * @param pWhere Where Not Null e.g. "WAREHOUSESITE=1 and PRODUCT=1"
   * @throws Exception - an exception
   **/
  <T> void updateEntityWhere(T pEntity, String pWhere) throws Exception;

  /**
   * <p>Delete entity with NON-COMPLEX ID from DB.</p>
   * @param <T> entity type
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  <T> void deleteEntity(T pEntity) throws Exception;

  /**
   * <p>Delete entity with NON-COMPLEX ID from DB by given ID.</p>
   * @param <T> entity type
   * @param pEntityClass entity class
   * @param pId ID
   * @throws Exception - an exception
   **/
  <T> void deleteEntity(Class<T> pEntityClass,
    Object pId) throws Exception;

  /**
   * <p>Delete entity with condition e.g. complex ID.</p>
   * @param <T> entity type
   * @param pEntityClass entity class
   * @param pWhere Not Null e.g. "WAREHOUSESITE=1 and PRODUCT=1"
   * @throws Exception - an exception
   **/
  <T> void deleteEntityWhere(Class<T> pEntityClass,
    String pWhere) throws Exception;

  /**
   * <p>Retrieve a list of all entities.</p>
   * @param <T> - type of business object,
   * @param pEntityClass entity class
   * @return list of all business objects or empty list, not null
   * @throws Exception - an exception
   */
  <T> List<T> retrieveList(
    Class<T> pEntityClass) throws Exception;

  /**
   * <p>Retrieve a list of entities.</p>
   * @param <T> - type of business object
   * @param pEntityClass entity class
   * @param pQueryConditions Not NULL e.g. "where name='U1' ORDER BY id"
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  <T> List<T> retrieveListWithConditions(Class<T> pEntityClass,
    String pQueryConditions) throws Exception;

  /**
   * <p>Retrieve a page of entities.</p>
   * @param <T> - type of business object,
   * @param pEntityClass entity class
   * @param pFirst number of the first record
   * @param pPageSize page size (max records)
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  <T> List<T> retrievePage(Class<T> pEntityClass,
      Integer pFirst, Integer pPageSize) throws Exception;

  /**
   * <p>Retrieve a page of entities.</p>
   * @param <T> - type of business object,
   * @param pEntityClass entity class
   * @param pQueryConditions not null e.g. "WHERE name='U1' ORDER BY id"
   * @param pFirst number of the first record
   * @param pPageSize page size (max records)
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  <T> List<T> retrievePageWithConditions(Class<T> pEntityClass,
    String pQueryConditions,
      Integer pFirst, Integer pPageSize) throws Exception;

  /**
   * <p>Calculate total rows for pagination.</p>
   * @param <T> - type of business object,
   * @param pEntityClass entity class
   * @param pWhere not null e.g. "ITSID > 33"
   * @return Integer row count
   * @throws Exception - an exception
   */
  <T> Integer evalRowCountWhere(Class<T> pEntityClass,
    String pWhere) throws Exception;

  /**
   * <p>Calculate total rows for pagination.</p>
   * @param <T> - type of business object,
   * @param pEntityClass entity class
   * @return Integer row count
   * @throws Exception - an exception
   */
  <T> Integer evalRowCount(Class<T> pEntityClass) throws Exception;

  /**
   * <p>Retrieve entity's owned lists (e.g. invoice lines for invoice).</p>
   * @param <T> - type of entity owned with NON-COMPLEX ID
   * @param pEntityClass owned e.g. org.model.InvoiceLine.class
   * @param pEntityOwner - Entity Owner e.g. an invoice
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  <T> List<T> retrieveEntityOwnedlist(Class<T> pEntityClass,
    Object pEntityOwner) throws Exception;

  /**
   * <p>Retrieve entity's owned lists (e.g. invoice lines for invoice).</p>
   * @param <T> - type of entity owned
   * @param pEntityClass owned e.g. org.model.InvoiceLine.class
   * @param pOwnerClass owner e.g. org.model.Invoice.class
   * @param pEntityOwnerId - Entity Owner ID e.g. an invoice ID
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  <T> List<T> retrieveEntityOwnedlist(Class<T> pEntityClass,
    Class<?> pOwnerClass, Object pEntityOwnerId) throws Exception;

  //to srv-database delegators:
  /**
   * <p>Getter for database ID.
   * Any database mist has ID, int is suitable type for that cause
   * its range is enough and it's faster than String.</p>
   * @return ID database
   **/
  int getIdDatabase();
}
