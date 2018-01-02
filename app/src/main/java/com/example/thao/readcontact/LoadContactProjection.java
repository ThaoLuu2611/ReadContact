package com.example.thao.readcontact;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by luu.phuong.thao on 10/10/2016.
 */

public class LoadContactProjection extends Activity {
    ListView listView;
    ArrayList<Contacts> contactsList;
    FullContactItemAdapter adapter;
    public static String CONTACT_ID_URI = ContactsContract.Contacts._ID;
    public static String DATA_CONTACT_ID_URI = ContactsContract.Data.CONTACT_ID;
    public static String MIMETYPE_URI = ContactsContract.Data.MIMETYPE;
    public static String EMAIL_URI = ContactsContract.CommonDataKinds.Email.DATA;
    public static String PHONE_URI = ContactsContract.CommonDataKinds.Phone.DATA;
    public static String NAME_URI = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) ? ContactsContract.Data.DISPLAY_NAME_PRIMARY : ContactsContract.Data.DISPLAY_NAME;
    public static String PICTURE_URI = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) ? ContactsContract.Contacts.PHOTO_THUMBNAIL_URI : ContactsContract.Contacts.PHOTO_ID;

    public static String MAIL_TYPE = ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE;
    public static String PHONE_TYPE = ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE;

    String TAG ="thao";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //outputText = (TextView) findViewById(R.id.textView1);
        listView = (ListView) findViewById(R.id.listView);
        contactsList = new ArrayList<>();
        adapter = new FullContactItemAdapter(contactsList, getApplicationContext());
        //fetchContacts();
        //getAllContacts();
        loadContactSmart();
     //   getDetailedContactList(null);
    }

    void getAllContacts() {
        long startnow;
        long endnow;

        startnow = android.os.SystemClock.uptimeMillis();

        Uri phone_uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Uri email_uri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri data_uri = ContactsContract.Data.CONTENT_URI;

        String[] projection_phone = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.Contacts._ID};
        //Cursor cursor = getApplicationContext().getContentResolver().query(phone_uri,projection_phone , null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        Cursor cursor = getApplicationContext().getContentResolver().query(phone_uri, projection_phone, null, null, ContactsContract.Contacts._ID);

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            String contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            int phoneContactID = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
            int contactID = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Log.d("con ", "name " + contactName + " " + " PhoeContactID " + phoneContactID + "  ContactID " + contactID);
         //   contactsList.add(new Contacts(Integer.toString(contactID), contactName, contactNumber, Integer.toString(phoneContactID)));
            cursor.moveToNext();
        }
        cursor.close();
        cursor = null;

        String[] projection_email = new String[]{ContactsContract.CommonDataKinds.Email._ID,
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.DISPLAY_NAME};

        cursor = getApplicationContext().getContentResolver().query(email_uri, projection_email, null, null, ContactsContract.Contacts._ID);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            String address = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
            String emailName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DISPLAY_NAME));
            int emailID = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email._ID));
            int contactID = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
             contactsList.add(new Contacts(Integer.toString(contactID), address, address, Integer.toString(emailID)));
            cursor.moveToNext();
        }
        //  cursor = getApplicationContext().getContentResolver().query(data_uri,null,null)
        listView.setAdapter(adapter);
        endnow = android.os.SystemClock.uptimeMillis();
        Log.d("END", "TimeForContacts " + (endnow - startnow) + " ms");
    }

    public void loadContactSmart() {
        Cursor contactsCursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, ContactsContract.Contacts._ID + " ASC");
        Cursor cursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null,
                ContactsContract.Data.MIMETYPE + "=? OR " + ContactsContract.Data.MIMETYPE + "=?",
                new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE},
                ContactsContract.Data.CONTACT_ID + " ASC");

        int contact_id = contactsCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
        int contact_displayName = contactsCursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);

        int data_contact_id = cursor.getColumnIndexOrThrow(ContactsContract.Data.CONTACT_ID);
        int data1Index = cursor.getColumnIndexOrThrow(ContactsContract.Contacts.Data.DATA1);
        boolean hasData = cursor.moveToNext();

        while (contactsCursor.moveToNext()) {
            long id = contactsCursor.getLong(contact_id);
           Log.d("thao","Contact id: " + id + ", Name: " + contactsCursor.getString(contact_displayName));
            if (cursor.moveToNext()) {
                long data_contact_cursor_id = cursor.getLong(data_contact_id);
                Log.d("thao","data cursor index = "+data_contact_cursor_id);
                while (data_contact_cursor_id <= id && hasData) {
               //     if (data_contact_cursor_id == id) {
          //              Log.d("thao"," data contact id: " + data_contact_cursor_id + "contact id:" + id + " data1: " + cursor.getString(data1Index));
              //      }
                 //   cursor.moveToNext();
                    if (cursor.moveToNext()) {
                        data_contact_cursor_id = cursor.getLong(data_contact_id);
                        Log.d("thao","cursor.getLong(data_contact_id); = "+cursor.getLong(data_contact_id));
                    }
                }
            }

        }
    }





    ////////////////////////////////////////
    public Cursor getContactCursor(String stringQuery, String sortOrder) {



        Long t0 = System.currentTimeMillis();

        Uri CONTENT_URI;

        if (stringQuery == null)
            CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        else
            CONTENT_URI = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, Uri.encode(stringQuery));

        String[] PROJECTION = new String[]{
                CONTACT_ID_URI,
                NAME_URI,
                PICTURE_URI
        };

        String SELECTION = NAME_URI + " NOT LIKE ?";
        String[] SELECTION_ARGS = new String[]{"%" + "@" + "%"};

        Cursor cursor = getApplicationContext().getContentResolver().query(CONTENT_URI, PROJECTION, SELECTION, SELECTION_ARGS, sortOrder);

        Long t1 = System.currentTimeMillis();

        Log.e(TAG, "ContactCursor finished in " + (t1 - t0) / 1000 + " secs");
        Log.e(TAG, "ContactCursor found " + cursor.getCount() + " contacts");
        Log.i(TAG, "+++++++++++++++++++++++++++++++++++++++++++++++++++");

        return cursor;
    }
    public Cursor getContactDetailsCursor() {



        Long t0 = System.currentTimeMillis();

        String[] PROJECTION = new String[]{
                DATA_CONTACT_ID_URI,
                MIMETYPE_URI,
                EMAIL_URI,
                PHONE_URI
        };

      //  String SELECTION = ContactManager.NAME_URI + " NOT LIKE ?" + " AND " + "(" + MIMETYPE_URI + "=? " + " OR " + MIMETYPE_URI + "=? " + ")";

      //  String[] SELECTION_ARGS = new String[]{"%" + "@" + "%", ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE};

        Cursor cursor = getApplicationContext().getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                PROJECTION,
                null,
                null,
                null);

        Long t1 = System.currentTimeMillis();

        Log.e(TAG, "ContactDetailsCursor finished in " + (t1 - t0) / 1000 + " secs");
        Log.e(TAG, "ContactDetailsCursor found " + cursor.getCount() + " contacts");
        Log.i(TAG, "+++++++++++++++++++++++++++++++++++++++++++++++++++");

        return cursor;
    }

    public List<Contacts> getDetailedContactList(String queryString) {

        /**
         * First we fetch the contacts name and picture uri in alphabetical order for
         * display purpose and store these data in HashMap.
         */

        Cursor contactCursor = getContactCursor(queryString, NAME_URI);

        List<Integer> contactIds = new ArrayList<>();

        if (contactCursor.moveToFirst()) {
            do {
                contactIds.add(contactCursor.getInt(contactCursor.getColumnIndex(CONTACT_ID_URI)));
            } while (contactCursor.moveToNext());
        }

        HashMap<Integer, String> nameMap = new HashMap<>();
        HashMap<Integer, String> pictureMap = new HashMap<>();

        int idIdx = contactCursor.getColumnIndex(CONTACT_ID_URI);

        int nameIdx = contactCursor.getColumnIndex(NAME_URI);
        int pictureIdx = contactCursor.getColumnIndex(PICTURE_URI);

        if (contactCursor.moveToFirst()) {
            do {
                nameMap.put(contactCursor.getInt(idIdx), contactCursor.getString(nameIdx));
                pictureMap.put(contactCursor.getInt(idIdx), contactCursor.getString(pictureIdx));
            } while (contactCursor.moveToNext());
        }

        /**
         * Then we get the remaining contact information. Here email and phone
         */

        Cursor detailsCursor = getContactDetailsCursor();

        HashMap<Integer, String> emailMap = new HashMap<>();
        HashMap<Integer, String> phoneMap = new HashMap<>();

        idIdx = detailsCursor.getColumnIndex(DATA_CONTACT_ID_URI);
        int mimeIdx = detailsCursor.getColumnIndex(MIMETYPE_URI);
        int mailIdx = detailsCursor.getColumnIndex(EMAIL_URI);
        int phoneIdx = detailsCursor.getColumnIndex(PHONE_URI);

        String mailString;
        String phoneString;

        if (detailsCursor.moveToFirst()) {
            do {

                /**
                 * We forget all details which are not correlated with the contact list
                 */

                if (!contactIds.contains(detailsCursor.getInt(idIdx))) {
                    continue;
                }

                if(detailsCursor.getString(mimeIdx).equals(MAIL_TYPE)){
                    mailString = detailsCursor.getString(mailIdx);

                    /**
                     * We remove all double contact having the same email address
                     */

                    if(!emailMap.containsValue(mailString.toLowerCase()))
                        emailMap.put(detailsCursor.getInt(idIdx), mailString.toLowerCase());

                } else {
                    phoneString = detailsCursor.getString(phoneIdx);
                    phoneMap.put(detailsCursor.getInt(idIdx), phoneString);
                }

            } while (detailsCursor.moveToNext());
        }

        contactCursor.close();
        detailsCursor.close();

        /**
         * Finally the contact list is build up
         */

        List<Contacts> contacts = new ArrayList<>();

        Set<Integer> detailsKeySet = emailMap.keySet();

        for (Integer key : contactIds) {

            if(!detailsKeySet.contains(key) || (emailMap.get(key) == null && phoneMap.get(key) == null))
                continue;

            contactsList.add(new Contacts(String.valueOf(key), nameMap.get(key), emailMap.get(key), phoneMap.get(key)));
          //  contactsList.a

        }
        listView.setAdapter(adapter);

        return contacts;
    }
}