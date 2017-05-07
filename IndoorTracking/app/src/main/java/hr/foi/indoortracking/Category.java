package hr.foi.indoortracking;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import hr.foi.core.LoggedUser;
import hr.foi.core.MainService;
import hr.foi.dbaccess.ApiEndpoint;
import hr.foi.dbaccess.CategoryModel;
import hr.foi.dbaccess.LocationModel;
import hr.foi.dbaccess.RetrofitConnection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import hr.foi.dbaccess.UserModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Zana on 2.1.2017..
 */

public class Category extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private SessionManager manager;
    private UserModel activeUser;

    private ListView locationsListView;
    ArrayAdapter<LocationModel> locationsListAdapter;

    int locID;

    public int catId;


    private ArrayList<LocationModel> data;
    private DataAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle("Lokacije");
        super.onCreate(savedInstanceState);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_main1);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        locationsListView = (ListView) findViewById(R.id.location_cat_list_view);

        locationsListAdapter = new ArrayAdapter<LocationModel>(this, R.layout.list_row_simple, new LinkedList<LocationModel>()) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                LocationModel locationModel = getItem(position);

                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row_simple, parent, false);
                }

                TextView location = (TextView)convertView.findViewById(R.id.textview_name);

                location.setText(locationModel.getName());

                return convertView;
            }
        };

        initViews();
    }

    private void initViews(){

        getLocations();
        locationsListView.setAdapter(locationsListAdapter);

        locationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LocationModel locationModel;
                locationModel = locationsListAdapter.getItem(position);
                locID = locationModel.getId();
                Intent intent = new Intent(Category.this, LocationDetails.class);
                intent.putExtra("ID",  Integer.toString(locID));
                startActivity(intent);
            }
        });
    }

    public void getLocations() {

        locationsListAdapter.clear();
        locationsListAdapter.notifyDataSetChanged();

        ApiEndpoint apiService = RetrofitConnection.Factory.getInstance();
        Call<List<LocationModel>> call = apiService.listAllLocations();
        call.enqueue(new Callback<List<LocationModel>>() {
            @Override
            public void onResponse(Call<List<LocationModel>> call, Response<List<LocationModel>> response) {
                if(response.body() != null) {

                    locationsListAdapter.addAll(response.body());

                }
            }

            @Override
            public void onFailure(Call<List<LocationModel>> call, Throwable t) {
                Toast.makeText(Category.this, "Greška prilikom dohvaćanja podataka!", Toast.LENGTH_SHORT).show();
            }
        });


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

            manager.setPreferences(Category.this, "id", "");
            manager.setPreferences(Category.this, "name", "");
            manager.setPreferences(Category.this, "username", "");
            manager.setPreferences(Category.this, "password", "");
            manager.setPreferences(Category.this, "locationId", "");
            manager.setPreferences(Category.this, "locationName", "");
            manager.setPreferences(Category.this, "locationCategory", "");
            manager.setPreferences(Category.this, "currentLocationId", "");
            manager.setPreferences(Category.this, "currentLocationName", "");
            manager.setPreferences(Category.this, "currentLocationCategory", "");
            manager.setPreferences(Category.this, "currentLocationDescription", "");
            manager.setPreferences(Category.this, "notification", "");


            LoggedUser.getUser().releaseUserModel();
            Intent intent = new Intent(Category.this, Login.class);
            startActivity(intent);
            finish();
            Toast.makeText(Category.this, "Uspješna odjava! ", Toast.LENGTH_LONG).show();
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
            Intent intent = new Intent(Category.this,MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_lokacije) {
            Intent intent = new Intent(Category.this,Category.class);
            startActivity(intent);
        } else if (id == R.id.nav_kretanja) {
            Intent intent = new Intent(Category.this, MyMovements.class);
            startActivity(intent);
        } else if (id == R.id.nav_korisnici) {
            Intent intent = new Intent(Category.this, Users.class);
            startActivity(intent);
        }
        return true;
    }



}
