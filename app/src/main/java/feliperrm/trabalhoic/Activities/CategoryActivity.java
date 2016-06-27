package feliperrm.trabalhoic.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import feliperrm.trabalhoic.Adapters.CategoriesAdapter;
import feliperrm.trabalhoic.Adapters.ImagesAdapter;
import feliperrm.trabalhoic.Model.Category;
import feliperrm.trabalhoic.R;
import feliperrm.trabalhoic.Util.Singleton;

public class CategoryActivity extends AppCompatActivity {

    public static final String CATEGORY_POS_KEY = "categoryPosKey";
    RecyclerView recyclerView;
    Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        loadBundle();
        findViews();
        setUpViews();
    }

    private void loadBundle(){
        category = Singleton.getSingleton().getCategories().get(getIntent().getExtras().getInt(CATEGORY_POS_KEY));
    }

    private void findViews(){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    private void setUpViews(){
        recyclerView.setLayoutManager(new GridLayoutManager(CategoryActivity.this, 2, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new ImagesAdapter(CategoryActivity.this, category.getFiles(), category.getName()));
    }

}
