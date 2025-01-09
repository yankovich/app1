package io.github.hathibelagal.eidetic;

import android.app.Activity;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;
import java.util.Set;

public class Speaker implements TextToSpeech.OnInitListener {
    private final TextToSpeech tts;
    private boolean ttsReady = false;
    private final Activity context;
    public Speaker(Activity context) {
        this.context = context;
        tts = new TextToSpeech(context, this);
    }

    @Override
    public void onInit(int i) {
        if(i == TextToSpeech.SUCCESS) {
            if(tts.setLanguage(Locale.ENGLISH) == TextToSpeech.LANG_AVAILABLE) {
                tts.setSpeechRate(1.2f);
                ttsReady = true;
                say(context.getString(R.string.tts_ready));
            }
        }
    }

    public void say(String s) {
        if(!ttsReady) { return; }
        tts.speak(s, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    public void releaseResources() {
        tts.stop();
        tts.shutdown();
    }
}
