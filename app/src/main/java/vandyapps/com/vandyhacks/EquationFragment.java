package vandyapps.com.vandyhacks;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.UUID;

/**
 * Created by Sam on 3/21/2015.
 */
public class EquationFragment extends Fragment {

    public String wholeEqn, fakeEqn;
    public TextView mEqn;

    public static final String EXTRA_EQN = "com.vandyapps.android.grapher.eqn";
    public static final String EXTRA_FAKE = "com.vandyapps.android.grapher.fakeeqn";

    public static EquationFragment newInstance(String eqn, String fakeEqn){
        Bundle args = new Bundle();
        args.putString(EXTRA_FAKE, fakeEqn);
        args.putString(EXTRA_EQN, eqn);

        EquationFragment fragment = new EquationFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wholeEqn = getArguments().getString(EXTRA_EQN);
        fakeEqn = getArguments().getString(EXTRA_FAKE);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.equation_activity, parent, false);

        mEqn = (TextView)v.findViewById(R.id.equation);
        mEqn.setText(Html.fromHtml(fakeEqn));

        return v;
    }
}
