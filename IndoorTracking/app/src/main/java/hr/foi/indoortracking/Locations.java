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

import com.example.dbaccess.ApiEndpoint;
import com.example.dbaccess.CategoryModel;
import com.example.dbaccess.LocationCategoryModel;
import com.example.dbaccess.LocationModel;
import com.example.dbaccess.RetrofitConnection;

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
    ArrayAdapter<LocationCategoryModel> locationsListAdapter;
    String catID;
    int locID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle("Lokacije");
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_locations);

        Intent mIntent = getIntent();
        catID = mIntent.getStringExtra("ID");

       //Toast.makeText(Locations.this, catID, Toast.LENGTH_SHORT).show();

        locationsListView = (ListView) findViewById(R.id.list_locations);

        locationsListAdapter = new ArrayAdapter<LocationCategoryModel>(this,
                android.R.layout.simple_list_item_1
                , new LinkedList<LocationCategoryModel>()) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {



                LocationCategoryModel locationCategoryModel = getItem(position);

                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                }

                ((TextView) convertView).setText(locationCategoryModel.getLocationInCategory());


                return convertView;
            }
        };

            getLocations(locationsListView,Integer.parseInt(catID));
            locationsListView.setAdapter(locationsListAdapter);

        locationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LocationCategoryModel locationCategoryModel;
                locationCategoryModel = locationsListAdapter.getItem(position);
                locID=locationCategoryModel.Id;
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
        apiService.getLocationInCategory(catID).enqueue(new Callback<List<LocationCategoryModel>>() {
            @Override
            public void onResponse(Call<List<LocationCategoryModel>> call, Response<List<LocationCategoryModel>> response) {
                if(response.body() != null) {

                   locationsListAdapter.addAll(response.body());

                }
            }

            @Override
            public void onFailure(Call<List<LocationCategoryModel>> call, Throwable t) {
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