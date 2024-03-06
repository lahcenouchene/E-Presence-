

package com.example.e_presence;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


public class Mydialogue extends DialogFragment{
    public static final String CLASS_ADD_DIALOG="addClass";
    public static final String STUDENT_ADD_DIALOG="addStudent";
     private OnClickListner listener;

    //
    public interface OnClickListner {
        void onClick(String text1, String text2);
    }
    public void setListener(OnClickListner listener){
        this.listener=listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = null;
        if (getTag().equals(CLASS_ADD_DIALOG)) dialog = getAddClassDialog();
        if (getTag().equals(STUDENT_ADD_DIALOG)) dialog = getAddStudentDialog();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return dialog;
    }

    private Dialog getAddClassDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.class_dialogue,null);
        builder.setView(view);
        TextView title=view.findViewById(R.id.title_id);
        title.setText("Ajouter Un Nouveau Cours");
        EditText class_id =view.findViewById(R.id.class_id);
        EditText subject_id=view.findViewById(R.id.section_id);
        class_id.setHint("Cours Numéro");
        subject_id.setHint("Nom De Cours");


        Button cancel=view.findViewById(R.id.cancel);
        Button add=view.findViewById(R.id.add);

        cancel.setOnClickListener(v ->dismiss() );
        add.setOnClickListener(v -> {
            String className= class_id.getText().toString();
            String subjectName= subject_id.getText().toString();
            listener.onClick(className,subjectName);
            dismiss();
        });
        return builder.create();
    }

    private Dialog getAddStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.class_dialogue, null);
        builder.setView(view);
        TextView title = view.findViewById(R.id.title_id);
        title.setText("Ajouter Un Nouveau Etudiant");
        EditText roll_edt = view.findViewById(R.id.class_id);
        EditText name_edt = view.findViewById(R.id.section_id);
        roll_edt.setHint("Role");
        name_edt.setHint("Nom");


        Button cancel = view.findViewById(R.id.cancel);
        Button add = view.findViewById(R.id.add);

        cancel.setOnClickListener(v -> dismiss());
        add.setOnClickListener(v -> {
            String roll = roll_edt.getText().toString();
            String name = name_edt.getText().toString();
            roll_edt.setText(String.valueOf(Integer.parseInt(roll) + 1));
            listener.onClick(roll, name);
            dismiss();
        });

        return builder.create();
    }
}


