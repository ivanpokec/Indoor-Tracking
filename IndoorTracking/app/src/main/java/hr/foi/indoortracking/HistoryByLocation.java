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
import com.example.dbaccess.HistoryModel;
import com.example.dbaccess.RetrofitConnection;

import java.util.LinkedList;
import java.util.List;

import hr.foi.core.LoggedUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Paula on 28.1.2017..
 */

public class HistoryByLocation extends AppCompatActivity {
    private ListView historyByLocationListView;
    private ArrayAdapter<HistoryModel> historyByLocationAdapter;
    private int locationId;
    private int userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_by_location);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Moja kretanja");

        historyByLocationListView = (ListView) findViewById(R.id.list_history_by_location);

        historyByLocationAdapter = new ArrayAdapter<HistoryModel>(this, android.R.layout.simple_list_item_1, new LinkedList<HistoryModel>()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                HistoryModel hm = getItem(position);

                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                }

                ((TextView) convertView).setText(hm.getDate() + " " + hm.getTime());

                return convertView;
            }
        };

        userId = LoggedUser.getUser().getUserModel().getId();
        Intent i = getIntent();
        locationId = i.getIntExtra("locationId",1);

        getHistoryByLocation();
        historyByLocationListView.setAdapter(historyByLocationAdapter);


    }

    private void getHistoryByLocation() {
        historyByLocationAdapter.clear();
        historyByLocationAdapter.notifyDataSetChanged();

        ApiEndpoint apiService = RetrofitConnection.Factory.getInstance();
        apiService.getHistoryByLocation(userId, locationId).enqueue(new Callback<List<HistoryModel>>() {
            @Override
            public void onResponse(Call<List<HistoryModel>> call, Response<List<HistoryModel>> response) {
                if (response.isSuccess()) {
                    historyByLocationAdapter.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<HistoryModel>> call, Throwable t) {
                Toast.makeText(HistoryByLocation.this, "Greška prilikom dohvaćanja podataka!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


}
