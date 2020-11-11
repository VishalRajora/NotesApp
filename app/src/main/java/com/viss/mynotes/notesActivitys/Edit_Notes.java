package com.viss.mynotes.notesActivitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.viss.mynotes.R;
import com.viss.mynotes.mainAcitivty.MainActivity;
import com.viss.mynotes.modelClasses.AddModelClass;

import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

public class Edit_Notes extends AppCompatActivity {

    String noteID;
    Intent intent;
    EditText titleEditNotes, contentEditNotes;
    FirebaseFirestore db = FirebaseFirestore.getInstance ();
CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.edit__notes_activity );

        intent = getIntent ();
        titleEditNotes = findViewById ( R.id.editText_Title_edit_note );
        contentEditNotes = findViewById ( R.id.editTextText_content_edit_note );

        String title = intent.getStringExtra ( "Title" );
        String content = intent.getStringExtra ( "Content" );
        noteID = intent.getStringExtra ( "noteID" );

        titleEditNotes.setText ( title );
        contentEditNotes.setText ( content );

       // coordinatorLayout =  findViewById ( R.id.editNoteCoordinatorLayout );

//        FloatingActionButton floatingActionButton = findViewById ( R.id.save_floatingActionButtonEditNote );
//        floatingActionButton.setOnClickListener ( new View.OnClickListener () {
//            @Override
//            public void onClick(View v) {
//
//                updateNote ();
//            }
//        } );

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed ();
        updateNote ();
        finish ();
    }

    private void updateNote() {
        String nTitle = titleEditNotes.getText ().toString ();
        String nContent = contentEditNotes.getText ().toString ();


        FirebaseUser user = FirebaseAuth.getInstance ().getCurrentUser ();
        String user_id = user.getUid ();
        DocumentReference documentReference = db.collection ( "UserData" ).document ( user_id ).collection ( "UserNotes" ).document ( noteID );
        Map < String, Object > modelClassMap = new HashMap <> ();
        modelClassMap.put ( "noteContent" , nContent );
        modelClassMap.put ( "noteTitle" , nTitle );
        documentReference.update ( modelClassMap ).addOnSuccessListener ( new OnSuccessListener < Void > () {
            @Override
            public void onSuccess(Void aVoid) {

            }
        } ).addOnFailureListener ( new OnFailureListener () {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText ( Edit_Notes.this , "failed" + e.toString () , LENGTH_SHORT ).show ();
            }
        } );

    }
}