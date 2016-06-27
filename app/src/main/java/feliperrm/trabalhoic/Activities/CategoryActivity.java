package feliperrm.trabalhoic.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import feliperrm.trabalhoic.R;

public class CategoryActivity extends AppCompatActivity {

    public static final String CATEGORY_POS_KEY = "categoryPosKey";
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        loadBundle();
        findViews();
        setUpViews();
    }

    private void loadBundle(){

    }

    private void findViews(){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    private void setUpViews(){

    }

}
