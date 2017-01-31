package hr.foi.indoortracking;

import android.content.BroadcastReceiver;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import hr.foi.dbaccess.ApiEndpoint;
import hr.foi.dbaccess.RetrofitConnection;
import hr.foi.dbaccess.UserModel;

import java.util.LinkedList;
import java.util.List;

import hr.foi.core.LoggedUser;
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
    private ProgressBar spinner;
    private String locationName;
    private int locationId;

    private ListView curretUserOnlocationView;
    ArrayAdapter<UserModel> curretUserOnlocationListAdapter;

    private ListView userOnlocationView;
    ArrayAdapter<UserModel> userOnlocationListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationName = LoggedUser.getUser().getUserModel().getCurrentLocationName();
        setTitle("Detalji za "+locationName);
        setContentView(R.layout.activity_details_current_location);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinner=(ProgressBar)findViewById(R.id.progressBar2);
        spinner.setVisibility(View.VISIBLE);

        locationId = LoggedUser.getUser().getUserModel().getCurrentLocationId();



        /*
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

        bindService(mService, sConnection, Context.BIND_AUTO_CREATE); */

        /*myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras(); */


                //int idLokcaije = extras.getInt("LocationId");
                //setTitle("Detalji za "+extras.getString("Naziv"));
                //Toast.makeText(DetailsCurrentLocation.this, "lokacija: "+idLokcaije, Toast.LENGTH_SHORT).show();

                curretUserOnlocationView = (ListView) findViewById(R.id.trenutnonalokacijilist);

                curretUserOnlocationListAdapter = new ArrayAdapter<UserModel>(DetailsCurrentLocation.this,
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

                getCurrentUserOnLocation(curretUserOnlocationView, locationId);
                curretUserOnlocationView.setAdapter(curretUserOnlocationListAdapter);
                spinner.setVisibility(View.INVISIBLE);


                //default user on location

                userOnlocationView = (ListView) findViewById(R.id.pridruzenilokacijilist);

                userOnlocationListAdapter = new ArrayAdapter<UserModel>(DetailsCurrentLocation.this,
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
                        thumb_image.setImageResource(R.mipmap.offline);


                        return convertView;
                    }
                };

                getDefaultUserOnLocation(userOnlocationView,locationId);
                userOnlocationView.setAdapter(userOnlocationListAdapter);

                //Toast.makeText(DetailsCurrentLocation.this, "ID lokacije:  " + idLokcaije, Toast.LENGTH_LONG).show();
            }
        //};

        //IntentFilter filter = new IntentFilter("ServiceIntent");

        //registerReceiver(myReceiver, filter);


    //}

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
                Toast.makeText(DetailsCurrentLocation.this, "Greska u citanju naziva lokacije.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getDefaultUserOnLocation(View view, int locId ){
        userOnlocationListAdapter.clear();
        userOnlocationListAdapter.notifyDataSetChanged();

        ApiEndpoint apiService = RetrofitConnection.Factory.getInstance();
        apiService.getUsersOnLocation(locId).enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if(response.body() != null) {

                    userOnlocationListAdapter.addAll(response.body());

                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
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
