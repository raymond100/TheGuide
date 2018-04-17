package com.ray100.theguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.media.CamcorderProfile.get;

/**
 * Created by ray100 on 10/03/18.
 */

public class GuideDesc extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_desc_view);

        Intent intent = getIntent();
        ImageView imageView = (ImageView) findViewById(R.id.img);


    }
}