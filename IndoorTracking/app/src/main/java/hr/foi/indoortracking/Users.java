package hr.foi.indoortracking;

import android.os.Bundle;
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
import com.example.dbaccess.RetrofitConnection;
import com.example.dbaccess.UserModel;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Zana on 24.1.2017..
 */


public class Users extends AppCompatActivity {
    private ListView usersListView;
    ArrayAdapter<UserModel> usersListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle("Korisnici");
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_users);

        usersListView = (ListView) findViewById(R.id.list_users);

        usersListAdapter = new ArrayAdapter<UserModel>(this,
                android.R.layout.simple_list_item_1
                , new LinkedList<UserModel>()) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {



                UserModel usersModel = getItem(position);

                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                }

                ((TextView) convertView).setText(usersModel.name);


                return convertView;
            }
        };
        getUsers(usersListView);
        usersListView.setAdapter(usersListAdapter);
            }
    public void getUsers(View view) {
        usersListAdapter.clear();
        usersListAdapter.notifyDataSetChanged();
        ApiEndpoint apiService = RetrofitConnection.Factory.getInstance();
        Call<List<UserModel>> call = apiService.listUsers();
        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.isSuccess()) {
                    usersListAdapter.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                Toast.makeText(Users.this, "Greška prilikom dohvaćanja podataka!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
