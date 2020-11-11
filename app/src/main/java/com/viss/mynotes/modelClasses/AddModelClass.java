package com.viss.mynotes.modelClasses;

import com.google.firebase.firestore.Exclude;

public class AddModelClass {
    private String noteTitle, noteContent;

    public AddModelClass() {
    }

    public AddModelClass(String noteTitle , String noteContent) {
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
    }


    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }
}
