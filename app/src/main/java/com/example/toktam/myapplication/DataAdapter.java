package com.example.toktam.myapplication;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//import com.squareup.picasso.Picasso;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public abstract class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {


    String img_adress;
    ArrayList<JSONObject> data_list;
    Context context;


    public DataAdapter(Context context, ArrayList<JSONObject> data_list) {
        this.context = context;
        this.data_list = data_list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ads, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        try {
            holder.ad_title.setText(data_list.get(position).getString("title"));


            String[] province_list = context.getResources().getStringArray(R.array.province);


            holder.ad_location.setText("استان " + province_list[data_list.get(position).getInt("province")]);


            ArrayAdapter<CharSequence> myadapter = ArrayAdapter.createFromResource(context, context.getResources().getIdentifier("array/city" + data_list.get(position).getInt("province"), null, context.getPackageName()), R.layout.item_list);


            holder.ad_location.setText(holder.ad_location.getText() + " , " + myadapter.getItem(data_list.get(position).getInt("city")) );


            int seconds = (int) ((System.currentTimeMillis() / 1000) - data_list.get(position).getInt("date"));

            String temp = "";
            if (seconds < 60) {
                temp = "چند ثانیه پیش";
            } else if (seconds >= 60 && seconds < 3600) {

                temp = (seconds / 60) + " دقیقه پیش ";

            } else {
                temp = (seconds / 3600) + " ساعت پیش ";
            }


            holder.ad_date.setText(temp);


            if (data_list.get(position).getInt("price_type") == 0) {
                holder.ad_price.setText("");
            }

            if (data_list.get(position).getInt("price_type") == 1) {
                holder.ad_price.setText("قیمت توافقی");
            }


            if (data_list.get(position).getInt("price_type") == 2) {
                holder.ad_price.setText("جهت معاوضه");
            }

            if (data_list.get(position).getInt("price_type") == 3) {

                NumberFormat numberformat = NumberFormat.getNumberInstance(Locale.US);

                holder.ad_price.setText(numberformat.format(data_list.get(position).getInt("price")) + " تومان ");
            }


/****************/

            if (!data_list.get(position).getString("image1").equals("")) {
                //img_adress = data_list.get(position).getString("image1");
                //String img_adress2 = img_adress.replaceAll("\\\\", "");
                Glide.with(context)
                        .load("http://10.0.2.2:80/fastsell/" + data_list.get(position).getString("image1"))
                        .apply(new RequestOptions().override(128, 128))
                        .into(holder.img);

            } else {
                if (!data_list.get(position).getString("image2").equals("")) {
                    img_adress = data_list.get(position).getString("image2");
                    String img_adress2 = img_adress.replaceAll("\\\\", "");
                    Glide.with(context).load("http://10.0.2.2:80/fastsell/" + img_adress2).apply(new RequestOptions().override(128, 128)).into(holder.img);
                } else {
                    if (!data_list.get(position).getString("image3").equals("")) {
                       // img_adress = data_list.get(position).getString("image3");
                        //String img_adress2 = img_adress.replaceAll("\\\\", "");
                        Glide.with(context).load("http://10.0.2.2:80/fastsell/" + data_list.get(position).getString("image3")).apply(new RequestOptions().override(128, 128)).into(holder.img);

                    } else {
                        if (!data_list.get(position).getString("image4").equals("")) {
                            //img_adress = data_list.get(position).getString("image4");
                           // String img_adress2 = img_adress.replaceAll("\\\\", "");
                            Glide.with(context).load("http://10.0.2.2:80/fastsell/" + data_list.get(position).getString("image4")).apply(new RequestOptions().override(128, 128)).into(holder.img);


                        } else {
                            if (!data_list.get(position).getString("image5").equals("")) {
                                //img_adress = data_list.get(position).getString("image5");
                               // String img_adress2 = img_adress.replaceAll("\\\\", "");
                                //Glide.with(context).load("http://10.0.2.2:80/fastsell/" + img_adress2).apply(new RequestOptions().override(128, 128)).into(holder.img);
                               Glide.with(context).load("http://10.0.2.2:80/fastsell/" +  data_list.get(position).getString("image5")).apply(new RequestOptions().override(128, 128)).into(holder.img);
                            }
                            else
                            {
                               Glide.with(context).load(R.drawable.icon).apply(new RequestOptions().override(128, 128)).into(holder.img);
                            }
                        }
                    }
                }
            }
/*************/
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (position >= getItemCount() - 1) {

            load_more();

        }


        holder.cardv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, ShowDetailsAdActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("ad", data_list.get(position).toString());
               context.startActivity(i);
            }
        });


    }

    public abstract void load_more();

    @Override
    public int getItemCount() {
        return data_list.size();
    }

    public void insert(int position, JSONArray ad_list) {

        try {

            for (int i = 0; i < ad_list.length(); i++) {

                data_list.add(ad_list.getJSONObject(i));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        notifyItemInserted(position);
    }


    public void clear_list() {

        int size = data_list.size();

        data_list.clear();

        notifyItemRangeRemoved(0, size);

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ad_title;
        TextView ad_location;
        TextView ad_price;
        TextView ad_date;
        ImageView img;
        CardView cardv;

        public ViewHolder(View item) {
            super(item);
            ad_title = (TextView) item.findViewById(R.id.item_title);
            ad_location = (TextView) item.findViewById(R.id.item_location);
            ad_price = (TextView) item.findViewById(R.id.item_price);
            ad_date = (TextView) item.findViewById(R.id.item_date);
            img = (ImageView) item.findViewById(R.id.item_imag);
            cardv = (CardView) item.findViewById(R.id.card_view_ads);
        }
    }
}
