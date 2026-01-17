package model.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import model.domain.Notes;

@Dao
public interface NotesDao {

    @Query("SELECT * FROM notes")
    List<Notes> findAll();

    @Query("SELECT * FROM notes WHERE title = :title")
    List<Notes> findByTitle(String title);

    @Insert
    void insert (Notes task);

    @Update
    void update (Notes task);

    @Delete
    void delete (Notes task);

    @Query("DELETE FROM notes WHERE title = :title")
    void deleteByTitle (String title);
}
