package com.shivamdev.contentresolver;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    static final Uri CONTENT_URL =
            Uri.parse("content://com.shivamdev.contentresolver.contactprovider.ContactProvider/cpcontacts");

    TextView tvContacts = null;
    EditText etDeleteId, etIdLookup, etAddName;

    ContentResolver resolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resolver = getContentResolver();

        tvContacts = (TextView) findViewById(R.id.contactsTextView);
        etDeleteId = (EditText) findViewById(R.id.deleteIDEditText);
        etIdLookup = (EditText) findViewById(R.id.idLookupEditText);
        etAddName = (EditText) findViewById(R.id.addNameEditText);

        getContacts();
    }

    public void getContacts() {


        String[] projection = new String[] {"id", "name"};

        Cursor cursor = resolver.query(CONTENT_URL, projection, null, null, null);

        String contactsList = "";

        if(cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex("id"));

                String name = cursor.getString(cursor.getColumnIndex("name"));

                contactsList = contactsList + id + " : " + name + "/n";
            } while (cursor.moveToNext());
        }
        tvContacts.setText(contactsList);
    }
    public void deleteContact(View view) {

        String idToDelete = etDeleteId.getText().toString();

        long idDeleted = resolver.delete(CONTENT_URL, "id = ? ", new String[] {idToDelete});

        getContacts();

    }

    public void lookupContact(View view) {

        String idToFind = etIdLookup.getText().toString();

        String[] projection = {"id", "name"};

        Cursor cursor = resolver.query(CONTENT_URL, projection,
                "id = ? ", new String[] {idToFind}, null);

        String contact = "";

        if(cursor.moveToFirst()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));

            String name = cursor.getString(cursor.getColumnIndex("name"));

            contact = contact + id + " : " + name + "\n";
        } else {
            L.t(this, "Contact Not Found");
        }

        tvContacts.setText(contact);

    }

    public void addContact(View view) {

        String nameToAdd = etAddName.getText().toString();

        ContentValues values = new ContentValues();

        values.put("name", nameToAdd);

        resolver.insert(CONTENT_URL, values);

        getContacts();

    }

    public void showContacts(View view) {

        getContacts();

    }
}
