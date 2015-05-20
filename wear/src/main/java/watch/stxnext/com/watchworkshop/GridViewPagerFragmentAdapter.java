package watch.stxnext.com.watchworkshop;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.wearable.view.FragmentGridPagerAdapter;

/**
 * Created by Tomasz Konieczny on 2015-05-19.
 */
public class GridViewPagerFragmentAdapter extends FragmentGridPagerAdapter {

    private ProgressFragment progressFragment;
    private OptionsListFragment optionsFragment;

    public GridViewPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getFragment(int column, int row) {
        Fragment fragment = null;
        switch (row) {
            case 0:
                progressFragment = new ProgressFragment();
                fragment = progressFragment;
                break;
            case 1:
                optionsFragment = new OptionsListFragment();
                fragment = optionsFragment;
                break;
        }

        return fragment;
    }

    public ProgressFragment getProgressFragment() {
        return progressFragment;
    }

    public OptionsListFragment getOptionsFragment() {
        return optionsFragment;
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
