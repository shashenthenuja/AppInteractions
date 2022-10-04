package edu.curtin.appinteractions;

import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Button contact;
    Button call;
    Button location;
    Button capture;
    Button captureFull;
    EditText number;
    EditText latitude;
    EditText longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contact = findViewById(R.id.contactBtn);
        call = findViewById(R.id.callBtn);
        location = findViewById(R.id.locationBtn);
        capture = findViewById(R.id.mainCapture);
        captureFull = findViewById(R.id.mainCaptureFull);
        number = findViewById(R.id.phoneInput);
        latitude = findViewById(R.id.latInput);
        longitude = findViewById(R.id.longInput);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Editable num = number.getText();
                if (!num.toString().isEmpty()) {
                    callButtonClicked(Integer.valueOf(num.toString()));
                }
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Editable lat = latitude.getText();
                Editable longi = longitude.getText();
                if (!checkEmpty(lat.toString(), longi.toString())) {
                    viewMapButtonClicked(Double.parseDouble(lat.toString()), Double.parseDouble(longi.toString()));
                }
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Contact.class);
                startActivity(intent);
            }
        });

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Capture.class);
                startActivity(intent);
            }
        });

        captureFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CaptureFull.class);
                startActivity(intent);
            }
        });

    }

    private void callButtonClicked(int number){
        Uri uri = Uri.parse(String.format("tel:%d", number));
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(uri);
        startActivity(intent);

    }

    private void viewMapButtonClicked(double lat, double longi){
        Uri uri = Uri.parse(String.format("geo:%f,%f", lat, longi));
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(uri);
        startActivity(intent);
    }

    public boolean checkEmpty(String a, String b) {
        if (a.isEmpty()) {
            return true;
        }else if (b.isEmpty()) {
            return true;
        }else {
            return false;
        }
    }
}