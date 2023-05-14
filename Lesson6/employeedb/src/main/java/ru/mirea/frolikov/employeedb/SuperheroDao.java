package ru.mirea.frolikov.employeedb;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SuperheroDao {
    @Query("SELECT * FROM Superhero")
    List<Superhero> getAll();
    @Query("SELECT * FROM Superhero WHERE id = :id")
    Superhero getById(long id);
    @Insert(onConflict = REPLACE)
    void insert(Superhero superhero);
    @Update
    void update(Superhero superhero);
    @Delete
    void delete(Superhero superhero);
}
