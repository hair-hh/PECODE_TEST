package com.impulsed.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity  {

    //constant for notification chanel
    private static final String CHANNEL_ID = "101";
    private static final String CHANNEL_NAME = "NotificationChannel";
    private static final String STATE_LIST = "items";
    //constant for page position
    private static final String POS = "position";
    private int restoredPosition = 0;

    //items for FragmentStateAdapter
    ArrayList<Integer> items = new ArrayList<>();

    ViewPager2 viewPager2;
    SlidePagerAdapter adapter;
    TextView textPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = restoreState();

        textPosition = findViewById(R.id.textPosition);

        //initiate viewPager with adapter
        viewPager2 = findViewById(R.id.viewPager);
        adapter = new SlidePagerAdapter(this, items);
        viewPager2.setAdapter(adapter);
        viewPager2.setCurrentItem(restoredPosition);
        textPosition.setText(String.valueOf(adapter.getItem(restoredPosition)));
        // to view page number
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                String s = Integer.toString(adapter.getItem(position));
                textPosition.setText(s);
                Log.d(Debudding.TAG, "SELECTED" + s);
            }
        });
        //handling intent from notification
        onNewIntent(getIntent());
    }

    //set current page from notification
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey(POS)) {
                int item = extras.getInt(POS);
                int position = adapter.getPosition(item);
                if (position >= 0)
                    viewPager2.setCurrentItem(position);
            }
        }
    }

    //onClick for notification button
    public void onNotify(View view) {
        createNotification(adapter.getItem(viewPager2.getCurrentItem()));
    }

    private void saveState(ArrayList<Integer> items, int position){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        StringBuilder s = new StringBuilder();
        for (int i : items){
            s.append(i).append(",");
        }
        sharedPreferences.edit().putString(STATE_LIST, s.toString())
                                .putInt(POS, position).apply();
    }

    private ArrayList<Integer> restoreState(){
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        ArrayList<Integer> items = new ArrayList<>();
        if (sp.contains(STATE_LIST)){
            String savedString = sp.getString(STATE_LIST, "");
            StringTokenizer st = new StringTokenizer(savedString, ",");
            while (st.hasMoreTokens()){
                items.add(Integer.parseInt(st.nextToken()));
            }
        } else {
            items.add(1);
        }
        restoredPosition = sp.getInt(POS, 0);
        return items;
    }

    //method to create notification
    public void createNotification(int position) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        CharSequence textTitle = "You create a notification";
        CharSequence text = "Notification " + position;
        //new bundle to put a page position
        Bundle bundle = new Bundle();
        bundle.putInt(POS, position);
        //intent for click
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.putExtras(bundle);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(this, position, notificationIntent, 0);
        Notification notification;
        //two versions of notification builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //need no create channel
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(textTitle)
                .setContentText(text)
                .setContentIntent(intent)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .build();
        notificationManager.notify(position, notification);
    }


    public void onMinusClick(View view) {
        int position = viewPager2.getCurrentItem();
        int notifPos = adapter.getItem(position); //value for notification id
        if (adapter.getItemCount() > 1) { //check to keep first fragment
            adapter.removeFragment(position);
            viewPager2.setCurrentItem(position - 1);
        }
        //delete notification
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notifPos);

        Log.d(Debudding.TAG, "ON_MINUS ");
    }

    public void onPlusClick(View view) {
        //add one more item
        int position = viewPager2.getCurrentItem();
        adapter.addFragment(position, adapter.getItem(position) + 1);
        //jump to this position
        viewPager2.setCurrentItem(position + 1);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putIntegerArrayList(STATE_LIST, adapter.getItems());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        saveState(adapter.getItems(), viewPager2.getCurrentItem());
        super.onDestroy();
    }
}