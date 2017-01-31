package hr.foi.indoortracking;

import android.util.Log;

import com.example.dbaccess.ApiEndpoint;
import com.example.dbaccess.HistoryModel;
import com.example.dbaccess.RetrofitConnection;

import java.util.List;

import hr.foi.core.ExchangeData;
import hr.foi.core.HistoryDataLoader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Paula on 28.1.2017..
 */

public class HistoryByDate implements HistoryDataLoader {

    @Override
    public ExchangeData getHistoryData(final ExchangeData exchangeData) {

        ApiEndpoint apiService = RetrofitConnection.Factory.getInstance();
        apiService.getHistoryByDate(exchangeData.getUserId(), exchangeData.getDateFrom(), exchangeData.getDateTo()).enqueue(new Callback<List<HistoryModel>>() {
            @Override
            public void onResponse(Call<List<HistoryModel>> call, Response<List<HistoryModel>> response) {
                if (response.isSuccess()) {
                    exchangeData.getHistoryAdapter().addAll(response.body());
                }
                if (response.body().isEmpty()) {
                    Log.i("RESPONSE", "empty");
                }
                if (response.body() == null) {
                    Log.i("RESPONSE", "null");
                    exchangeData.setError(1);
                    //Log.i("RESPONSE-ERR", String.valueOf(exchangeData.getError()));
                }
            }

            @Override
            public void onFailure(Call<List<HistoryModel>> call, Throwable t) {
                exchangeData.setError(2);
            }
        });

        return exchangeData;
    }
}
