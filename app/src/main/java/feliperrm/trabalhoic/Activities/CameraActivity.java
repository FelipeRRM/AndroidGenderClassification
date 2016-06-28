package feliperrm.trabalhoic.Activities;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.commonsware.cwac.camera.CameraHost;
import com.commonsware.cwac.camera.CameraHostProvider;
import com.commonsware.cwac.camera.CameraView;
import com.commonsware.cwac.camera.PictureTransaction;

import feliperrm.trabalhoic.Camera.MyCameraHost;
import feliperrm.trabalhoic.R;
import feliperrm.trabalhoic.RNA.Mlp;
import feliperrm.trabalhoic.Util.Geral;
import feliperrm.trabalhoic.Util.Singleton;

public class CameraActivity extends AppCompatActivity implements CameraHostProvider, MyCameraHost.CameraHostCallback {


    CameraView cameraView;
    MyCameraHost cameraHost;
    ImageView takePicture;
    ImageView acceptPicture;
    ImageView dennyPicture;
    Bitmap lastTakenPicture;
    ImageView imageTaken;
    TextView resultTxt;
    RelativeLayout resultLayout;
    Button closeResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cameraHost = new MyCameraHost(this, this);
        cameraHost.useSingleShotMode();
        setContentView(R.layout.activity_camera);
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
        imageTaken = (ImageView) findViewById(R.id.imageView);
        resultTxt = (TextView) findViewById(R.id.result);
        resultLayout = (RelativeLayout) findViewById(R.id.resultLayout);
        closeResult = (Button) findViewById(R.id.closeResult);
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
                imageTaken.setImageBitmap(lastTakenPicture);
                resultLayout.setVisibility(View.VISIBLE);
                final Mlp mlp = Singleton.getSingleton().getMlp();
                if (mlp == null)
                    Toast.makeText(CameraActivity.this, getString(R.string.mlp_null), Toast.LENGTH_LONG).show();
                else {
                    final ProgressDialog progressDialog = new ProgressDialog(CameraActivity.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage(getString(R.string.testing_network));
                    progressDialog.show();
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            results= mlp.evaluate(Geral.getImageGreyscaleArray(Geral.getLowResBitmap(lastTakenPicture)));
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            String resultString;
                            resultString = "[0] = " + results[0] + "\n";
                            int i = 1;
                            while(i<results.length){
                               resultString = resultString + Singleton.getSingleton().getCategories().get(i-1).getName() + ": " + String.format("%.1f", results[i]*100) + "%\n";
                                i++;
                            }
                            resultTxt.setText(resultString);
                            progressDialog.dismiss();
                        }
                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        });

        closeResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultLayout.setVisibility(View.GONE);
                resultTxt.setText("");
            }
        });

    }

    float[] results;

    @Override
    public CameraHost getCameraHost() {
        return cameraHost;
    }

    @Override
    public void saveImage(PictureTransaction xact, Bitmap bitmap) {
        Log.d("saveImage", "Bitmap");
        lastTakenPicture = Geral.getScaledBitmap(Geral.getSquareBitmap(bitmap),300);
    }

    @Override
    public void saveImage(PictureTransaction xact, byte[] image) {
        Log.d("saveImage", "array");
    }
}
