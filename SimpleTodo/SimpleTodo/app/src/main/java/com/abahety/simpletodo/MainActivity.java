package com.abahety.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private ListView lvItems;
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private String blankString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize list of items
        //items = new ArrayList<String>();
        readItems();
           //initialize adapter to load to list view
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);

        //get handle to listView
        lvItems = (ListView)findViewById(R.id.lvItems);


        //attach adapter to listView
        lvItems.setAdapter(itemsAdapter);

        //set onclick listener for list items
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();//save to file
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editIntent = new Intent(MainActivity.this, EditItemActivity.class);
                editIntent.putExtra("oldItem", items.get(position).toString());
                editIntent.putExtra("itemPos",position);
                startActivityForResult(editIntent,1);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

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
        itemsAdapter.add(newItemValue);
        //clear the editText field
        etNewItem.setText(blankString);

        //save to file
        writeItems();
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
        File todoFile = getFileHandle();

        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getFileHandle() {
        File fileDir = getFilesDir();
        return new File(fileDir, "todo.txt");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode==RESULT_OK && requestCode==1){
            String editedItem = data.getExtras().getString("newItem");
            int itemPosition = data.getExtras().getInt("itemPos");
            items.remove(itemPosition);
            itemsAdapter.notifyDataSetChanged();
            items.add(itemPosition, editedItem);
            writeItems();
        }


    }
}
