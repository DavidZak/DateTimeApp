package mradmin.example.com.datetimeapp.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yks-11 on 1/15/18.
 */

public class NoteContent implements Serializable {

    private String title;
    private String description;
    private String imageUrl;
    private Date time;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public NoteContent(String title, String description, String imageUrl, Date time) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.time = time;
    }

    public NoteContent() {
    }

    public NoteContent(String title, String description, String imageUrl) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
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
}
