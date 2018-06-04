package com.figengungor.thedictionary.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by figengungor on 5/27/2018.
 */

@Dao
public interface SearchHistoryDao {

    @Query("SELECT * FROM search_history ORDER BY updated_at DESC")
    LiveData<List<SearchHistoryEntry>> loadAllSearchHistoryEntries();

    @Query("SELECT * FROM search_history ORDER BY updated_at DESC LIMIT 10")
    List<SearchHistoryEntry> getSearchHistoryEntries();

    @Insert
    void insertSearchHistoryEntry(SearchHistoryEntry searchHistoryEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateSearchHistoryEntry(SearchHistoryEntry searchHistoryEntry);

    @Delete
    void deleteSearchHistoryEntry(SearchHistoryEntry searchHistoryEntry);

    @Query("SELECT * FROM search_history WHERE entry = :entry")
    LiveData<SearchHistoryEntry> loadSearchHistoryEntryByEntryLD(String entry);

    @Query("SELECT * FROM search_history WHERE entry = :entry")
    SearchHistoryEntry loadSearchHistoryEntryByEntry(String entry);

    @Query("DELETE FROM search_history")
    void deleteAll();

}
