

package com.example.e_presence;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class Myclendar extends DialogFragment {
    Calendar calendar=Calendar.getInstance();

    public interface OnCalendarOnClickListner{
        void onClick(int year,int month,int day);
    }
    public OnCalendarOnClickListner onCalendarOnClickListner;
    public void setOnCalendarOnClickListner(OnCalendarOnClickListner onCalendarOnClickListner){
        this.onCalendarOnClickListner=onCalendarOnClickListner;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(),((View,year,month,dayOfMonth)->{
            onCalendarOnClickListner.onClick(year,month,dayOfMonth);

        }),calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
    }
    void setDate(int year,int month,int day){
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);


        calendar.set(Calendar.DAY_OF_MONTH,day);
    }
    String getDate(){
        return DateFormat.format("dd.MM.yyyy",calendar).toString();
    }
}
