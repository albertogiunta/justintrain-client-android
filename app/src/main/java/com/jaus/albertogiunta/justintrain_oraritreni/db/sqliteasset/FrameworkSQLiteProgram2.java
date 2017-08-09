package com.jaus.albertogiunta.justintrain_oraritreni.db.sqliteasset;

import android.arch.persistence.db.SupportSQLiteProgram;
import android.database.sqlite.SQLiteProgram;

/**
 * An wrapper around {@link SQLiteProgram} to implement {@link SupportSQLiteProgram} API.
 */
class FrameworkSQLiteProgram2 implements SupportSQLiteProgram {
    private final SQLiteProgram mDelegate;

    FrameworkSQLiteProgram2(SQLiteProgram delegate) {
        mDelegate = delegate;
    }

    @Override
    public void bindNull(int index) {
        mDelegate.bindNull(index);
    }

    @Override
    public void bindLong(int index, long value) {
        mDelegate.bindLong(index, value);
    }

    @Override
    public void bindDouble(int index, double value) {
        mDelegate.bindDouble(index, value);
    }

    @Override
    public void bindString(int index, String value) {
        mDelegate.bindString(index, value);
    }

    @Override
    public void bindBlob(int index, byte[] value) {
        mDelegate.bindBlob(index, value);
    }

    @Override
    public void clearBindings() {
        mDelegate.clearBindings();
    }
}
