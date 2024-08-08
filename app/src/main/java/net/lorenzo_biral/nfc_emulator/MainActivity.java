package net.lorenzo_biral.nfc_emulator;

import android.content.Context;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import net.lorenzo_biral.nfc_emulator.database.NfcDatabase;
import net.lorenzo_biral.nfc_emulator.database.entity.Card;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

        Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        NfcDatabase db = Room.databaseBuilder(getApplicationContext(),
                NfcDatabase.class, "card").build();

        EditText cardNameEditText = findViewById(R.id.cardNameText);
        Button createButton = findViewById(R.id.createButton);
        Spinner cardSpinner = findViewById(R.id.cardList);
        Button deleteButton = findViewById(R.id.deleteButton);
        Button emulateButton = findViewById(R.id.emulateButton);

        setCurrentDateAsText(cardNameEditText);

        List<Card> cards = new ArrayList<>();
        ArrayAdapter<Card> cardSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cards);
        cardSpinner.setAdapter(cardSpinnerAdapter);
        new Thread(() -> cardSpinnerAdapter.addAll(db.cardDao().getAll())).start();

        createButton.setOnClickListener((View v) -> new Thread(() -> {
            Card card = new Card(cardNameEditText.getText().toString(), "testText");
            db.cardDao().insertAll(card);
            runOnUiThread(() -> {
                addOrUpdate(cards, cardSpinnerAdapter, card);
                setCurrentDateAsText(cardNameEditText);
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));
            });
        }).start());

        deleteButton.setOnClickListener((View v) -> new Thread(() -> {
            if(cards.isEmpty()) return;
            Card card = (Card)cardSpinner.getSelectedItem();
            System.out.println(card);
            db.cardDao().delete(card);
            runOnUiThread(() -> {
                cardSpinnerAdapter.remove(card);
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK));
            });
        }).start());

        emulateButton.setOnClickListener((View v) -> System.out.println("emulate"));
    }

    public static void setCurrentDateAsText(@NotNull EditText cardNameEditText) {
        LocalDateTime dateTime = LocalDateTime.now();
        String month = dateTime.getMonth().name();
        month = month.charAt(0) + month.substring(1, 3).toLowerCase();
        cardNameEditText.setText(String.format(Locale.ENGLISH, "Card %s %d %d %d:%d",
                month, dateTime.getDayOfMonth(), dateTime.getYear(),
                dateTime.getHour(), dateTime.getMinute()));
    }

    public static void addOrUpdate(@NotNull List<Card> cards,
                                   @NotNull ArrayAdapter<Card> cardSpinnerAdapter,
                                   @NotNull Card card) {
        for(Card c : cards) {
            if(c.name.equals(card.name)) {
                c.content = card.content;
                return;
            }
        }
        cardSpinnerAdapter.add(card);
    }
}