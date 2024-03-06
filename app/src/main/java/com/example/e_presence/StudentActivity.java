

package com.example.e_presence;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {
    Toolbar toolbar;
    private String subjectName;
    private String className;
    private int position;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private StudentAdapter adapter;
    private ArrayList<StudentItem> studentItems=new ArrayList<>();
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        Intent inet=getIntent();
        className=inet.getStringExtra("className");
        subjectName=inet.getStringExtra("subjectName");
        position=inet.getIntExtra("position",-1);
        setToolbar();
        recyclerView=findViewById(R.id.studen_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StudentAdapter(this,studentItems);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListner(position1 -> changeStatus(position));
    }

    private void changeStatus(int position) {
        String status=studentItems.get(position).getStatus();
        if (status.equals("P"))status="A";
        else status="P";
        studentItems.get(position).setStatus(status);
        adapter.notifyItemChanged(position);
    }

    private void setToolbar() {
        toolbar=findViewById(R.id.toolbar);
        TextView title=(TextView) toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle=(TextView) toolbar.findViewById(R.id.subtitel_toolbar);
        ImageButton back=(ImageButton) toolbar.findViewById(R.id.back);
        ImageButton save=(ImageButton) toolbar.findViewById(R.id.save);
        title.setText(className);
        subtitle.setText(subjectName);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });
        toolbar.inflateMenu(R.menu.menu);
        toolbar.setOnMenuItemClickListener(menuItem->onMenuItemClick(menuItem));
    }

    private boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.add_student){
            showStudentDialogue();
        }
        return true;
    }

    private void showStudentDialogue() {
        Mydialogue dialog=new Mydialogue();
        dialog.show(getSupportFragmentManager(),Mydialogue.STUDENT_ADD_DIALOG);
        dialog.setListener((roll,name)->addStudent(roll,name));
    }

    private void addStudent(String roll, String name) {
        studentItems.add(new StudentItem(roll,name));
        adapter.notifyDataSetChanged();
    }
}