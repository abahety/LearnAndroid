package com.abahety.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class EditItemActivity extends ActionBarActivity {
    private EditText etItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        String oldItemValue = getIntent().getStringExtra("oldItem");
        etItem = (EditText)findViewById(R.id.etItem);
        etItem.setText(oldItemValue);
        etItem.setSelection(oldItemValue.length());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
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

    public void onSave(View view) {
        Intent saveIntent = new Intent(EditItemActivity.this, MainActivity.class);
        String newValue = etItem.getText().toString();
        saveIntent.putExtra("newItem",newValue);
        saveIntent.putExtra("itemPos", getIntent().getIntExtra("itemPos",0));
        setResult(RESULT_OK,saveIntent);
        finish();
    }
}
