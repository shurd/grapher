package vandyapps.com.vandyhacks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Sam on 3/21/2015.
 */
public class CameraFragment extends Fragment{
    private static final String TAG = "CameraFragment";
    private Camera mCamera;
    private Camera.Parameters mParameters;
    private byte[] mBuffer;

    private SurfaceView mSurfaceView;

    @Override
    @SuppressWarnings("deprecation")
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_camera, parent, false);
        mSurfaceView = (SurfaceView)v.findViewById(R.id.camera_surfaceView);

        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        holder.addCallback(new SurfaceHolder.Callback(){
            public void surfaceCreated(SurfaceHolder holder){
                try{
                    if(mCamera!=null){
                        mCamera.setPreviewDisplay(holder);
                       //below this added
                        mCamera.addCallbackBuffer(mBuffer);
                        mCamera.setPreviewCallbackWithBuffer(new Camera.PreviewCallback() {
                            public synchronized void onPreviewFrame(byte[] data, Camera c){
                                if(mCamera!=null){
                                    mCamera.addCallbackBuffer(mBuffer);
                                }
                            }
                        });
                        //above this added
                    }
                } catch (IOException exception){
                    //error
                }
            }

            public void surfaceDestroyed(SurfaceHolder holder){
                if(mCamera!=null){
                    mCamera.stopPreview();
                    mCamera.release();
                    mCamera = null;
                }
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int w, int h){
                if(mCamera==null) return;
                mParameters = mCamera.getParameters();
                Camera.Size s =getBestSupportedSize(mParameters.getSupportedPreviewSizes(), w, h);
                mParameters.setPreviewSize(s.width, s.height);
                mCamera.setParameters(mParameters);
                mCamera.setDisplayOrientation(90);//fixes the sideways orientation
                try{
                    mCamera.startPreview();
                }catch(Exception e){
                    //error
                    mCamera.release();
                    mCamera = null;
                }
                //below this added
                Camera.Size p = mCamera.getParameters().getPreviewSize();
                mCamera.startPreview();
                //above this added
            }
        });

        return v;
    }

    public Camera.Parameters getCameraParameters(){
        return mCamera.getParameters();
    }

    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes, int width, int height){
        Camera.Size bestSize = sizes.get(0);
        int largestArea = bestSize.width*bestSize.height;
        for(Camera.Size s : sizes){
            int area = s.width*s.height;
            if(area>largestArea){
                bestSize = s;
                largestArea = area;
            }
        }
        return bestSize;
    }

    public void onResume(){
        super.onResume();
        mCamera=Camera.open(0);
    }

    public void onPause(){
        super.onPause();
        if(mCamera!=null){
            mCamera.release();
            mCamera=null;
        }
    }

    //below this useless

    public Bitmap getPic(int x, int y, int width, int height) {
        System.gc();
        Bitmap b = null;
        Camera.Size s = mParameters.getPreviewSize();

        YuvImage yuvimage = new YuvImage(mBuffer, ImageFormat.NV21, s.width, s.height, null);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        yuvimage.compressToJpeg(new Rect(x, y, width, height), 100, outStream); // make JPG
        b = BitmapFactory.decodeByteArray(outStream.toByteArray(), 0, outStream.size());
        if (b != null) {
            //Log.i(TAG, "getPic() WxH:" + b.getWidth() + "x" + b.getHeight());
        } else {
            //Log.i(TAG, "getPic(): Bitmap is null..");
        }
        yuvimage = null;
        outStream = null;
        System.gc();
        return b;
    }
    private void updateBufferSize() {
        mBuffer = null;
        System.gc();
        // prepare a buffer for copying preview data to
        int h = mCamera.getParameters().getPreviewSize().height;
        int w = mCamera.getParameters().getPreviewSize().width;
        int bitsPerPixel =
                ImageFormat.getBitsPerPixel(mCamera.getParameters().getPreviewFormat());
        mBuffer = new byte[w * h * bitsPerPixel / 8];
        //Log.i("surfaceCreated", "buffer length is " + mBuffer.length + " bytes");
    }
}
