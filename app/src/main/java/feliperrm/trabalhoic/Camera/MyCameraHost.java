package feliperrm.trabalhoic.Camera;

import android.content.Context;
import android.graphics.Bitmap;

import com.commonsware.cwac.camera.PictureTransaction;
import com.commonsware.cwac.camera.SimpleCameraHost;

/**
 * Created by felip on 24/06/2016.
 */
public class MyCameraHost extends SimpleCameraHost {

    CameraHostCallback cameraHostCallback;

    public MyCameraHost(Context _ctxt, CameraHostCallback cameraHostCallback) {
        super(_ctxt);
        this.cameraHostCallback = cameraHostCallback;
    }

    @Override
    public void saveImage(PictureTransaction xact, Bitmap bitmap) {
        super.saveImage(xact, bitmap);
        cameraHostCallback.saveImage(xact, bitmap);
    }

    @Override
    public void saveImage(PictureTransaction xact, byte[] image) {
        super.saveImage(xact, image);
        cameraHostCallback.saveImage(xact, image);
    }

    public interface CameraHostCallback{
        public void saveImage(PictureTransaction xact, Bitmap bitmap);
        public void saveImage(PictureTransaction xact, byte[] image);
    }
}
