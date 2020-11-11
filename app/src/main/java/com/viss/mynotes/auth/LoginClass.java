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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.viss.mynotes.R;
import com.viss.mynotes.mainAcitivty.MainActivity;

public class LoginClass extends AppCompatActivity {
    EditText emailEditText, passwordEditText;
    TextView registerTextView, forgotPassword;
    Button loginBtton;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_login_class );

        //internet check
        checkNetworkConnectionIsAvailable ();

        mAuth = FirebaseAuth.getInstance ();


        progressDialog = new ProgressDialog ( LoginClass.this );
        progressDialog.setTitle ( "Login" );
        progressDialog.setMessage ( "Please wait" );
        progressDialog.setCanceledOnTouchOutside ( false );


        forgotPassword = findViewById ( R.id.forgotPassswordEditTextLogin );
        emailEditText = findViewById ( R.id.editTextTextEmailSignup );
        passwordEditText = findViewById ( R.id.editTextTextPasswordSignup );
        registerTextView = findViewById ( R.id.loginHereText );
        registerTextView.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent ( LoginClass.this , SignUpClass.class ) );
                finish ();
            }
        } );
        loginBtton = findViewById ( R.id.SignUpButton_SIgnup );
        loginBtton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                //progressDialog.show ();
                //internet check
                checkNetworkConnectionIsAvailable ();
                loginMethod ();
            }
        } );
        forgotPassword.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent ( LoginClass.this , ForgotPasswordClass.class ) );
            }
        } );


    }

    @Override
    protected void onStart() {
        super.onStart ();
        user = mAuth.getCurrentUser ();
        if (user != null && user.isEmailVerified ()) {
            startActivity ( new Intent ( LoginClass.this , MainActivity.class ) );
            finish ();
        }
    }

    private void loginMethod() {
        //    Toast.makeText ( this , "click" , Toast.LENGTH_SHORT ).show ();

        String login_email = emailEditText.getText ().toString ().trim ();
        String login_password = passwordEditText.getText ().toString ().trim ();

        if (login_email.isEmpty ()) {
            emailEditText.setError ( "Field can't be empty" );
            emailEditText.requestFocus ();
            return;
        } else {
            emailEditText.setError ( null );
        }
        if (!Patterns.EMAIL_ADDRESS.matcher ( login_email ).matches ()) {
            emailEditText.setError ( "Enter Correct Email" );
            emailEditText.requestFocus ();
            return;
        } else {
            emailEditText.setError ( null );
        }
        if (login_password.isEmpty ()) {
            passwordEditText.setError ( "Field can't be empty" );
            passwordEditText.requestFocus ();
            return;
        } else {
            passwordEditText.setError ( null );
        }
        if (login_password.length () < 6) {
            passwordEditText.setError ( "Min password length should be 6 character!" );
            passwordEditText.requestFocus ();
            return;
        } else {
            passwordEditText.setError ( null );
        }
        progressDialog.show ();

        mAuth.signInWithEmailAndPassword ( login_email , login_password ).addOnCompleteListener ( new OnCompleteListener < AuthResult > () {
            @Override
            public void onComplete(@NonNull Task < AuthResult > task) {

                if (task.isSuccessful ()) {
                    FirebaseUser currentUser = FirebaseAuth.getInstance ().getCurrentUser ();
                    if (currentUser != null && currentUser.isEmailVerified ()) {
                        progressDialog.dismiss ();
                        startActivity ( new Intent ( LoginClass.this , MainActivity.class ) );
                        finish ();

                    } else {
                        currentUser.sendEmailVerification ();
                        progressDialog.dismiss ();
                        // Toast.makeText ( LoginClass.this , "Check your email to verify your account & login" , Toast.LENGTH_SHORT ).show ();
                        MaterialAlertDialogBuilder myDialog = new MaterialAlertDialogBuilder ( LoginClass.this )
                                .setTitle ( "Login" )
                                .setMessage ( "Check your email to verify your account " ).setPositiveButton ( "Ok" , null );
                        AlertDialog alertDialog = myDialog.create ();
                        alertDialog.setCancelable ( true );
                        myDialog.show ();
                    }
                } else {
                    progressDialog.dismiss ();
                    // Toast.makeText ( LoginClass.this , "Failed" + task.getException ().toString () , Toast.LENGTH_SHORT ).show ();
                    if (task.getException () instanceof FirebaseAuthInvalidCredentialsException) {

                        MaterialAlertDialogBuilder myDialog = new MaterialAlertDialogBuilder ( LoginClass.this )
                                .setTitle ( "Login" )
                                .setMessage ( "Invalid Password " ).setPositiveButton ( "Ok" , null );
                        AlertDialog alertDialog = myDialog.create ();
                        alertDialog.setCancelable ( true );
                        myDialog.show ();
                    } else if (task.getException () instanceof FirebaseAuthInvalidUserException) {
                        MaterialAlertDialogBuilder myDialog = new MaterialAlertDialogBuilder ( LoginClass.this )
                                .setTitle ( "Login" )
                                .setMessage ( "Email Not in use " ).setPositiveButton ( "Ok" , null );
                        AlertDialog alertDialog = myDialog.create ();
                        alertDialog.setCancelable ( true );
                        myDialog.show ();
                    }
                }
            }
        } ).addOnFailureListener ( new OnFailureListener () {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss ();
                // Toast.makeText ( LoginClass.this , "Failed" + e.toString () , Toast.LENGTH_SHORT ).show ();

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
            MaterialAlertDialogBuilder myDialog = new MaterialAlertDialogBuilder ( LoginClass.this )
                    .setTitle ( "Connect to a Network" )
                    .setMessage ( "To use Application,turn on mobile data or connect to Wi-fi." ).setPositiveButton ( "Ok" , null );
            AlertDialog alertDialog = myDialog.create ();
            alertDialog.setCancelable ( true );
            myDialog.show ();
        }

    }
}