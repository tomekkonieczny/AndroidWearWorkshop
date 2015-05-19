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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.Random;

/**
 * Created by Tomasz Konieczny on 2015-05-05.
 */
public class MainActivity extends Activity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private static final String KEY = "key";

    private static final int COUNTDOWN = 0;
    private static final int STOP = 1;

    private static final int NOTIFICATION_ID = 100;

    private static final String PROGRESS_KEY = "watch.stxnext.progress";

    private GoogleApiClient googleApiClient;
    private TextView valueTextView;
    private boolean countdownLock = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        valueTextView = (TextView) findViewById(R.id.random_int_text_view);

        prepareWearableConnection();
        prepareValueButtons();
        prepareNotifyButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        googleApiClient.disconnect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Google API Client connection failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(Bundle bundle) {
        prepareSynchronizeButton();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Google API Client connection suspensed", Toast.LENGTH_SHORT).show();
    }

    private void prepareSynchronizeButton() {
        Button button = (Button) findViewById(R.id.synchronize_button);
        button.setEnabled(true);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendProgressToWearable();
            }
        });
    }

    private void prepareWearableConnection() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApiIfAvailable(Wearable.API)
                //.addApi(Wearable.API)
                .build();
    }

    private void prepareValueButtons() {
        ImageButton nextButton = (ImageButton) findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = Integer.valueOf(valueTextView.getText().toString());
                if (value <= 95) {
                    value += 5;
                }

                valueTextView.setText(String.valueOf(value));
            }
        });

        ImageButton prevButton = (ImageButton) findViewById(R.id.prev_buttton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = Integer.valueOf(valueTextView.getText().toString());
                value = value >= 5 ? value - 5 : 0;

                valueTextView.setText(String.valueOf(value));
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
                mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

            }
        });
    }

    private void sendProgressToWearable() {
        String value = valueTextView.getText().toString();

        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/progress");
        putDataMapReq.getDataMap().putInt(PROGRESS_KEY, Integer.valueOf(value));
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();

        Wearable.DataApi.putDataItem(googleApiClient, putDataReq);
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
        if (!countdownLock) {
            if (value > 0) {
                value--;
                valueTextView.setText(String.valueOf(value));
                valueTextView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        countdown();
                        sendProgressToWearable();
                    }
                }, 1000);
            } else {
                NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(MainActivity.this);
                mNotificationManager.cancel(NOTIFICATION_ID);
            }
        } else {
            countdownLock = false;
        }
    }

}
