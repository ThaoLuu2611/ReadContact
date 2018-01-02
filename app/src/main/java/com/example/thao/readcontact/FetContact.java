package com.example.thao.readcontact;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by luu.phuong.thao on 10/10/2016.
 */

public class FetContact extends Activity {
    public TextView outputText;
    ListView listView;
    ArrayList<Contacts> contactsList;
    FullContactItemAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //outputText = (TextView) findViewById(R.id.textView1);
        listView = (ListView)findViewById(R.id.listView);
        contactsList = new ArrayList<>();
        adapter = new FullContactItemAdapter(contactsList,getApplicationContext());
        //fetchContacts();
        fetchContactSmart();
    }

    public void fetchContactSmart(){
        String[] projection = {
                ContactsContract.Data.MIMETYPE,
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Email._ID,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID,
                ContactsContract.CommonDataKinds.Email.RAW_CONTACT_ID,
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.TYPE
        };
        String selection = ContactsContract.Data.MIMETYPE + " in (?, ?)";
        String[] selectionArgs = {
                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
        };
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME;

        Uri uri = ContactsContract.Data.CONTENT_URI;

        Cursor cursor = getContentResolver().query(uri,projection, selection, selectionArgs, sortOrder);
        // Loop for every contact in the phone
        int i =0;
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                final int mimeTypeIdx = cursor.getColumnIndex(ContactsContract.Data.MIMETYPE);
                final String idIdx = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID));
                final String nameIdx = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                final String emailID = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email._ID));
                final String emailContactID = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID));
                final String emailRawContactID = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.RAW_CONTACT_ID));
                final String emailAddress = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                final String emailType = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

                contactsList.add(new Contacts(emailAddress, emailAddress,emailAddress, emailAddress));
            }
        }
        listView.setAdapter(adapter);
    }

    public void fetchContacts() {

        String phoneNumber = "";
        String email = "";

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        Uri Data_content_uri = ContactsContract.Data.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        Uri EmailCONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;
       // StringBuffer output = new StringBuffer();
        ContentResolver contentResolver = getContentResolver();
        String select =  ContactsContract.Contacts._ID + " = ?";
        String[] selectArg = {"558"};
        //Cursor cursor = contentResolver.query(CONTENT_URI, null,select, selectArg, DISPLAY_NAME);
        Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, DISPLAY_NAME);
        // Loop for every contact in the phone
        int i =0;
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));
                Log.d("thao","cursor move to next contact_id="+contact_id);
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));
              //  if (hasPhoneNumber > 0) {
              //      output.append("\n First Name:" + name);
                    // Query and loop for every phone number of the contact
                   Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
               // Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null,null,null, null);
                    while (phoneCursor.moveToNext()) {
                        String id = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        Log.d("thao","phone number = "+phoneNumber+" contact id = "+contact_id+" name = "+name+" id ....="+id);
                        if(id.equalsIgnoreCase(contact_id))
                            break;
                    }
                    phoneCursor.close();
                    // Query and loop for every email of the contact
                 /*   Cursor emailCursor = contentResolver.query(EmailCONTENT_URI,	null, EmailCONTACT_ID+ " = ?", new String[] { contact_id }, null);
                    while (emailCursor.moveToNext()) {
                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                    }
                    emailCursor.close();
                if(name == "")
                    name="No name";*/
               contactsList.add(new Contacts(contact_id, email,phoneNumber, name));
                }
            }
            listView.setAdapter(adapter);
        }
    }