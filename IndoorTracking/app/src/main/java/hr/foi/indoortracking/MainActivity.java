package hr.foi.indoortracking;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import hr.foi.core.LoggedUser;
import hr.foi.core.MainService;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {
    SessionManager manager;
    private ServiceConnection sConnection;
    private MainService mainService;
    private BroadcastReceiver myReceiver;

    private static TextView txtCurrentLocation;
    private static TextView txtCurrentLocationDesc;
    Button details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Trenutna lokacija");
        checkPermissions();

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        manager = new SessionManager();

        /*
        Login login = new Login();

        login.activeUser.setId(Integer.parseInt(manager.getPreferences(this, "id")));
        login.activeUser.setName(manager.getPreferences(this,"name"));
        login.activeUser.setUsername(manager.getPreferences(this,"userName"));
        login.activeUser.setPassword(manager.getPreferences(this,"password"));
        login.activeUser.setOdjel(manager.getPreferences(this,"locationName")); */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        txtCurrentLocation = (TextView) findViewById(R.id.txtCurrentLocation);
        txtCurrentLocation.setText("Traženje...");
        txtCurrentLocationDesc = (TextView) findViewById(R.id.txtCurrentLocationOpis);
        txtCurrentLocationDesc.setText("");
        details = (Button) findViewById(R.id.buttonDetails);

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,DetailsCurrentLocation.class);
                startActivity(intent);
            }
        });

        final Intent mService = new Intent(this, MainService.class);
        startService(mService);

        sConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mainService =((MainService.MyBinder)service).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mainService = null;
            }
        };

        bindService(mService, sConnection, Context.BIND_AUTO_CREATE);

        myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                String nazivLokacija = extras.getString("Naziv");
                String opisLokacija = extras.getString("Opis");
                txtCurrentLocation.setText(nazivLokacija);
                txtCurrentLocationDesc.setText(opisLokacija);

                //Toast.makeText(MainActivity.this, "Sada se nalazite na " + nazivLokacija, Toast.LENGTH_LONG).show();
            }
        };

        IntentFilter filter = new IntentFilter("ServiceIntent");

        registerReceiver(myReceiver, filter);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            this.finishAffinity();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_profil) {
            Intent intent = new Intent(this, Profile.class);
            startActivity(intent);
        }

        if(id== R.id.action_logout) {
            manager.setPreferences(MainActivity.this, "id", "");
            manager.setPreferences(MainActivity.this, "name", "");
            manager.setPreferences(MainActivity.this, "userName", "");
            manager.setPreferences(MainActivity.this, "passWord", "");
            manager.setPreferences(MainActivity.this, "locationName", "");
            LoggedUser.getUser().releaseUserModel();
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        if (id == R.id.nav_trenutna) {

        } else if (id == R.id.nav_lokacije) {

        } else if (id == R.id.nav_kretanja) {
            Intent intent = new Intent(MainActivity.this, MyMovements.class);
            startActivity(intent);
        } else if (id == R.id.nav_korisnici) {

        }
        return true;
    }

    private boolean checkPermissions(){

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH},
                    2);


            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            return false;
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH_ADMIN)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH_ADMIN},
                    2);


            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            return false;
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    2);


            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            return false;
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    2);


            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            return false;
        }
        return true;
    }

}
