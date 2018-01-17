package mradmin.example.com.datetimeapp.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yks-11 on 1/15/18.
 */

@Entity
public class NoteEntity implements Serializable {

    @PrimaryKey
    @NonNull
    private String id;
    private int listPosition;
    private String title;
    private String description;
    private String imageUrl;
    private boolean pinned;
    private String date;
    private boolean dated;

    public NoteEntity(String id, int listPosition, String title, String description, String imageUrl, boolean pinned, String date, boolean dated) {
        this.id = id;
        this.listPosition = listPosition;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.pinned = pinned;
        this.date = date;
        this.dated = dated;
    }

    public NoteEntity() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    @Override
    public String toString() {
        return id + " === " + title + " -- " + description + " ---- " + date + " --- " + isDated();
    }
}
