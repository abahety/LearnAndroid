package com.abahety.simpletodo.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.abahety.simpletodo.R;
import com.abahety.simpletodo.storage.ToDoItem;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements EditItemDialog.EditItemDialogListener {

    private ListView lvItems;
    private ArrayList<ToDoItem> items;
    private ArrayAdapter<ToDoItem> itemsAdapter;
    private String blankString = "";
    private static String SPACE_DELETED = " deleted !!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize list of items from DB
        readItems();
        //initialize adapter to load to list view
        itemsAdapter = new ArrayAdapter<ToDoItem>(this, android.R.layout.simple_list_item_1,items);

        //get handle to listView
        lvItems = (ListView)findViewById(R.id.lvItems);

        //attach adapter to listView
        lvItems.setAdapter(itemsAdapter);

        //set onclick listener for list items
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String deletedItemName = items.get(position).getItemName();
                //delete from DB
                removeItem(position);
                //notify
                showDeletedNotification(deletedItemName);
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fm = getSupportFragmentManager();
                ToDoItem item = items.get(position);
                EditItemDialog dialog = EditItemDialog.newInstance(item.getItemName(), position);
                dialog.show(fm, "activity_edit_item_dialog");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void onAddItem(View view) {
        //get handle to editText menu
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String newItemValue = etNewItem.getText().toString();
        if(newItemValue==null || newItemValue.isEmpty()){
            return;//do nothing
        }
        //add item to adapter
        ToDoItem newItem = getNewItem(newItemValue);
        itemsAdapter.add(newItem);
        //clear the editText field
        etNewItem.setText(blankString);
        //save to Db
        saveToDb(newItem);
    }

    private void writeItems() {
        File todoFile = getFileHandle();

        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readItems(){
        items = new ArrayList<ToDoItem>(ToDoItem.getAllItems());
    }

    private File getFileHandle() {
        File fileDir = getFilesDir();
        return new File(fileDir, "todo.txt");
    }

    // function to be called when dialog button save is clicked
    @Override
    public void saveItem(String editedItemName, int position) {
        removeItem(position);
        //create new item and save it in DB
        ToDoItem newItem = getNewItem(editedItemName);
        items.add(position, newItem);
        //writeItems();
        saveToDb(newItem);

    }

    private void removeItem(int position) {
        ToDoItem oldItem  = items.get(position);
        //delete from DB
        oldItem.delete();
        String deletedItemName = oldItem.getItemName();
        //change in in memory array and notify adaptor
        items.remove(position);
        itemsAdapter.notifyDataSetChanged();
    }

    private ToDoItem getNewItem(String itemName){
        return new ToDoItem(itemName);
    }

    private void saveToDb(ToDoItem newItem) {
        newItem.save();
    }

    private void showDeletedNotification(String item){
        Toast.makeText(this,item + SPACE_DELETED,Toast.LENGTH_SHORT).show();
    }
}
