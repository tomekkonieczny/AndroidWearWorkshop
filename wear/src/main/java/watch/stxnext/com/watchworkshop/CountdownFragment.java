package watch.stxnext.com.watchworkshop;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;

/**
 * Created by Tomasz Konieczny on 2015-05-19.
 */
public class CountdownFragment extends Fragment {

    private OnCountdownButtonListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnCountdownButtonListener) getActivity();
        } catch (ClassCastException ex) {
            throw new RuntimeException("OnCountdownButtonListener is not implemented in "
                    + getActivity().getClass().getName());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.options_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button startButton = (Button) view.findViewById(R.id.start_button);
        Button stopButton = (Button) view.findViewById(R.id.stop_button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCountdownActionInvoked(true);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCountdownActionInvoked(false);
            }
        });
    }

    public interface OnCountdownButtonListener {

        void onCountdownActionInvoked(boolean countdownEnabled);

    }
}
