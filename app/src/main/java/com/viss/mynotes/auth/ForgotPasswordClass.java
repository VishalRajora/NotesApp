package com.viss.mynotes.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.viss.mynotes.R;

public class ForgotPasswordClass extends AppCompatActivity {

    EditText forgotPaswword;
    Button resetpassword;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_forgot_password_class );
        //internet check
        checkNetworkConnectionIsAvailable ();

        mAuth = FirebaseAuth.getInstance ();
        forgotPaswword = findViewById ( R.id.forgotPasswrdEditText_resetpasswrd );
        resetpassword = findViewById ( R.id.SignUpButton_SIgnup );

        progressDialog = new ProgressDialog ( ForgotPasswordClass.this );
        progressDialog.setTitle ( "Reset Password" );
        progressDialog.setMessage ( "Please wait" );
        progressDialog.setCanceledOnTouchOutside ( false );

        resetpassword.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                //internet check
                checkNetworkConnectionIsAvailable ();
                resetPasswordMethod ();
            }
        } );
    }

    private void resetPasswordMethod() {
        String resetPasswrd = forgotPaswword.getText ().toString ().trim ();
        if (resetPasswrd.isEmpty ()) {
            forgotPaswword.setError ( "Field can't be empty" );
            forgotPaswword.requestFocus ();
            return;
        } else {
            forgotPaswword.setError ( null );
        }
        if (!Patterns.EMAIL_ADDRESS.matcher ( resetPasswrd ).matches ()) {
            forgotPaswword.setError ( "Please provide valid email" );
        } else {
            forgotPaswword.setError ( null );
        }
        progressDialog.show ();
        mAuth.sendPasswordResetEmail ( resetPasswrd ).addOnCompleteListener ( new OnCompleteListener < Void > () {
            @Override
            public void onComplete(@NonNull Task < Void > task) {
                if (task.isSuccessful ()) {
                    progressDialog.dismiss ();
                    MaterialAlertDialogBuilder myDialog = new MaterialAlertDialogBuilder ( ForgotPasswordClass.this  )
                    .setTitle ( "Reset password" )
                    .setMessage ( "Check your email to reset  your password " ).setPositiveButton ( "Ok" , new DialogInterface.OnClickListener () {
                                @Override
                                public void onClick(DialogInterface dialog , int which) {
                                    startActivity ( new Intent (ForgotPasswordClass.this , LoginClass.class) );
                                    finish ();
                                }
                            } );
                    AlertDialog alertDialog = myDialog.create ();
                    alertDialog.setCancelable ( true );
                    myDialog.show ();
                } else {
                    progressDialog.dismiss ();
                   // Toast.makeText ( ForgotPasswordClass.this , "Failed" , Toast.LENGTH_SHORT ).show ();
                    if(task.getException () instanceof FirebaseAuthInvalidUserException)
                    {
                        MaterialAlertDialogBuilder myDialog = new MaterialAlertDialogBuilder ( ForgotPasswordClass.this )
                                .setTitle ( "Login" )
                                .setMessage ( "Email Not in use " ).setPositiveButton ( "Ok" , null );
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
            MaterialAlertDialogBuilder myDialog = new MaterialAlertDialogBuilder ( ForgotPasswordClass.this )
                    .setTitle ( "Connect to a Network" )
                    .setMessage ( "To use Application,turn on mobile data or connect to Wi-fi." ).setPositiveButton ( "Ok" , null );
            AlertDialog alertDialog = myDialog.create ();
            alertDialog.setCancelable ( true );
            myDialog.show ();
        }

    }
}
