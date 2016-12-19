package hr.foi.indoortracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Zana on 19.12.2016..
 */

public class Profile  extends AppCompatActivity {

    Button button_LogOut;

    SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        button_LogOut = (Button) findViewById(R.id.button_LogOut);
        manager = new SessionManager();
     button_LogOut.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             manager.setPreferences(Profile.this, "id", "");
             manager.setPreferences(Profile.this, "name", "");
             manager.setPreferences(Profile.this, "username", "");
             Intent intent = new Intent(Profile.this, Login.class);
             startActivity(intent);
             finish();
         }
     });

    }


}
