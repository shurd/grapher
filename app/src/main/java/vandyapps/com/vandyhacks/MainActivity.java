package vandyapps.com.vandyhacks;

import android.support.v4.app.Fragment;

/**
 * Created by Sam on 3/21/2015.
 */
public class MainActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment(){return new MainFragment();}
}
