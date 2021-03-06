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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import hr.foi.dbaccess.UserModel;

import hr.foi.core.LoggedUser;
import hr.foi.core.MainService;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

    private SessionManager manager;
    private UserModel activeUser;

    private ServiceConnection sConnection;
    private MainService mainService;
    private BroadcastReceiver myReceiver;
    private IntentFilter filter;

    private static TextView txtCurrentLocation;
    private static TextView txtCurrentLocationDesc;
    private static TextView txtCategory;
    private Button details;

    Intent mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Trenutna lokacija");
        boolean permisions = checkPermissions(2);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        manager = new SessionManager();
        mainService = new MainService();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        txtCurrentLocation = (TextView) findViewById(R.id.txtCurrentLocation);
        txtCurrentLocation.setText(LoggedUser.getUser().getUserModel().getCurrentLocationName());

        if (LoggedUser.getUser().getUserModel().getCurrentLocationName() != "") {
            txtCurrentLocation.setText(LoggedUser.getUser().getUserModel().getCurrentLocationName());
            if(txtCurrentLocation.getText().toString().equals("Mobilisis Ulaz")) {
                ImageView img= (ImageView) findViewById(R.id.imageView_plan);
                img.setImageResource(R.mipmap.ulaz);
            }else if (txtCurrentLocation.getText().toString().equals("soba1 T1")) {
                ImageView img= (ImageView) findViewById(R.id.imageView_plan);
                img.setImageResource(R.mipmap.soba1);
            }else if (txtCurrentLocation.getText().toString().equals("soba1 T3")) {
                ImageView img= (ImageView) findViewById(R.id.imageView_plan);
                img.setImageResource(R.mipmap.t3);
             }else if (txtCurrentLocation.getText().toString().equals("Wc")) {
                ImageView img= (ImageView) findViewById(R.id.imageView_plan);
                img.setImageResource(R.mipmap.wc);
            }else {
                ImageView img= (ImageView) findViewById(R.id.imageView_plan);
                img.setImageResource(R.mipmap.plan);
                        }
        }

        txtCurrentLocationDesc = (TextView) findViewById(R.id.txtCurrentLocationOpis);
        txtCategory = (TextView) findViewById(R.id.txtCategory);

        txtCategory.setText(LoggedUser.getUser().getUserModel().getCurrentLocationCategory());
        txtCurrentLocationDesc.setText(LoggedUser.getUser().getUserModel().getCurrentLocationDescription());

        details = (Button) findViewById(R.id.buttonDetails);

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,DetailsCurrentLocation.class);
                startActivity(intent);
            }
        });

        mService = new Intent(this, MainService.class);
        if(permisions == true){
            startService(mService);

        }

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
                String kategorija = extras.getString("Kategorija");
                String opisLokacija = extras.getString("Opis");

                txtCurrentLocation.setText(nazivLokacija);
                txtCategory.setText(kategorija);
                txtCurrentLocationDesc.setText(opisLokacija);

                LoggedUser.getUser().getUserModel().setCurrentLocationId(extras.getInt("LocationId"));
                LoggedUser.getUser().getUserModel().setCurrentLocationName(nazivLokacija);
                LoggedUser.getUser().getUserModel().setCurrentLocationDescription(opisLokacija);
                LoggedUser.getUser().getUserModel().setCurrentLocationCategory(kategorija);


                if(nazivLokacija.toString().equals("Mobilisis Ulaz")) {
                    ImageView img= (ImageView) findViewById(R.id.imageView_plan);
                    img.setImageResource(R.mipmap.ulaz);
                } else if (nazivLokacija.toString().equals("soba1 T1")) {
                    ImageView img= (ImageView) findViewById(R.id.imageView_plan);
                    img.setImageResource(R.mipmap.soba1);
                } else if (nazivLokacija.toString().equals("soba1 T3")) {
                    ImageView img= (ImageView) findViewById(R.id.imageView_plan);
                    img.setImageResource(R.mipmap.t3);
                } else if (nazivLokacija.toString().equals("Wc")) {
                    ImageView img= (ImageView) findViewById(R.id.imageView_plan);
                    img.setImageResource(R.mipmap.wc);
                } else {
                    ImageView img= (ImageView) findViewById(R.id.imageView_plan);
                    img.setImageResource(R.mipmap.plan);
                }

                //Toast.makeText(MainActivity.this, "Sada se nalazite na " + nazivLokacija, Toast.LENGTH_LONG).show();
            }
        };

        filter = new IntentFilter("ServiceIntent");

        registerReceiver(myReceiver, filter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        startService(mService);
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
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
        }
        if (id == R.id.action_profil) {
            Intent intent = new Intent(this, Profile.class);
            startActivity(intent);
        }

        if(id== R.id.action_logout) {

            manager.setPreferences(MainActivity.this, "id", "");
            manager.setPreferences(MainActivity.this, "name", "");
            manager.setPreferences(MainActivity.this, "username", "");
            manager.setPreferences(MainActivity.this, "password", "");
            manager.setPreferences(MainActivity.this, "locationId", "");
            manager.setPreferences(MainActivity.this, "locationName", "");
            manager.setPreferences(MainActivity.this, "locationCategory", "");
            manager.setPreferences(MainActivity.this, "currentLocationId", "");
            manager.setPreferences(MainActivity.this, "currentLocationName", "");
            manager.setPreferences(MainActivity.this, "currentLocationCategory", "");
            manager.setPreferences(MainActivity.this, "currentLocationDescription", "");
            manager.setPreferences(MainActivity.this, "notification", "");

            /*
            manager.setPreferences(MainActivity.this, "id", "");
            manager.setPreferences(MainActivity.this, "name", "");
            manager.setPreferences(MainActivity.this, "userName", "");
            manager.setPreferences(MainActivity.this, "passWord", "");
            manager.setPreferences(MainActivity.this, "locationName", "");*/

            unbindService(sConnection);
            unregisterReceiver(myReceiver);
            stopService(mService);

            LoggedUser.getUser().releaseUserModel();
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
            Toast.makeText(MainActivity.this, "Uspješna odjava! ", Toast.LENGTH_LONG).show();
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
            Intent intent = new Intent(MainActivity.this,MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_lokacije) {
            Intent intent = new Intent(MainActivity.this,Category.class);
            startActivity(intent);
        } else if (id == R.id.nav_kretanja) {
            Intent intent = new Intent(MainActivity.this, MyMovements.class);
            startActivity(intent);
        } else if (id == R.id.nav_korisnici) {
            Intent intent = new Intent(MainActivity.this, Users.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

        switch (requestCode) {
            case 2: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(MainActivity.this, "Permission3", Toast.LENGTH_SHORT).show();
                    checkPermissions(3);
                } else {



                }
                return;
            }
            case 3: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(MainActivity.this, "Permission4", Toast.LENGTH_SHORT).show();
                    checkPermissions(4);
                } else {



                }
                return;
            }
            case 4: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(MainActivity.this, "Permission5", Toast.LENGTH_SHORT).show();
                    checkPermissions(5);
                } else {



                }
                return;
            }
            case 5: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(MainActivity.this, "Permission6", Toast.LENGTH_SHORT).show();
                    startService(mService);

                } else {



                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    private boolean checkPermissions(int perm){

        switch (perm){
            case 2:{
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.BLUETOOTH)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.BLUETOOTH},
                            2);



                    return false;
                }
            }
            case 3:{
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.BLUETOOTH_ADMIN)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.BLUETOOTH_ADMIN},
                            3);

                    return false;
                }
            }
            case 4:{
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            4);

                    return false;
                }
            }
            case 5:{
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            5);

                }
            }

        }

        return true;
    }



}
