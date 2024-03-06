package com.example.e_presence;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DpHlper extends SQLiteOpenHelper {
    // Table cours
    private static final String TABLE_COURS = "table_cours";
    private static final String C_ID = "c_id";
    private static final String NOM_COURS = "nom_de_cours";
    private static final String SUBJECT_NAME = "subject_name";
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

    //table etudiant lalcdshjsahvjcehkbeiffeij

    private static final String TABLE_Etudiant= "table_etudiant";
    private static final String S_ID = "s_id";
    private static final String NOM_ETUDIANT = "nom_etudiant";
    private static final String POSITION_ETUDIANT= "position_etudiant";
    private static final String CREATE_TABLE_ETUDIANT =
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
    private static final String STATUS_ID = "status_id";
    private static final String DATE = "date";
    private static final String STATUS_key= "status";
    private static final String CREATE_TABLE_STATUS=
            "CREATE TABLE "
                    + TABLE_STATUS
                    + " ("
                    + STATUS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + S_ID + " INTEGER NOT NULL, "
                    + DATE + " DATE NOT NULL, "
                    + STATUS_key + " TEXT NOT NULL, "
                    + "UNIQUE(" + S_ID + ", " + DATE + "), "
                    + "FOREIGN KEY (" + S_ID + ") REFERENCES " + TABLE_Etudiant + "(" + S_ID + ")"
                    + ")";


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
}
