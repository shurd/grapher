package vandyapps.com.vandyhacks;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;
import com.wolfram.alpha.WASubpod;

import java.util.UUID;

/**
 * Created by Sam on 3/21/2015.
 */
public class EquationFragment extends Fragment {

    public String wholeEqn, fakeEqn;
    public TextView mEqn;
    public Button mSearch;

    private static String appid="YY2TWK-E68V483KT6";

    public static final String EXTRA_EQN = "com.vandyapps.android.grapher.eqn";
    public static final String EXTRA_FAKE = "com.vandyapps.android.grapher.fakeeqn";

    public static EquationFragment newInstance(String eqn, String fakeEqn){
        Bundle args = new Bundle();
        args.putString(EXTRA_FAKE, fakeEqn);
        args.putString(EXTRA_EQN, eqn);

        EquationFragment fragment = new EquationFragment();
        fragment.setArguments(args);

       // WAEngine news = new WAEngine();

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
        mSearch = (Button)v.findViewById(R.id.query);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new YourTask().execute();
            }
        });

        return v;
    }

    WAQueryResult queryResult;

    private class YourTask extends AsyncTask<WAQueryResult, Void, WAQueryResult> {


        protected void onPreExecute() {

        }

        @Override
        protected WAQueryResult doInBackground(WAQueryResult... urls) {
            String input = "who is the president";
            WAEngine engine = new WAEngine();
            engine.setAppID(appid);
            engine.addFormat("plaintext");

            // Create the query.
            WAQuery query = engine.createQuery();
            query.setInput(input);
            queryResult = null;
            try {
                queryResult = engine.performQuery(query);
            } catch (WAException e) {
                e.printStackTrace();
            }
            return queryResult;
        }

        @Override
        protected void onPostExecute(WAQueryResult response) {
            if (queryResult.isError()) {
                System.out.println("Query error");
                System.out.println("  error code: " + queryResult.getErrorCode());
                System.out.println("  error message: " + queryResult.getErrorMessage());

            } else if (!queryResult.isSuccess()) {
                System.out.println("Query was not understood; no results available.");

            } else {

                // Got a result.
                System.out.println("Successful query. Pods follow:\n");
                for (WAPod pod : queryResult.getPods()) {
                    if (!pod.isError()) {
                        if (pod.getTitle().equals("Result")) {
                            System.out.println(pod.getTitle());
                            for (WASubpod subpod : pod.getSubpods()) {
                                for (Object element : subpod.getContents()) {
                                    if (element instanceof WAPlainText) {
                                        System.out.println(((WAPlainText) element).getText());
                                        Toast.makeText(getActivity().getApplicationContext(), ((WAPlainText) element).getText(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
