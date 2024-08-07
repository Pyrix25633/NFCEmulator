package net.lorenzo_biral.nfc_emulator;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import net.lorenzo_biral.nfc_emulator.database.NfcDatabase;
import net.lorenzo_biral.nfc_emulator.database.entity.Card;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        NfcDatabase db = Room.databaseBuilder(getApplicationContext(),
                NfcDatabase.class, "card").build();

        EditText cardNameText = findViewById(R.id.cardNameText);
        Button newCardButton = findViewById(R.id.newCardButton);
        Spinner cardList = findViewById(R.id.cardList);
        List<Card> cards = new ArrayList<>();
        ArrayAdapter<Card> cardListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cards);
        cardList.setAdapter(cardListAdapter);
        new Thread(() -> cardListAdapter.addAll(db.cardDao().getAll())).start();

        newCardButton.setOnClickListener((View v) -> {
            new Thread(() -> {
                Card card = new Card(cardNameText.getText().toString(), "testText");
                db.cardDao().insertAll(card);
                runOnUiThread(() -> {
                    update(cards, card);
                });
            }).start();
        });
    }

    public static void update(@NotNull List<Card> cards, @NotNull Card card) {
        for(Card c : cards) {
            if(c.name.equals(card.name)) {
                c.content = card.content;
                return;
            }
        }
        cards.add(card);
    }
}