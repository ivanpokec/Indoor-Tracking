package hr.foi.indoortracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
 * Created by Paula on 27.1.2017..
 */

public class HistoryDetails extends AppCompatActivity {
    private String chosenDate;
    private ArrayAdapter<HistoryModel> historyDetailsAdapter;
    private ListView historyDetailsListView;
    private int userId = LoggedUser.getUser().getUserModel().getId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_history_details);
        setTitle("Moja kretanja");

        Intent mIntent = getIntent();
        chosenDate = mIntent.getStringExtra("Date");

        historyDetailsListView = (ListView) findViewById(R.id.list_history_details);
        historyDetailsAdapter = new ArrayAdapter<HistoryModel>(this, android.R.layout.simple_list_item_1, new LinkedList<HistoryModel>()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                HistoryModel hm = getItem(position);

                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                }

                ((TextView) convertView).setText(hm.getTime() + " " + hm.getLocation());

                return convertView;
            }
        };

        getHistoryDetails(chosenDate);
        historyDetailsListView.setAdapter(historyDetailsAdapter);


    }

    private void getHistoryDetails(String chosenDate) {
        historyDetailsAdapter.clear();
        historyDetailsAdapter.notifyDataSetChanged();

        ApiEndpoint apiService = RetrofitConnection.Factory.getInstance();
        apiService.getHistoryDetails(userId, chosenDate).enqueue(new Callback<List<HistoryModel>>() {
            @Override
            public void onResponse(Call<List<HistoryModel>> call, Response<List<HistoryModel>> response) {
                if (response.isSuccess()) {
                    historyDetailsAdapter.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<HistoryModel>> call, Throwable t) {
                Toast.makeText(HistoryDetails.this, "Greška prilikom dohvaćanja podataka!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
