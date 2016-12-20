package hr.foi.indoortracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dbaccess.ApiEndpoint;
import com.example.dbaccess.HistoryModel;
import com.example.dbaccess.RetrofitConnection;
import com.example.dbaccess.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Zana on 19.12.2016..
 */

public class Profile  extends AppCompatActivity {

    private TextView nameTextView;
    private TextView surnameTextView;
    private TextView usernameTextView;
    private TextView passwordTextView;
    private TextView passwordTextView1;
    private TextView odjelTextView;
    Button button_LogOut;
    SessionManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Moj profil");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        button_LogOut = (Button) findViewById(R.id.button_LogOut);
        manager = new SessionManager();
        nameTextView = (TextView) findViewById(R.id.textview_name);
        surnameTextView = (TextView) findViewById(R.id.textview_surname);
        usernameTextView = (TextView) findViewById(R.id.textview_username);
        passwordTextView = (TextView) findViewById(R.id.textview_password);
        odjelTextView = (TextView) findViewById(R.id.textview_odjel);
        passwordTextView1 = (TextView) findViewById(R.id.textview_password1);
        //String name = manager.getPreferences(Profile.this,"username");
        Login login = new Login();
        String username = login.activeUser.getUsername();
        String odjel = login.activeUser.getOdjel();
        String name = login.activeUser.getName();
        String[] odvojeno = name.split(" ");
        String passWord = login.activeUser.getPassword();


        usernameTextView.setText(String.format("Korisničko ime: %s",username));
        nameTextView.setText(String.format("Ime: %s",odvojeno[0]));
        surnameTextView.setText(String.format("Prezime: %s",odvojeno[1]));
        passwordTextView1.setText("Lozinka: ");
        passwordTextView.setTransformationMethod(new PasswordTransformationMethod());
        passwordTextView.setText(String.format(passWord));
        odjelTextView.setText(String.format("Odjel: %s",odjel));



        button_LogOut.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             manager.setPreferences(Profile.this, "id", "");
             manager.setPreferences(Profile.this, "name", "");
             manager.setPreferences(Profile.this, "userName", "");
             manager.setPreferences(Profile.this, "passWord", "");
             manager.setPreferences(Profile.this, "locationName", "");
             Intent intent = new Intent(Profile.this, Login.class);
             startActivity(intent);
             finish();
         }
     });

    }

}
