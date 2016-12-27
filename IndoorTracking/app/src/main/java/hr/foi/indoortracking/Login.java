package hr.foi.indoortracking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dbaccess.ApiEndpoint;
import com.example.dbaccess.RetrofitConnection;
import com.example.dbaccess.UserModel;

import java.util.ArrayList;

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
            activeUser.setId(Integer.parseInt(id));
            activeUser.setName(manager.getPreferences(this,"name"));
            activeUser.setUsername(manager.getPreferences(this,"userName"));
            activeUser.setPassword(manager.getPreferences(this,"password"));
            activeUser.setOdjel(manager.getPreferences(this,"locationName"));

            LoggedUser.getUser().setUserModel(activeUser);

            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        }

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userName.getText().toString().isEmpty() || passWord.getText().toString().isEmpty()) {
                    Toast.makeText(Login.this, "Niste unijeli potrebne podatke!", Toast.LENGTH_SHORT).show();
                } else {
                    spinner.setVisibility(View.VISIBLE);
                    //Log.d("user",userName.getText().toString());
                    //String status = manager.getPreferences(Login.this, "status");
                    //Log.d("status", status);

                    ApiEndpoint apiService = RetrofitConnection.Factory.getInstance();
                    apiService.getUser(userName.getText().toString(), passWord.getText().toString()).enqueue(new Callback<UserModel>() {

                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            if (response.body() != null) {
                                //Log.i("LOGIN", response.body().getName());

                                manager.setPreferences(Login.this, "id", String.valueOf(response.body().getId()));
                                manager.setPreferences(Login.this, "password", String.valueOf(response.body().getPassword()));
                                manager.setPreferences(Login.this, "userName", response.body().getUsername());
                                manager.setPreferences(Login.this, "name", response.body().getName());
                                manager.setPreferences(Login.this, "locationName", response.body().getOdjel());

                                activeUser = response.body();
                                LoggedUser.getUser().setUserModel(activeUser);

                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);

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


}
