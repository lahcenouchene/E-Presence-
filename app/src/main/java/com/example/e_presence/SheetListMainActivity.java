

package com.example.e_presence;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SheetListMainActivity extends AppCompatActivity {

    private ListView sheetList;
    private ArrayAdapter adapter;
    private ArrayList listItems=new ArrayList();
    private  long cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_list_main);
        cid=getIntent().getLongExtra("cid",-1);
        loadListItems();

        sheetList=findViewById(R.id.sheetList);
        adapter=new ArrayAdapter(this,R.layout.sheet_list,R.id.date_list_item,listItems);
        sheetList.setAdapter(adapter);


    }

    private void loadListItems() {
        Cursor cursor=new DpHlper(this).getDistinctMonths(cid);
        while (cursor.moveToNext()){
            int indexcolmn=cursor.getColumnIndex(DpHlper.DATE);
            String date=cursor.getString(indexcolmn);
            listItems.add(date.substring(3));
        }
    }
}