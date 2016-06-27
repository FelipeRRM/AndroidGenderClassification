package feliperrm.trabalhoic.Util;

import android.app.Application;
import android.content.Context;


/**
 * Created by felip on 17/05/2016.
 */
public class MyApp extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
