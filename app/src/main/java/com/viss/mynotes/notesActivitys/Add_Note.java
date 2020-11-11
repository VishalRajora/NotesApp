package com.viss.mynotes.notesActivitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.viss.mynotes.R;
import com.viss.mynotes.mainAcitivty.MainActivity;
import com.viss.mynotes.modelClasses.AddModelClass;

public class Add_Note extends AppCompatActivity {

    FirebaseFirestore db;
    FloatingActionButton floatingActionButton;
    EditText noteTitle , noteContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_add__note );

        db = FirebaseFirestore.getInstance ();

        noteTitle = findViewById ( R.id.editText_Title_edit_note );
        noteContent =  findViewById ( R.id.editTextText_content_edit_note );


        floatingActionButton = findViewById ( R.id.save_floatingActionButtonEditNote );
        floatingActionButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                saveToFirestore();

               // startActivity ( new Intent (Add_Note.this , MainActivity.class ) );
                finish ();
            }
        } );

    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed ();
//        saveToFirestore ();
//        finish ();
//    }

    private void saveToFirestore()
    {
        String title =  noteTitle.getText ().toString ().trim ();
        String content =  noteContent.getText ().toString ().trim ();

        FirebaseUser user = FirebaseAuth.getInstance ().getCurrentUser ();

        String user_id = user.getUid ();

        AddModelClass addModelClass  =  new AddModelClass ( title , content );

        db.collection ( "UserData" ).document (user_id).collection ( "UserNotes" ).add ( addModelClass ).addOnCompleteListener ( new OnCompleteListener < DocumentReference > () {
            @Override
            public void onComplete(@NonNull Task < DocumentReference > task) {
                if(task.isSuccessful ())
                {
//                    startActivity ( new Intent (Add_Note.this , MainActivity.class ) );
//                 finish ();
                }

            }
        } ).addOnFailureListener ( new OnFailureListener () {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText ( Add_Note.this , "Failed to save note" , Toast.LENGTH_SHORT ).show ();
                finish ();
            }
        } );

    }
}