package hr.foi.indoortracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import hr.foi.dbaccess.ApiEndpoint;
import hr.foi.dbaccess.RetrofitConnection;
import hr.foi.dbaccess.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Zana on 25.1.2017..
 */

public class UserProfile extends AppCompatActivity {

    String userID;
    private TextView nameTextView;
    private TextView surnameTextView;
    private TextView odjelTextView;
    String nameFL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Profil korisnika");
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_user_profile);
        Intent mIntent = getIntent();
        userID = mIntent.getStringExtra("ID");

        nameTextView = (TextView) findViewById(R.id.textview_name);
        surnameTextView = (TextView) findViewById(R.id.textview_surname);
        odjelTextView = (TextView) findViewById(R.id.textview_odjel);
        ApiEndpoint apiService = RetrofitConnection.Factory.getInstance();
        apiService.getUser(Integer.parseInt(userID)).enqueue(new Callback <UserModel>() {

            @Override
            public void onResponse(Call <UserModel> call, Response <UserModel> response) {
                if (response.body() != null) {

                    nameFL = response.body().getName();

                     odjelTextView.setText(response.body().getLocationName());
                    String[] odvojeno = nameFL.split(" ");
                    nameTextView.setText(String.format(odvojeno[0]));
                    surnameTextView.setText(String.format(odvojeno[1]));
                }
                else {
                    Toast.makeText(UserProfile.this, "Pogrešni podaci za prijavu!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(UserProfile.this, "Greška prilikom prijave!", Toast.LENGTH_SHORT).show();
            }

        });




    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
