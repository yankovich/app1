package io.github.hathibelagal.eidetic;

import android.content.Context;
import android.graphics.Color;
import android.media.ToneGenerator;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.content.res.AppCompatResources;

public class NumberButton extends androidx.appcompat.widget.AppCompatButton {

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    private int value = -1;

    public NumberButton(Context context) {
        super(context);
        setTextSize(24f);
        setTextColor(Color.WHITE);
        setBackground(AppCompatResources.getDrawable(context, R.drawable.number_button_ready));
    }

    public NumberButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumberButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
