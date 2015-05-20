package watch.stxnext.com.watchworkshop;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.wearable.view.WatchViewStub;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;

/**
 * Created by Tomasz Konieczny on 2015-05-19.
 */
public class ProgressFragment extends Fragment {

    private TextView textView;
    private DonutProgress progressView;
    private int progress = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.progress_layout_stub, container, false);
        WatchViewStub stub = (WatchViewStub) view.findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub watchViewStub) {
                textView = (TextView) watchViewStub.findViewById(R.id.text);
                progressView = (DonutProgress) watchViewStub.findViewById(R.id.progress_bar);
            }
        });

        return view;
    }

    public void updateProgress(int progress) {
        this.progress = progress;
        updateProgress();
    }

    private void updateProgress() {
        if (getView() != null) {
            int currentValue = Integer.valueOf(textView.getText().toString());
            if (currentValue != progress) {
                currentValue = currentValue > progress ? currentValue - 1 : currentValue + 1;
                progressView.setProgress(currentValue);
                textView.setText(String.valueOf(currentValue));
                textView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateProgress();
                    }
                }, 10);
            }
        }
    }
}
