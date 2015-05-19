package watch.stxnext.com.watchworkshop;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.wearable.view.FragmentGridPagerAdapter;

/**
 * Created by Tomasz Konieczny on 2015-05-19.
 */
public class GridViewPagerFragmentAdapter extends FragmentGridPagerAdapter {

    private static final String PROGRESS_FRAGMENT_TAG = "progress_fragment";

    private static final String OPTIONS_FRAGMENT_TAG = "options_fragment";

    public GridViewPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getFragment(int column, int row) {
        Fragment fragment = null;
        switch (column) {
            case 0:
                fragment = new ProgressFragment();
                break;
            case 1:
                fragment = new OptionsListFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getRowCount() {
        return 0;
    }

    @Override
    public int getColumnCount(int i) {
        return 2;
    }
}
