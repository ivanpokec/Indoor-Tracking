package hr.foi.indoortracking;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dbaccess.ApiEndpoint;
import com.example.dbaccess.RetrofitConnection;
import com.example.dbaccess.UserModel;

import hr.foi.core.LoggedUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Zana on 19.12.2016..
 */

public class Profile  extends AppCompatActivity {

    public static UserModel activeUser = new UserModel();
    private TextView nameTextView;
    private TextView surnameTextView;
    private TextView usernameTextView;
    private TextView passwordTextView;
    private TextView passwordTextView1;
    private TextView odjelTextView;
    Button button_LogOut;
    SessionManager manager;
    final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle("Moj profil");
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_profile);
        button_LogOut = (Button) findViewById(R.id.button_LogOut);
        manager = new SessionManager();
        nameTextView = (TextView) findViewById(R.id.textview_name);
        surnameTextView = (TextView) findViewById(R.id.textview_surname);
        usernameTextView = (TextView) findViewById(R.id.textview_username);
        passwordTextView = (TextView) findViewById(R.id.textview_password);
        odjelTextView = (TextView) findViewById(R.id.textview_odjel);
        passwordTextView1 = (TextView) findViewById(R.id.textview_password1);

        final String username = LoggedUser.getUser().getUserModel().getUsername();
        final String odjel = LoggedUser.getUser().getUserModel().getLocationName();
        final String name = LoggedUser.getUser().getUserModel().getName();
        final int id = LoggedUser.getUser().getUserModel().getUserId();


        String[] odvojeno = name.split(" ");
        final String passWord = LoggedUser.getUser().getUserModel().getPassword();


        usernameTextView.setText(String.format(username));
        nameTextView.setText(String.format(odvojeno[0]));
        surnameTextView.setText(String.format(odvojeno[1]));
        passwordTextView1.setText("Lozinka: ");
        passwordTextView.setTransformationMethod(new PasswordTransformationMethod());
        passwordTextView.setText(String.format(passWord));
        odjelTextView.setText(String.format(odjel));


        passwordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Profile.this);
                dialog.setContentView(R.layout.activity_change_password);
                dialog.setTitle("Promijeni lozinku");

                //final Context context = dialog.getContext();
                //final LayoutInflater inflater = LayoutInflater.from(context);
                //final View layout = View.inflate(this, R.layout.activity_change_password, null);

                Button dialogButton1 = (Button) dialog.findViewById(R.id.dialogButtonCancle);
                Button dialogButton2 = (Button) dialog.findViewById(R.id.dialogButtonSave);
                final EditText passWordChange = (EditText) dialog.findViewById(R.id.editText_Password_change);


                dialogButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialogButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Profile.this,String.valueOf(id), Toast.LENGTH_SHORT).show();
                        if (passWordChange.getText().toString().isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Niste unijeli lozinku!", Toast.LENGTH_SHORT).show();
                        } else {

                            ApiEndpoint apiService = RetrofitConnection.Factory.getInstance();
                            apiService.changePassword(passWordChange.getText().toString(),id).enqueue(new Callback<UserModel>() {
                                @Override
                                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                                    if (response.body() != null) {

                                        //manager.setPreferences(Profile.this, "id",String.valueOf(id));

                                        manager.setPreferences(Profile.this, "id", String.valueOf(response.body().getUserId()));
                                        manager.setPreferences(Profile.this, "name", response.body().getName());
                                        manager.setPreferences(Profile.this, "username", response.body().getUsername());
                                        manager.setPreferences(Profile.this, "locationId", String.valueOf(response.body().getLocationId()));
                                        manager.setPreferences(Profile.this, "locationName", response.body().getLocationName());
                                        manager.setPreferences(Profile.this, "locationCategory", response.body().getLocationCategory());
                                        manager.setPreferences(Profile.this, "currentLocationId", String.valueOf(response.body().getCurrentLocationId()));
                                        manager.setPreferences(Profile.this, "currentLocationName", response.body().getCurrentLocationName());
                                        manager.setPreferences(Profile.this, "currentLocationCategory", response.body().getCurrentLocationCategory());
                                        manager.setPreferences(Profile.this, "currentLocationDescription", response.body().getCurrentLocationDescription());
                                        manager.setPreferences(Profile.this, "notification", String.valueOf(response.body().getNotification()));

                                        /*
                                        manager.setPreferences(Profile.this, "password", String.valueOf(response.body().getPassword()));
                                        manager.setPreferences(Profile.this, "userName",username);
                                        manager.setPreferences(Profile.this, "name",name);
                                        manager.setPreferences(Profile.this, "locationName",odjel); */

                                        activeUser = response.body();
                                        LoggedUser.getUser().setUserModel(activeUser);

                                    }  else {
                                        Toast.makeText(Profile.this, "Neuspješna promjena!", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<UserModel> call, Throwable t) {
                                    Toast.makeText(Profile.this, "Greška prilikom promjene!", Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                   dialog.dismiss();
                    }


                });

                dialog.show();

            }
        });

        button_LogOut.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

             manager.setPreferences(Profile.this, "id", "");
             manager.setPreferences(Profile.this, "name", "");
             manager.setPreferences(Profile.this, "username", "");
             manager.setPreferences(Profile.this, "locationId", "");
             manager.setPreferences(Profile.this, "locationName", "");
             manager.setPreferences(Profile.this, "locationCategory", "");
             manager.setPreferences(Profile.this, "currentLocationId", "");
             manager.setPreferences(Profile.this, "currentLocationName", "");
             manager.setPreferences(Profile.this, "currentLocationCategory", "");
             manager.setPreferences(Profile.this, "currentLocationDescription", "");
             manager.setPreferences(Profile.this, "notification", "");

             /*
             manager.setPreferences(Profile.this, "id", "");
             manager.setPreferences(Profile.this, "name", "");
             manager.setPreferences(Profile.this, "userName", "");
             manager.setPreferences(Profile.this, "passWord", "");
             manager.setPreferences(Profile.this, "locationName", ""); */

             LoggedUser.getUser().releaseUserModel();
             Intent intent = new Intent(Profile.this, Login.class);
             startActivity(intent);
             finish();
             Toast.makeText(Profile.this, "Uspješna odjava! ", Toast.LENGTH_LONG).show();

         }
     });

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}


