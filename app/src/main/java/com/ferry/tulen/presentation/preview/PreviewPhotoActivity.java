package com.ferry.tulen.presentation.preview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.ferry.tulen.R;
import com.github.chrisbanes.photoview.PhotoView;

public class PreviewPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_preview_photo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
//        photoView.setImageResource(R.drawable.image);


        Intent intent = getIntent();

        String url = intent.getStringExtra("url");


        Glide.with(this)
                .load(Uri.parse(url))
                .into(photoView);
    }
}