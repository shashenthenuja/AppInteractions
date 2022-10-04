package edu.curtin.appinteractions;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

public class CaptureFull extends AppCompatActivity {

    private static final int REQUEST_PHOTO = 3;
    private File photoFile;
    private Intent photoIntent;
    ImageView image;
    Button capture;
    Button gray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_full);

        image = findViewById(R.id.captureFullPhoto);
        capture = findViewById(R.id.captureBtn);
        gray = findViewById(R.id.grayBtn);
        gray.setVisibility(View.GONE);


        photoFile = new File(getFilesDir(),"photo.jpg");
        Uri cameraUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID+".fileprovider",photoFile);
        Intent photoIntent = new Intent();
        photoIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        photoIntent.putExtra(MediaStore.EXTRA_OUTPUT,cameraUri);

        PackageManager pm = getPackageManager();
        for(ResolveInfo a : pm.queryIntentActivities(
                photoIntent, PackageManager.MATCH_DEFAULT_ONLY)) {

            grantUriPermission(a.activityInfo.packageName, cameraUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }



        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(photoIntent, REQUEST_PHOTO);
            }
        });

        gray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setImageGray();
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultIntent)
    {
        if(requestCode == REQUEST_PHOTO && resultCode == RESULT_OK){
            Bitmap photo = BitmapFactory.decodeFile(photoFile.toString());
            image.setImageBitmap(photo);
            gray.setVisibility(View.VISIBLE);
        }
    }

    private void setImageGray() {
        Glide.with(this)
                .load(photoFile)
                .apply(RequestOptions.circleCropTransform())
                .into(image);

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
        image.setColorFilter(filter);
    }
}