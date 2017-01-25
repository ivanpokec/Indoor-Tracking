package hr.foi.indoortracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dbaccess.ApiEndpoint;
import com.example.dbaccess.RetrofitConnection;
import com.example.dbaccess.UserLocationModel;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Zana on 25.1.2017..
 */

public class LocationDetails extends AppCompatActivity {
    String locID;
    private ListView curretUserOnlocationView;
    ArrayAdapter<UserLocationModel> curretUserOnlocationListAdapter;

    private ListView userOnlocationView;
    ArrayAdapter<UserLocationModel> userOnlocationListAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Detalji lokacije");
        setContentView(R.layout.activity_location_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent mIntent = getIntent();
        locID = mIntent.getStringExtra("ID");

        curretUserOnlocationView = (ListView) findViewById(R.id.trenutnonalokacijilist);

        curretUserOnlocationListAdapter = new ArrayAdapter<UserLocationModel>(LocationDetails.this,
                android.R.layout.simple_list_item_1
                , new LinkedList<UserLocationModel>()) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {



                UserLocationModel userModel = getItem(position);

                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                }

                ((TextView) convertView).setText(userModel.getUsrName());


                return convertView;
            }
        };
        getCurrentUserOnLocation(curretUserOnlocationView,Integer.parseInt(locID));
        curretUserOnlocationView.setAdapter(curretUserOnlocationListAdapter);

        //default user on location

        userOnlocationView = (ListView) findViewById(R.id.pridruzenilokacijilist);

        userOnlocationListAdapter = new ArrayAdapter<UserLocationModel>(LocationDetails.this,
                android.R.layout.simple_list_item_1
                , new LinkedList<UserLocationModel>()) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                UserLocationModel userModel = getItem(position);

                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                }

                ((TextView) convertView).setText(userModel.getUsrName());


                return convertView;
            }
        };


        getDefaultUserOnLocation(userOnlocationView,Integer.parseInt(locID));
        userOnlocationView.setAdapter(userOnlocationListAdapter);
    };

    public void getCurrentUserOnLocation(View view, int locId ){
        curretUserOnlocationListAdapter.clear();
        curretUserOnlocationListAdapter.notifyDataSetChanged();

        ApiEndpoint apiService = RetrofitConnection.Factory.getInstance();
        apiService.getCurrentUsersOnLocation(locId).enqueue(new Callback<List<UserLocationModel>>() {
            @Override
            public void onResponse(Call<List<UserLocationModel>> call, Response<List<UserLocationModel>> response) {
                if(response.body() != null) {

                    curretUserOnlocationListAdapter.addAll(response.body());

                }
            }

            @Override
            public void onFailure(Call<List<UserLocationModel>> call, Throwable t) {
                Toast.makeText(LocationDetails.this, "Greska u citanju naziva lokacije.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getDefaultUserOnLocation(View view, int locId ){
        userOnlocationListAdapter.clear();
        userOnlocationListAdapter.notifyDataSetChanged();

        ApiEndpoint apiService = RetrofitConnection.Factory.getInstance();
        apiService.getUsersOnLocation(locId).enqueue(new Callback<List<UserLocationModel>>() {
            @Override
            public void onResponse(Call<List<UserLocationModel>> call, Response<List<UserLocationModel>> response) {
                if(response.body() != null) {

                    userOnlocationListAdapter.addAll(response.body());

                }
            }

            @Override
            public void onFailure(Call<List<UserLocationModel>> call, Throwable t) {
                Toast.makeText(LocationDetails.this, "Greska u citanju naziva lokacije.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
