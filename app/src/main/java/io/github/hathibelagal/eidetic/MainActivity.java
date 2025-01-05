package io.github.hathibelagal.eidetic;

import android.content.DialogInterface;
import android.graphics.Color;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {

    private static final int WIN = 1;
    private static final int LOSE = 0;
    final private int nButtons = 3;
    final private ArrayList<Button> buttons = new ArrayList<>(9);
    final private List<Integer> sequence = IntStream.range(1, 10).boxed().collect(Collectors.toList());
    final private ToneGenerator toneGenerator = new ToneGenerator(ToneGenerator.TONE_DTMF_0, 70);
    private boolean gameStarted = false;
    private int expectedNumber = 1;
    private GridLayout grid;

    private long startTime = 0;

    private SavedData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = new SavedData(this);
        grid = findViewById(R.id.grid);
        resetGrid();
    }

    private void resetGrid() {
        startTime = new Date().getTime();
        grid.removeAllViews();
        buttons.clear();
        generateSequence();
        createButtons();
        expectedNumber = 1;
        gameStarted = false;
    }

    private void generateSequence() {
        Collections.shuffle(sequence);
    }

    private void createButtons() {
        int k = 0;
        for (int i = 0; i < nButtons; i++) {
            for (int j = 0; j < nButtons; j++) {
                NumberButton b = new NumberButton(MainActivity.this);
                b.setText(String.valueOf(sequence.get(k)));
                b.setValue(sequence.get(k));

                GridLayout.LayoutParams gridParams = new GridLayout.LayoutParams();
                gridParams.height = 0;
                gridParams.width = 0;
                gridParams.rowSpec = GridLayout.spec(i, 1f);
                gridParams.columnSpec = GridLayout.spec(j, 1f);

                b.setLayoutParams(gridParams);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!gameStarted && b.getValue() != expectedNumber) {
                            toneGenerator.startTone(ToneGenerator.TONE_DTMF_0, 30);
                            Toast.makeText(
                                    MainActivity.this,
                                    "Please start with 1",
                                    Toast.LENGTH_SHORT
                            ).show();
                            return;
                        }
                        if (b.getValue() == expectedNumber) {
                            if (!gameStarted) {
                                gameStarted = true;
                                activatePuzzleMode();
                            }
                            expectedNumber += 1;
                            toneGenerator.stopTone();
                            toneGenerator.startTone(b.getValue(), 100);
                            b.setVisibility(View.INVISIBLE);

                            if (expectedNumber == 10) {
                                toneGenerator.stopTone();
                                toneGenerator.startTone(ToneGenerator.TONE_DTMF_A, 200);
                                showRestart(WIN);
                            }
                        } else {
                            toneGenerator.stopTone();
                            toneGenerator.startTone(ToneGenerator.TONE_DTMF_0, 200);
                            data.resetStreak();
                            showRestart(LOSE);
                        }
                    }
                });
                buttons.add(b);
                grid.addView(b);

                k++;
            }
        }
    }

    private void showRestart(int status) {
        int timeTaken = (int)TimeUnit.MILLISECONDS.toSeconds(new Date().getTime() - startTime);
        boolean createdRecord = false;
        if(status == WIN) {
            data.incrementStreak();
            createdRecord = data.updateFastestTime(timeTaken);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(
                status == WIN ?
                        String.format(Locale.ENGLISH,
                                 getString(
                                         createdRecord ?
                                                 R.string.success_message_record
                                                 : R.string.success_message
                                 ),
                                timeTaken,
                                data.getFastestTime()
                        ) :
                        getString(R.string.game_over_message)
        );
        builder.setTitle(status == WIN ? String.format(Locale.ENGLISH, "🤩 You win!\n🙌 Streak: %d", data.getStreak())
                : "😖 Game over!");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                resetGrid();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        builder.create().show();
    }

    private void activatePuzzleMode() {
        for (Button b : buttons) {
            b.setText("?");
        }
    }
}