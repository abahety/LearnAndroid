package com.abahety.instagramclient.model;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.abahety.instagramclient.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by abahety on 12/2/15.
 */
public class PopularMediaArrayAdapter extends ArrayAdapter<PopularMedia> {

    private static class ViewHolder{
        TextView tvUsername;
        ImageView ivPhoto;
        TextView tvCaption;
        RoundedImageView ivProfile;
        TextView timestamp;
    }

    public PopularMediaArrayAdapter(Context context, ArrayList<PopularMedia> mediaList) {
        super(context, 0, mediaList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final PopularMedia media = getItem(position);
        final ViewHolder viewHolder;
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.media_list_entry, parent, false);
            viewHolder.tvUsername =(TextView) convertView.findViewById(R.id.tvUserName);
            viewHolder.ivPhoto = (ImageView)convertView.findViewById(R.id.imvPhoto);
            viewHolder.tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
            viewHolder.ivProfile = (RoundedImageView)convertView.findViewById(R.id.ivProfile);
            viewHolder.ivProfile.setBorderColor(Color.LTGRAY);
            viewHolder.ivPhoto.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            viewHolder.timestamp = (TextView)convertView.findViewById(R.id.tvTimeStamp);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.tvUsername.setText(media.getUserName());
        viewHolder.tvCaption.setText(media.getCaption());

        String timeStampRelative = DateUtils.getRelativeTimeSpanString(Long.valueOf(media.getTimeStamp()).longValue() * 1000,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        timeStampRelative = timeStampRelative.substring(0,timeStampRelative.indexOf(' ')+2).replace(" ","");
        viewHolder.timestamp.setText(timeStampRelative);
        //clear out image view
        viewHolder.ivPhoto.setImageResource(0);
        //load profile image
         Picasso.with(getContext()).load(media.getProfilePicUrl()).fit().into(viewHolder.ivProfile);
        // load media photo
        viewHolder.ivPhoto.setScaleType(ImageView.ScaleType.CENTER_INSIDE); //display placeholder
         Picasso.with(getContext()).load(media.getImageUrl())
            .placeholder(R.drawable.photo_loading).into(viewHolder.ivPhoto,new Callback() {
             @Override
             public void onSuccess() {
                viewHolder.ivPhoto.setScaleType(ImageView.ScaleType.FIT_XY);
             }

             @Override
             public void onError() {

             }
         });

        // Return the completed view to render on screen
        return convertView;
    }

}
