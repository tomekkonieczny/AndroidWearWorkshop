package watch.stxnext.com.watchworkshop;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        DataApi.DataListener {

    private static final String PROGRESS_KEY = "watch.stxnext.progress";

    private GoogleApiClient googleApiClient;
    private TextView textView;
    private DonutProgress progressView;

    private int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                textView = (TextView) stub.findViewById(R.id.text);
                progressView = (DonutProgress) stub.findViewById(R.id.progress_bar);
            }
        });

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();
    }

    @Override
    protected void onResume() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.DataApi.removeListener(googleApiClient, this);
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.DataApi.addListener(googleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        for (DataEvent event : dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/progress") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    progress = dataMap.getInt(PROGRESS_KEY);
                    updateProgress();
                }
            }
        }
    }

    private void updateProgress() {
        if (textView != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int currentValue = Integer.valueOf(textView.getText().toString());
                    if (currentValue != progress) {
                        currentValue = currentValue > progress ? currentValue - 1 : currentValue + 1;

                        textView.setText(String.valueOf(currentValue));
                        progressView.setProgress(currentValue);
                        textView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateProgress();
                            }
                        }, 10);

                    }
                }
            });
        }
    }
}
