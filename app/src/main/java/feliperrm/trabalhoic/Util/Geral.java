package feliperrm.trabalhoic.Util;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by felip on 25/11/2015.
 */
public class Geral {


    private static final String ARQUIVO = "sharedpreferences";
    private static final String APP_NAME = "AndroidClassification";

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }


    public static void salvarSharedPreference(Context context, String chave, String dado){
        if(context==null) return;
        SharedPreferences shared = context.getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(chave, dado);
        editor.commit();
    }

    public static  void clearSharedPreference(Context context) {
        SharedPreferences shared = context.getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.clear();
        editor.commit();
    }

    public static void deleteSharedPreference(Context context, String chave){
        SharedPreferences shared = context.getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.remove(chave);
        editor.commit();
    }

    public static String loadSharedPreference(Context context, String chave, String padrao){
        SharedPreferences shared = context.getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
        return shared.getString(chave, padrao);
    }

    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }

    public static String getDatedeHojeAmericanoComHorario(){
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df1.format(new Date());
    }

    public static String getDataDeHoje(){
        DateFormat df1 = new SimpleDateFormat("yyyy/MM/dd");
        return df1.format(new Date());
    }

    public static int getDayOfMonth(){
        Calendar cal = Calendar.getInstance();
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        return dayOfMonth;
    }

    public static int getTodayMonth(){
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        return month;
    }

    public static int getTodayYear(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        return year;
    }

    public static String getNomeData(String input){
        try {
            DateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date inDate = inFormat.parse(input);
            DateFormat outputFormat1 = new SimpleDateFormat("EEEE, dd");
            DateFormat outputFormat2 = new SimpleDateFormat("MMMM");
            DateFormat outputFormat3 = new SimpleDateFormat("yyyy");
            return outputFormat1.format(inDate).substring(0,1).toUpperCase()
                    + outputFormat1.format(inDate).substring(1)
                    + " de " + outputFormat2.format(inDate).substring(0,1).toUpperCase()
                    + outputFormat2.format(inDate).substring(1)
                    + " de " + outputFormat3.format(inDate);
        }
        catch (Exception e){
            return null;
        }

    }

    public static String getDataBarrada(String input){
        try {
            DateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date inDate = inFormat.parse(input);
            DateFormat outputFormat = new SimpleDateFormat("dd/MM/yy");
            return outputFormat.format(inDate);
        }
        catch (Exception e){
            return null;
        }

    }

    public static String getHorario(){
        DateFormat df1 = new SimpleDateFormat("HH:mm:ss");
        Log.d("getHorario", df1.format(new Date()));
        return df1.format(new Date());

    }

    public static String getImei(Context context){
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

 public static void setAnimationElevation(View viewToClick, final View viewToAnimate, final boolean upWhenClicked){

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

        final float inicial = Geral.convertDpToPixel(2,viewToAnimate.getContext());
        final float finalZ;
        if(!upWhenClicked)
            finalZ = 0f;
        else
            finalZ = inicial*3.5f;
        viewToAnimate.setElevation(0);
        viewToAnimate.setTranslationZ(inicial);

        viewToClick.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getActionMasked();
                ViewPropertyAnimator animacaoAtual = null;
        /* Raise view on ACTION_DOWN and lower it on ACTION_UP. */
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (animacaoAtual != null)
                            animacaoAtual.cancel();
                            animateZ(viewToAnimate, finalZ);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (animacaoAtual != null)
                            animacaoAtual.cancel();
                            animateZ(viewToAnimate, inicial);

                        break;
                    case MotionEvent.ACTION_CANCEL:
                        if (animacaoAtual != null)
                            animacaoAtual.cancel();
                            animateZ(viewToAnimate, inicial);
                        break;
                }
                return false;
            }
        });

    }
}


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static ViewPropertyAnimator animateZ(View v, float elevation){
        return v.animate().translationZ(elevation).setDuration(DURATION);
    }

    private static final int DURATION = 250;


    public static void circularReveal(final View view, int x, int y){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    v.removeOnLayoutChangeListener(this);
                    // get the center for the clipping circle
                    int cx = 0;//parent.getWidth() / 2;
                    int cy = 0; //parent.getHeight() / 2;

                    // get the final radius for the clipping circle
                    float finalRadius = (float) Math.hypot(view.getWidth(), view.getHeight());

                    // create the animator for this view (the start radius is zero)
                    Animator anim =
                            ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
                    anim.setDuration(500);
                    // make the view visible and start the animation
                    view.setVisibility(View.VISIBLE);
                    anim.start();

                }
            });
        }

    }

    public static boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApp.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiInfo != null && wifiInfo.isConnected()) || (mobileInfo != null && mobileInfo.isConnected())) {
            return true;
        }else{
            return false;
        }
    }

    public static String toMD5(String s){

        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";

    }


    public static String getDeviceId(Context context){
        return  Settings.Secure.getString(context.getContentResolver(),  Settings.Secure.ANDROID_ID);
    }

    public static View getRootViewFromActivity(Activity activity){
        return  ((ViewGroup) activity
                .findViewById(android.R.id.content)).getChildAt(0);
    }



    public static String getCategoryFolderPath(String category) {
        try {
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +"/" + APP_NAME + "/" + category + "/";
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getCategoryFolderParent(){
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +"/" + APP_NAME + "/";
    }

    public static String getNewImageFilePath(String category) {
        try {
            Date date = new Date();
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +"/" + APP_NAME + "/" + category + "/" + date.getTime() + ".jpg";
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }

    public static void prepareFileForNetwork(String absolutePath) {
        File file = new File(absolutePath);
    }

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        boolean deleted = file.delete();
        Log.d("Deleted File", String.valueOf(deleted));
    }
}
