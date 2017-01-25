package hr.foi.indoortracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Zana on 25.1.2017..
 */

public class UserProfile extends AppCompatActivity {

    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Profil korisnika");
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_user_profile);
        Intent mIntent = getIntent();
        userID = mIntent.getStringExtra("ID");

        Toast.makeText(UserProfile.this, userID, Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
