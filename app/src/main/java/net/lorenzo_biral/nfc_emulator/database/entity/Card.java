package net.lorenzo_biral.nfc_emulator.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Card {
    public Card(@NonNull String name, @NonNull String content) {
        this.name = name;
        this.content = content;
    }

    @PrimaryKey
    @NonNull
    public String name;

    @ColumnInfo(name = "content")
    @NonNull
    public String content;

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}