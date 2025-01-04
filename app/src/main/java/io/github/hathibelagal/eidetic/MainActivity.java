package io.github.hathibelagal.eidetic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private GridLayout grid;
    private ArrayList<Button> buttons;
    final private int nButtons = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        grid = findViewById(R.id.grid);
        resetGrid();
    }

    private void resetGrid() {
        if(grid.getChildCount() == 0) {
            buttons = new ArrayList<>();
            createButtons();
        }
    }

    private void createButtons() {
        for(int i=0; i<nButtons; i++) {
            for(int j=0;j<nButtons;j++) {
                Button b = new Button(MainActivity.this);
                b.setText(String.valueOf(i));

                GridLayout.LayoutParams gridParams = new GridLayout.LayoutParams();
                gridParams.height = 0;
                gridParams.width = 0;
                gridParams.rowSpec = GridLayout.spec(i, 1f);
                gridParams.columnSpec = GridLayout.spec(j, 1f);
                gridParams.setMargins(0, 0, 0, 0);

                b.setLayoutParams(gridParams);
                buttons.add(b);
                grid.addView(b);
            }
        }
    }
}