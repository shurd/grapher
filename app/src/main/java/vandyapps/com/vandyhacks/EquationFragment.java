package vandyapps.com.vandyhacks;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAImage;
import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;
import com.wolfram.alpha.WASubpod;

import java.io.InputStream;
import java.util.UUID;

/**
 * Created by Sam on 3/21/2015.
 */
public class EquationFragment extends Fragment {

    public String wholeEqn, fakeEqn, imgurl;
    public TextView mEqn;
    public Button mSearch;
    public TextView mRoots, mDerivatives, mIntegral;
    public ImageView mImage;

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
        mImage = (ImageView)v.findViewById(R.id.image);
        mRoots = (TextView)v.findViewById(R.id.roots);
        mRoots.setText("");
        mDerivatives=(TextView)v.findViewById(R.id.derivatives);
        mDerivatives.setText("");
        mIntegral=(TextView)v.findViewById(R.id.integral);
        mIntegral.setText("");

        //new DownloadImageTask(mImage).execute(imgurl);

        return v;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    WAQueryResult queryResult;

    private class YourTask extends AsyncTask<WAQueryResult, Void, WAQueryResult> {


        protected void onPreExecute() {

        }

        @Override
        protected WAQueryResult doInBackground(WAQueryResult... urls) {
            String input = wholeEqn;
             // The WAEngine is a factory for creating WAQuery objects,
            // and it also used to perform those queries. You can set properties of
            // the WAEngine (such as the desired API output format types) that will
            // be inherited by all WAQuery objects created from it. Most applications
            // will only need to crete one WAEngine object, which is used throughout
            // the life of the application.
            WAEngine engine = new WAEngine();

            // These properties will be set in all the WAQuery objects created from this WAEngine.
            engine.setAppID(appid);
            //engine.addFormat("plaintext");

            // Create the query.
            WAQuery query = engine.createQuery();

            // Set properties of the query.
            query.setInput(input);

            try {
                // For educational purposes, print out the URL we are about to send:
                //System.out.println("Query URL:");
                //System.out.println(engine.toURL(query));
                //System.out.println("");

                // This sends the URL to the Wolfram|Alpha server, gets the XML result
                // and parses it into an object hierarchy held by the WAQueryResult object.
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
                        System.out.println(pod.getTitle());
                        System.out.println("------------");
                        for (WASubpod subpod : pod.getSubpods()) {
                            for (Object element : subpod.getContents()) {
                               // System.out.println(element.toString());
                                if (element instanceof WAPlainText) {
                                    if(pod.getTitle().equals("Root")){
                                        mRoots.setText(((WAPlainText) element).getText());
                                    }
                                    if(pod.getTitle().equals("Derivative")){
                                        mDerivatives.setText(((WAPlainText) element).getText());
                                    }
                                    if(pod.getTitle().equals("Indefinite integral")){
                                        mIntegral.setText(((WAPlainText) element).getText());
                                    }
                                }
                                if (pod.getTitle().equals("Plot")&&element instanceof WAImage) {
                                    imgurl = ((WAImage) element).getURL();
                                    //System.out.println("");
                                }
                            }
                        }
                        System.out.println("");
                    }
                }
                // We ignored many other types of Wolfram|Alpha output, such as warnings, assumptions, etc.
                // These can be obtained by methods of WAQueryResult or objects deeper in the hierarchy.
            }
            new DownloadImageTask(mImage).execute(imgurl);
        }
    }
}
