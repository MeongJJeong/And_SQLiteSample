package com.example.sqlitesample;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase sqliteDB;
    ContactDBHelper dbHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqliteDB = init_database();
        init_tables();
        load_values();

        Button buttonSave = (Button) findViewById(R.id.buttonSave);
        Button buttonClear = (Button) findViewById(R.id.buttonClear);

        buttonSave.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_values();
            }
        });

        buttonClear.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_values();
            }
        });

    }

    private void delete_values() {


        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL(ContactDBCtrct.SQL_DELETE);

        EditText editTextNum = (EditText) findViewById(R.id.edtTextNo);
        editTextNum.setText("");

        EditText editTextName = (EditText) findViewById(R.id.edtTextName);
        editTextName.setText("");

        EditText editTextPhone = (EditText) findViewById(R.id.edtTextPhone);
        editTextPhone.setText("");

        CheckBox checkBoxOver20 = (CheckBox) findViewById(R.id.checkBoxOver20);
        checkBoxOver20.setChecked(false);
    }

    private void save_values() {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL(ContactDBCtrct.SQL_DELETE);

        EditText editTextNum = (EditText) findViewById(R.id.edtTextNo);
        String noText = editTextNum.getText().toString();
        int num = 0;
        if(noText != null && !noText.isEmpty()){
            num = Integer.parseInt(noText);
        }

        EditText editTextName = (EditText) findViewById(R.id.edtTextName);
        String name = editTextName.getText().toString();

        EditText editTextPhone = (EditText) findViewById(R.id.edtTextPhone);
        String phone = editTextPhone.getText().toString();

        CheckBox checkBoxOver20 = (CheckBox) findViewById(R.id.checkBoxOver20);
        boolean isOver20 = checkBoxOver20.isChecked();

        String sqlInsert = ContactDBCtrct.SQL_INSERT +
                "(" +
                Integer.toString(num) + ", " +
                "'" + name + "', "+
                "'" + phone + "', "+
                ((isOver20 == true) ? "1" : "0") +
                ")" ;

        db.execSQL(sqlInsert) ;


    }

    private void load_values() {

        if (sqliteDB != null){

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(ContactDBCtrct.SQL_SELECT, null);

            if (cursor.moveToNext()){ //레코드가 존재한다면,
                // num (INTEGER) 값 가져오기.
                int num = cursor.getInt(0);
                EditText editTextNum = (EditText) findViewById(R.id.edtTextNo);
                editTextNum.setText(Integer.toString(num));

                // name (TEXT) 값 가져오기.
                String name = cursor.getString(1);
                EditText editTextName = (EditText)findViewById(R.id.edtTextName);
                editTextName.setText(name);

                // phone (TEXT) 값 가져오기.
                String phone = cursor.getString(2);
                EditText editTextPhone = (EditText)findViewById(R.id.edtTextPhone);
                editTextPhone.setText(phone);

                // over20 (INTEGER) 값 가져오기.
                int over20 = cursor.getInt(3);
                CheckBox checkBoxOver20 = (CheckBox) findViewById(R.id.checkBoxOver20);
                if (over20 == 0){
                    checkBoxOver20.setChecked((false));
                } else {
                    checkBoxOver20.setChecked(true);
                }

            }
        }
    }

    private void init_tables() {

        dbHelper = new ContactDBHelper(this);
    }

    private SQLiteDatabase init_database() {

        SQLiteDatabase db = null;
        File file = new File(getFilesDir(),"contact.db");

        System.out.println("PATH : "+ file.toString());
        try {
            db = SQLiteDatabase.openOrCreateDatabase(file,null);

        }catch (SQLiteException e){
                e.printStackTrace();
        }

        if (db == null){
            System.out.println("DB creation failed. " + file.getAbsolutePath());
        }

        return db ;
    }

}
