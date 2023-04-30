package com.example.mobileassfundraisingapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mobileassfundraisingapp.admin.DetailActivity;
import com.example.mobileassfundraisingapp.admin.UpdateActivity;
import com.example.mobileassfundraisingapp.admin.adminActivity;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class EventDetailsUser extends Fragment {

    TextView detailDesc, detailTitle, detailLang;
    ImageView detailImage;

    String key = "";
    String imageUrl = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_details_user, container, false);

        detailDesc = view.findViewById(R.id.detailDesc);
        detailImage = view.findViewById(R.id.detailImage);
        detailTitle = view.findViewById(R.id.detailTitle);

        detailLang = view.findViewById(R.id.detailLang);

        Bundle bundle = getArguments();
        if (bundle != null) {
            detailDesc.setText(bundle.getString("Description"));
            detailTitle.setText(bundle.getString("Title"));
            detailLang.setText(bundle.getString("Language"));
            key = bundle.getString("Key");
            imageUrl = bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
        }

        return view;
    }
}
