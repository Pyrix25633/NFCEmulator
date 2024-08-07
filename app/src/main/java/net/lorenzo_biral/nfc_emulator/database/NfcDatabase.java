package net.lorenzo_biral.nfc_emulator.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import net.lorenzo_biral.nfc_emulator.database.dao.CardDao;
import net.lorenzo_biral.nfc_emulator.database.entity.Card;

@Database(entities = {Card.class}, version = 1)
public abstract class NfcDatabase extends RoomDatabase {
    public abstract CardDao cardDao();
}