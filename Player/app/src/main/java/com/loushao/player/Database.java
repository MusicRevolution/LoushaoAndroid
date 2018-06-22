package com.loushao.player;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper{
    private static final String TAG = "Database";
    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists Login(autologin integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void updata(int i){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("autologin",i);
        db.replace("Login",null,values);
        //Log.e(TAG, "updata: "+i );
        db.close();
    }
    public int query(){
        int i=0;
        SQLiteDatabase db=getWritableDatabase();
        Cursor cursor=db.query("Login",null,null,null,null,null,null);
        //Log.e(TAG, "cursor: "+cursor.getCount() );
        if (cursor.moveToFirst()){
            do{
                i=cursor.getInt(cursor.getColumnIndex("autologin"));
                //Log.e(TAG, "query: "+i );
            }while (cursor.moveToNext());
        }
       // Log.e(TAG, "query: "+i);
        cursor.close();
        db.close();
        return i;
    }
}
