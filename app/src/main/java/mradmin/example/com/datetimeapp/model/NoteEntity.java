package mradmin.example.com.datetimeapp.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yks-11 on 1/15/18.
 */

public class NoteEntity implements Serializable {

    private String id;
    private int listPosition;
    private NoteContent content;
    private boolean pinned;
    private Date date;
    private boolean dated;

    public NoteEntity(String id, int listPosition, NoteContent content, boolean pinned, Date date, boolean dated) {
        this.id = id;
        this.listPosition = listPosition;
        this.content = content;
        this.pinned = pinned;
        this.date = date;
        this.dated = dated;
    }

    public NoteEntity() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isDated() {
        return dated;
    }

    public void setDated(boolean dated) {
        this.dated = dated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getListPosition() {
        return listPosition;
    }

    public void setListPosition(int listPosition) {
        this.listPosition = listPosition;
    }

    public NoteContent getContent() {
        return content;
    }

    public void setContent(NoteContent content) {
        this.content = content;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }
}
