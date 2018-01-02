package com.mafeibiao.testapplication.contentProvider;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.mafeibiao.testapplication.R;

public class ProviderActivity extends Activity {
    private static final String TAG = "ProviderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        // Uri uri = Uri.parse("content://com.ryg.chapter_2.book.provider");
        // getContentResolver().query(uri, null, null, null, null);
        // getContentResolver().query(uri, null, null, null, null);
        // getContentResolver().query(uri, null, null, null, null);

        Uri bookUri = Uri.parse("content://com.ryg.chapter_2.book.provider/book");
        ContentValues values = new ContentValues();
        values.put("_id", 6);
        values.put("name", "程序设计的艺术");
        getContentResolver().insert(bookUri, values);
        Cursor bookCursor = getContentResolver().query(bookUri, new String[]{"_id", "name"}, null, null, null);
        while (bookCursor.moveToNext()) {

        }
        bookCursor.close();

    }
}
