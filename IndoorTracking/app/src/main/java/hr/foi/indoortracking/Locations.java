package hr.foi.indoortracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import hr.foi.dbaccess.ApiEndpoint;
import hr.foi.dbaccess.LocationModel;
import hr.foi.dbaccess.RetrofitConnection;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Zana on 24.1.2017..
 */

public class Locations extends AppCompatActivity {
    private ListView locationsListView;
    ArrayAdapter<LocationModel> locationsListAdapter;
    String catID;
    String catName;
    int locID;
    TextView textView_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle("Lokacije");
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_locations);

        Intent mIntent = getIntent();
        catID = mIntent.getStringExtra("ID");
        catName = mIntent.getStringExtra("name");

        //Toast.makeText(Locations.this, catID, Toast.LENGTH_SHORT).show();

        locationsListView = (ListView) findViewById(R.id.list_locations);
        textView_title = (TextView) findViewById(R.id.txtCategory);
        textView_title.setText(catName);

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

        getLocations(locationsListView, Integer.parseInt(catID));
        locationsListView.setAdapter(locationsListAdapter);

        locationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LocationModel locationModel;
                locationModel = locationsListAdapter.getItem(position);
                locID = locationModel.getId();
                Intent intent = new Intent(Locations.this, LocationDetails.class);
                intent.putExtra("ID",  Integer.toString(locID));
                startActivity(intent);
            }
        });


    }


    public void getLocations(View view, int catID){
        locationsListAdapter.clear();
        locationsListAdapter.notifyDataSetChanged();

        ApiEndpoint apiService = RetrofitConnection.Factory.getInstance();
        apiService.getLocationInCategory(catID).enqueue(new Callback<List<LocationModel>>() {
            @Override
            public void onResponse(Call<List<LocationModel>> call, Response<List<LocationModel>> response) {
                if(response.body() != null) {

                   locationsListAdapter.addAll(response.body());

                }
            }

            @Override
            public void onFailure(Call<List<LocationModel>> call, Throwable t) {
                Toast.makeText(Locations.this, "Greska u citanju naziva lokacije.", Toast.LENGTH_SHORT).show();
            }
        });

    }



    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}