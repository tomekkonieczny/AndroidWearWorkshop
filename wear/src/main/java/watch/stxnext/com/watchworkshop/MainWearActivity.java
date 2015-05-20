package watch.stxnext.com.watchworkshop;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridViewPager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

public class MainWearActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        DataApi.DataListener {

    private static final String PROGRESS_KEY = "watch.stxnext.progress";

    private GoogleApiClient googleApiClient;
    private GridViewPagerFragmentAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridViewPager pager = (GridViewPager) findViewById(R.id.grid_view_pager);
        adapter = new GridViewPagerFragmentAdapter(getFragmentManager());
        pager.setAdapter(adapter);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();

        DotsPageIndicator indicator = (DotsPageIndicator) findViewById(R.id.indicator);
        indicator.setPager(pager);
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
                    final int progress = dataMap.getInt(PROGRESS_KEY);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateProgress(progress);
                        }
                    });
                }
            }
        }
    }

    private void updateProgress(int progress) {
        ProgressFragment progressFragment = adapter.getProgressFragment();
        if (progressFragment != null && progressFragment.isAdded()) {
            progressFragment.updateProgress(progress);
        }
    }
}