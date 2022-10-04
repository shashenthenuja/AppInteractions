package edu.curtin.appinteractions;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Contact extends AppCompatActivity {

    private static final int REQUEST_CONTACT = 2;
    private static final int REQUEST_READ_CONTACT_PERMISSION = 3;
    Button pickContact;
    Button more;
    TextView email;
    TextView phone;
    TextView name;

    int contactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        pickContact = findViewById(R.id.pickContact);
        email = findViewById(R.id.emailView);
        name = findViewById(R.id.nameView);
        phone = findViewById(R.id.phoneView);
        more = findViewById(R.id.moreButton);
        name.setVisibility(View.INVISIBLE);
        email.setVisibility(View.INVISIBLE);
        phone.setVisibility(View.INVISIBLE);
        more.setVisibility(View.INVISIBLE);

        pickContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickContactButtonClicked();
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(Contact.this, Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(Contact.this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACT_PERMISSION);
                }
                else {
                    moreButtonClicked();
                    moreButtonClickedPhone();
                }
            }
        });
    }

    private void pickContactButtonClicked(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent,REQUEST_CONTACT);
    }

    private void moreButtonClicked(){

        String result="";
        Uri emailUri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String[] queryFields = new String[] {
                ContactsContract.CommonDataKinds.Email.ADDRESS
        };

        String whereClause = ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?";
        String [] whereValues = new String[]{
                String.valueOf(this.contactId)
        };
        Cursor c = getContentResolver().query(
                emailUri, queryFields, whereClause,whereValues, null);
        try{
            c.moveToFirst();
            do{
                String emailAddress = c.getString(0);
                result = result+emailAddress+" ";
            }
            while (c.moveToNext());

        }
        finally {
            c.close();
        }

        email.setText(result);
        email.setVisibility(View.VISIBLE);

    }

    private void moreButtonClickedPhone(){

        String result="";
        Uri emailUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] queryFields = new String[] {
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        String whereClause = ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?";
        String [] whereValues = new String[]{
                String.valueOf(this.contactId)
        };
        Cursor c = getContentResolver().query(
                emailUri, queryFields, whereClause,whereValues, null);
        try{
            c.moveToFirst();
            do{
                String phone = c.getString(0);
                result = result+phone+" ";
            }
            while (c.moveToNext());

        }
        finally {
            c.close();
        }

        phone.setText(result);
        phone.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CONTACT && resultCode == RESULT_OK){
            Uri contactUri = data.getData();
            String[] queryFields = new String[] {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            Cursor c = getContentResolver().query(
                    contactUri, queryFields, null, null, null);
            try {
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    this.contactId = c.getInt(0);         // ID first
                    String contactName = c.getString(1); // Name second
                    name.setVisibility(View.VISIBLE);
                    name.setText(String.valueOf(contactId) + " : " + contactName);
                    more.setVisibility(View.VISIBLE);
                }
            }
            finally {
                c.close();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_READ_CONTACT_PERMISSION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(Contact.this, "Contact Reading Permission Granted", Toast.LENGTH_SHORT).show();
                moreButtonClicked();
            }
        }
    }
}