package com.fancy.listrecyclerplugin.helper;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.fancy.listrecyclerplugin.R;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by sunflowerseat on 2016/8/5.
 */
public class HeaderHelper{

    public static View createBannerHeader(LayoutInflater inflater, List<Integer> localImages) {

        View view = inflater.inflate(R.layout.header_banner, null);
        BGABanner banner = (BGABanner) view.findViewById(R.id.banner);
        banner.setAdapter(new BGABanner.Adapter() {
            @Override
            public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
//                Glide.with(banner.getContext()).load(model).dontAnimate().thumbnail(0.1f).into((ImageView) view);
                ImageView imageView = (ImageView) view;
                imageView.setImageResource((Integer) model);
            }
        });
        List<String> texts = new ArrayList<>();
        for (Integer localImage : localImages) {
            texts.add("提示文案");
        }
        banner.setData(localImages, texts);

        return banner;
    }
}
