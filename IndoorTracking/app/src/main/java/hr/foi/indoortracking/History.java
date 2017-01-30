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

import com.example.dbaccess.HistoryModel;

import java.util.LinkedList;

import hr.foi.core.ExchangeData;
import hr.foi.core.HistoryDataLoader;
import hr.foi.core.LoggedUser;

/**
 * Created by Paula on 30.1.2017..
 */

public class History extends AppCompatActivity {
    private HistoryDataLoader historyDataLoader;
    private ExchangeData exchangeData;
    private ListView historyListView;
    private ArrayAdapter<HistoryModel> historyAdapter;
    private int locationId;
    private int userId;
    private int historyType;
    private String dateFrom;
    private String dateTo;
    private String chosenDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Moja kretanja");

        historyListView = (ListView) findViewById(R.id.list_history_by_location);

        historyAdapter = new ArrayAdapter<HistoryModel>(this,R.layout.list_row_history, new LinkedList<HistoryModel>()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                HistoryModel hm = getItem(position);

                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row_history, parent, false);
                }

                TextView txt = (TextView)convertView.findViewById(R.id.textview_name);

                txt.setText(hm.showData(historyType));

                return convertView;
            }
        };

        exchangeData = new ExchangeData();
        userId = LoggedUser.getUser().getUserModel().getUserId();
        exchangeData.setUserId(userId);

        Intent i = getIntent();
        historyType = i.getIntExtra("HistoryType", -1);

        switch (historyType) {
            case 0:
                historyDataLoader = new HistoryAll();
                break;
            case 1:
                chosenDate = i.getStringExtra("chosenDate");
                String dateFormatted = HistoryModel.convertDate(chosenDate, "dd.MM.yyyy");
                exchangeData.setChosenDate(dateFormatted);
                historyDataLoader = new HistoryAllDetails();
                break;
            case 2:
                dateFrom = i.getStringExtra("dateFrom");
                dateTo = i.getStringExtra("dateTo");
                String dateFromFormatted = HistoryModel.convertDate(dateFrom, "d.M.yyyy");
                String dateToFormatted = HistoryModel.convertDate(dateTo, "d.M.yyyy");
                exchangeData.setDateFrom(dateFromFormatted);
                exchangeData.setDateTo(dateToFormatted);
                historyDataLoader = new HistoryByDate();

                break;
            case 3:
                locationId = i.getIntExtra("locationId", 1);
                exchangeData.setLocationId(locationId);
                historyDataLoader = new HistoryByLocation();
                break;
        }


        historyAdapter.clear();
        historyAdapter.notifyDataSetChanged();
        exchangeData.setHistoryAdapter(historyAdapter);

        if (historyDataLoader == null) {
            Log.i("HISTORY", "Null object!");
        } else {
            exchangeData = historyDataLoader.getHistoryData(exchangeData);
        }

        historyListView.setAdapter(exchangeData.getHistoryAdapter());

        if (historyType == 0) {
            historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    HistoryModel hm;
                    hm = historyAdapter.getItem(position);
                    Intent intent = new Intent(History.this, History.class);
                    intent.putExtra("HistoryType", 1);
                    intent.putExtra("chosenDate",  hm.getDate());
                    startActivity(intent);
                }
            });
        }


    /*
        Log.i("ERROR", String.valueOf(exchangeData.getError()));
        switch (exchangeData.getError()) {
            case 1:
                Toast.makeText(History.this, "Nema podataka!", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(History.this, "Greška prilikom dohvata podataka!", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;


        }
        */


    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
