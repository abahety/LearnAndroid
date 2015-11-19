//package com.abahety.simpletodo.storage;
//
//import android.content.Context;
//import android.database.Cursor;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CursorAdapter;
//import android.widget.TextView;
//
///**
// * Created by abahety on 11/16/15.
// */
//public class ToDoItemAdaptor extends CursorAdapter {
//
//
//    public ToDoItemAdaptor(Context context, Cursor c) {
//        super(context, c, 0);
//    }
//
//    @Override
//    public View newView(Context context, Cursor cursor, ViewGroup parent) {
//        TextView content = new TextView(context);
//        content.setTag("content");
//        return content;
//    }
//
//    @Override
//    public void bindView(View view, Context context, Cursor cursor) {
//        if(cursor==null)return;
//        TextView tv = (TextView) view.findViewWithTag("content");
//        tv.setText(cursor.getString(cursor.getColumnIndex("name")));
//    }
//}
