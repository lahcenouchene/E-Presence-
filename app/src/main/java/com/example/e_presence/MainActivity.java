package com.example.e_presence;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    ClassAdapter classAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ClassItems> classItems=new ArrayList<>();
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab_main);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAll();
            }
        });

        recyclerView=findViewById(R.id.recyclview);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        classAdapter=new ClassAdapter(this,classItems);
        recyclerView.setAdapter(classAdapter);
        classAdapter.setOnItemClickListner(position -> goToItemActivity(position));
        setToolbar();
    }

    @SuppressLint("SetTextI18n")
    private void setToolbar() {
        toolbar=findViewById(R.id.toolbar);
        TextView title=(TextView) toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle=(TextView) toolbar.findViewById(R.id.subtitel_toolbar);
        ImageButton back=(ImageButton) toolbar.findViewById(R.id.back);
        ImageButton save=(ImageButton) toolbar.findViewById(R.id.save);
        title.setText("Attendance App");
        subtitle.setVisibility(View.INVISIBLE);
        back.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
    }

    private void goToItemActivity(int position) {
        Intent intent=new Intent(this,StudentActivity.class);
        intent.putExtra("className",classItems.get(position).getClassName());
        intent.putExtra("subjectName",classItems.get(position).getSubjectName());
        intent.putExtra("position",position);
        startActivity(intent);
    }

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    private void showAll(){
        Mydialogue dialog =new Mydialogue();
        dialog.show(getSupportFragmentManager(),Mydialogue.CLASS_ADD_DIALOG);
        dialog.setListener((className,subjectName)->addClass(className,subjectName));

    }
    @SuppressLint("NotifyDataSetChanged")
    private void addClass(String className, String subjectName){
        classItems.add(new ClassItems(className,subjectName));
        classAdapter.notifyDataSetChanged();
    }
}
