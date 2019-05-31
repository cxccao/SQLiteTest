package com.cxc.sqlitetest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;
import com.cxc.sqlitetest.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class DbDAO {
    private static final String TAG = "DbDAO";

    private static String[] DB_COLUMNS = new String[]{"Id", "Name", "Price", "Country"};

    private DbOpenHelper dbOpenHelper;
    private Context context;

    DbDAO(Context context) {
        this.context = context;
        dbOpenHelper = new DbOpenHelper(context);
    }

    // 初始化表
    void initTable() {
        SQLiteDatabase db = null;
        try {
            db = dbOpenHelper.getWritableDatabase();
            db.beginTransaction();

            db.execSQL("insert into " + DbOpenHelper.TABLE_NAME + " (Id, Name, Price, Country) values (1, 'cxc', 100, 'china')");
            db.execSQL("insert into " + DbOpenHelper.TABLE_NAME + " (Id, Name, Price, Country) values (2, 'yxy', 200, 'china')");
            db.execSQL("insert into " + DbOpenHelper.TABLE_NAME + " (Id, Name, Price, Country) values (3, 'lxl', 300, 'uk')");
            db.execSQL("insert into " + DbOpenHelper.TABLE_NAME + " (Id, Name, Price, Country) values (4, 'zxz', 400, 'uk')");
            db.execSQL("insert into " + DbOpenHelper.TABLE_NAME + " (Id, Name, Price, Country) values (5, 'cxk', 999, 'island')");

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

    // 获取所有数据
    List<Db> getAllData() {
        return getData(null, null);
    }

    // 获取按名字归类的数据
    List<Db> getDataByName(String name) {
        return getData("Name = ?", new String[]{name});
    }

    List<Db> getDataByIntervalPrice(String[] interval) {
        String selection = "Price between ? and ?";
        return getData(selection, interval);
    }

    // 获取表中数据
    private List<Db> getData(String selection, String[] strings) {

        try (SQLiteDatabase db = dbOpenHelper.getReadableDatabase(); Cursor cursor = db.query(DbOpenHelper.TABLE_NAME, DB_COLUMNS, selection, strings, null, null, null)) {
            if (cursor.getCount() > 0) {
                List<Db> dbList = new ArrayList<>(cursor.getCount());
                while (cursor.moveToNext()) {
                    dbList.add(parseDb(cursor));
                }
                return dbList;
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
        return Collections.emptyList();
    }

    // 判断表中是否有数据
    boolean isExist() {
        int count = 0;
        try (SQLiteDatabase db = dbOpenHelper.getReadableDatabase(); Cursor cursor = db.query(DbOpenHelper.TABLE_NAME, new String[]{"count(Id)"}, null, null, null, null, null)) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            if (count > 0) {
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
        return false;
    }

    // 执行insert、delete、update和CREATE TABLE之类有更改行为的SQL语句
    void executeSQL(String sql) {
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

    // 插入一条数据
    void insertData(int Id, String Name, int Price, String Country) {
        SQLiteDatabase db = null;
        try {
            db = dbOpenHelper.getWritableDatabase();
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put("Id", Id);
            contentValues.put("Name", Name);
            contentValues.put("Price", Price);
            contentValues.put("Country", Country);
            db.insertOrThrow(DbOpenHelper.TABLE_NAME, null, contentValues);
            db.setTransactionSuccessful();

        } catch (SQLiteConstraintException e) {
            Toast.makeText(context, "主键重复", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    // 删除一条数据
    public void deleteData(int id) {
        SQLiteDatabase db = null;
        try {
            db = dbOpenHelper.getWritableDatabase();
            db.beginTransaction();
            db.delete(DbOpenHelper.TABLE_NAME, "Id=?", new String[]{String.valueOf(id)});
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

    // 修改一条数据
    void updateData(int Id, String Name, int Price, String Country) {
        SQLiteDatabase db = null;
        try {
            db = dbOpenHelper.getWritableDatabase();
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put("Name", Name);
            contentValues.put("Price", Price);
            contentValues.put("Country", Country);
            db.update(DbOpenHelper.TABLE_NAME, contentValues, "Id=?", new String[]{String.valueOf(Id)});
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

    // 获取所有Id
    List<String> getAllId() {
        try (SQLiteDatabase db = dbOpenHelper.getReadableDatabase(); Cursor cursor = db.query(DbOpenHelper.TABLE_NAME, new String[]{"Id"}, null, null, null, null, null)) {
            if (cursor.getCount() > 0) {
                List<String> idList = new ArrayList<>(cursor.getCount());
                while (cursor.moveToNext()) {
                    idList.add(String.valueOf(cursor.getInt(cursor.getColumnIndex("Id"))));
                }
                return idList;
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
        return Collections.emptyList();
    }

    // 获取所有提供商姓名
    List<String> getAllName() {
        try (SQLiteDatabase db = dbOpenHelper.getReadableDatabase(); Cursor cursor = db.query(DbOpenHelper.TABLE_NAME, new String[]{"Name"}, null, null, "Name", null, null)) {
            if (cursor.getCount() > 0) {
                List<String> nameList = new ArrayList<>(cursor.getCount());
                while (cursor.moveToNext()) {
                    nameList.add(cursor.getString(0));
                }
                return nameList;
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
        return Collections.emptyList();
    }

    // 将查询到的数据转换Db类
    private Db parseDb(Cursor cursor) {
        Db db = new Db();
        db.setId(cursor.getInt(cursor.getColumnIndex("Id")));
        db.setName(cursor.getString(cursor.getColumnIndex("Name")));
        db.setPrice(cursor.getInt(cursor.getColumnIndex("Price")));
        db.setCountry(cursor.getString(cursor.getColumnIndex("Country")));
        return db;
    }
}
