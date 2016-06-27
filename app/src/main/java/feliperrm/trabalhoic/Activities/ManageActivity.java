package feliperrm.trabalhoic.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import feliperrm.trabalhoic.Adapters.CategoriesAdapter;
import feliperrm.trabalhoic.Model.Category;
import feliperrm.trabalhoic.R;
import feliperrm.trabalhoic.Util.Geral;
import feliperrm.trabalhoic.Util.Singleton;

public class ManageActivity extends AppCompatActivity {

    TextView networkStatusTxt;
    Button btnAdd, btnTrain;
    RecyclerView recyclerView;

    public TextView getNetworkStatusTxt() {
        return networkStatusTxt;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Singleton.getSingleton().getNeedsToTrainNetwork())
            networkStatusTxt.setText(getString(R.string.needs_training));
        else
            networkStatusTxt.setText(getString(R.string.network_is_trained));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        findViews();
        setUpViews();
        readFileSystemAndUpdate();
    }

    private void findViews(){
        btnAdd = (Button) findViewById(R.id.btnAddCategory);
        btnTrain = (Button) findViewById(R.id.btnTrainNetwork);
        networkStatusTxt = (TextView) findViewById(R.id.networkstatus_text);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    private void setUpViews(){
        btnTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trainNetwork();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ManageActivity.this);
                final EditText edittext = new EditText(ManageActivity.this);
                alert.setMessage(getString(R.string.enter_category_name));
                alert.setTitle(getString(R.string.add_category));
                edittext.setFocusable(true);
                edittext.setFocusableInTouchMode(true);
                alert.setView(edittext);

                alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String title = edittext.getText().toString();
                        if(Singleton.getSingleton().isStringInCategory(title)){
                            new AlertDialog.Builder(ManageActivity.this).setMessage(getString(R.string.title_already_exists)).setTitle(getString(R.string.error)).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create().show();
                        }
                        else{
                            Singleton.getSingleton().addToCategories(new Category(title, new ArrayList<String>()));
                            recyclerView.getAdapter().notifyItemInserted(recyclerView.getAdapter().getItemCount());
                            String filePath = Geral.getCategoryFolderPath(title);
                            File file = new File(filePath);
                            try {
                                Log.d("Folder Path", filePath);
                                boolean folderCreated = file.mkdirs();
                                Log.d("Folder Created", String.valueOf(folderCreated));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                alert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });

                alert.show();

                edittext.requestFocus();
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(ManageActivity.this, 2, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new CategoriesAdapter(ManageActivity.this, Singleton.getSingleton().getCategories()));
    }


    private void trainNetwork(){
        final ProgressDialog progressDialog = new ProgressDialog(ManageActivity.this);
        progressDialog.setMessage(getString(R.string.training_network));
        progressDialog.setCancelable(false);
        progressDialog.show();
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();
                if(progressDialog!=null)
                    progressDialog.dismiss();
                if(networkStatusTxt!=null)
                    networkStatusTxt.setText(getString(R.string.network_is_trained));
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    public void readFileSystemAndUpdate(){
        final ProgressDialog progressDialog = new ProgressDialog(ManageActivity.this);
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
                if(Singleton.getSingleton().getNeedsToTrainNetwork()) {
                    networkStatusTxt.setText(getString(R.string.needs_training));
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
                else
                    networkStatusTxt.setText(getString(R.string.network_is_trained));
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
