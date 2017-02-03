package hr.foi.indoortracking;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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
import hr.foi.dbaccess.RetrofitConnection;
import hr.foi.dbaccess.UserModel;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Zana on 24.1.2017..
 */


public class Users extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    private ListView usersListView;
    ArrayAdapter<UserModel> usersListAdapter;
    public int userID;
    private Context context;


    private SessionManager manager;
    private UserModel activeUser;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle("Korisnici");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        usersListView = (ListView) findViewById(R.id.list_users);

        usersListAdapter = new ArrayAdapter<UserModel>(this,
                R.layout.list_row_users
                , new LinkedList<UserModel>()) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                UserModel usersModel = getItem(position);

                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row_users, parent, false);
                }

                TextView user = (TextView)convertView.findViewById(R.id.textview_name);
                TextView currentL = (TextView)convertView.findViewById(R.id.textview_current);

                user.setText(usersModel.getName());
                currentL.setText("Trenutna lokacija: "+usersModel.getCurrentLocationName());
                ImageView thumb_image=(ImageView)convertView.findViewById(R.id.list_image);
                thumb_image.setImageResource(R.mipmap.profil);

                return convertView;
            }
        };

        getUsers(usersListView);
        usersListView.setAdapter(usersListAdapter);
        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserModel userModel;
                userModel = usersListAdapter.getItem(position);
                userID=userModel.getUserId();
                Intent intent = new Intent(Users.this, UserProfile.class);
                intent.putExtra("ID",  Integer.toString(userID));
                startActivity(intent);
            }
        });


    }


    public void getUsers(View view) {
        usersListAdapter.clear();
        usersListAdapter.notifyDataSetChanged();
        ApiEndpoint apiService = RetrofitConnection.Factory.getInstance();
        Call<List<UserModel>> call = apiService.listUsers();
        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.isSuccess()) {
                    usersListAdapter.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                Toast.makeText(Users.this, "Greška prilikom dohvaćanja podataka!", Toast.LENGTH_SHORT).show();
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

            manager.setPreferences(Users.this, "id", "");
            manager.setPreferences(Users.this, "name", "");
            manager.setPreferences(Users.this, "username", "");
            manager.setPreferences(Users.this, "password", "");
            manager.setPreferences(Users.this, "locationId", "");
            manager.setPreferences(Users.this, "locationName", "");
            manager.setPreferences(Users.this, "locationCategory", "");
            manager.setPreferences(Users.this, "currentLocationId", "");
            manager.setPreferences(Users.this, "currentLocationName", "");
            manager.setPreferences(Users.this, "currentLocationCategory", "");
            manager.setPreferences(Users.this, "currentLocationDescription", "");
            manager.setPreferences(Users.this, "notification", "");







            LoggedUser.getUser().releaseUserModel();
            Intent intent = new Intent(Users.this, Login.class);
            startActivity(intent);
            finish();
            Toast.makeText(Users.this, "Uspješna odjava! ", Toast.LENGTH_LONG).show();
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
            Intent intent = new Intent(Users.this,MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_lokacije) {
            Intent intent = new Intent(Users.this,Category.class);
            startActivity(intent);
        } else if (id == R.id.nav_kretanja) {
            Intent intent = new Intent(Users.this, MyMovements.class);
            startActivity(intent);
        } else if (id == R.id.nav_korisnici) {
            Intent intent = new Intent(Users.this, Users.class);
            startActivity(intent);
        }
        return true;
    }



}
