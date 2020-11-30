package com.impulsed.myapplication;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class SlidePagerAdapter extends FragmentStateAdapter {
    //items to display page number
    private  ArrayList<String> items;

    //just constructor
    public SlidePagerAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<String> items) {
        super(fragmentActivity);
        this.items = items;
    }




    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d(Debudding.TAG, "SlideAdapter creates fragment on position: " + position);
        return FirstFragment.newInstance(items.get(position));
    }

    @Override
    public int getItemCount() {
        Log.d(Debudding.TAG, "Returning pageCount from Adapter " + items.size());
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).hashCode();
    }



    public void addFragment(String fragment){
        items.add(fragment);
        notifyDataSetChanged();
    }
    public void removeFragment(int position){
        items.remove(position);
        notifyItemRangeChanged(position, items.size());
        notifyDataSetChanged();
    }



}
