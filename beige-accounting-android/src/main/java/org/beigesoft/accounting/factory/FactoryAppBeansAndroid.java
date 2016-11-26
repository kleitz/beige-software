package org.beigesoft.accounting.factory;

/*
 * Beigesoft ™
 *
 * Licensed under the Apache License, Version 2.0
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import java.io.File;

import android.os.Environment;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.beigesoft.web.factory.AFactoryAppBeans;
import org.beigesoft.orm.service.SrvOrmAndroid;
import org.beigesoft.log.ILogger;
import org.beigesoft.exception.ExceptionWithCode;

import org.beigesoft.android.log.Logger;
import org.beigesoft.replicator.service.PrepareDbAfterGetCopy;
import org.beigesoft.android.sqlite.service.CursorFactory;
import org.beigesoft.android.sqlite.service.SrvDatabase;
import org.beigesoft.android.sqlite.service.SrvRecordRetriever;

/**
 * <p>
 * Application beans factory for Android.
 * </p>
 *
 * @author Yury Demidenko
 */
public class FactoryAppBeansAndroid extends AFactoryAppBeans<Cursor> {

  /**
   * <p>Service that  release AppFactory beans.</p>
   */
  private PrepareDbAfterGetCopy prepareDbAfterGetCopy;

  /**
   * <p>Android context.</p>
   **/
  private Context context;

  /**
   * <p>Logger.</p>
   */
  private ILogger logger;

  /**
   * <p>Record retriever service.</p>
   **/
  private SrvRecordRetriever srvRecordRetriever;

  /**
   * <p>Database service.</p>
   **/
  private SrvDatabase srvDatabase;

  /**
   * <p>Cursor factory.</p>
   **/
  private CursorFactory cursorFactory;

  /**
   * <p>Database manager for Android.</p>
   **/
  private MngDatabaseAndroid mngDatabaseAndroid;

  /**
   * <p>Release beans (memory). This is "memory friendly" factory</p>
   * @throws Exception - an exception
   */
  public final synchronized void releaseBeans() throws Exception {
    if (getFactoryOverBeans() != null) {
      getFactoryOverBeans().releaseBeans();
    }
    this.logger = null;
    this.srvRecordRetriever = null;
    if (this.srvDatabase != null) {
      try {
        this.srvDatabase.releaseResources();
      } catch (Exception e) {
        e.printStackTrace();
      }
      this.srvDatabase = null;
    }
    this.prepareDbAfterGetCopy = null;
    this.cursorFactory = null;
    this.mngDatabaseAndroid = null;
    setUtlReflection(null);
    setUtlProperties(null);
    setUtlJsp(null);
    setSrvI18n(null);
    setMngUvdSettings(null);
    setSrvWebEntity(null);
    setSrvPage(null);
    setMngSoftware(null);
    setSrvOrm(null);
    setSrvWebMvc(null);
    setHlpInsertUpdate(null);
    getEntitiesMap().clear();
    getBeansMap().clear();
  }

  /**
   * <p>Get Service that prepare Database after full import
   * in lazy mode.</p>
   * @return IDelegator - preparator Database after full import.
   * @throws Exception - an exception
   */
  @Override
  public final synchronized PrepareDbAfterGetCopy
    lazyGetPrepareDbAfterFullImport() throws Exception {
    if (this.prepareDbAfterGetCopy == null) {
      this.prepareDbAfterGetCopy = new PrepareDbAfterGetCopy();
      this.prepareDbAfterGetCopy.setLogger(lazyGetLogger());
      this.prepareDbAfterGetCopy.setFactoryAppBeans(this);
      lazyGetLogger().info(FactoryAppBeansAndroid.class,
        "PrepareDbAfterGetCopy has been created.");
    }
    return this.prepareDbAfterGetCopy;
  }

  /**
   * <p>Get other bean in lazy mode (if bean is null then initialize it).</p>
   * @param pBeanName - bean name
   * @return Object - requested bean
   * @throws Exception - an exception
   */
  @Override
  public final synchronized Object lazyGetOtherBean(
    final String pBeanName) throws Exception {
    if ("CursorFactory".equals(pBeanName)) {
      return lazyGetCursorFactory();
    } else if ("IMngDatabaseExt".equals(pBeanName)) {
      return lazyGetMngDatabaseAndroid();
    } else {
      return null;
    }
  }

  /**
   * <p>Instantiate ORM  service.</p>
   * @return SrvOrmAndroid - ORM  service
   */
  @Override
  public final synchronized SrvOrmAndroid<Cursor> instantiateSrvOrm() {
    SrvOrmAndroid<Cursor> srvOrmAndroid = new SrvOrmAndroid<Cursor>();
    srvOrmAndroid.setIsNeedsToSqlEscape(false); //!!! this is important
    return srvOrmAndroid;
  }

  /**
   * <p>Get SrvDatabase in lazy mode.</p>
   * @return SrvDatabase - SrvDatabase
   * @throws Exception - an exception
   */
  @Override
  public final synchronized ILogger lazyGetLogger() throws Exception {
    if (this.logger == null) {
      this.logger = new Logger();
      this.logger.setIsShowDebugMessages(getIsShowDebugMessages());
    }
    return this.logger;
  }

  /**
   * <p>Get SrvDatabase in lazy mode.</p>
   * @return SrvDatabase - SrvDatabase
   * @throws Exception - an exception
   */
  @Override
  public final synchronized SrvDatabase lazyGetSrvDatabase() throws Exception {
    if (this.srvDatabase == null) {
      this.srvDatabase = new SrvDatabase();
      SQLiteDatabase db = this.context.openOrCreateDatabase(getDatabaseName(),
       Context.MODE_PRIVATE, lazyGetCursorFactory());
      this.srvDatabase.setSrvRecordRetriever(lazyGetSrvRecordRetriever());
      this.srvDatabase.setSqliteDatabase(db);
      this.srvDatabase.setLogger(lazyGetLogger());
    }
    return this.srvDatabase;
  }

  /**
   * <p>Get MngDatabaseAndroid in lazy mode.</p>
   * @return MngDatabaseAndroid - MngDatabaseAndroid
   * @throws Exception - an exception
   */
  public final synchronized MngDatabaseAndroid
    lazyGetMngDatabaseAndroid() throws Exception {
    if (this.mngDatabaseAndroid == null) {
      this.mngDatabaseAndroid = new MngDatabaseAndroid();
      this.mngDatabaseAndroid.setFactoryAppBeansAndroid(this);
      ContextWrapper cw = new ContextWrapper(this.context);
      this.mngDatabaseAndroid.setDatabaseDir(cw.getFilesDir()
        .getAbsolutePath().replace("files", "databases"));
      File bkDir = new File(Environment.getExternalStorageDirectory()
        .getAbsolutePath() + File.separator + "BeigeAccountingBackup");
      if (!bkDir.exists() && !bkDir.mkdirs()) {
        throw new ExceptionWithCode(ExceptionWithCode.SOMETHING_WRONG,
          "Can't create dir: " + bkDir);
      }
      this.mngDatabaseAndroid.setBackupDir(bkDir.getAbsolutePath());
    }
    return this.mngDatabaseAndroid;
  }

  /**
   * <p>Get SrvRecordRetriever in lazy mode.</p>
   * @return SrvRecordRetriever - SrvRecordRetriever
   */
  @Override
  public final synchronized SrvRecordRetriever lazyGetSrvRecordRetriever() {
    if (this.srvRecordRetriever == null) {
      this.srvRecordRetriever = new SrvRecordRetriever();
    }
    return this.srvRecordRetriever;
  }

  /**
   * <p>Get lazy for cursorFactory.</p>
   * @return CursorFactory
   **/
  public final synchronized CursorFactory lazyGetCursorFactory() {
    if (this.cursorFactory == null) {
      this.cursorFactory = new CursorFactory();
    }
    return this.cursorFactory;
  }

  /**
   * <p>Geter for context.</p>
   * @return Context
   **/
  public final synchronized Context getContext() {
    return this.context;
  }

  /**
   * <p>Setter for context.</p>
   * @param pContext reference
   **/
  public final synchronized void setContext(final Context pContext) {
    this.context = pContext;
  }

  /**
   * <p>Setter for cursorFactory.</p>
   * @param pCursorFactory reference
   **/
  public final synchronized void setCursorFactory(
    final CursorFactory pCursorFactory) {
    this.cursorFactory = pCursorFactory;
  }

  //To insure if it's null
  /**
   * <p>Getter for srvDatabase.</p>
   * @return SrvDatabase
   **/
  public final synchronized SrvDatabase getSrvDatabase() {
    return this.srvDatabase;
  }
}
