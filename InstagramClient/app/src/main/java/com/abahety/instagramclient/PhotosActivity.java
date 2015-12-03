package com.abahety.instagramclient;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.abahety.instagramclient.client.InstagramClient;
import com.abahety.instagramclient.model.PopularMedia;
import com.abahety.instagramclient.model.PopularMediaArrayAdapter;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class PhotosActivity extends ActionBarActivity {

    ListView mediaListView;
    //initialize to empty as loading from API happens async. If not, we get an exception
    ArrayList<PopularMedia> mediaList = new ArrayList<PopularMedia>();
   // ArrayAdapter<PopularMedia> adapter;
    PopularMediaArrayAdapter mediaAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        //populate mediaList
        populateMediaList();
        //get list view
        mediaListView = (ListView)findViewById(R.id.lvMedia);
        // get the adapter
        //adapter = new ArrayAdapter<PopularMedia>(this, android.R.layout.simple_list_item_1,mediaList);
        mediaAdapter = new PopularMediaArrayAdapter(this,mediaList);
        //set the adapter
        mediaListView.setAdapter(mediaAdapter);


    }

    private void populateMediaList() {
        //make network call and parse JSON array
        InstagramClient.getPopularMedia(new RequestParams(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("Debug",response.toString());
                try {
                    // Get and store decoded array of business results
                    JSONArray mediaJson = response.getJSONArray("data");
                    mediaList.clear();// clear existing items if needed
                    mediaList.addAll(PopularMedia.fromJson(mediaJson)); // add new items
                    mediaAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("Debug", response.toString());

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("Error","Error connecting to instagram api. Status code:"+statusCode);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
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
}
