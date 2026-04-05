package com.kkgrocery.user;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class PhotoViewActivity extends AppCompatActivity {
    Context context = this;
    PhotoView photoView;

    int drawableId;
    String imageUrl;
    String shareUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        photoView = findViewById(R.id.photoView);

        drawableId = getIntent().getIntExtra("drawableId", -1);
        imageUrl = getIntent().getStringExtra("imageUrl");
        shareUrl = getIntent().getStringExtra("shareUrl");

        if (drawableId != -1)
            Picasso.get().load(drawableId).into(photoView);
        else
            Picasso.get().load(imageUrl).into(photoView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photo_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_share:
                share();
                return true;
            case R.id.action_wallpaper:
                setAsWallpaper();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setAsWallpaper() {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        try {
            Bitmap bitmap = ((BitmapDrawable)photoView.getDrawable()).getBitmap();
            wallpaperManager.setBitmap(bitmap);
            Toast.makeText(context, getString(R.string.wallpaper_has_been_set), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(context, String.format("%s\n%s", getString(R.string.could_not_change_wallpaper), e.getMessage()), Toast.LENGTH_LONG).show();
        }
    }

    private void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, String.format("%s\n%s",
                getString(R.string.share_text), shareUrl != null ? shareUrl : imageUrl));
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getString(R.string.share)));
    }

}
