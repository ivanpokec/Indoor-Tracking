package hr.foi.indoortracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.dbaccess.HistoryModel;

/**
 * Created by Paula on 20.12.2016..
 */

public class MyMovements extends AppCompatActivity {

    Button showAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_movements);

        setTitle("Moja kretanja");

       showAll = (Button) findViewById(R.id.button_ShowAllHistory);
        showAll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MyMovements.this, HistoryAll.class);
                startActivity(intent);
                // Perform action on click
            }
        });


    }


}
