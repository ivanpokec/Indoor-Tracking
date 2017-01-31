package hr.foi.indoortracking;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dbaccess.ApiEndpoint;
import com.example.dbaccess.RetrofitConnection;
import com.example.dbaccess.UserModel;

import hr.foi.core.LoggedUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends Activity {
    EditText userName, passWord;
    Button logIn;
    SessionManager manager;
    private ProgressBar spinner;
    Button b2;
    public static UserModel activeUser = new UserModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.INVISIBLE);
        manager = new SessionManager();
        userName = (EditText) findViewById(R.id.editText_User);
        passWord = (EditText) findViewById(R.id.editText_Password);
        logIn = (Button) findViewById(R.id.button_LogIn);

        String id = manager.getPreferences(Login.this, "id");

        if (id != "") {

            activeUser.setUserId(Integer.parseInt(id));
            activeUser.setName(manager.getPreferences(Login.this, "name"));
            activeUser.setUsername(manager.getPreferences(Login.this, "username"));
            try {
                activeUser.setLocationId(Integer.parseInt(manager.getPreferences(Login.this, "locationId")));
            }
            catch (NumberFormatException ex) {
                activeUser.setLocationId(0);
            }

            activeUser.setLocationName(manager.getPreferences(Login.this, "locationName"));
            activeUser.setLocationCategory(manager.getPreferences(Login.this, "locationCategory"));
            try {
                activeUser.setCurrentLocationId(Integer.parseInt(manager.getPreferences(Login.this, "currentLocationId")));
            }
            catch (NumberFormatException ex) {
                activeUser.setCurrentLocationId(0);
            }

            activeUser.setCurrentLocationName(manager.getPreferences(Login.this, "currentLocationName"));
            activeUser.setCurrentLocationCategory(manager.getPreferences(Login.this, "currentLocationCategory"));
            activeUser.setCurrentLocationDescription(manager.getPreferences(Login.this, "currentLocationDescription"));
            try {
                activeUser.setNotification(Integer.parseInt(manager.getPreferences(Login.this, "notification")));
            }
            catch (NumberFormatException ex) {
                activeUser.setNotification(1);
            }

            LoggedUser.getUser().setUserModel(activeUser);

            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);

            /*
            ApiEndpoint apiService = RetrofitConnection.Factory.getInstance();
            apiService.getUser(userId).enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.isSuccess()) {
                        activeUser = response.body();
                        LoggedUser.getUser().setUserModel(activeUser);
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);

                    }

                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    //Log.i("JOOOJ", "lol xD");
                }
            }); */


        }
        else {

        }

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userName.getText().toString().isEmpty() || passWord.getText().toString().isEmpty()) {
                    Toast.makeText(Login.this, "Niste unijeli potrebne podatke!", Toast.LENGTH_SHORT).show();
                } else {
                    spinner.setVisibility(View.VISIBLE);

                    ApiEndpoint apiService = RetrofitConnection.Factory.getInstance();
                    apiService.getUser(userName.getText().toString(), passWord.getText().toString()).enqueue(new Callback<UserModel>() {

                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            if (response.body() != null) {

                                //checkPermissions();

                                manager.setPreferences(Login.this, "id", String.valueOf(response.body().getUserId()));
                                manager.setPreferences(Login.this, "name", response.body().getName());
                                manager.setPreferences(Login.this, "username", response.body().getUsername());
                                manager.setPreferences(Login.this, "password", response.body().getPassword());
                                manager.setPreferences(Login.this, "locationId", String.valueOf(response.body().getLocationId()));
                                manager.setPreferences(Login.this, "locationName", response.body().getLocationName());
                                manager.setPreferences(Login.this, "locationCategory", response.body().getLocationCategory());
                                manager.setPreferences(Login.this, "currentLocationId", String.valueOf(response.body().getCurrentLocationId()));
                                manager.setPreferences(Login.this, "currentLocationName", response.body().getCurrentLocationName());
                                manager.setPreferences(Login.this, "currentLocationCategory", response.body().getCurrentLocationCategory());
                                manager.setPreferences(Login.this, "currentLocationDescription", response.body().getCurrentLocationDescription());
                                manager.setPreferences(Login.this, "notification", String.valueOf(response.body().getNotification()));

                                activeUser = response.body();
                                LoggedUser.getUser().setUserModel(activeUser);

                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(Login.this, "Uspješno ste se prijavili! ", Toast.LENGTH_LONG).show();

                            }
                            else {
                                Toast.makeText(Login.this, "Pogrešni podaci za prijavu!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            Toast.makeText(Login.this, "Greška prilikom prijave!", Toast.LENGTH_SHORT).show();
                        }

                    });
                }

            }
        });


    }


    private boolean checkPermissions(){

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH},
                    2);


            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            return false;
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH_ADMIN)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH_ADMIN},
                    2);


            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            return false;
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    2);


            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            return false;
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    2);


            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            return false;
        }
        return true;
    }

}
