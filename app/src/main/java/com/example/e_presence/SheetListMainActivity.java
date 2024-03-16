

package com.example.e_presence;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

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
        setToolbar();

    }
    @SuppressLint("SetTextI18n")
    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);
        save.setVisibility(View.INVISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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