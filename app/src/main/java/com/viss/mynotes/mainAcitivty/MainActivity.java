package com.viss.mynotes.mainAcitivty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.viss.mynotes.R;
import com.viss.mynotes.adapterClasses.NoteAdapter;
import com.viss.mynotes.auth.LoginClass;
import com.viss.mynotes.modelClasses.AddModelClass;
import com.viss.mynotes.notesActivitys.Add_Note;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    FloatingActionButton floatingActionButton;

    //RecyclerView
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    //adapterClass
    NoteAdapter adapter;

    //firebase
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser mUser;

    ArrayList < AddModelClass > list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        toolbar = findViewById ( R.id.main_toolbar );
        setSupportActionBar ( toolbar );

        mAuth = FirebaseAuth.getInstance ();
        db = FirebaseFirestore.getInstance ();
        mUser = FirebaseAuth.getInstance ().getCurrentUser ();


        //set Drawer Layout in your activity
        drawerLayout = findViewById ( R.id.drawer_layout );
        navigationView = findViewById ( R.id.navigation_view );
        navigationView.setNavigationItemSelectedListener ( this );
        actionBarDrawerToggle = new ActionBarDrawerToggle ( MainActivity.this , drawerLayout , toolbar , R.string.openDrawer , R.string.closeDrawer );
        drawerLayout.addDrawerListener ( actionBarDrawerToggle );
        actionBarDrawerToggle.syncState ();

        //set the UserName and Email of current user into nav header
        View headerView = navigationView.getHeaderView ( 0 );
        TextView userName = headerView.findViewById ( R.id.header_user_name );
        TextView userEmail = headerView.findViewById ( R.id.header_email );

        String dataUser = mUser.getUid ();

        db.collection ( "AuthDetails" ).document ( dataUser ).addSnapshotListener ( new EventListener < DocumentSnapshot > () {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value , @Nullable FirebaseFirestoreException error) {
                //if is  necessary otherwise crush a app wile logout
                if (error != null) {
                    Log.d ( "Errorr" , error.toString () );
                } else {
                    userName.setText ( value.getString ( "name" ) );
                    userEmail.setText ( value.getString ( "email" ) );
                }
            }
        } );


        floatingActionButton = findViewById ( R.id.add_floatingActionButton );
        floatingActionButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent ( MainActivity.this , Add_Note.class ) );
            }
        } );

        //Method to set the recyclerView into activity
        setUpRecyclerView ();

    }

    private void setUpRecyclerView() {

        recyclerView = findViewById ( R.id.recycelr_view );

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize ( true );

        // use a linear layout manager
        layoutManager = new StaggeredGridLayoutManager ( 2 , LinearLayout.VERTICAL );
        recyclerView.setLayoutManager ( layoutManager );


        FirebaseUser user = FirebaseAuth.getInstance ().getCurrentUser ();
        String ID = user.getUid ();
        Query query = db.collection ( "UserData" ).document ( ID ).collection ( "UserNotes" ).orderBy ( "noteTitle" , Query.Direction.ASCENDING );
        FirestoreRecyclerOptions < AddModelClass > options = new FirestoreRecyclerOptions.Builder < AddModelClass > ().setQuery ( query , AddModelClass.class ).build ();

        adapter = new NoteAdapter ( options , getApplicationContext () );
        recyclerView.setAdapter ( adapter );
    }

    @Override
    protected void onStart() {
        super.onStart ();
        adapter.startListening ();
    }

    @Override
    protected void onStop() {
        super.onStop ();
        adapter.stopListening ();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer ( GravityCompat.START );
        int ID = item.getItemId ();
        switch (ID) {
            case R.id.note_id:
                break;

            case R.id.addNote_id:
                startActivity ( new Intent ( MainActivity.this , Add_Note.class ) );
                break;

            case R.id.rateApp_id:
                Toast.makeText ( this , "rate app" , Toast.LENGTH_SHORT ).show ();
                break;

            case R.id.shareApp_id:
                Toast.makeText ( this , "share click" , Toast.LENGTH_SHORT ).show ();
                break;

            case R.id.logout_id:
                //Toast.makeText ( this , "logout click" , Toast.LENGTH_SHORT ).show ();
                mAuth.signOut ();
                startActivity ( new Intent ( MainActivity.this , LoginClass.class ) );
                finish ();
                break;

        }
        return true;
    }
}
