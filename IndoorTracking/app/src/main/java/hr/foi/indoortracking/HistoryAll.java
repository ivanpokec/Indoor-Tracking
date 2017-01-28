package hr.foi.indoortracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
 * Created by Paula on 20.12.2016..
 */

public class HistoryAll extends AppCompatActivity{
    private ListView historyListView;
    private ArrayAdapter<HistoryModel> historyListAdapter;
    private int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_all);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Moja kretanja");

        historyListView = (ListView) findViewById(R.id.list_history_all);

        historyListAdapter = new ArrayAdapter<HistoryModel>(this, android.R.layout.simple_list_item_1, new LinkedList<HistoryModel>()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                HistoryModel hm = getItem(position);
                //hm.separateDateTime();

                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                }

                ((TextView) convertView).setText(hm.getDate());

                return convertView;
            }
        };

        id = LoggedUser.getUser().getUserModel().getId();

        getHistory(id);
        historyListView.setAdapter(historyListAdapter);

        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HistoryModel hm;
                hm = historyListAdapter.getItem(position);
                String chosenDate = hm.convertDate(hm.getDate(), "dd.MM.yyyy");
                Intent intent = new Intent(HistoryAll.this, HistoryDetails.class);
                intent.putExtra("Date",  chosenDate);
                startActivity(intent);
            }
        });


    }

    private void getHistory(int id) {
        historyListAdapter.clear();
        historyListAdapter.notifyDataSetChanged();

        ApiEndpoint apiService = RetrofitConnection.Factory.getInstance();
        apiService.getHistory(id).enqueue(new Callback<List<HistoryModel>>() {
            @Override
            public void onResponse(Call<List<HistoryModel>> call, Response<List<HistoryModel>> response) {
                if (response.isSuccess()) {
                    historyListAdapter.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<HistoryModel>> call, Throwable t) {
                Toast.makeText(HistoryAll.this, "Greška prilikom dohvaćanja podataka!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
