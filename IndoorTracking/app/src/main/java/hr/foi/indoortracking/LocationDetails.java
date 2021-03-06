package hr.foi.indoortracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import hr.foi.dbaccess.ApiEndpoint;
import hr.foi.dbaccess.RetrofitConnection;
import hr.foi.dbaccess.UserModel;

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
    ArrayAdapter<UserModel> curretUserOnlocationListAdapter;

//    private ListView userOnlocationView;
//    ArrayAdapter<UserModel> userOnlocationListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Detalji lokacije");
        setContentView(R.layout.activity_location_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent mIntent = getIntent();
        locID = mIntent.getStringExtra("ID");

        curretUserOnlocationView = (ListView) findViewById(R.id.trenutnonalokacijilist);

        curretUserOnlocationListAdapter = new ArrayAdapter<UserModel>(LocationDetails.this,
                R.layout.list_row_on_location
                , new LinkedList<UserModel>()) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                UserModel userModel = getItem(position);

                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row_on_location, parent, false);
                }

                TextView user = (TextView)convertView.findViewById(R.id.textview_name);

                user.setText(userModel.getName());
                ImageView thumb_image=(ImageView)convertView.findViewById(R.id.list_image);
                thumb_image.setImageResource(R.mipmap.online);


                return convertView;
            }
        };
        getCurrentUserOnLocation(curretUserOnlocationView,Integer.parseInt(locID));
        curretUserOnlocationView.setAdapter(curretUserOnlocationListAdapter);

        //default user on location

//        userOnlocationView = (ListView) findViewById(R.id.pridruzenilokacijilist);
//
//        userOnlocationListAdapter = new ArrayAdapter<UserModel>(LocationDetails.this,
//                R.layout.list_row_on_location
//                , new LinkedList<UserModel>()) {
//
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//
//                UserModel userModel = getItem(position);
//
//                if (convertView == null) {
//                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row_on_location, parent, false);
//                }
//
//                TextView user = (TextView)convertView.findViewById(R.id.textview_name);
//
//                user.setText(userModel.getName());
//                ImageView thumb_image=(ImageView)convertView.findViewById(R.id.list_image);
//                thumb_image.setImageResource(R.mipmap.offline);
//
//                return convertView;
//            }
//        };
//
//
//        getDefaultUserOnLocation(userOnlocationView,Integer.parseInt(locID));
//        userOnlocationView.setAdapter(userOnlocationListAdapter);
    }

    public void getCurrentUserOnLocation(View view, int locId ){
        curretUserOnlocationListAdapter.clear();
        curretUserOnlocationListAdapter.notifyDataSetChanged();

        ApiEndpoint apiService = RetrofitConnection.Factory.getInstance();
        apiService.getCurrentUsersOnLocation(locId).enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if(response.body() != null) {

                    curretUserOnlocationListAdapter.addAll(response.body());

                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                Toast.makeText(LocationDetails.this, "Greska u citanju naziva lokacije.", Toast.LENGTH_SHORT).show();
            }
        });

    }

//    public void getDefaultUserOnLocation(View view, int locId ){
//        userOnlocationListAdapter.clear();
//        userOnlocationListAdapter.notifyDataSetChanged();
//
//        ApiEndpoint apiService = RetrofitConnection.Factory.getInstance();
//        apiService.getUsersOnLocation(locId).enqueue(new Callback<List<UserModel>>() {
//            @Override
//            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
//                if(response.body() != null) {
//
//                    userOnlocationListAdapter.addAll(response.body());
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<UserModel>> call, Throwable t) {
//                Toast.makeText(LocationDetails.this, "Greska u citanju naziva lokacije.", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
