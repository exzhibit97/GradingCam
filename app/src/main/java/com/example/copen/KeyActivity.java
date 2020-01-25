package com.example.copen;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.copen.Extensions.DatabaseHelper;

import java.util.ArrayList;

public class KeyActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText editName, editKey;
    Button btnAddData;
    Button btnGetExams;
    Spinner examsSpinner;
    ArrayList<String> examsList = new ArrayList<>();
    TextView examKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);

        editName = findViewById(R.id.nameText);
        editKey = findViewById(R.id.keyText);
        btnAddData = findViewById(R.id.saveBT);
        btnGetExams = findViewById(R.id.getBT);
        examsSpinner = findViewById(R.id.examsSpinner);
        examKey = findViewById(R.id.examKeyTV);

        addData();

        Cursor res = myDb.getExams();
        if(res.getCount() == 0){
            Toast.makeText(KeyActivity.this, "Brak danych w bazie", Toast.LENGTH_LONG).show();
            return;
        }

        while(res.moveToNext()){
            examsList.add(res.getString(0));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, examsList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        examsSpinner.setAdapter(arrayAdapter);

        //getExams();
        getKey();

    }

    public void addData(){
        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = myDb.insertData(editName.getText().toString(), editKey.getText().toString());

                        if (isInserted = true)
                            Toast.makeText(KeyActivity.this, "Dodano do bazy", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(KeyActivity.this, "Nie dodano do bazy", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void getKey(){
        examsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selection = parent.getItemAtPosition(position).toString();

                Cursor res = myDb.getKey(selection);

                while(res.moveToNext()){
                    String key = res.getString(0);

                    Toast.makeText(KeyActivity.this, key, Toast.LENGTH_LONG).show();
                    examKey.setText(key);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
