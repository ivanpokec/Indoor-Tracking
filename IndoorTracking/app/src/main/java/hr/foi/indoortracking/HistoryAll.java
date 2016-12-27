package hr.foi.indoortracking;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.dbaccess.ApiEndpoint;
import com.example.dbaccess.HistoryModel;
import com.example.dbaccess.RetrofitConnection;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import hr.foi.core.LoggedUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Paula on 20.12.2016..
 */

public class HistoryAll extends AppCompatActivity{

    public List<HistoryModel> HistoryModelList = new List<HistoryModel>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @NonNull
        @Override
        public Iterator<HistoryModel> iterator() {
            return null;
        }

        @NonNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NonNull
        @Override
        public <T> T[] toArray(T[] a) {
            return null;
        }

        @Override
        public boolean add(HistoryModel historyModel) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends HistoryModel> c) {
            return false;
        }

        @Override
        public boolean addAll(int index, Collection<? extends HistoryModel> c) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public HistoryModel get(int index) {
            return null;
        }

        @Override
        public HistoryModel set(int index, HistoryModel element) {
            return null;
        }

        @Override
        public void add(int index, HistoryModel element) {

        }

        @Override
        public HistoryModel remove(int index) {
            return null;
        }

        @Override
        public int indexOf(Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }

        @Override
        public ListIterator<HistoryModel> listIterator() {
            return null;
        }

        @NonNull
        @Override
        public ListIterator<HistoryModel> listIterator(int index) {
            return null;
        }

        @NonNull
        @Override
        public List<HistoryModel> subList(int fromIndex, int toIndex) {
            return null;
        }
    };
    int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_all);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Moja kretanja");

        ApiEndpoint apiService = RetrofitConnection.Factory.getInstance();

        id = LoggedUser.getUser().getUserModel().getId();
        apiService.getHistory(id).enqueue(new Callback<List<HistoryModel>>() {
            @Override
            public void onResponse(Call<List<HistoryModel>> call, Response<List<HistoryModel>> response) {
                if (response.isSuccess()) {
                    HistoryModelList = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<HistoryModel>> call, Throwable t) {
                Toast.makeText(HistoryAll.this, "Greška prilikom dohvaćanja podataka!", Toast.LENGTH_SHORT).show();
            }
        });

        if (!HistoryModelList.isEmpty()) {
            for (int i=0; i<HistoryModelList.size(); i++) {
                HistoryModel hm = HistoryModelList.get(i);

                if (i-1>0 && hm.getDatum().equals(HistoryModelList.get(i-1).getDatum())) { //TODO: ako je još uvijek isti datum, ispiši vrijeme ispod

                }
                else { //TODO: ako nije, ispiši novi datum i vremena ispod

                }
            }
        }


    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
