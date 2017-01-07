package hr.foi.indoortracking;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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
import com.example.dbaccess.CategoryModel;
import com.example.dbaccess.LocationModel;
import com.example.dbaccess.RetrofitConnection;
import com.example.dbaccess.UserLocationModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import hr.foi.core.MainService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Zana on 22.12.2016..
 */

public class DetailsCurrentLocation  extends AppCompatActivity{

    private BroadcastReceiver DetailReceiver;
    private ServiceConnection sConnection;
    private MainService mainService;
    private BroadcastReceiver myReceiver;

    private ListView curretUserOnlocationView;
    ArrayAdapter<UserLocationModel> curretUserOnlocationListAdapter;

    private ListView userOnlocationView;
    ArrayAdapter<UserLocationModel> userOnlocationListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Detalji");
        setContentView(R.layout.activity_details_current_location);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent mService = new Intent(this, MainService.class);

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


                int idLokcaije = extras.getInt("LocationId");
                setTitle("Detalji za "+extras.getString("Naziv"));
                Toast.makeText(DetailsCurrentLocation.this, "lokacija: "+idLokcaije, Toast.LENGTH_SHORT).show();

                curretUserOnlocationView = (ListView) findViewById(R.id.trenutnonalokacijilist);

                curretUserOnlocationListAdapter = new ArrayAdapter<UserLocationModel>(DetailsCurrentLocation.this,
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
                getCurrentUserOnLocation(curretUserOnlocationView,idLokcaije);
                curretUserOnlocationView.setAdapter(curretUserOnlocationListAdapter);

                //default user on location

                userOnlocationView = (ListView) findViewById(R.id.pridruzenilokacijilist);

                userOnlocationListAdapter = new ArrayAdapter<UserLocationModel>(DetailsCurrentLocation.this,
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
                getDefaultUserOnLocation(userOnlocationView,idLokcaije);
                userOnlocationView.setAdapter(userOnlocationListAdapter);

                //Toast.makeText(DetailsCurrentLocation.this, "ID lokacije:  " + idLokcaije, Toast.LENGTH_LONG).show();
            }
        };

        IntentFilter filter = new IntentFilter("ServiceIntent");

        registerReceiver(myReceiver, filter);


    }

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
                Toast.makeText(DetailsCurrentLocation.this, "Greska u citanju naziva lokacije.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(DetailsCurrentLocation.this, "Greska u citanju naziva lokacije.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
