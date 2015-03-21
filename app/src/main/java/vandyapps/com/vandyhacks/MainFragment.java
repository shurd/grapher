package vandyapps.com.vandyhacks;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Sam on 3/21/2015.
 */
public class MainFragment extends Fragment {
    public static final String EXTRA_SEND_FAKE="com.vandyapps.android.grapher.sendfake";
    public static final String EXTRA_SEND_REAL="com.vandyapps.android.grapher.sendreal";
    public Button mScan, mSearch;
    public EditText mEdit;
    public boolean sub;
    public TextView eqn;
    public String wholeEqn, fake;
   // public ArrayList<Integer> carrots;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.main_fragment, parent, false);
        sub = false;
        //carrots = new ArrayList<>();



        mScan = (Button)v.findViewById(R.id.scan_button);
        mScan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(getActivity(), CameraActivity.class);
                startActivity(i);
            }
        });

        eqn = (TextView)v.findViewById(R.id.equation);
        eqn.setTextColor(Color.BLACK);

        mSearch = (Button)v.findViewById(R.id.search_button);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EquationActivity.class);
                i.putExtra(EXTRA_SEND_FAKE, fake);
                i.putExtra(EXTRA_SEND_REAL, wholeEqn);
                startActivity(i);
            }
        });

        mEdit = (EditText)v.findViewById(R.id.equation_box);
        //mEdit.setText(Html.fromHtml("hey"+"<sup><small>"+"there"+"</small></sup>"));
        mEdit.addTextChangedListener(new TextWatcher() {
            int flag_text = 0;
            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count){
                wholeEqn = c.toString();
                flag_text = wholeEqn.length();
                for(int i = 0; i<flag_text;i++){
                    if(i==0){
                        fake = "";
                    }
                    if(wholeEqn.charAt(i)!='^'){
                        fake=fake+wholeEqn.charAt(i);
                    }
                    if(sub){
                        fake=fake+"</sup></small>";

                        sub=false;
                    }
                    if(wholeEqn.charAt(i)=='^'){
                        fake = fake+"<sup><small>";

                        sub=true;
                    }
                    eqn.setText(Html.fromHtml(fake));
                }
                //eqn.setText(c.toString());
                /*if(start+count>0){
                    char currentChar = c.charAt(start+count-1);
                    Log.d("letter", currentChar+" "+count);
                    if(sub ==true&&flag_text==0){
                        flag_text=1;
                        //mEdit.setText(Html.fromHtml(c.toString().substring(0,c.toString().length()-2)+"<sup><small>"+currentChar+"</sup></small>"));

                        mEdit.setText(Html.fromHtml(c.toString().substring(0,c.toString().length())+"<sup><small>"+currentChar+"</sup></small>"));

                        mEdit.setSelection(mEdit.getText().length());
                        flag_text=0;
                        sub = false;
                    }
                    if(currentChar=='^'){
                        sub = true;
                    }

//                    mEdit.setText(Html.fromHtml(c.toString().substring(0,count)+"<sup><small>"));
                }*/

            }
            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after){
            }
            @Override
            public void afterTextChanged(Editable c){
            }
        });

        return v;
    }
}
