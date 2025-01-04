package io.github.hathibelagal.eidetic;

import android.media.ToneGenerator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {

    final private int nButtons = 3;
    private boolean gameStarted = false;
    private int expectedNumber = 1;
    final private ArrayList<Button> buttons = new ArrayList<>(9);
    final private List<Integer> sequence = IntStream.range(1, 10)
            .boxed()
            .collect(Collectors.toList());
    final private ToneGenerator toneGenerator = new ToneGenerator(
            ToneGenerator.TONE_DTMF_0, 70);
    private GridLayout grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        grid = findViewById(R.id.grid);
        resetGrid();
    }

    private void resetGrid() {
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
                        if(!gameStarted && b.getValue() != expectedNumber) {
                            toneGenerator.startTone(ToneGenerator.TONE_DTMF_0, 30);
                            return;
                        }
                        if(b.getValue() == expectedNumber) {
                            if(!gameStarted) {
                                gameStarted = true;
                            }
                            expectedNumber += 1;
                            toneGenerator.stopTone();
                            toneGenerator.startTone(b.getValue(), 100);
                            b.setVisibility(View.INVISIBLE);

                            if(expectedNumber == 10) {
                                resetGrid();
                            }
                        } else {
                            resetGrid();
                        }
                    }
                });
                buttons.add(b);
                grid.addView(b);

                k++;
            }
        }
    }
}