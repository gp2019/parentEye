package com.example.parenteye;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class Notification extends Fragment {



    private RecyclerView recyclerView;

    //public Notifications notifications =new Notifications() ;

    public static NotificationAdapter notificationAdapter  ;

    public static ActivityLogAdapter activityLogAdapter;

    public List<Notifications> notificationsList  = new ArrayList<>();


    public Notification() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        //recyclerView hold notification_recycler_view
        recyclerView = view.findViewById(R.id.notification_recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        setNotifiList();

        return view;

    }







    public void setNotifiList()
    {

        Notifications notifi =new Notifications() ;

        notifi.readNotifications();

        notificationAdapter = new NotificationAdapter(getContext(), notifi.notificationsList);

        recyclerView.setAdapter(notificationAdapter);
        //notifiList= notifications;


        // notificationAdapter.notifyDataSetChanged();


    }


}
