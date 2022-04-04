package com.example.loveforjava;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.Inflater;

/**
 * Displays who have seen the QR code
 */
public class SeenByFragment extends ListFragment {

    private ArrayList<String> seenBy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstances){
        //return super.onCreateView(inflater, container, savedInstances);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            seenBy = bundle.getStringArrayList("seen_by");
        }
        View v = inflater.inflate(R.layout.seen_by_list, container, false);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstances){
        super.onCreate(savedInstances);

        //ListView seen_list = findViewById(R.id.seen_by_list);

        ArrayAdapter seenByAdapter = new ArrayAdapter<>(getContext(), R.layout.seen_by_list, seenBy);
        setListAdapter(seenByAdapter);
    }
}
