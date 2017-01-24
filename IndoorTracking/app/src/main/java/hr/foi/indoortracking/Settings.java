package hr.foi.indoortracking;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Created by Zana on 24.1.2017..
 */

public class Settings extends AppCompatActivity {
    Switch aSwitch;
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle("Postavke");
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_settings);
        aSwitch = (Switch) findViewById(R.id.switch1);
        save = (Button) findViewById(R.id.button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (aSwitch.isChecked()) {
                    if (Context.NOTIFICATION_SERVICE!=null) {

                        String ns = Context.NOTIFICATION_SERVICE;

                        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);

                        nMgr.cancelAll();
                        Toast.makeText(getApplicationContext(), "Obavijesti isključene", Toast.LENGTH_LONG).show();
                    }
                }

                else
                {

                }


            }
        });

    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
