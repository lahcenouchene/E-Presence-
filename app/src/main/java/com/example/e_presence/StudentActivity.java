package com.example.e_presence;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class StudentActivity extends AppCompatActivity {
    Toolbar toolbar;
    private String subjectName;
    private String className;
    private int position;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private StudentAdapter adapter;
    private ArrayList<StudentItem> studentItems=new ArrayList<>();
    private DpHlper dpHlper;
    private long cid;
    private Myclendar calendar;
    private  TextView subtitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        calendar=new Myclendar();
        dpHlper=new DpHlper(this);

        Intent inet=getIntent();
        className=inet.getStringExtra("className");
        subjectName=inet.getStringExtra("subjectName");
        position=inet.getIntExtra("position",-1);
        cid=inet.getLongExtra("cid",-1);
        setToolbar();
        loadData();
        recyclerView=findViewById(R.id.studen_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StudentAdapter(this,studentItems);
        recyclerView.setAdapter(adapter);
        loadStatusData();
        adapter.setOnItemClickListner(position1 -> changeStatus(position1));
        loadStatusData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload status data when the activity resumes
        loadStatusData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveStatus();
    }


    private void loadData() {
        Cursor cursor = dpHlper.getStudents(cid);
        Log.i("1234567890", "loadData:" + cid);
        studentItems.clear();
        while (cursor.moveToNext()) {
            int colIndex = cursor.getColumnIndex(DpHlper.S_ID);
            long sid = cursor.getLong(colIndex);
            int rollIndex = cursor.getColumnIndex(DpHlper.POSITION_ETUDIANT);
            int roll = cursor.getInt(rollIndex);
            int nameIndex = cursor.getColumnIndex(DpHlper.NOM_ETUDIANT);
            String name = cursor.getString(nameIndex);
            // Fetch status for each student
            String status = dpHlper.getStatus(sid, calendar.getDate());
            studentItems.add(new StudentItem(sid, roll, name, status)); // Pass the status to the constructor
        }
        cursor.close();
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
        TextView title= toolbar.findViewById(R.id.title_toolbar);
        subtitle= toolbar.findViewById(R.id.subtitel_toolbar);
        ImageButton back=(ImageButton) toolbar.findViewById(R.id.back);
        ImageButton save=(ImageButton) toolbar.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveStatus();
                try {
                    telechargerPdf();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                Toast.makeText(StudentActivity.this, "Saved successfully", Toast.LENGTH_SHORT).show();
            }
        });
        title.setText(className);
        subtitle.setText(subjectName+" | "+calendar.getDate());
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });
        toolbar.inflateMenu(R.menu.menu);
        toolbar.setOnMenuItemClickListener(menuItem->onMenuItemClick(menuItem));
    }

    private void telechargerPdf() throws FileNotFoundException {
        String cours = getIntent().getStringExtra("subjectName");
        String date = calendar.getDate();

        // Charger les informations des étudiants depuis la base de données
        List<StudentItem> studentList = loadStudentDataFromDatabase();

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Attendance.pdf");
        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);
        document.add(new Paragraph("Course: " + cours));
        document.add(new Paragraph("Date: " + date));

        // Ajouter les informations des étudiants au document PDF
        for (StudentItem student : studentList) {
            document.add(new Paragraph("ID: " + student.getSid() + ", Name: " + student.getName() + ", Statuts: " + student.getStatus()));
        }

        document.close();
    }

    private List<StudentItem> loadStudentDataFromDatabase() {
        List<StudentItem> studentList = new ArrayList<>();

        // Charger les informations des étudiants à partir de la base de données
        Cursor cursor = dpHlper.getStudents(cid);
        while (cursor.moveToNext()) {
            int colIndex = cursor.getColumnIndex(DpHlper.S_ID);
            long sid = cursor.getLong(colIndex);
            int rollIndex = cursor.getColumnIndex(DpHlper.POSITION_ETUDIANT);
            int roll = cursor.getInt(rollIndex);
            int nameIndex = cursor.getColumnIndex(DpHlper.NOM_ETUDIANT);
            String name = cursor.getString(nameIndex);
            // Fetch status for each student
            String status = dpHlper.getStatus(sid, calendar.getDate());
            studentList.add(new StudentItem(sid, roll, name, status)); // Pass the status to the constructor
        }
        cursor.close();

        return studentList;
    }


    private void saveStatus() {
        for (StudentItem studentItem : studentItems){
            String status=studentItem.getStatus();
            if (!status.equals("P")) status = "A";
            long value =dpHlper.addStatus(studentItem.getSid(),cid,calendar.getDate(),status);

            if (value==-1)dpHlper.updateStatus(studentItem.getSid(),calendar.getDate(),status);

        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private void loadStatusData(){
        for (StudentItem studentItem : studentItems){
            String status=dpHlper.getStatus(studentItem.getSid(),calendar.getDate());
            if (status != null) {studentItem.setStatus(status);}
            else {
                studentItem.setStatus("");
            }


        }
        adapter.notifyDataSetChanged();
    }

    private boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.add_student){
            showStudentDialogue();
        }
        else if (menuItem.getItemId() == R.id.show_Calendar){
            showCalendar();
        }
        else if (menuItem.getItemId() == R.id.show_attendance_sheet){
            openSheetList();
        }
        return true;
    }

    private void openSheetList() {
        Intent intent=new Intent(this, SheetListMainActivity.class);
        intent.putExtra("cid",cid);
        startActivity(intent);
    }

    private void showCalendar() {

        calendar.show(getSupportFragmentManager(),"");
        calendar.setOnCalendarOnClickListner(this::onCalendarOnClicked);
    }

    private void onCalendarOnClicked(int year, int month, int day) {
        calendar.setDate(year, month, day);
        subtitle.setText(subjectName+" | "+calendar.getDate());
        loadStatusData();
    }

    private void showStudentDialogue() {
        Mydialogue dialog=new Mydialogue();
        dialog.show(getSupportFragmentManager(),Mydialogue.STUDENT_ADD_DIALOG);
        dialog.setListener((roll,name)->addStudent(roll,name));
    }

    private void addStudent(String roll_string, String name) {
        int roll = Integer.parseInt(roll_string);
        long sid = dpHlper.addStudent(cid, roll, name);
        // Fetch status for the newly added student
        String status = dpHlper.getStatus(sid, calendar.getDate());
        StudentItem studentItem = new StudentItem(sid, roll, name, status);
        studentItems.add(studentItem);
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 0:
                showUpdateStudentDialog(item.getGroupId());
                break;
            case 1:
                deletStudent(item.getGroupId());
        }

        return super.onContextItemSelected(item);
    }

    private void showUpdateStudentDialog(int position) {
        Mydialogue dialog =new Mydialogue(studentItems.get(position).getRoll(),studentItems.get(position).getName());
        dialog.show(getSupportFragmentManager(),Mydialogue.STUDENT_UPDATE_DIALOG);
        dialog.setListener((roll_string,name)->updateStudent(position,name));
    }

    private void updateStudent(int position, String name) {
        dpHlper.updateStudent(studentItems.get(position).getSid(),name);
        studentItems.get(position).setName(name);
        adapter.notifyItemChanged(position);
    }

    private void deletStudent(int position) {
        dpHlper.deleteStudenet(studentItems.get(position).getSid());
        studentItems.remove(position);
        adapter.notifyItemRemoved(position);
    }
}
