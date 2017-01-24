package hr.foi.indoortracking;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dbaccess.ApiEndpoint;
import com.example.dbaccess.CategoryModel;
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
    ArrayAdapter<LocationModel> locationsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle("Lokacije");
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_locations);


        locationsListView = (ListView) findViewById(R.id.list_category);

       /* locationsListAdapter = new ArrayAdapter<LocationModel>(this,
                android.R.layout.simple_list_item_1
                , new LinkedList<LocationModel>()) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {



               LocationModel locationModel = getItem(position);

                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                }

                ((TextView) convertView).setText(locationModel.name);


                return convertView;
            }
        }; */

       // getLocations(locationsListView);
        //locationsListView.setAdapter(locationsListAdapter);


        }


   /* public void getLocations(View view) {
        locationsListAdapter.clear();
        locationsListAdapter.notifyDataSetChanged();
        ApiEndpoint apiService = RetrofitConnection.Factory.getInstance();
        Call<List<LocationModel>> call = apiService.getLocationInCategory();
        call.enqueue(new Callback<List<LocationModel>>() {
            @Override
            public void onResponse(Call<List<LocationModel>> call, Response<List<LocationModel>> response) {
                if (response.isSuccess()) {
                    locationsListAdapter.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<LocationModel>> call, Throwable t) {
                Toast.makeText(Locations.this, "Greška prilikom dohvaćanja podataka!", Toast.LENGTH_SHORT).show();
            }
        });


    }
*/
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}