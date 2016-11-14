package hr.foi.indoortracking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dbaccess.RetrofitConnection;
import com.example.dbaccess.UserModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    EditText userName, password;
    Button logIn;
    SessionManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        manager = new SessionManager();
        userName = (EditText) findViewById(R.id.editText_User);
        password = (EditText) findViewById(R.id.editText_Password);
        logIn = (Button) findViewById(R.id.button_LogIn);

        String id = manager.getPreferences(Login.this, "id");
        if (id != "") {
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        }

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userName.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    Toast.makeText(Login.this, "Niste unijeli potrebne podatke!", Toast.LENGTH_SHORT).show();
                } else {
                    //Log.d("user",userName.getText().toString());
                    //String status = manager.getPreferences(Login.this, "status");
                    //Log.d("status", status);

                    RetrofitConnection.Factory.getIstance().response(userName.getText().toString(), password.getText().toString()).enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            if (response.body() != null) {
                                //Log.i("LOGIN", response.body().getName());
                                
                                manager.setPreferences(Login.this, "id", String.valueOf(response.body().getId()));
                                manager.setPreferences(Login.this, "username", response.body().getUsername());
                                manager.setPreferences(Login.this, "name", response.body().getName());

                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(Login.this, "Pogrešni podaci za prijavu!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            Log.i("LOGIN", "error");
                            Toast.makeText(Login.this, "Greška prilikom prijave!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });
    }


}
