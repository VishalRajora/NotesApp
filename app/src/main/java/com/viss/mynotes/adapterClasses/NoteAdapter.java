package com.viss.mynotes.adapterClasses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.viss.mynotes.R;
import com.viss.mynotes.modelClasses.AddModelClass;
import com.viss.mynotes.notesActivitys.Edit_Notes;
import com.viss.mynotes.notesActivitys.VIew_Note_Activity;

public class NoteAdapter extends FirestoreRecyclerAdapter < AddModelClass, NoteAdapter.MyNoteViewHolderClass > implements PopupMenu.OnMenuItemClickListener {

    Context context;
    String UID;
    FirebaseAuth mAuth = FirebaseAuth.getInstance ();
    FirebaseFirestore db = FirebaseFirestore.getInstance ();
    Intent longClickIntent, onClickIntent;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public NoteAdapter(@NonNull FirestoreRecyclerOptions < AddModelClass > options , Context context) {
        super ( options );
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyNoteViewHolderClass holder , int position , @NonNull AddModelClass model) {
        holder.title.setText ( model.getNoteTitle () );
        holder.content.setText ( model.getNoteContent () );
        holder.cardView.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
              //  Toast.makeText ( v.getContext () , "done" , Toast.LENGTH_SHORT ).show ();
                onClickIntent = new Intent ( v.getContext () , VIew_Note_Activity.class );
                onClickIntent.putExtra ( "Title" , model.getNoteTitle () );
                onClickIntent.putExtra ( "Content" , model.getNoteContent () );
                v.getContext ().startActivity ( onClickIntent );
            }
        } );
        //to extract the current Note id(this is use for delete and edit the current note)

        holder.cardView.setOnLongClickListener ( new View.OnLongClickListener () {
            @Override
            public boolean onLongClick(View v) {
                UID = NoteAdapter.this.getSnapshots ().getSnapshot ( position ).getId ();
                longClickIntent = new Intent ( context , Edit_Notes.class );
                longClickIntent.addFlags ( Intent.FLAG_ACTIVITY_NEW_TASK );
                longClickIntent.putExtra ( "Title" , model.getNoteTitle () );
                longClickIntent.putExtra ( "Content" , model.getNoteContent () );
                longClickIntent.putExtra ( "noteID" , UID );

                //this is method
                longClickMenu ( v );

                return true;
            }
        } );

    }


    @NonNull
    @Override
    public MyNoteViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent , int viewType) {

        View view = LayoutInflater.from ( parent.getContext () ).inflate ( R.layout.custom_layout_item_view , parent , false );
        return new MyNoteViewHolderClass ( view );
    }

    private void longClickMenu(View v) {

        PopupMenu popupMenu = new PopupMenu ( context , v );
        popupMenu.inflate ( R.menu.popup_menu );
        popupMenu.show ();
        popupMenu.setOnMenuItemClickListener ( NoteAdapter.this );

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        int menuItemID = item.getItemId ();
        switch (menuItemID) {
            case R.id.editNote_menu:
              //  Toast.makeText ( context , "editClick" , Toast.LENGTH_SHORT ).show ();
                updateNote ();
                break;

            case R.id.deletenote_menu:
              //  Toast.makeText ( context , "DeleteClick" , Toast.LENGTH_SHORT ).show ();
                deleteNote ();
                break;
        }
        return true;
    }

    private void deleteNote() {

        FirebaseUser user = FirebaseAuth.getInstance ().getCurrentUser ();
        String user_id = user.getUid ();
        DocumentReference documentReference = db.collection ( "UserData" ).document ( user_id ).collection ( "UserNotes" ).document ( UID );
        documentReference.delete ().addOnSuccessListener ( new OnSuccessListener < Void > () {
            @Override
            public void onSuccess(Void aVoid) {
               // Toast.makeText ( context , "Done delete" , Toast.LENGTH_SHORT ).show ();
            }
        } ).addOnFailureListener ( new OnFailureListener () {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText ( context , "failed" , Toast.LENGTH_SHORT ).show ();
            }
        } );

    }

    private void updateNote() {
        context.startActivity ( longClickIntent );

    }

    class MyNoteViewHolderClass extends RecyclerView.ViewHolder {
        TextView title, content;
        CardView cardView;

        public MyNoteViewHolderClass(@NonNull View itemView) {
            super ( itemView );
            title = itemView.findViewById ( R.id.recycler_title );
            content = itemView.findViewById ( R.id.recyclerView_content );
            cardView = itemView.findViewById ( R.id.cardView );
        }
    }


}
