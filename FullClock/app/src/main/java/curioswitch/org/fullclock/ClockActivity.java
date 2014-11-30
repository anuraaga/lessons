package curioswitch.org.fullclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.common.collect.Iterators;

import java.util.Iterator;
import java.util.Locale;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class ClockActivity extends ActionBarActivity {

    @InjectView(R.id.topLayout) RelativeLayout topLayout;
    TextView clockText;
    @InjectView(R.id.hideBar) SeekBar hideBar;

    TextToSpeech textToSpeech;

    Iterator<Integer> backgrounds = Iterators.cycle(
            R.drawable.maki, R.drawable.ishihara, R.drawable.aya);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        clockText = (TextView) findViewById(R.id.clockText);

        ButterKnife.inject(this);

        hideBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                topLayout.getBackground().setAlpha((int)((progress / 100.0) * 255));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        setTime();
        setBackground();

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setTime();
                setBackground();
            }
        }, intentFilter);

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                textToSpeech.setLanguage(Locale.ENGLISH);
            }
        });
    }

    @OnClick(R.id.changeColorButton)
    void changeColorButtonClicked() {
        Random random = new Random();
        int color = Color.rgb(
                random.nextInt(256), random.nextInt(256), random.nextInt(256));
        clockText.setTextColor(color);
        textToSpeech.speak(clockText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
    }

    @OnClick(R.id.mapButton)
    void mapButonClicked() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    private void setBackground() {
        int backgroundId = backgrounds.next();
        topLayout.setBackground(getResources().getDrawable(backgroundId));
    }

    private void setTime() {
        String timeText = DateUtils.formatDateTime(
                this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME);
        clockText.setText(timeText);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_clock, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
