package edu.curtin.appinteractions;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Capture extends AppCompatActivity {

    ImageView imageView;

    static final int REQUEST_THUMBNAIL = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        Button captureImage = findViewById(R.id.captureBtn);
        imageView = findViewById(R.id.capturePhoto);

        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_THUMBNAIL);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode== Activity.RESULT_OK && requestCode == REQUEST_THUMBNAIL){

            Bitmap image = (Bitmap) data.getExtras().get("data");
            if(image != null){
                imageView.setImageBitmap(image);
            }

        }
    }
}