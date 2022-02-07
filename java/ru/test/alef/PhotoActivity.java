package ru.test.alef;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class PhotoActivity extends AppCompatActivity {

    public static final String EXTRA_PHOTO = "PhotoActivity.PHOTO";

    private Photo photo;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        mImageView = (ImageView) findViewById(R.id.image);
        Intent intent = getIntent();


        Glide.with(this)
                .load(intent.getStringExtra(EXTRA_PHOTO))
                //.asBitmap()
                //.error(R.drawable.uyu)
                //.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mImageView);

        mImageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

    }
}