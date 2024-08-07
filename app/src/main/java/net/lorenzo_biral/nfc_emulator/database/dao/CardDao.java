package net.lorenzo_biral.nfc_emulator.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import net.lorenzo_biral.nfc_emulator.database.entity.Card;

import java.util.List;

@Dao
public interface CardDao {
    @Query("SELECT * FROM card")
    List<Card> getAll();

    @Query("SELECT * FROM card WHERE name = :name")
    List<Card> loadByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Card... card);

    @Delete
    void delete(Card card);

}