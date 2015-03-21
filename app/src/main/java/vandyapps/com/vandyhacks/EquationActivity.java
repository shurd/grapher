package vandyapps.com.vandyhacks;

import android.support.v4.app.Fragment;

/**
 * Created by Sam on 3/21/2015.
 */
public class EquationActivity extends SingleFragmentActivity{
    @Override
    protected Fragment createFragment() {
        String fake = getIntent().getStringExtra(MainFragment.EXTRA_SEND_FAKE);
        String real = getIntent().getStringExtra(MainFragment.EXTRA_SEND_REAL);

        return EquationFragment.newInstance(real, fake);
    }
}
