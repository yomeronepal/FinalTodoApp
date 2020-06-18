package com.example.finaltodoapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.TimeZoneFormat;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.finaltodoapp.model.Viewmodel.TodoViewModel;
import com.example.finaltodoapp.model.entity.ETodo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class EditTodoFragment extends Fragment {

    Button btnSave,btnCancel;
    View rootView;
    AlertDialog.Builder mAlertDialogue;
    DatePickerDialog mDatePicker;

    TimePickerDialog mTimePicker;

    EditText txtTitle,txtDescription,txtDate,txtTime;
    RadioGroup rgPriority;
    RadioButton rbHigh,rbMedium,rbLow,rbSelected;
    CheckBox chkIsComplete;

    public static final  int High_Priority=1;
    public static final int Medium_Priority=2;
    public static final int Low_Priority=3;

    private TodoViewModel mTodoViewModel;

    private int todoId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_edit_todo, container, false);
        mTodoViewModel= ViewModelProviders.of(this).get(TodoViewModel.class);
        txtDate =rootView.findViewById(R.id.edit_txt_date);
        txtTime = rootView.findViewById(R.id.edit_txt_time);
        btnSave= rootView.findViewById(R.id.edit_button_save);
        btnCancel=rootView.findViewById(R.id.edit_btn_cancel);
        txtTitle = rootView.findViewById(R.id.edit_txt_title);
        txtDescription=rootView.findViewById(R.id.edit_txt_description);
        rgPriority=rootView.findViewById(R.id.edit_rg_priority);
        rbHigh=rootView.findViewById(R.id.edit_rb_high);
        rbMedium=rootView.findViewById(R.id.edit_rb_medium);
        rbLow=rootView.findViewById(R.id.edit_rb_low);
        chkIsComplete=rootView.findViewById(R.id.edit_chkbox_iscomplited);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveTodo();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DisplayAlertDialoge();


            }
        });

        txtDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction()==MotionEvent.ACTION_DOWN) {
                    DisplayTodoDate();
                }
                return false;
            }
        });
        txtTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event
                .getAction()==MotionEvent.ACTION_DOWN)
                {
                    DisplayTodoTime();
                }

                return false;
            }
        });

        todoId =getActivity().getIntent().getIntExtra("TodoId",-1);
        if(todoId != -1)
        {
            btnSave.setText(getText(R.string.edit_update));
            ETodo todo = mTodoViewModel.getTodoById(todoId);
            txtTitle.setText(todo.getTitle());
            txtDescription.setText(todo.getDescription());
            DateFormat formater;
            formater =new SimpleDateFormat("yyyy-MM-dd");
            txtDate.setText(formater.format(todo.getTodo_date()));
            DateFormat Timeformater;
            Timeformater= new SimpleDateFormat("HH-mm");
            txtTime.setText(Timeformater.format(todo.getTodo_time()));
            switch(todo.getPriority())
            {
                case 1:
                    rgPriority.check(R.id.edit_rb_high);
                            break;

                case 2:
                    rgPriority.check(R.id.edit_rb_medium);
                    break;

                case 3:
                    rgPriority.check(R.id.edit_rb_low);
                    break;
            }
            chkIsComplete.setSelected(todo.isIs_completed());
        }




    return rootView;
    }

    void DisplayAlertDialoge()
    {
        mAlertDialogue = new AlertDialog.Builder(getContext());

        mAlertDialogue.setMessage(getString(R.string.edit_cancel_prompt))
                .setCancelable(false)
                .setTitle(getString(R.string.app_name))
                .setIcon(R.mipmap.ic_launcher);

        mAlertDialogue.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });
        mAlertDialogue.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        mAlertDialogue.show();
    }
    void DisplayTodoDate()
    {

        Calendar calendar = Calendar.getInstance();
        int cDay= calendar.get(Calendar.DAY_OF_MONTH);
        int cMonth = calendar.get(Calendar.MONTH);
        int cYear=calendar.get(Calendar.YEAR);

        mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                txtDate.setText(year+"-"+month+"-"+dayOfMonth);

            }
        },cYear,cMonth,cDay);
        mDatePicker.show();
    }

    void DisplayTodoTime()
    {
        Calendar calendar = Calendar.getInstance();
        int cHour=calendar.get(Calendar.HOUR_OF_DAY);
        int cMinute=calendar.get(Calendar.MINUTE);
        int cSecond=calendar.get(Calendar.SECOND);

        mTimePicker= new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                txtTime.setText(hourOfDay+"-"+minute);
            }
        },cHour,cMinute,false);
        mTimePicker.show();
    }

    void SaveTodo()
    {

        ETodo todo=new ETodo();
        Date todoDate;
        int priority =1;
        int checkedPriority=-1;

        todo.setTitle(txtTitle.getText().toString());
        todo.setDescription(txtDescription.getText().toString());
        try {
            DateFormat formater;
            formater =new SimpleDateFormat("yyyy-MM-dd");
            todoDate=(Date)formater.parse(txtDate.getText().toString());
            todo.setTodo_date(todoDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        checkedPriority=rgPriority.getCheckedRadioButtonId();
        switch (checkedPriority)
        {
            case R.id.edit_rb_high:
                priority=High_Priority;
                break;

            case R.id.edit_rb_medium:
                priority=Medium_Priority;
                break;

            case R.id.edit_rb_low:
                priority=Low_Priority;
                break;
        }
        todo.setPriority(priority);

        todo.setIs_completed(chkIsComplete.isChecked());

        if (todoId != -1)
        {
            todo.setId(todoId);
            mTodoViewModel.update(todo);
            Toast.makeText(getActivity(),getText(R.string.crud_update),Toast.LENGTH_SHORT).show();
        }
        else
        {
            mTodoViewModel.insert(todo);

            Toast.makeText(getActivity(),getText(R.string.crud_save),Toast.LENGTH_SHORT).show();
        }



        Intent intent =new Intent(getActivity(),MainActivity.class);
        startActivity(intent);
    }
}