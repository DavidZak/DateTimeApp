package mradmin.example.com.datetimeapp.model;

/**
 * Created by yks-11 on 1/15/18.
 */

public class NoteEntity {

    private String id;
    private int listPosition;
    private NoteContent content;
    private boolean pinned;

    public NoteEntity() {
    }

    public NoteEntity(String id, int listPosition, NoteContent content, boolean pinned) {
        this.id = id;
        this.listPosition = listPosition;
        this.content = content;
        this.pinned = pinned;
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
