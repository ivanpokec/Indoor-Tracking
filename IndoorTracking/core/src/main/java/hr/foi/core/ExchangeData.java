package hr.foi.core;

import android.widget.ArrayAdapter;

import com.example.dbaccess.HistoryModel;

/**
 * Created by Paula on 30.1.2017..
 */

public class ExchangeData {
    private ArrayAdapter<HistoryModel> historyAdapter;
    private int locationId;
    private String dateFrom;
    private String dateTo;
    private int userId;
    private int error;
    private String chosenDate;

    public ArrayAdapter<HistoryModel> getHistoryAdapter() {
        return historyAdapter;
    }

    public void setHistoryAdapter(ArrayAdapter<HistoryModel> historyAdapter) {
        this.historyAdapter = historyAdapter;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getChosenDate() {
        return chosenDate;
    }

    public void setChosenDate(String chosenDate) {
        this.chosenDate = chosenDate;
    }
}
