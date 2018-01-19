package mradmin.example.com.datetimeapp.util;

import java.sql.Date;
import java.util.Comparator;

import mradmin.example.com.datetimeapp.model.NoteEntity;

/**
 * Created by yks-11 on 1/19/18.
 */

public class NoteEntityComparator implements Comparator<NoteEntity> {

    @Override
    public int compare(NoteEntity noteEntity, NoteEntity t1) {
        if (noteEntity.isPinned()) {
            if (t1.isPinned()) {
                return noteEntity.getCreationDate() >= t1.getCreationDate() ? -1 : 1;
            } else {
                return -1;
            }
        } else {
            if (t1.isPinned()) {
                return 1;
            } else {
                return noteEntity.getCreationDate() >= t1.getCreationDate() ? -1 : 1;
            }
        }
    }
}
