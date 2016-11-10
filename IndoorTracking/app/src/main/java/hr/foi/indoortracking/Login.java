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

public class Login extends AppCompatActivity {
    EditText userName, password;
    Button logIn;
    SessionManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        manager = new SessionManager();
        userName = (EditText)findViewById(R.id.editText_User);
        password = (EditText)findViewById(R.id.editText_Password);
        logIn=(Button)findViewById(R.id.button_LogIn);

       logIn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (userName.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {

                   Toast.makeText(Login.this,"Niste unijeli potrebne podatke!",Toast.LENGTH_SHORT).show();
               }
               else  {


                   //Log.d("user",userName.getText().toString());


                   manager.setPreferences(Login.this,"status","1");
                   String status=manager.getPreferences(Login.this,"status");
                   Log.d("status", status);
                   Intent intent = new Intent(Login.this, MainActivity.class);
                   startActivity(intent);
               }




           }
       });
    }


}
