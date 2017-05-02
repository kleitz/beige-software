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
import java.util.Map;

/**
 * <p>Abstraction of ORM service.
 * According specification Beige-ORM version #4.
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
   * <p>Refresh entity from DB.</p>
   * @param <T> entity type
   * @param pAddParam additional param
   * @param pEntity entity
   * @return entity or null
   * @throws Exception - an exception
   **/
  <T> T retrieveEntity(Map<String, Object> pAddParam,
    T pEntity) throws Exception;

  /**
   * <p>Refresh entity from DB by its ID.</p>
   * @param <T> entity type
   * @param pAddParam additional param
   * @param pEntityClass entity class
   * @param pItsId entity ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  <T> T retrieveEntityById(Map<String, Object> pAddParam,
    Class<T> pEntityClass, Object pItsId) throws Exception;

  /**
   * <p>Retrieve entity from DB by given query conditions.
   * The first record in record-set will be returned.</p>
   * @param <T> entity type
   * @param pAddParam additional param
   * @param pEntityClass entity class
   * @param pQueryConditions Not NULL e.g. "where name='U1' ORDER BY id"
   * or "" that means without filter/order
   * @return entity or null
   * @throws Exception - an exception
   **/
  <T> T retrieveEntityWithConditions(Map<String, Object> pAddParam,
    Class<T> pEntityClass, String pQueryConditions) throws Exception;

  /**
   * <p>Insert entity into DB.</p>
   * @param <T> entity type
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  <T> void insertEntity(Map<String, Object> pAddParam,
    T pEntity) throws Exception;

  /**
   * <p>Update entity with ID in DB.</p>
   * @param <T> entity type
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  <T> void updateEntity(Map<String, Object> pAddParam,
    T pEntity) throws Exception;

  /**
   * <p>Delete entity with ID from DB.</p>
   * @param <T> entity type
   * @param pAddParam additional param
   * @param pEntity entity
   * @throws Exception - an exception
   **/
  <T> void deleteEntity(Map<String, Object> pAddParam,
    T pEntity) throws Exception;

  /**
   * <p>Delete entity(is) with condition.</p>
   * @param <T> entity type
   * @param pAddParam additional param
   * @param pEntityClass entity class
   * @param pWhere Not Null e.g. "WAREHOUSESITE=1 and PRODUCT=1"
   * @throws Exception - an exception
   **/
  <T> void deleteEntityWhere(Map<String, Object> pAddParam,
    Class<T> pEntityClass, String pWhere) throws Exception;

  /**
   * <p>Retrieve a list of all entities.</p>
   * @param <T> - type of business object,
   * @param pAddParam additional param
   * @param pEntityClass entity class
   * @return list of all business objects or empty list, not null
   * @throws Exception - an exception
   */
  <T> List<T> retrieveList(Map<String, Object> pAddParam,
    Class<T> pEntityClass) throws Exception;

  /**
   * <p>Retrieve a list of entities.</p>
   * @param <T> - type of business object
   * @param pAddParam additional param
   * @param pEntityClass entity class
   * @param pQueryConditions Not NULL e.g. "where name='U1' ORDER BY id"
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  <T> List<T> retrieveListWithConditions(Map<String, Object> pAddParam,
    Class<T> pEntityClass, String pQueryConditions) throws Exception;

  /**
   * <p>Retrieve a list of entities by complex query that may contain
   * additional joins and filters, see Beige-Webstore for example.</p>
   * @param <T> - type of business object
   * @param pAddParam additional param, e.g. list of needed fields
   * @param pEntityClass entity class
   * @param pQuery Not NULL complex query
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  <T> List<T> retrieveListByQuery(Map<String, Object> pAddParam,
    Class<T> pEntityClass, String pQuery) throws Exception;

  /**
   * <p>Retrieve entity's lists for field that used as filter
   * (e.g. invoice lines for invoice).</p>
   * @param <T> - type of entity owned
   * @param pAddParam additional param
   * @param pEntity entity InvoiceLine
   * @param pFieldFor - name of field to be filter, e.g. "invoice"
   * to retrieve invoices lines for invoice
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  <T> List<T> retrieveListForField(Map<String, Object> pAddParam, T pEntity,
    String pFieldFor) throws Exception;

  /**
   * <p>Retrieve a page of entities.</p>
   * @param <T> - type of business object,
   * @param pAddParam additional param
   * @param pEntityClass entity class
   * @param pFirst number of the first record (from 0)
   * @param pPageSize page size (max records)
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  <T> List<T> retrievePage(Map<String, Object> pAddParam,
    Class<T> pEntityClass, Integer pFirst, Integer pPageSize) throws Exception;

  /**
   * <p>Retrieve a page of entities.</p>
   * @param <T> - type of business object,
   * @param pAddParam additional param
   * @param pEntityClass entity class
   * @param pQueryConditions not null e.g. "WHERE name='U1' ORDER BY id"
   * @param pFirst number of the first record (from 0)
   * @param pPageSize page size (max records)
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  <T> List<T> retrievePageWithConditions(Map<String, Object> pAddParam,
    Class<T> pEntityClass, String pQueryConditions,
      Integer pFirst, Integer pPageSize) throws Exception;

  /**
   * <p>Retrieve a page of entities by given complex query.
   * For example it used to retrieve page ItemInList to sell in Beige-Webstore
   * by complex query what may consist of joints to filtered goods/services,
   * it also may has not all fields e.g. omit unused auctioning fields for
   * performance advantage.</p>
   * @param <T> - type of business object,
   * @param pAddParam additional param, e.g. set of needed fields names.
   * @param pEntityClass entity class
   * @param pQuery not null complex query without page conditions
   * @param pFirst number of the first record (from 0)
   * @param pPageSize page size (max records)
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  <T> List<T> retrievePageByQuery(Map<String, Object> pAddParam,
    Class<T> pEntityClass, String pQuery,
      Integer pFirst, Integer pPageSize) throws Exception;

  /**
   * <p>Evaluate common(without WHERE) SQL SELECT
   * statement for entity type. It's used externally
   * to make more complex query with additional joints and filters.</p>
   * @param <T> entity type
   * @param pAddParam additional param, e.g. set of needed fields names,
   * deep of foreign classes, etc.
   * @param pEntityClass entity class
   * @return String SQL DML query
   * @throws Exception - an exception
   **/
  <T> String evalSqlSelect(Map<String, Object> pAddParam,
    Class<T> pEntityClass) throws Exception;

  /**
   * <p>Calculate total rows for pagination.</p>
   * @param <T> - type of business object,
   * @param pAddParam additional param
   * @param pEntityClass entity class
   * @param pWhere not null e.g. "ITSID > 33"
   * @return Integer row count
   * @throws Exception - an exception
   */
  <T> Integer evalRowCountWhere(Map<String, Object> pAddParam,
    Class<T> pEntityClass, String pWhere) throws Exception;

  /**
   * <p>Calculate total rows for pagination.</p>
   * @param <T> - type of business object,
   * @param pAddParam additional param
   * @param pEntityClass entity class
   * @return Integer row count
   * @throws Exception - an exception
   */
  <T> Integer evalRowCount(Map<String, Object> pAddParam,
    Class<T> pEntityClass) throws Exception;

  /**
   * <p>Calculate total rows for pagination by custom query.</p>
   * @param <T> - type of business object,
   * @param pAddParam additional param
   * @param pEntityClass entity class
   * @param pQuery not null custom query with named TOTALROWS
   * @return Integer row count
   * @throws Exception - an exception
   */
  <T> Integer evalRowCountByQuery(Map<String, Object> pAddParam,
    Class<T> pEntityClass, String pQuery) throws Exception;

  /**
   * <p>Getter for new database ID. It used when new database has been created.
   * Any database mist has ID, int is suitable type for that cause
   * its range is enough and it's faster than String.</p>
   * @return ID for new database
   **/
  int getNewDatabaseId();

  /**
   * <p>Setter for new database ID.
   * Any database mist has ID, int is suitable type for that cause
   * its range is enough and it's faster than String.</p>
   * @param pNewDatabaseId ID for new database
   **/
  void setNewDatabaseId(int pNewDatabaseId);

  //to srv-database delegators:
  /**
   * <p>Getter for database ID.
   * Any database mist has ID, int is suitable type for that cause
   * its range is enough and it's faster than String.</p>
   * @return ID database
   **/
  int getIdDatabase();
}
