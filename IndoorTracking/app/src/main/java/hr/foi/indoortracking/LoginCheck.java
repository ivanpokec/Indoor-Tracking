package hr.foi.indoortracking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Zana on 10.11.2016..
 */

public class LoginCheck extends AppCompatActivity {
    SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        manager = new SessionManager();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                String status = manager.getPreferences(LoginCheck.this,"status");
                Log.d("status",status);
                if (status=="1"){

                    Intent i=new Intent(LoginCheck.this,MainActivity.class);

                    startActivity(i);

                }else{

                    Intent i=new Intent(LoginCheck.this,Login.class);

                    startActivity(i);

                }
                finish();
            }
        }, 3*1000);
    }
            }


