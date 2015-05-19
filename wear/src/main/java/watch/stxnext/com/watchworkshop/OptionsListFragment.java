package watch.stxnext.com.watchworkshop;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;

/**
 * Created by Tomasz Konieczny on 2015-05-19.
 */
public class OptionsListFragment extends Fragment {

    private TextView textView;
    private DonutProgress progressView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.progress_layout_stub, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = (TextView) view.findViewById(R.id.text);
        progressView = (DonutProgress) view.findViewById(R.id.progress_bar);
    }

}
