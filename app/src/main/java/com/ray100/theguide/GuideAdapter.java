package com.ray100.theguide;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by ray100 on 07/03/18.
 */

public class GuideAdapter extends ArrayAdapter<Guide> {
    public GuideAdapter(@NonNull Context context, ArrayList<Guide> guides) {
        super(context, 0, guides);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View guideView = convertView;
        /**
         * Inflate ths layout, if the view is empty
         */
        if(guideView == null){
            guideView = LayoutInflater.from(getContext()).inflate(R.layout.guide_list_item_view,parent,false);
        }

        TextView guideName = (TextView) guideView.findViewById(R.id.item_name);
        TextView guideStreet = (TextView) guideView.findViewById(R.id.item_street);

        ImageView imageView = (ImageView) guideView.findViewById(R.id.img);

        Guide currentGuide = getItem(position);

        guideName.setText(currentGuide.getGuideName());
        guideStreet.setText(currentGuide.getGuideStreet());

        if(currentGuide.hasImage()){
            //imageView.setImageResource(currentGuide.getImageResourceID());
            Picasso.with(getContext())
                    .load(currentGuide.getImageResourceID())
                    .transform(new CropCircleTransformation())
                    .into(imageView);

            imageView.setVisibility(View.VISIBLE);
        }else {
            imageView.setVisibility(View.GONE);
        }

        return guideView;
    }
}
