package hr.foi.indoortracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dbaccess.ApiEndpoint;
import com.example.dbaccess.CategoryModel;
import com.example.dbaccess.RetrofitConnection;
import com.example.dbaccess.UserModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Zana on 2.1.2017..
 */

public class Category extends AppCompatActivity {
    private ListView categoryListView;

    ArrayAdapter<CategoryModel> categoryListAdapter;
    public int catId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle("Kategorije lokacija");
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_category);

        categoryListView = (ListView) findViewById(R.id.list_category);

        categoryListAdapter = new ArrayAdapter<CategoryModel>(this,
                        android.R.layout.simple_list_item_1
                        , new LinkedList<CategoryModel>()) {

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {



                        CategoryModel categoryModel = getItem(position);

                        if (convertView == null) {
                            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                        }

                        ((TextView) convertView).setText(categoryModel.catName);



                        return convertView;
            }
        };
        getLocations(categoryListView);
        categoryListView.setAdapter(categoryListAdapter);

        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CategoryModel catmodel;
                catmodel = categoryListAdapter.getItem(position);
                catId=catmodel.catId;
                Intent intent = new Intent(Category.this, Locations.class);
                intent.putExtra("ID",  Integer.toString(catId));
                startActivity(intent);
            }
        });
    }

    public void getLocations(View view) {
        categoryListAdapter.clear();
        categoryListAdapter.notifyDataSetChanged();
        ApiEndpoint apiService = RetrofitConnection.Factory.getInstance();
        Call<List<CategoryModel>> call = apiService.listLocations();
        call.enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                if (response.isSuccess()) {
                    categoryListAdapter.addAll(response.body());
                                    }
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                Toast.makeText(Category.this, "Greška prilikom dohvaćanja podataka!", Toast.LENGTH_SHORT).show();
            }
        });


    }




    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
