package com.noel201296gmail.journals.activities.model;

public class EntriesModel {

    public String noteTitle;
    public String noteTime;
    public String noteContent;

    public EntriesModel() {

    }

    public EntriesModel(String noteTitle, String noteTime, String noteContent) {
        this.noteTitle = noteTitle;
        this.noteTime = noteTime;
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

    public String getNoteTime() {
        return noteTime;
    }

    public void setNoteTime(String noteTime) {
        this.noteTime = noteTime;
    }
}