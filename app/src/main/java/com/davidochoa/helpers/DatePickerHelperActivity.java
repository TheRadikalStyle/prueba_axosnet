package com.davidochoa.helpers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.davidochoa.R;

import java.util.Calendar;

public class DatePickerHelperActivity extends AppCompatActivity implements View.OnClickListener{
    private DatePicker datePickerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker_helper);

        Calendar c = Calendar.getInstance();



        datePickerHelper = findViewById(R.id.datepickerhelper_datepicker_helper);
        datePickerHelper.setMaxDate(c.getTime().getTime());

        Button buttonCancel = findViewById(R.id.datepickerhelper_button_cancel);
        Button buttonDone = findViewById(R.id.datepickerhelper_button_done);

        buttonCancel.setOnClickListener(this);
        buttonDone.setOnClickListener(this);
    }

    private void RegresaFecha(){
        Intent intent = new Intent();
        intent.putExtra("year", datePickerHelper.getYear());
        intent.putExtra("month", datePickerHelper.getMonth());
        intent.putExtra("day", datePickerHelper.getDayOfMonth());

        setResult(RESULT_OK, intent);
        finish();
    }

    private void Cancel(){
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.datepickerhelper_button_done)
            RegresaFecha();

        if(id == R.id.datepickerhelper_button_cancel)
            Cancel();
    }
}