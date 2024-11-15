package com.example.newtaskpage;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView textDate;

    private TextView textTime;

    private Button dateAndTimeButton;

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.new_task);

        spinner = findViewById(R.id.priorityButton);

        /**
         * Uses an ArrayAdapter for the Priority Selector
         */
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priority, android.R.layout.simple_spinner_item);

        /**
         * Allows for the user to see the options inside the Priority Selector
         */
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        textDate = findViewById(R.id.showTextDate);

        textTime = findViewById(R.id.showTextTime);

        dateAndTimeButton = findViewById(R.id.dateAndTimeButton);

        dateAndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog();

            }
        });

        //ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
        //Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
        //return insets;
        //});
    }

    /**
     * Inputs the year, month, and day that the user selects. Also includes how to change the time as well
     */
    private void openDialog() {

        /**
         * Method that sets the time, allowing for the hours and minutes to be selected
         */
        TimePickerDialog dialog1 = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

                textTime.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));

            }
        }, 15, 00, true);

        /**
         * Method that sets the date, allowing for the year, month, and day to be selected
         */
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                textDate.setText(String.valueOf(year) + "." + String.valueOf(month+1) + "." + String.valueOf(dayOfMonth));

            }
        }, 2024, 0, 15);

        /**
         * Gives the dialog for the date an initial date, and on the onDateSet method, we get the date the user selected
         * and show it in a text view
         */
        dialog.show();
        dialog1.show();

    }

}