package feliperrm.trabalhoic.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import feliperrm.trabalhoic.R;

public class MenuActivity extends AppCompatActivity {

    Button manage, classify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        findViews();
        setUpViews();
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
                Intent prox = new Intent(MenuActivity.this, CameraActivity.class);
                startActivity(prox);
            }
        });

    }
}
