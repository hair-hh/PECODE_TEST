package com.impulsed.myapplication;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class SlidePagerAdapter extends FragmentStateAdapter {
    //items to display page number
    private final ArrayList<Integer> items;

    //just constructor
    public SlidePagerAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<Integer> items) {
        super(fragmentActivity);
        this.items = items;
    }

    public ArrayList<Integer> getItems() {
        return items;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return FirstFragment.newInstance(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return items.get(position);
    }

    @Override
    public boolean containsItem(long itemId) {
        return items.contains((int) itemId);
    }

    public int getItem(int position) {
        return items.get(position);
    }

    public int getPosition(int item) {
        return items.contains(item) ? items.indexOf(item) : -1;
    }

    // add a new fragment
    public void addFragment(int fragment) {
        items.add(fragment - 1, fragment);
        notifyItemRangeChanged(fragment, items.size());
    }

    //remove fragment on position
    public void removeFragment(int position) {
        items.remove(position);
        notifyItemRemoved(position);
        Log.d(Debudding.TAG, "REMOVE ");
    }


}
