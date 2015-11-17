package com.abahety.simpletodo.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
* Created by abahety on 11/16/15.
*/
public class ToDoItemDatabase extends SQLiteOpenHelper {
    //DB info
    private static final String DATABASE_NAME = "ToDoItems";
    private static final int DATABASE_VERSION = 1;

    // Table Name
    private static final String TABLE_ITEMS = "Items";

    private static ToDoItemDatabase dbInstance;

    public static synchronized ToDoItemDatabase getInstance(Context context) {
        if(dbInstance==null){
            dbInstance=new ToDoItemDatabase(context);
        }
        return dbInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private ToDoItemDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS +
                "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "name" + " TEXT" +
                ")";
        db.execSQL(CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        if (oldVersion != newVersion) {
//            // Simplest implementation is to drop all old tables and recreate them
//            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
//            onCreate(db);
//        }
    }
}