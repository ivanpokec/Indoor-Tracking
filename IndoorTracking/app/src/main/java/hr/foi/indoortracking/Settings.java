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
import com.example.dbaccess.UserModel;

import hr.foi.core.LoggedUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Zana on 24.1.2017..
 */

public class Settings extends AppCompatActivity {
    private Switch aSwitch;
    private Button save;
    private UserModel activeUser;

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

        int notification = LoggedUser.getUser().getUserModel().getNotification();
        if (notification == 0) {
            aSwitch.setChecked(true);
        }
        else {
            aSwitch.setChecked(false);
        }


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (aSwitch.isChecked()) {
                    Log.i("CHECKED", "bla");
                    apiService.updateNotificationSetting(userId, 0).enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            if (response.isSuccess()) {
                                activeUser = response.body();
                                LoggedUser.getUser().setUserModel(activeUser);
                                Toast.makeText(getApplicationContext(), "Obavijesti su isključene.", Toast.LENGTH_LONG).show();
                            }
                            if (response.body() == null) {
                                Log.i("JOOOJ", "null");
                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {

                        }
                    });
                    /*
                    if (Context.NOTIFICATION_SERVICE!=null) {

                        String ns = Context.NOTIFICATION_SERVICE;

                        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);

                        nMgr.cancelAll();

                    } */
                }

                else
                {
                    apiService.updateNotificationSetting(userId, 1).enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            if (response.isSuccess()) {
                                Log.i("JOOOJ", String.valueOf(response.body().getNotification()));
                                activeUser = response.body();
                                LoggedUser.getUser().releaseUserModel();
                                LoggedUser.getUser().setUserModel(activeUser);
                                Toast.makeText(getApplicationContext(), "Obavijesti su uključene.", Toast.LENGTH_LONG).show();
                            }
                            if (response.body() == null) {
                                Log.i("JOOOJ", "uključi null");
                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {

                        }
                    });
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
