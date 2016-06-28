package feliperrm.trabalhoic.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import feliperrm.trabalhoic.Model.Category;
import feliperrm.trabalhoic.R;
import feliperrm.trabalhoic.Util.Geral;
import feliperrm.trabalhoic.Util.Singleton;

public class MenuActivity extends AppCompatActivity {

    Button manage, classify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        findViews();
        setUpViews();
        readFileSystemAndUpdate();
    }

    private void findViews(){
        manage = (Button) findViewById(R.id.manageGroups);
        classify = (Button) findViewById(R.id.classifyImage);
    }

    private void setUpViews(){
        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent prox = new Intent(MenuActivity.this, ManageActivity.class);
                startActivity(prox);
            }
        });

        classify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Singleton.getSingleton().getMlp()==null){
                    Toast.makeText(MenuActivity.this, getString(R.string.mlp_null), Toast.LENGTH_LONG).show();
                }
                else {
                    Intent prox = new Intent(MenuActivity.this, CameraActivity.class);
                    startActivity(prox);
                }
            }
        });

    }


    public void readFileSystemAndUpdate(){
        final ProgressDialog progressDialog = new ProgressDialog(MenuActivity.this);
        progressDialog.setMessage(getString(R.string.loading_categories));
        progressDialog.setCancelable(false);
        progressDialog.show();
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                navigateDirectoryAndPrepareFiles(new File(Geral.getCategoryFolderParent()));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    public void navigateDirectoryAndPrepareFiles(File dir) {
        try {
            File[] files = dir.listFiles();
            for (File dirs : files) {
                if (dirs.isDirectory()) {
                    Log.d("Directory", dirs.getName());
                    if(Singleton.getSingleton().isStringInCategory(dirs.getName())) {
                        File[] listOfFiles = dirs.listFiles();
                        for (File file : listOfFiles) {
                            if (file.isFile()) {
                                if(!Singleton.getSingleton().isFileInCategory(file.getName(), dirs.getName())) {
                                    Singleton.getSingleton().addFileToCategory(file.getName(), dirs.getName());
                                    Singleton.getSingleton().setNeedsToTrainNetwork(true);
                                    Geral.prepareFileForNetwork(file.getAbsolutePath());
                                }
                            }
                        }
                    }
                    else
                    {
                        Singleton.getSingleton().setNeedsToTrainNetwork(true);
                        ArrayList<String> imageFiles = new ArrayList<>();
                        File[] listOfFiles = dirs.listFiles();
                        for (File file : listOfFiles) {
                            if (file.isFile()) {
                                imageFiles.add(new String(file.getName()));
                                Geral.prepareFileForNetwork(file.getAbsolutePath());
                            }
                        }
                        Singleton.getSingleton().addToCategories(new Category(dirs.getName(), imageFiles));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
