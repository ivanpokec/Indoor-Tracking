package hr.foi.indoortracking;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
    }

}
