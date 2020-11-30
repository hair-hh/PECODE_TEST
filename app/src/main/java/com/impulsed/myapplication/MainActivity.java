package com.impulsed.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //constant for notification chanel
    private static final String CHANNEL_ID = "888";
    //constant for page position
    private static final String POS = "position";

    //items for FragmentStateAdapter
    ArrayList<String> items = new ArrayList<>();

    ViewPager2 viewPager2;
    SlidePagerAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //just add first item
        items.add("1");

        //initiate viewPager with adapter
        viewPager2 = findViewById(R.id.viewPager);
        adapter = new SlidePagerAdapter(this, items);
        viewPager2.setAdapter(adapter);
        //handling intent from notification
        onNewIntent(getIntent());

        Log.d(Debudding.TAG, "Activity onCreate");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        if (extras!=null){
            if (extras.containsKey(POS)){
                int position = extras.getInt(POS);
                viewPager2.setCurrentItem(position);
            }
        }
    }

    //onClick for notification button
    public void onNotify(View view) {
        createNotification(viewPager2.getCurrentItem());
    }

    //method to create notification
    public void createNotification(int position) {

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        CharSequence textTitle = "You create a notification";
        int t = position + 1;
        CharSequence text = "Notification " + t;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle(textTitle)
                    .setContentText(text)
                    .setContentIntent(intent)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .build();
        } else {
            notification = new Notification.Builder(this)
                    .setContentTitle(textTitle)
                    .setContentText(text)
                    .setContentIntent(intent)
                    .setSmallIcon(R.drawable.blue)
                    .build();
        }
        notificationManager.notify(position, notification);
        Log.d(Debudding.TAG, "createNotification");
    }


    public void onMinusClick(View view) {
        int position = viewPager2.getCurrentItem();
        adapter.removeFragment(position);
        //adapter.notifyDataSetChanged();
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(position);
        Log.d(Debudding.TAG, "onMinusClick");
    }

    public void onPlusClick(View view) {
        int position = adapter.getItemCount();
        adapter.addFragment(String.valueOf(position+1));
        viewPager2.setCurrentItem(position+1);
        Log.d(Debudding.TAG, "OnPlusClick");

    }

}