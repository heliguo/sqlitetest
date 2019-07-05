package com.example.sqlitetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Environment;

import org.json.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CommonSqlHelper extends SQLiteOpenHelper {

    private static final String CREATE_BOOK = "create table if not exists LoginInfo("
            + "USERNAME VARCHAR2(255),"
            + "USERCODE VARCHAR2(255),"
            + "USERID VARCHAR2(255),"
            + "PASSWORD VARCHAR2(255),"
            + "GENDER VARCHAR2(255),"
            + "DEPTNAME VARCHAR2(255),"
            + "DEPTID VARCHAR2(255),"
            + "IDCARD VARCHAR2(255))";

    public static        String  DATABASE_NAME = "TJ_ZCXT";
    private              Context context;

    //必须有这个构造方法
    public CommonSqlHelper(Context context, String name,
                           CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, version);
        this.context = context;
    }


    public int insert(String table, ContentValues values) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
        } catch (Exception localException1) {
        }
        int row = -1;
        try {
            row = new Long(db.insert(table, null, values)).intValue();
            if (row > 1)
                row = 1;
        } catch (Exception e) {
        }
        try {
            db.close();
        } catch (Exception localException2) {
        }
        return row;
    }

    public long delete(String table, String where, String[] where_values) {
        SQLiteDatabase db = null;
        long ret = -1L;
        try {
            db = getWritableDatabase();
        } catch (Exception localException1) {
        }
        try {
            ret = db.delete(table, where, where_values);
        } catch (Exception e) {
            //CommLog.e("," + e.getMessage());//做一个日志记录操作数�??
        }
        try {
            db.close();
        } catch (Exception localException2) {
        }
        return ret;
    }

    public int update(String table, String where, String[] where_values,
                      ContentValues columns) {
        SQLiteDatabase db = null;
        int ret = -1;
        try {
            db = getWritableDatabase();
        } catch (Exception localException1) {
        }
        try {
            ret = db.update(table, columns, where, where_values);
        } catch (Exception e) {
            //CommLog.e("日志[" + table + "]," + e.getMessage());//做一个日志记录操作数�??
        }
        try {
            db.close();
        } catch (Exception localException2) {
        }
        return ret;
    }

    public void execSQL(String sql, Object[] obj) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
        } catch (Exception localException1) {
        }
        if (obj == null) {
            db.execSQL(sql);
        } else if (obj != null) {
            db.execSQL(sql, obj);
        }
        try {
            db.close();
        } catch (Exception localException2) {
        }
    }

    public String[][] queryAll(String table, String orderBy) {
        return query(table, null, null, null, null, null, orderBy);
    }

    public String[][] query(String table, String[] columns, String selection,
                            String[] selectionArgs, String groupBy, String having,
                            String orderBy) {
        SQLiteDatabase db = null;
        String[][] ret = (String[][]) null;
        try {
            db = getWritableDatabase();
        } catch (Exception localException1) {
        }
        try {
            /*if (columns == null)
				columns = MetaTool.getTableCols(table);*/
            Cursor c = null;
            try {
                c = db.query(table, columns, selection, selectionArgs, groupBy,
                        having, orderBy);
                ret = MetaTool.CursorToArray(c);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                c.close();
            } catch (Exception localException2) {
            }
            try {
                db.close();
            } catch (Exception localException3) {
            }
            String[][] arrayOfString1 = ret;
            return arrayOfString1;
        } catch (Exception e) {
            //CommLog.e("CommSqlHelper query error " + table + "<" + db, e);
            e.printStackTrace();
            return null;
        } finally {
            try {
                db.close();
            } catch (Exception localException6) {
            }
        }
    }

    //执行distinct操作时需要用�?? 直接传一个sql
    public String[][] rawQuery(String sql, String[] selectionArgs) {
        SQLiteDatabase db = null;
        String[][] ret = (String[][]) null;
        try {
            db = getWritableDatabase();
        } catch (Exception localException1) {
        }
        try {
			/*if (columns == null)
				columns = MetaTool.getTableCols(table);*/
            Cursor c = null;
            try {
                c = db.rawQuery(sql, selectionArgs);
                ret = MetaTool.CursorToArray(c);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                c.close();
            } catch (Exception localException2) {
            }
            try {
                db.close();
            } catch (Exception localException3) {
            }
            String[][] arrayOfString1 = ret;
            return arrayOfString1;
        } catch (Exception e) {
            //CommLog.e("CommSqlHelper query error " + table + "<" + db, e);
            e.printStackTrace();
            return null;
        } finally {
            try {
                db.close();
            } catch (Exception localException6) {
            }
        }
    }

    public JSONArray queryJson(String table, String[] columns, String selection,
                               String[] selectionArgs, String groupBy, String having,
                               String orderBy) {
        SQLiteDatabase db = null;
        JSONArray ret = (JSONArray) null;
        try {
            db = getWritableDatabase();
        } catch (Exception localException1) {
        }
        try {
			/*if (columns == null)
				columns = MetaTool.getTableCols(table);*/
            Cursor c = null;
            try {
                c = db.query(table, columns, selection, selectionArgs, groupBy,
                        having, orderBy);
                ret = MetaTool.cursorToJson(c);
            } catch (Exception e) {
                //CommLog.e("," + e.getMessage());
            }
            try {
                c.close();
            } catch (Exception localException2) {
            }
            try {
                db.close();
            } catch (Exception localException3) {
            }
            JSONArray arrayOfString1 = ret;
            return arrayOfString1;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                db.close();
            } catch (Exception localException6) {
            }
        }
    }

    public List<HashMap<String, String>> queryList(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        SQLiteDatabase db = null;
        List<HashMap<String, String>> ret = new ArrayList<HashMap<String, String>>();
        try {
            db = getWritableDatabase();
        } catch (Exception localException1) {
        }
        try {
	       /* if (columns == null)
	         columns = MetaTool.getTableCols(table);*/
            Cursor c = null;
            try {
                c = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
                ret = MetaTool.CursorToList(c);
            } catch (Exception e) {
                //CommLog.e("," + e.getMessage());
            }
            try {
                c.close();
            } catch (Exception localException2) {
            }
            try {
                db.close();
            } catch (Exception localException3) {
            }
            return ret;
        } catch (Exception e) {
            //CommLog.e("CommSqlHelper query error " + table + "<" + db, e);
            List<HashMap<String, String>> localList1 = ret;
            return localList1;
        } finally {
            try {
                db.close();
            } catch (Exception localException6) {
            }
        }
    }

    public Cursor queryCursor(String table, String[] columns, String selection,
                              String[] selectionArgs, String groupBy, String having,
                              String orderBy) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
        } catch (Exception e) {
            //CommLog.e("" + table, e);
        }
        try {
            Cursor c = db.query(table, columns, selection, selectionArgs,
                    groupBy, having, orderBy);
            return c;
        } catch (Exception e) {
            //CommLog.e("CommSqlHelper, query error " + table + "<"+ e.getMessage());
        }
        return null;
    }

    //    public synchronized SQLiteDatabase getWritableDatabase() {
    //        try {
    //            moveDbToSDcard();
    //            String databaseFilename = DATABASE_PATH + "/" + DATABASE_NAME;
    //
    //            SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(
    //                    databaseFilename, null);
    //            return database;
    //        } catch (Exception e) {
    //            System.out.println(e.getMessage());
    //        }
    //        return super.getWritableDatabase();
    //    }

    //    public void moveDbToSDcard() {
    //        try {
    //            String databaseFilename = DATABASE_PATH + "/" + DATABASE_NAME;
    //            File dir = new File(DATABASE_PATH);
    //            // 判断SD卡下是否存在存放数据库的目录，如果不存在，新建目�??
    //            if (!dir.exists()) {
    //                dir.mkdirs();
    //            } else {
    //            }
    //            try {
    //                // 如果数据库已经在SD卡的目录下存在，那么不需要重新创建，否则创建文件，并拷贝/res/raw下面的数据库文件
    //                if ((new File(databaseFilename)).exists()) {
    //                    return;
    //                } else {
    //                    // /res/raw数据库作为输出流
    //                    // AssetManager ast = this.context.getAssets();
    //                    // InputStream is = ast.open("initdb.db");
    //                    InputStream is = CommonSqlHelper.context.getResources()
    //                            .openRawResource(R.raw.init);
    //                    // 用于存放数据库信息的数据�??
    //                    FileOutputStream fos = new FileOutputStream(
    //                            databaseFilename);
    //                    byte[] buffer = new byte[8192];
    //                    int count = 0;
    //                    // 把数据写入SD卡目录下
    //                    while ((count = is.read(buffer)) > 0) {
    //                        fos.write(buffer, 0, count);
    //                    }
    //                    fos.flush();
    //                    fos.close();
    //                    is.close();
    //                }
    //            } catch (FileNotFoundException e) {
    //                e.printStackTrace();
    //            } catch (IOException e) {
    //                e.printStackTrace();
    //            }
    //        } catch (Exception e) {
    //        }
    //    }

    public void execSQLList(String[] sqls) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            db.beginTransaction();
            //手动设置�??始事�??
            //数据插入操作循环
            SQLiteStatement statement = null;
            for (String sql : sqls) {
                statement = db.compileStatement(sql);
                statement.executeInsert();
            }
            db.setTransactionSuccessful();       //设置事务处理成功，不设置会自动回滚不提交
            db.endTransaction();       //处理完成
            db.close();
        } catch (Exception ex) {
            db.close();
            ex.printStackTrace();
        }
    }

    public void execSQLListUpdate(String[] sqls) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            db.beginTransaction();       //手动设置�??始事�??
            //数据插入操作循环
            SQLiteStatement statement = null;
            for (String sql : sqls) {
                statement = db.compileStatement(sql);
                statement.executeUpdateDelete();
            }
            db.setTransactionSuccessful();       //设置事务处理成功，不设置会自动回滚不提交
            db.endTransaction();       //处理完成
            db.close();
        } catch (Exception ex) {
            db.close();
            ex.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TJ_ZCXT";
        File pathFile = new File(path);
        File file = new File(path + "/TJ_ZCXT.db");
        try {
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        db = SQLiteDatabase.openOrCreateDatabase(file, null);
        db.execSQL(CREATE_BOOK);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
