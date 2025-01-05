package io.github.hathibelagal.eidetic;

import android.animation.Animator;
import android.graphics.Color;
import android.graphics.Point;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {

    private final int MAX_VALUE = 9;
    private static final int WIN = 1;
    private static final int LOSE = 0;
    final private int nRows = 6;
    final private int nCols = 3;
    final private ArrayList<Button> buttons = new ArrayList<>(9);
    final private List<Integer> sequence = IntStream.range(1, 10).boxed().collect(Collectors.toList());
    final private ToneGenerator toneGenerator = new ToneGenerator(ToneGenerator.TONE_DTMF_0, 70);
    private boolean gameStarted = false;
    private int expectedNumber = 1;
    private GridLayout grid;
    private View gridContainer;

    private long startTime = 0;

    private SavedData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = new SavedData(this);
        grid = findViewById(R.id.grid);
        gridContainer = findViewById(R.id.grid_container);
        resetGrid();
    }

    private void resetGrid() {
        gridContainer.setBackgroundColor(Color.parseColor("#121212"));
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
        List<Point> taken = new ArrayList<>();
        while(true) {
            ROWS: for (int i = 0; i < nRows; i++) {
                COLS: for (int j = 0; j < nCols; j++) {
                    if(k >= MAX_VALUE) {
                        return;
                    }
                    if(Math.random() > 0.5) {
                        continue;
                    }
                    for(Point p: taken) {
                        if(p.x == i && p.y == j) {
                            continue COLS;
                        }
                    }
                    taken.add(new Point(i, j));
                    NumberButton b = new NumberButton(MainActivity.this);
                    b.setText(String.valueOf(sequence.get(k)));
                    b.setValue(sequence.get(k));

                    GridLayout.LayoutParams gridParams = new GridLayout.LayoutParams();
                    gridParams.height = 0;
                    gridParams.width = 0;
                    gridParams.rowSpec = GridLayout.spec(i, 1f);
                    gridParams.columnSpec = GridLayout.spec(j, 1f);

                    b.setLayoutParams(gridParams);
                    b.setOnClickListener(view -> {
                        Log.d("EIDETIC", String.format("%d", expectedNumber));
                        if (!gameStarted && b.getValue() != expectedNumber) {
                            toneGenerator.startTone(ToneGenerator.TONE_DTMF_0, 30);
                            Toast.makeText(MainActivity.this, "Please start with 1", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (b.getValue() == expectedNumber) {
                            if (!gameStarted) {
                                gameStarted = true;
                                activatePuzzleMode();
                            }
                            expectedNumber += 1;
                            playTone(b.getValue(), false);
                            b.animate().setDuration(200).scaleX(0).scaleY(0).setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(@NonNull Animator animator) {

                                }

                                @Override
                                public void onAnimationEnd(@NonNull Animator animator) {
                                    b.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onAnimationCancel(@NonNull Animator animator) {

                                }

                                @Override
                                public void onAnimationRepeat(@NonNull Animator animator) {

                                }
                            });

                            if (expectedNumber == MAX_VALUE + 1) {
                                playTone(ToneGenerator.TONE_DTMF_A, true);
                                showRestart(WIN);
                            }
                        } else {
                            playTone(ToneGenerator.TONE_DTMF_0, true);
                            data.resetStreak();
                            showRestart(LOSE);
                        }
                    }); buttons.add(b);
                    grid.addView(b);

                    k++;
                }
            }
        }
    }

    private void playTone(int tone, boolean isLong) {
        toneGenerator.stopTone();
        toneGenerator.startTone(tone, isLong ? 200 : 100);
    }

    private void showRestart(int status) {
        if(status == WIN) {
            gridContainer.setBackgroundColor(getColor(R.color.win));
        } else {
            gridContainer.setBackgroundColor(getColor(R.color.lose));
        }
        int timeTaken = (int) TimeUnit.MILLISECONDS.toSeconds(new Date().getTime() - startTime);
        boolean createdRecord = false;
        if (status == WIN) {
            data.incrementStreak();
            createdRecord = data.updateFastestTime(timeTaken);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(status == WIN ? String.format(Locale.ENGLISH, getString(createdRecord ? R.string.success_message_record : R.string.success_message), timeTaken, data.getFastestTime()) : getString(R.string.game_over_message));
        builder.setTitle(status == WIN ? String.format(Locale.ENGLISH, "🤩 You win!\n🙌 Streak: %d", data.getStreak()) : "😖 Game over!");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> resetGrid());

        builder.setNegativeButton("No", (dialogInterface, i) -> finish());

        builder.create().show();
    }

    private void activatePuzzleMode() {
        for (Button b : buttons) {
            b.setText("?");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_reload) {
            data.resetStreak();
            resetGrid();
        }
        if(item.getItemId() == R.id.menu_language) {

        }
        return super.onOptionsItemSelected(item);
    }
}