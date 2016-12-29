package hr.foi.indoortracking;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dbaccess.ApiEndpoint;
import com.example.dbaccess.HistoryModel;
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
        //String name = manager.getPreferences(Profile.this,"username");

        /*
        Login login = new Login();

        String username = login.activeUser.getUsername();
        String odjel = login.activeUser.getOdjel();
        String name = login.activeUser.getName(); */

        String username = LoggedUser.getUser().getUserModel().getUsername();
        String odjel = LoggedUser.getUser().getUserModel().getOdjel();
        String name = LoggedUser.getUser().getUserModel().getName();


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
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.activity_change_password);
                dialog.setTitle("Promijeni lozinku");

                final Context context = dialog.getContext();
                final LayoutInflater inflater = LayoutInflater.from(context);
                final View view = inflater.inflate(R.layout.activity_change_password, null, false);

                Button dialogButton1 = (Button) dialog.findViewById(R.id.dialogButtonCancle);
                Button dialogButton2 = (Button) dialog.findViewById(R.id.dialogButtonSave);
                final EditText passwordC = (EditText) view.findViewById(R.id.editText_Password_change);


                dialogButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialogButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (passwordC.getText().toString().isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Niste unijeli lozinku!", Toast.LENGTH_SHORT).show();
                        } else {


                        }
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
             manager.setPreferences(Profile.this, "userName", "");
             manager.setPreferences(Profile.this, "passWord", "");
             manager.setPreferences(Profile.this, "locationName", "");
             LoggedUser.getUser().releaseUserModel();
             Intent intent = new Intent(Profile.this, Login.class);
             startActivity(intent);
             finish();
         }
     });

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}


