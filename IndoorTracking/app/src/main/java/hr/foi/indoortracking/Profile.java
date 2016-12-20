package hr.foi.indoortracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Zana on 19.12.2016..
 */

public class Profile  extends AppCompatActivity {

    private TextView nameTextView;
    private TextView surnameTextView;
    private TextView usernameTextView;
    private TextView passwordTextView;
    private TextView odjelTextView;
    Button button_LogOut;
    SessionManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        button_LogOut = (Button) findViewById(R.id.button_LogOut);
        manager = new SessionManager();
        nameTextView = (TextView) findViewById(R.id.textview_name);
        surnameTextView = (TextView) findViewById(R.id.textview_surname);
        usernameTextView = (TextView) findViewById(R.id.textview_username);
        passwordTextView = (TextView) findViewById(R.id.textview_password);
        odjelTextView = (TextView) findViewById(R.id.textview_odjel);

       // usernameTextView.setText(String.format("Korisničko ime: %s",user.getUsername()));

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
