package com.example.toktam.myapplication;

import org.json.JSONException;
import org.json.JSONObject;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class MainSliderAdapter extends SliderAdapter {
    JSONObject item = new JSONObject();

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {
        switch (position) {
            case 0:
                try {
                    viewHolder.bindImageSlide("http://10.0.2.2:80/fastsell/" + item.getString("image1").trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    viewHolder.bindImageSlide("http://10.0.2.2:80/fastsell/" + item.getString("image2").trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    viewHolder.bindImageSlide("http://10.0.2.2:80/fastsell/" + item.getString("image3").trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    viewHolder.bindImageSlide("http://10.0.2.2:80/fastsell/" + item.getString("image4").trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    viewHolder.bindImageSlide("http://10.0.2.2:80/fastsell/" + item.getString("image5").trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
    }
}