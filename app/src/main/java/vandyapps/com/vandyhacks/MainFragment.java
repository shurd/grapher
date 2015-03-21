package vandyapps.com.vandyhacks;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

/**
 * Created by Sam on 3/21/2015.
 */
public class MainFragment extends Fragment {
    public Button mScan;
    public EditText mEdit;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.main_fragment, parent, false);
        mScan = (Button)v.findViewById(R.id.scan_button);
        mScan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(getActivity(), CameraActivity.class);
                startActivity(i);
            }
        });

        mEdit = (EditText)v.findViewById(R.id.equation_box);

        return v;
    }
}
