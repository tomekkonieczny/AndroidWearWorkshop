package watch.stxnext.com.watchworkshop;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Tomasz Konieczny on 2015-05-05.
 */
public class MainActivity extends Activity {

    private static final String KEY = "key";

    private static final int COUNTDOWN = 0;
    private static final int STOP = 1;

    private TextView valueTextView;
    private boolean countdownLock = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        valueTextView = (TextView) findViewById(R.id.random_int_text_view);

        prepareNextButton();
        prepareNotifyButton();
    }

    private void prepareNextButton() {
        Button nextButton = (Button) findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                valueTextView.setText(String.valueOf(random.nextInt(1000)));
            }
        });
    }

    private void prepareNotifyButton() {
        Button notifyButton = (Button) findViewById(R.id.notify_button);
        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Notificaiton builder
                final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.this);

                //Standard notification
                mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Random int: " + valueTextView.getText())
                        .setContentText(valueTextView.getText())
                        .setVibrate(new long[]{0, 200, 1000, 200})
                        .setColor(Color.parseColor("#607d8b"))
                        .setAutoCancel(true);

                //Action for handheld and wearable
                Intent globalIntent = new Intent(MainActivity.this, MainActivity.class);
                globalIntent.putExtra(KEY, COUNTDOWN);
                PendingIntent globalPI = PendingIntent.getActivity(MainActivity.this, 0, globalIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.addAction(R.mipmap.ic_launcher, "Countdown", globalPI);

                //Action only for wearable
                NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender();
                Intent stopIntent = new Intent(MainActivity.this, MainActivity.class);
                stopIntent.putExtra(KEY, STOP);
                PendingIntent stopPI = PendingIntent.getActivity(MainActivity.this, 1, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                wearableExtender.addAction(new NotificationCompat.Action(R.mipmap.ic_launcher, "Countdown", globalPI));
                wearableExtender.addAction(new NotificationCompat.Action(R.mipmap.ic_launcher, "Stop", stopPI));
                mBuilder.extend(wearableExtender);

                //Show notification
                NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(MainActivity.this);
                mNotificationManager.notify(100, mBuilder.build());

            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int value = intent.getIntExtra(KEY, -1);

        switch (value) {
            case COUNTDOWN:
                countdown();
                break;
            case STOP:
                countdownLock = true;
                break;
        }
    }

    private void countdown() {
        int value = Integer.parseInt(valueTextView.getText().toString());
        if (value > 0 && !countdownLock) {
            value--;
            valueTextView.setText(String.valueOf(value));
            valueTextView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    countdown();
                }
            }, 10);
        } else if (countdownLock) {
            countdownLock = false;
        }
    }
}
