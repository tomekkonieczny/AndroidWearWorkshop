package watch.stxnext.com.watchworkshop;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.wearable.view.FragmentGridPagerAdapter;

/**
 * Created by Tomasz Konieczny on 2015-05-19.
 */
public class GridViewPagerFragmentAdapter extends FragmentGridPagerAdapter {

    private ProgressFragment progressFragment;
    private CountdownFragment optionsFragment;

    public GridViewPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getFragment(int row, int column) {
        Fragment fragment = null;
        switch (column) {
            case 0:
                progressFragment = new ProgressFragment();
                fragment = progressFragment;
                break;
            case 1:
                optionsFragment = new CountdownFragment();
                fragment = optionsFragment;
                break;
        }

        return fragment;
    }

    public ProgressFragment getProgressFragment() {
        return progressFragment;
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount(int i) {
        return 2;
    }
}
