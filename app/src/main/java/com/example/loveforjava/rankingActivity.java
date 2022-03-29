package com.example.loveforjava;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.CDATASection;

import java.util.LinkedList;
import java.util.List;

public class rankingActivity extends AppCompatActivity {

    private ListView list_single;
    private ListView list_num;
    private ListView list_all_scans;
    private CustomList_single Adapter_single = null;
    //private List<ContactsContract.Contacts.Data> mData = null;
    private Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rank_list_single);
        mContext = rankingActivity.this;
        //mData = new LinkedList<ContactsContract.Contacts.Data>();
        //Adapter_single = new CustomList_single((LinkedList<ContactsContract.Contacts.Data>) mData,mContext);
        //mData.add(data);
        //notifyDataSetChanged();
        list_single.setAdapter(Adapter_single);
    }

}