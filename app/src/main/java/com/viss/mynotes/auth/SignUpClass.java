package com.viss.mynotes.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.viss.mynotes.R;
import com.viss.mynotes.mainAcitivty.MainActivity;
import com.viss.mynotes.modelClasses.SignUpModelClass;

public class SignUpClass extends AppCompatActivity {
    EditText username, phone, email, password;
    Button signup;
    TextView loginHereText;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_sign_up_class );
        //internet check
        checkNetworkConnectionIsAvailable ();

        mAuth = FirebaseAuth.getInstance ();
        db = FirebaseFirestore.getInstance ();

        currentUser = FirebaseAuth.getInstance ().getCurrentUser ();
        if (currentUser != null && currentUser.isEmailVerified ()) {
            startActivity ( new Intent ( SignUpClass.this , MainActivity.class ) );
            finish ();
        }

        //progress dialog set
        progressDialog = new ProgressDialog ( SignUpClass.this );
        progressDialog.setTitle ( "Registration" );
        progressDialog.setMessage ( "Please Wait" );
        progressDialog.setCanceledOnTouchOutside ( false );

        username = findViewById ( R.id.editTextTextuserName );
        phone = findViewById ( R.id.editTextContact );
        email = findViewById ( R.id.editTextTextEmailSignup );
        password = findViewById ( R.id.editTextTextPasswordSignup );

        loginHereText = findViewById ( R.id.loginHereText );
        loginHereText.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent ( SignUpClass.this , LoginClass.class ) );
            finish ();
            }
        } );
        signup = findViewById ( R.id.SignUpButton_SIgnup );
        signup.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                //internet check
                checkNetworkConnectionIsAvailable ();
                signUpMethod ();
            }
        } );
    }

    private void signUpMethod() {
       // Toast.makeText ( this , "Done" , Toast.LENGTH_SHORT ).show ();
        String user_name = username.getText ().toString ().trim ();
        String user_phone = phone.getText ().toString ().trim ();
        String user_email = email.getText ().toString ().trim ();
        String user_password = password.getText ().toString ().trim ();


        if (user_name.isEmpty ()) {
            username.setError ( "Field Can't be empty" );
            username.requestFocus ();
            return;
        } else {
            username.setError ( null );
        }

        if (user_phone.isEmpty ()) {
            phone.setError ( "Field Can't be empty" );
            phone.requestFocus ();
            return;
        } else {
            phone.setError ( null );
        }
        if (user_phone.length () < 10) {
            phone.setError ( "Enter Valid Mobile Number" );
            phone.requestFocus ();
            return;
        }
        if (!user_phone.matches ( "[1-9][0-9]{9}" )) {
            phone.setError ( "Enter Valid Mobile Number" );
            phone.requestFocus ();
            return;
        }
        if (user_email.isEmpty ()) {
            email.setError ( "Field Can't be empty" );
            email.requestFocus ();
            return;
        } else if (!user_email.contains ( "@" )) {
            email.setError ( "Enter Valid Email" );
        } else {
            email.setError ( null );
        }
        if (!Patterns.EMAIL_ADDRESS.matcher ( user_email ).matches ()) {
            email.setError ( "Please provide valid email" );
        } else {
            email.setError ( null );
        }
        if (user_password.isEmpty ()) {
            password.setError ( "Field Can't be empty" );
            password.requestFocus ();
            return;
        } else {
            password.setError ( null );
        }
        if (user_password.length () < 6) {
            password.setError ( "Min password length should be 6 character!" );
            password.requestFocus ();
            return;
        } else {
            password.setError ( null );
        }
        progressDialog.show ();

        mAuth.createUserWithEmailAndPassword ( user_email , user_password ).addOnCompleteListener ( new OnCompleteListener < AuthResult > () {
            @Override
            public void onComplete(@NonNull Task < AuthResult > task) {

                if (task.isSuccessful ()) {

                    FirebaseUser user = FirebaseAuth.getInstance ().getCurrentUser ();
                    String user_id = user.getUid ();
                    SignUpModelClass signUpModelClass = new SignUpModelClass ( user_name , user_phone , user_email );
                    db.collection ( "AuthDetails" ).document ( user_id ).set ( signUpModelClass ).addOnSuccessListener ( new OnSuccessListener < Void > () {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if(user.isEmailVerified ())
                            {
                                progressDialog.dismiss ();
                                startActivity ( new Intent ( SignUpClass.this , MainActivity.class ) );
                                finish ();
                            }
                            else
                            {
                                progressDialog.dismiss ();
                                user.sendEmailVerification ();
                               // Toast.makeText ( SignUpClass.this , "Check your email to verify your account & login" , Toast.LENGTH_SHORT ).show ();
                                MaterialAlertDialogBuilder myDialog = new MaterialAlertDialogBuilder ( SignUpClass.this )
                                        .setTitle ( "Login" )
                                        .setMessage ( "Check your email to verify your account & login" ).setPositiveButton ( "Ok" , null );
                                AlertDialog alertDialog = myDialog.create ();
                                alertDialog.setCancelable ( true );
                                myDialog.show ();
                            }


                        }
                    } ).addOnFailureListener ( new OnFailureListener () {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                           // Toast.makeText ( SignUpClass.this , "Failed" + e.toString () , Toast.LENGTH_SHORT ).show ();
                            progressDialog.dismiss ();
                        }
                    } );
                } else {
                  //  Toast.makeText ( SignUpClass.this , "Failed" + task.getException ().toString () , Toast.LENGTH_SHORT ).show ();
                    progressDialog.dismiss ();
                if(task.getException () instanceof FirebaseAuthUserCollisionException)
                {
                    MaterialAlertDialogBuilder myDialog = new MaterialAlertDialogBuilder ( SignUpClass.this )
                            .setTitle ( "Login" )
                            .setMessage ( "Email already in use " ).setPositiveButton ( "Ok" , null );
                    AlertDialog alertDialog = myDialog.create ();
                    alertDialog.setCancelable ( true );
                    myDialog.show ();
                }
                }

            }
        } );
    }

    //now check the internet conection
    public void checkNetworkConnectionIsAvailable() {
        ConnectivityManager connectivityManager = ( ConnectivityManager ) getSystemService ( Context.CONNECTIVITY_SERVICE );
        NetworkInfo activNetwork = connectivityManager.getActiveNetworkInfo ();
        boolean isConnected = activNetwork != null && activNetwork.isConnected ();

        if (isConnected) {
            // onStart ();
        } else {
            MaterialAlertDialogBuilder myDialog = new MaterialAlertDialogBuilder ( SignUpClass.this )
                    .setTitle ( "Connect to a Network" )
                    .setMessage ( "To use Application,turn on mobile data or connect to Wi-fi." ).setPositiveButton ( "Ok" , null );
            AlertDialog alertDialog = myDialog.create ();
            alertDialog.setCancelable ( true );
            myDialog.show ();
        }

    }
}