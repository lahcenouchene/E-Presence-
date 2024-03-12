package com.example.e_presence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
    DpHlper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper =new DpHlper(this);

        fab = findViewById(R.id.fab_main);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAll();
            }
        });
        loadData();
        recyclerView=findViewById(R.id.recyclview);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        classAdapter=new ClassAdapter(this,classItems);
        recyclerView.setAdapter(classAdapter);
        classAdapter.setOnItemClickListner(position -> goToItemActivity(position));
        setToolbar();
    }
    private  void loadData(){
        Cursor cursor=dbHelper.getClassTable();
        classItems.clear();
        while (cursor.moveToNext()){
            int columnIndex1 = cursor.getColumnIndex(DpHlper.C_ID);
            int id=cursor.getInt(columnIndex1);
            int columnIndex = cursor.getColumnIndex(DpHlper.NOM_COURS);
            String className = cursor.getString(columnIndex);
            int columnIndex2 = cursor.getColumnIndex(DpHlper.SUBJECT_NAME);
            String subjectName = cursor.getString(columnIndex2);

            classItems.add(new ClassItems(className,subjectName,id));
        }
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

        save.setVisibility(View.INVISIBLE);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créez une intention pour démarrer LoginActivity
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                // Terminez l'activité actuelle
                finish();
            }
        });

    }

    private void goToItemActivity(int position) {
        Intent intent=new Intent(this,StudentActivity.class);
        intent.putExtra("className",classItems.get(position).getClassName());
        intent.putExtra("subjectName",classItems.get(position).getSubjectName());
        intent.putExtra("position",position);
        intent.putExtra("cid",classItems.get(position).getCid());
        startActivity(intent);
    }


    private void showAll(){
        Mydialogue dialog =new Mydialogue();
        dialog.show(getSupportFragmentManager(),Mydialogue.CLASS_ADD_DIALOG);
        dialog.setListener((className,subjectName)->addClass(className,subjectName));

    }

    @SuppressLint("NotifyDataSetChanged")
    private void addClass(String className, String subjectName){
        long cid=dbHelper.addClass(className,subjectName);
        ClassItems classItems1=new ClassItems(className,subjectName,cid);
        classItems.add(classItems1);
        classAdapter.notifyDataSetChanged();


    }
    public boolean onContextItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case 0:
                showUpdatedialog(item.getGroupId());
            break;
            case 1:
            deletClass(item.getGroupId());
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdatedialog(int position) {
        Mydialogue dialog =new Mydialogue();
        dialog.show(getSupportFragmentManager(),Mydialogue.CLASS_UPDATE_DIALOG);
        dialog.setListener((className,subjectNmae)->updateClass(position,className,subjectNmae));
    }

    private void updateClass(int position, String className, String subjectName) {
        dbHelper.updateClass(classItems.get(position).getCid(),className,subjectName);
        classItems.get(position).setClassName(className);
        classItems.get(position).setSubjectName(subjectName);
        classAdapter.notifyItemChanged(position);
    }

    private void deletClass(int position) {
        dbHelper.deleteClass(classItems.get(position).getCid());
        classItems.remove(position);
        classAdapter.notifyItemRemoved(position);

    }

}
