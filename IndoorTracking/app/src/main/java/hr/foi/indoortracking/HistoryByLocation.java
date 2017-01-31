package hr.foi.indoortracking;

import hr.foi.dbaccess.ApiEndpoint;
import hr.foi.dbaccess.HistoryModel;
import hr.foi.dbaccess.RetrofitConnection;

import java.util.List;

import hr.foi.core.ExchangeData;
import hr.foi.core.HistoryDataLoader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Paula on 28.1.2017..
 */

public class HistoryByLocation implements HistoryDataLoader{

    @Override
    public ExchangeData getHistoryData(final ExchangeData exchangeData) {

        ApiEndpoint apiService = RetrofitConnection.Factory.getInstance();
        apiService.getHistoryByLocation(exchangeData.getUserId(), exchangeData.getLocationId()).enqueue(new Callback<List<HistoryModel>>() {
            @Override
            public void onResponse(Call<List<HistoryModel>> call, Response<List<HistoryModel>> response) {
                if (response.isSuccess()) {
                    exchangeData.getHistoryAdapter().addAll(response.body());
                }
                if (response.body() == null) {
                    exchangeData.setError(1);

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
