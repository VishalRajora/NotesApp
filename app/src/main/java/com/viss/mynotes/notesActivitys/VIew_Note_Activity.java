package com.viss.mynotes.notesActivitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.viss.mynotes.R;

public class VIew_Note_Activity extends AppCompatActivity {

    TextView titleEditText , contentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_v_iew__note_ );

        titleEditText =  findViewById ( R.id.editText_Title_edit_note );
        contentEditText =  findViewById ( R.id.editTextText_content_edit_note );


        Intent intent =  getIntent ();

        String title = intent.getStringExtra ( "Title" );
        String content = intent.getStringExtra ( "Content" );

        titleEditText.setText ( title );
        contentEditText.setText ( content );


    }
}