package org.fitchfamily.mylocation;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

public class CellLocationFile {
    public static final String BY_SPEC = "mcc=? AND mnc=? AND lac=? AND cid=?";

    private static final String TABLE_CELLS = "cells";
    private static final String COL_LATITUDE = "latitude";
    private static final String COL_LONGITUDE = "longitude";
    private static final String COL_ACCURACY = "accuracy";
    private static final String COL_MCC = "mcc";
    private static final String COL_MNC = "mnc";
    private static final String COL_LAC = "lac";
    private static final String COL_CID = "cid";
    private final File file;
    private SQLiteDatabase database;

    private CellSpec lastCellSpec = new CellSpec(Radio.GSM, 0, 0, 0, 0);
    private LocationSample lastLocation = new LocationSample(0, 0, 0);

    public CellLocationFile(File file) {
        this.file = file;
    }

    private void assertDatabaseOpen() {
        if (database == null) {
            throw new IllegalArgumentException("You need to open the file first!");
        }
    }

    public void close() {
        if (database != null) {
            database.close();
            database = null;
        }
    }

    private boolean codeMatches(CellSpec cellSpec, int mcc, int mnc) {
        return ((mcc < 0) || (cellSpec.getMcc() == mcc)) && (mnc < 0 || cellSpec.getMnc() == mnc);
    }

    public boolean exists() {
        return file.exists() && file.canRead();
    }

    private String[] getBySpecArgs(CellSpec spec) {
        return new String[]{Integer.toString(spec.getMcc()), Integer.toString(spec.getMnc()),
                            Integer.toString(spec.getLac()), Integer.toString(spec.getCid())};
    }

    public LocationSample getLocation(CellSpec spec) {
        if (spec == null)
            return null;
        if ((this.file == null) ||
            (!file.exists()) ||
            (!file.canRead()))
            return null;
        assertDatabaseOpen();

        // Even simple query on large database is slow, see if this cell is same
        // as last, if so then return last result
        if (lastCellSpec.equals(spec)) {
            return lastLocation;
        }
        Cursor cursor =
                database.query(TABLE_CELLS, new String[]{COL_LATITUDE, COL_LONGITUDE, COL_ACCURACY},
                               BY_SPEC, getBySpecArgs(spec), null, null, null);
        if (cursor != null) {
            try {
                if (cursor.getCount() > 0) {
                    while (!cursor.isLast()) {
                        cursor.moveToNext();
                        lastLocation = new LocationSample(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LATITUDE)),
                                                          cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LONGITUDE)),
                                                          cursor.getDouble(cursor.getColumnIndexOrThrow(COL_ACCURACY)));
                        lastCellSpec = spec;
                        return lastLocation;
                    }
                }
            } finally {
                cursor.close();
            }
        }
        return null;
    }

    public String getPath() {
        return file.getAbsolutePath();
    }

    public void open() {
        if ((this.file != null) &&
            file.exists() &&
            file.canRead() &&
            (database == null)) {
            database = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        }
    }

}
