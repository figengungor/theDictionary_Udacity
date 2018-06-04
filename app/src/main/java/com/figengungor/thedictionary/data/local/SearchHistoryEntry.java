package com.figengungor.thedictionary.data.local;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by figengungor on 5/27/2018.
 */

@Entity(tableName = "search_history")
public class SearchHistoryEntry {

    @PrimaryKey @NonNull
    private String entry;
    @ColumnInfo(name = "updated_at")
    private Date updatedAt;

    public SearchHistoryEntry(String entry, Date updatedAt) {
        this.entry = entry;
        this.updatedAt = updatedAt;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
