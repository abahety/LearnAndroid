package com.abahety.instagramclient.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.abahety.instagramclient.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by abahety on 12/2/15.
 */
public class PopularMediaArrayAdapter extends ArrayAdapter<PopularMedia> {

    public PopularMediaArrayAdapter(Context context, ArrayList<PopularMedia> mediaList) {
        super(context, 0, mediaList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        PopularMedia media = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.media_list_entry, parent, false);
        }
        // Lookup view for data population
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        ImageView imvPhoto = (ImageView)convertView.findViewById(R.id.imvPhoto);

        // Populate the data into the template view using the data object
        tvUsername.setText(media.getUserName());
        tvCaption.setText(media.getCaption());
        Picasso.with(getContext()).load(media.getImageUrl()).into(imvPhoto);
        // Return the completed view to render on screen
        return convertView;
    }

}
