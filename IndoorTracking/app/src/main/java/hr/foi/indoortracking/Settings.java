package hr.foi.indoortracking;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.dbaccess.ApiEndpoint;
import com.example.dbaccess.RetrofitConnection;

import hr.foi.core.LoggedUser;

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

        final int userId = LoggedUser.getUser().getUserModel().getUserId();
        final ApiEndpoint apiService = RetrofitConnection.Factory.getInstance();


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (aSwitch.isChecked()) {
                    Log.i("CHECKED", "bla");
                    apiService.updateNotificationSetting(userId, 0);
                    /*
                    if (Context.NOTIFICATION_SERVICE!=null) {

                        String ns = Context.NOTIFICATION_SERVICE;

                        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);

                        nMgr.cancelAll();
                        Toast.makeText(getApplicationContext(), "Obavijesti isključene", Toast.LENGTH_LONG).show();
                    } */
                }

                else
                {
                    apiService.updateNotificationSetting(userId, 1);
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
