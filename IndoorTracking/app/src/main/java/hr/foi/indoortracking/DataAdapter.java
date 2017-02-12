package hr.foi.indoortracking;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import hr.foi.dbaccess.CategoryModel;

/**
 * Created by Zana on 12.2.2017..
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder>  {

    private ArrayList<CategoryModel> android;
    public int catID;
    Context  context;

    public DataAdapter(ArrayList<CategoryModel> category) {
        this.android = category;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row_locations, viewGroup, false);
        context = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.location_name.setText(android.get(i).getCatName());
        if(android.get(i).getCatName().toString().equals("M2 - Razvoj softvera")) {
            viewHolder.thumb_image.setImageResource(R.mipmap.software);
        }else if (android.get(i).getCatName().toString().equals("M3 - Razvoj hardvera")) {
            viewHolder.thumb_image.setImageResource(R.mipmap.hardware);
        }else if(android.get(i).getCatName().toString().equals("Prodaja")){
            viewHolder.thumb_image.setImageResource(R.mipmap.sale);
        }else {
            viewHolder.thumb_image.setImageResource(R.mipmap.other);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(context, Locations.class);
               intent.putExtra("ID",  Integer.toString(android.get(i).getCatId()));
               intent.putExtra("name",  android.get(i).getCatName());
               context.startActivity(intent);
           }
       });



    }



    @Override
    public int getItemCount() {
        return android.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView location_name;
        private ImageView thumb_image;
        public ViewHolder(View view) {
            super(view);

            location_name = (TextView)view.findViewById(R.id.textview_name);
            thumb_image=(ImageView)view.findViewById(R.id.list_image);


        }
    }
}
