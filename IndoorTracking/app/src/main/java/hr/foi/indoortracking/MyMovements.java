package hr.foi.indoortracking;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dbaccess.ApiEndpoint;
import com.example.dbaccess.HistoryModel;
import com.example.dbaccess.LocationCategoryModel;
import com.example.dbaccess.LocationModel;
import com.example.dbaccess.RetrofitConnection;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Paula on 20.12.2016..
 */

public class MyMovements extends AppCompatActivity {

    private Button showAll;
    private Button searchByDate;
    private Button searchByLocation;
    private ArrayAdapter<LocationModel> spinnerArrayAdapter;
    private Spinner spinner;
    private EditText from_editText;
    private EditText to_editText;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int locationId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_movements);
        setTitle("Moja kretanja");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }




}