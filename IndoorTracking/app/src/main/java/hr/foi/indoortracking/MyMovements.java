package hr.foi.indoortracking;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import hr.foi.core.LoggedUser;
import hr.foi.dbaccess.ApiEndpoint;
import hr.foi.dbaccess.LocationModel;
import hr.foi.dbaccess.RetrofitConnection;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import hr.foi.dbaccess.UserModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Paula on 20.12.2016..
 */

public class MyMovements extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Button showAll;
    private Button searchByDate;
    private Button searchByLocation;
    private ArrayAdapter<LocationModel> spinnerArrayAdapter;
    private Spinner spinner;
    private EditText from_editText;
    private EditText to_editText;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int locationId;

    private SessionManager manager;
    private UserModel activeUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Moja kretanja");
        setContentView(R.layout.activity_main3);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // FIRST OPTION
        showAll = (Button) findViewById(R.id.button_ShowAllHistory);
        showAll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MyMovements.this, History.class);
                intent.putExtra("HistoryType", 0);
                startActivity(intent);
                // Perform action on click
            }
        });


        //THIRD OPTION
         spinner = (Spinner) findViewById(R.id.spinner);

         spinnerArrayAdapter = new ArrayAdapter<LocationModel>(this,
                android.R.layout.simple_spinner_item
                , new LinkedList<LocationModel>()) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                LocationModel locationModel = getItem(position);
                locationId = locationModel.getId();

                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
                }

                ((TextView) convertView).setText(locationModel.getName());


                return convertView;
            }

             @Override
             public View getDropDownView(int position, View convertView,
                                         ViewGroup parent) {
                 LocationModel locationModel = getItem(position);
                 if (convertView == null) {
                     convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
                 }
                 ((TextView) convertView).setText(locationModel.getName());


                 return convertView;
             }
        };





        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getLocations(spinner);
        spinner.setAdapter(spinnerArrayAdapter);


        searchByLocation = (Button) findViewById(R.id.button_search_by_location);
        searchByLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TextView tv = (TextView) spinner.getSelectedView();
                //String proba = tv.getText().toString();
                Log.i("SPINER", String.valueOf(locationId));

                Intent intent = new Intent(MyMovements.this, History.class);
                intent.putExtra("HistoryType", 3);
                intent.putExtra("locationId", locationId);
                startActivity(intent);
            }
        });


        // SECOND OPTION
        from_editText = (EditText) findViewById(R.id.from_editText);
        to_editText = (EditText) findViewById(R.id.to_editText);

        from_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MyMovements.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                from_editText.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();


            }

        });

        to_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MyMovements.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                to_editText.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();


            }

        });

        searchByDate = (Button) findViewById(R.id.button_search);
        searchByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateFrom = from_editText.getText().toString();
                String dateTo = to_editText.getText().toString();
                if (!from_editText.getText().toString().isEmpty() && !to_editText.getText().toString().isEmpty()) {
                    Intent intent = new Intent(MyMovements.this, History.class);
                    intent.putExtra("HistoryType", 2);
                    intent.putExtra("dateFrom", dateFrom);
                    intent.putExtra("dateTo", dateTo);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MyMovements.this, "Niste unijeli datume!", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    public void getLocations(View view) {
        spinnerArrayAdapter.clear();
        spinnerArrayAdapter.notifyDataSetChanged();

        ApiEndpoint apiService = RetrofitConnection.Factory.getInstance();
        apiService.listAllLocations().enqueue(new Callback<List<LocationModel>>() {
            @Override
            public void onResponse(Call<List<LocationModel>> call, Response<List<LocationModel>> response) {
                if (response.body() != null) {

                    spinnerArrayAdapter.addAll(response.body());


                }
            }

            @Override
            public void onFailure(Call<List<LocationModel>> call, Throwable t) {
                Toast.makeText(MyMovements.this, "Greska u citanju naziva lokacije.", Toast.LENGTH_SHORT).show();
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

            manager.setPreferences(MyMovements.this, "id", "");
            manager.setPreferences(MyMovements.this, "name", "");
            manager.setPreferences(MyMovements.this, "username", "");
            manager.setPreferences(MyMovements.this, "password", "");
            manager.setPreferences(MyMovements.this, "locationId", "");
            manager.setPreferences(MyMovements.this, "locationName", "");
            manager.setPreferences(MyMovements.this, "locationCategory", "");
            manager.setPreferences(MyMovements.this, "currentLocationId", "");
            manager.setPreferences(MyMovements.this, "currentLocationName", "");
            manager.setPreferences(MyMovements.this, "currentLocationCategory", "");
            manager.setPreferences(MyMovements.this, "currentLocationDescription", "");
            manager.setPreferences(MyMovements.this, "notification", "");







            LoggedUser.getUser().releaseUserModel();
            Intent intent = new Intent(MyMovements.this, Login.class);
            startActivity(intent);
            finish();
            Toast.makeText(MyMovements.this, "Uspješna odjava! ", Toast.LENGTH_LONG).show();
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
            Intent intent = new Intent(MyMovements.this,MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_lokacije) {
            Intent intent = new Intent(MyMovements.this,Category.class);
            startActivity(intent);
        } else if (id == R.id.nav_kretanja) {
            Intent intent = new Intent(MyMovements.this, MyMovements.class);
            startActivity(intent);
        } else if (id == R.id.nav_korisnici) {
            Intent intent = new Intent(MyMovements.this, Users.class);
            startActivity(intent);
        }
        return true;
    }





}