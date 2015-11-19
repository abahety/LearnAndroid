package com.abahety.simpletodo.storage;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by abahety on 11/16/15.
 * Represents ORM for DB to store ToDo items
 */

@Table(name= "Items")
public class ToDoItem extends Model{

    @Column(name="name")
    public String itemName;

    //for active android to work
    public ToDoItem(){
        super();
    }

    public ToDoItem(String itemName){
        super();
        this.itemName=itemName;
    }

    public String getItemName(){
        return this.itemName;
    }

    /**
     * get all the to-do items from database
     * @return list of all to-do items in the database
     */
    public static List<ToDoItem> getAllItems(){
        return new Select().all().from(ToDoItem.class).execute();
    }

    // this function will be called by array adaptor to put value in textview section of ListView
    @Override
    public String toString() {
        return this.itemName;
    }
}

