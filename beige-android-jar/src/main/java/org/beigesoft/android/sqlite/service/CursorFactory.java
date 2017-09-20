package org.beigesoft.android.sqlite.service;

/*
 * Copyright (c) 2015-2017 Beigesoft ™
 *
 * Licensed under the GNU General Public License (GPL), Version 2.0
 * (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteCursorDriver;

/**
 * <p>
 * Cursor factory for Android, copy from Android SDK.
 * </p>
 */
public class CursorFactory implements SQLiteDatabase.CursorFactory {

  @Override
  public final Cursor newCursor(final SQLiteDatabase pDb,
    final SQLiteCursorDriver pMasterQuery, final String pEditTable,
      final SQLiteQuery pQuery) {
    return new SQLiteCursor(pDb, pMasterQuery, pEditTable, pQuery) {

      @Override
      public boolean requery() {
          setSelectionArguments(new String[] {"2"});
          return super.requery();
      }
    };
  }
}
