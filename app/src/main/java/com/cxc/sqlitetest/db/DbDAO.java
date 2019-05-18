package com.cxc.sqlitetest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.Toast;

import com.cxc.sqlitetest.R;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class DbDAO {
    private static final String TAG = "DbDAO";

    private static String[] DB_COLUMNS = new String[]{"Id", "Name", "Price", "Country"};

    private DbOpenHelper dbOpenHelper;
    private Context context;

    public DbDAO(Context context) {
        this.context=context;
        dbOpenHelper=new DbOpenHelper(context);
    }

    public void initTable() {
        SQLiteDatabase db = null;
        try {
            db = dbOpenHelper.getWritableDatabase();
            db.beginTransaction();

            db.execSQL("insert into " + DbOpenHelper.TABLE_NAME + " (Id, Name, Price, Country) values (1, 'cxc', 100, 'china')");
            db.execSQL("insert into " + DbOpenHelper.TABLE_NAME + " (Id, Name, Price, Country) values (2, 'yxy', 100, 'uk')");

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public List<Db> getAllData() {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbOpenHelper.getReadableDatabase();
            cursor = db.query(DbOpenHelper.TABLE_NAME, DB_COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                List<Db> dbList = new ArrayList<>(cursor.getCount());
                while (cursor.moveToNext()) {
                    dbList.add(parseDb(cursor));
                }
                return dbList;
            }
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return Collections.emptyList();
    }

    public boolean isExist() {
        int count = 0;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbOpenHelper.getReadableDatabase();
            cursor = db.query(DbOpenHelper.TABLE_NAME, new String[]{"count(Id)"}, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            if (count > 0) {
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return false;
    }

    public void executeSQL(String sql) {
        SQLiteDatabase db = null;
        try {
            db = dbOpenHelper.getWritableDatabase();
            db.beginTransaction();
            db.execSQL(sql);
            db.setTransactionSuccessful();
            Toast.makeText(context, R.string.strSuccessSql, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(context, R.string.strErrorSql, Toast.LENGTH_SHORT).show();
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public boolean insertData() {
        SQLiteDatabase db = null;
        try {
            db = dbOpenHelper.getWritableDatabase();
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put("Id", 12);
            contentValues.put("Name", "as");
            contentValues.put("Price", 234);
            contentValues.put("Country", "island");
            db.insertOrThrow(DbOpenHelper.TABLE_NAME, null, contentValues);
            db.setTransactionSuccessful();

            return true;
        } catch (SQLiteConstraintException e) {
            Toast.makeText(context, "主键重复", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }

    public boolean deleteData() {
        SQLiteDatabase db = null;
        try {
            db = dbOpenHelper.getWritableDatabase();
            db.beginTransaction();
            db.delete(DbOpenHelper.TABLE_NAME, "Id=?", new String[]{String.valueOf(7)});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }

    public boolean updateData() {
        SQLiteDatabase db = null;
        try {
            db = dbOpenHelper.getWritableDatabase();
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put("Price", 800);
            db.update(DbOpenHelper.TABLE_NAME, contentValues, "Id=?", new String[]{String.valueOf(6)});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }


    private Db parseDb(Cursor cursor) {
        Db db = new Db();
        db.setId(cursor.getInt(cursor.getColumnIndex("Id")));
        db.setName(cursor.getString(cursor.getColumnIndex("Name")));
        db.setPrice(cursor.getInt(cursor.getColumnIndex("Price")));
        db.setCountry(cursor.getString(cursor.getColumnIndex("Country")));
        return db;
    }


}
