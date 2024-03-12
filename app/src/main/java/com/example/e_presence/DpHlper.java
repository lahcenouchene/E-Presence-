package com.example.e_presence;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DpHlper extends SQLiteOpenHelper {
    // Table cours
    private static final String TABLE_COURS = "table_cours";
    public static final String C_ID = "c_id";
    public static final String NOM_COURS = "nom_de_cours";
    public static final String SUBJECT_NAME = "subject_name";
    private static final String CREATE_TABLE_COURS =
            "CREATE TABLE "
                    + TABLE_COURS
                    + " ("
                    + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + NOM_COURS + " TEXT NOT NULL, "
                    + SUBJECT_NAME + " TEXT NOT NULL, "
                    + "UNIQUE(" + NOM_COURS + ", " + SUBJECT_NAME + ")"
                    + ")";

    private static final String DROP_COURS_TABLE = "DROP TABLE IF EXISTS " + TABLE_COURS;
    private static final String SELECT_COURS_TABLE = "SELECT * FROM " + TABLE_COURS;

    //table etudiant

    private static final String TABLE_Etudiant= "table_etudiant";
    public static final String S_ID = "s_id";
    public static final String NOM_ETUDIANT = "nom_etudiant";
    public static final String POSITION_ETUDIANT= "position_etudiant";
    public static final String CREATE_TABLE_ETUDIANT =
            "CREATE TABLE "
                    + TABLE_Etudiant
                    + " ("
                    + S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + C_ID + " INTEGER NOT NULL, "
                    + NOM_ETUDIANT + " TEXT NOT NULL, "
                    + POSITION_ETUDIANT + " INTEGER, "
                    + "UNIQUE(" + C_ID + ", " + NOM_ETUDIANT + "), "
                    + "FOREIGN KEY (" + C_ID + ") REFERENCES " + TABLE_COURS + "(" + C_ID + ")"
                    + ")";


    private static final String DROP_TABLE_Etudiant= "DROP TABLE IF EXISTS " + TABLE_Etudiant;
    private static final String SELECT_TABLE_Etudiant= "SELECT * FROM " + TABLE_Etudiant;

    // table status
    private static final String TABLE_STATUS= "table_status";
    public static final String STATUS_ID = "status_id";
    public static final String DATE = "date";
    public static final String STATUS_key= "status";
    private static final String CREATE_TABLE_STATUS=
            "CREATE TABLE "
                    + TABLE_STATUS
                    + " ("
                    + STATUS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + S_ID + " INTEGER NOT NULL, "
                    + C_ID + " INTEGER NOT NULL, "
                    + DATE + " DATE NOT NULL, "
                    + STATUS_key + " TEXT NOT NULL, "
                    + "UNIQUE(" + S_ID + ", " + DATE + "), "
                    + "FOREIGN KEY (" + S_ID + ") REFERENCES " + TABLE_Etudiant + "(" + S_ID + "),"
                    + "FOREIGN KEY (" + C_ID + ") REFERENCES " + TABLE_COURS + "(" + C_ID + ")"
                    + ");";


    private static final String DROP_TABLE_STATUS= "DROP TABLE IF EXISTS " + TABLE_STATUS;
    private static final String SELECT_TABLE_STATUS= "SELECT * FROM " + TABLE_STATUS;

    public DpHlper(@Nullable Context context) {
        super(context, "ATTENDANCE.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COURS);
        db.execSQL(CREATE_TABLE_ETUDIANT);
        db.execSQL(CREATE_TABLE_STATUS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_COURS_TABLE);
            db.execSQL(DROP_TABLE_Etudiant);
            db.execSQL(DROP_TABLE_STATUS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    long addClass(String className, String subjectName){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(NOM_COURS,className);
        values.put(SUBJECT_NAME,subjectName);
        return database.insert(TABLE_COURS,null,values);
    }
    Cursor getClassTable(){
        SQLiteDatabase database=this.getReadableDatabase();
        return database.rawQuery(SELECT_COURS_TABLE,null);
    }


    int deleteClass(long cid){
        SQLiteDatabase database=this.getReadableDatabase();
        return database.delete(TABLE_COURS,C_ID+"=?",new String[]{String.valueOf(cid)});
    }
    long updateClass(long cid,String className, String subjectName){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(NOM_COURS,className);
        values.put(SUBJECT_NAME,subjectName);
        return database.update(TABLE_COURS,values,C_ID+"=?",new String[]{String.valueOf(cid)});
    }
    long addStudent(long cid,int roll, String name){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(C_ID,cid);
        values.put(NOM_ETUDIANT,name);
        values.put(POSITION_ETUDIANT,roll);
        return database.insert(TABLE_Etudiant,null,values);
    }
    Cursor getStudents(long cid){
        SQLiteDatabase database=this.getReadableDatabase();
        return database.query(TABLE_Etudiant,null,C_ID+"=?",new String[]{String.valueOf(cid)},null,null,POSITION_ETUDIANT);
    }
    int deleteStudenet(long sid){
        SQLiteDatabase database=this.getReadableDatabase();
        return database.delete(TABLE_Etudiant,S_ID+"=?",new String[]{String.valueOf(sid)});
    }
    long updateStudent(long sid,String name){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(NOM_ETUDIANT,name);

        return database.update(TABLE_Etudiant,values,S_ID+"=?",new String[]{String.valueOf(sid)});

    }
    public long addStatus(long sid, long cid, String date, String status) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(S_ID, sid);
        values.put(C_ID, cid);
        values.put(DATE, date);
        values.put(STATUS_key, status);
        long result = database.insert(TABLE_STATUS, null, values);
        Log.d("Database", "addStatus result: " + result);
        return result;
    }

    public long updateStatus(long sid, String date, String status) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STATUS_key, status);
        String whereClause = DATE + "='" + date + "' AND " + S_ID + "=" + sid;
        long result = database.update(TABLE_STATUS, values, whereClause, null);
        Log.d("Database", "updateStatus result: " + result);
        return result;
    }

    public String getStatus(long sid, String date) {
        String status = null;
        SQLiteDatabase database = this.getReadableDatabase();
        String whereClause = DATE + "='" + date + "' AND " + S_ID + "=" + sid;
        try (Cursor cursor = database.query(TABLE_STATUS, null, whereClause, null, null, null, null)) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(STATUS_key);
                status = cursor.getString(columnIndex);
                Log.d("Database", "getStatus: " + status);
            }
        }
        return status;
    }


    Cursor getDistinctMonths(long cid){
        SQLiteDatabase database=this.getReadableDatabase();
        return database.query(TABLE_STATUS,new String[]{DATE},C_ID+"="+cid,null,"substr("+DATE+",4,7)",null,null);
    }

}