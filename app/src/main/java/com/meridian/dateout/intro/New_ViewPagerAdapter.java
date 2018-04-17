package com.meridian.dateout.intro;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.meridian.dateout.R;

import java.util.ArrayList;

public class New_ViewPagerAdapter extends PagerAdapter {
     Context context;
     ArrayList<BannerModel> imageURL;

    public New_ViewPagerAdapter(Context ReceivedContext, ArrayList<BannerModel> ReceivedImageURL)
    {
        this.context=ReceivedContext;
        this.imageURL=ReceivedImageURL;
    }

    @Override
    public int getCount() {
        return imageURL.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final ImageView imgView;
        final ProgressBar progress_bar;

        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView=inflater.inflate(R.layout.new_viewpager_item,container,false);

        progress_bar=(ProgressBar)itemView.findViewById(R.id.progress_bar);
        imgView=(ImageView)itemView.findViewById(R.id.image);
        progress_bar.setVisibility(View.VISIBLE);

        System.out.println("#################################################################################");
        System.out.println("URI in PAGE ADAPTER : "+ Uri.parse(imageURL.get(position).getBanner()));
        System.out.println("#################################################################################");

        try {

            Glide.with(context).load(imageURL.get(position).getBanner())
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            progress_bar.setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progress_bar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .fitCenter()
                    .crossFade()
                    .into(imgView);
       }catch (Exception e){
           e.printStackTrace();
       }
        ((ViewPager)container).addView(itemView);

        return itemView;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
