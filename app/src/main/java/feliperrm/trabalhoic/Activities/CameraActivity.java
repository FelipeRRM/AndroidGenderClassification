package feliperrm.trabalhoic.Activities;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.commonsware.cwac.camera.CameraHost;
import com.commonsware.cwac.camera.CameraHostProvider;
import com.commonsware.cwac.camera.CameraView;
import com.commonsware.cwac.camera.PictureTransaction;
import com.commonsware.cwac.camera.SimpleCameraHost;

import feliperrm.trabalhoic.Camera.MyCameraHost;
import feliperrm.trabalhoic.R;

public class CameraActivity extends AppCompatActivity implements CameraHostProvider, MyCameraHost.CameraHostCallback {


    CameraView cameraView;
    MyCameraHost cameraHost;
    ImageView takePicture;
    ImageView acceptPicture;
    ImageView dennyPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cameraHost = new MyCameraHost(this, this);
        cameraHost.useSingleShotMode();
        setContentView(R.layout.activity_main);
        findViews();
        setUpViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.onPause();
    }

    private void findViews(){
        cameraView = (CameraView) findViewById(R.id.cameraView);
        takePicture = (ImageView) findViewById(R.id.takePicture);
        acceptPicture = (ImageView) findViewById(R.id.acceptPicture);
        dennyPicture = (ImageView) findViewById(R.id.dennyPicture);
    }

    private void setUpViews(){
        cameraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (cameraView.isAutoFocusAvailable())
                        cameraView.autoFocus();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    cameraView.takePicture(true, true);
                    acceptPicture.setVisibility(View.VISIBLE);
                    dennyPicture.setVisibility(View.VISIBLE);
                    takePicture.setVisibility(View.GONE);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        dennyPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dennyPicture.setVisibility(View.GONE);
                acceptPicture.setVisibility(View.GONE);
                takePicture.setVisibility(View.VISIBLE);
                cameraView.restartPreview();
            }
        });

        acceptPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dennyPicture.setVisibility(View.GONE);
                acceptPicture.setVisibility(View.GONE);
                takePicture.setVisibility(View.VISIBLE);
                cameraView.restartPreview();
            }
        });
    }

    @Override
    public CameraHost getCameraHost() {
        return cameraHost;
    }

    @Override
    public void saveImage(PictureTransaction xact, Bitmap bitmap) {

    }

    @Override
    public void saveImage(PictureTransaction xact, byte[] image) {

    }
}
