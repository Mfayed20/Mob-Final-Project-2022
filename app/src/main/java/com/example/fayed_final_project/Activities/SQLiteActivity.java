package com.example.fayed_final_project.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.fayed_final_project.Databases.Firebase;
import com.example.fayed_final_project.Databases.SQLiteHelper;
import com.example.fayed_final_project.R;
import com.example.fayed_final_project.Student.Student;

import java.text.DateFormat;
import java.util.Calendar;
import es.dmoral.toasty.Toasty;

public class SQLiteActivity extends AppCompatActivity {

    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);

        getSupportActionBar().setTitle("SQLite");
        getSupportActionBar().setSubtitle("Fayed - 200002");

        // display home button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon);

        //  icon object
        ImageView weatherIconImageVDb = findViewById(R.id.weatherIconInDb);

        //  get iconURL from WeatherActivity using Intent
        Intent intent = getIntent();
        city = intent.getStringExtra("city");
        String iconUrl = intent.getStringExtra("weather");
        Glide.with(this).load(iconUrl).into(weatherIconImageVDb);

        EditText studentSQLId = findViewById(R.id.studentSQLId);
        EditText studentSQLName = findViewById(R.id.studentSQLName);
        EditText studentSQLFathersName = findViewById(R.id.studentSQLFathersName);
        EditText studentSQLSurname = findViewById(R.id.studentSQLSurname);
        EditText studentSQLNationalId = findViewById(R.id.studentSQLNationalId);

        RadioButton mRadioSQLBTN = findViewById(R.id.mRadioSQLBTN);
        RadioButton fRadioSQLBTN = findViewById(R.id.fRadioSQLBTN);
        TextView dataDisplaySQL = findViewById(R.id.dataDisplaySQL);
        Button dateSQLBTN = findViewById(R.id.dateSQLBTN);

        Button insertSQLBTN = findViewById(R.id.insertSQLBTN);
        Button updateSQLBTN = findViewById(R.id.updateSQLBTN);
        Button deleteSQLBTN = findViewById(R.id.deleteSQLBTN);

        Button syncDataBTN = findViewById(R.id.syncDataBTN);
        Button viewAllSQLBTN = findViewById(R.id.viewAllSQLBTN);
        Button viewByIdSQLBTN = findViewById(R.id.viewByIdSQLBTN);
        Button goToSQListBTN = findViewById(R.id.goToSQListBTN);
        Button goToFirebaseTN = findViewById(R.id.goToFirebaseTN);

        SQLiteHelper myDB = new SQLiteHelper(SQLiteActivity.this);

        // data picker
        Calendar c = Calendar.getInstance();
        DateFormat txtDate = DateFormat.getDateInstance();
        DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, monthOfYear);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dataDisplaySQL.setText(txtDate.format(c.getTime()));
            }
        };

        // pick date button
        dateSQLBTN.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SQLiteActivity.this, d,
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // view by Id SQLBTN
        viewByIdSQLBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = studentSQLId.getText().toString();
                Cursor res = myDB.getDataById(id);
                if (res.getCount() == 0) {
                    Toasty.error(SQLiteActivity.this, "ID not found", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuilder buffer = new StringBuilder();
                while (res.moveToNext()) {
                    buffer.append("Id : ").append(res.getString(0)).append("\n");
                    buffer.append("Name : ").append(res.getString(1)).append("\n");
                    buffer.append("Fathers Name : ").append(res.getString(2)).append("\n");
                    buffer.append("Surname : ").append(res.getString(3)).append("\n");
                    buffer.append("National Id : ").append(res.getString(4)).append("\n");
                    buffer.append("Gender: ").append(res.getString(5)).append("\n");
                    buffer.append("Date of birth: ").append(res.getString(6)).append("\n\n");

                }
                AlertDialog.Builder builder = new AlertDialog.Builder(SQLiteActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Student Data");
                builder.setMessage(buffer.toString());
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });


        // view all Database
        viewAllSQLBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cur = myDB.getAllDataInCursor();
                StringBuilder buffer = new StringBuilder();

                while(cur.moveToNext()){
                    buffer.append("id: ").append(cur.getString(0)).append("\n");
                    buffer.append("Name: ").append(cur.getString(1)).append("\n");
                    buffer.append("Fathers Name: ").append(cur.getString(2)).append("\n");
                    buffer.append("Surname: ").append(cur.getString(3)).append("\n");
                    buffer.append("National ID: ").append(cur.getString(4)).append("\n");
                    buffer.append("Gender: ").append(cur.getString(5)).append("\n");
                    buffer.append("Date of birth: ").append(cur.getString(6)).append("\n\n");

                }

                AlertDialog.Builder builder = new AlertDialog.Builder(SQLiteActivity.this);
                builder.setCancelable(true);
                builder.setTitle("All Students");
                builder.setMessage(buffer.toString());
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();

            }
        });

        // insert button
        insertSQLBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String studentId = studentSQLId.getText().toString();
                String studentName = studentSQLName.getText().toString();
                String studentFathersName = studentSQLFathersName.getText().toString();
                String studentSurname = studentSQLSurname.getText().toString();
                String studentNationalId = studentSQLNationalId.getText().toString();
                String studentGender = "";
                if (mRadioSQLBTN.isChecked()) {
                    studentGender = "M";
                } else if (fRadioSQLBTN.isChecked()) {
                    studentGender = "F";
                }
                String studentBirthDate = dataDisplaySQL.getText().toString();

                if (studentId.isEmpty() || studentName.isEmpty() || studentFathersName.isEmpty() || studentSurname.isEmpty() || studentNationalId.isEmpty() || studentGender.isEmpty() || studentBirthDate.isEmpty()) {
                    Toasty.error(SQLiteActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // check if id exists
                    if(myDB.isIdExist(studentId)){
                        Toasty.error(SQLiteActivity.this, "ID already exists", Toast.LENGTH_SHORT).show();
                    }else{
                        // create student object
                        Student student = new Student(studentId, studentName, studentFathersName, studentSurname, studentNationalId, studentGender, studentBirthDate);
                        boolean isInserted = myDB.insertData(student);
                        if (isInserted) {
                            Toasty.success(SQLiteActivity.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toasty.error(SQLiteActivity.this, "Data not inserted", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        // UPDATE button
        updateSQLBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String studentId = studentSQLId.getText().toString();
                String studentName = studentSQLName.getText().toString();
                String studentFathersName = studentSQLFathersName.getText().toString();
                String studentSurname = studentSQLSurname.getText().toString();
                String studentNationalId = studentSQLNationalId.getText().toString();
                String studentGender = "";
                if (mRadioSQLBTN.isChecked()) {
                    studentGender = "M";
                } else if (fRadioSQLBTN.isChecked()) {
                    studentGender = "F";
                }
                String studentBirthDate = dataDisplaySQL.getText().toString();

                if (studentId.isEmpty() || studentName.isEmpty() || studentFathersName.isEmpty() || studentSurname.isEmpty() || studentNationalId.isEmpty() || studentGender.isEmpty() || studentBirthDate.isEmpty()) {
                    Toasty.error(SQLiteActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // check if id exists
                    if(myDB.isIdExist(studentId)){
                        // create student object
                        Student student = new Student(studentId, studentName, studentFathersName, studentSurname, studentNationalId, studentGender, studentBirthDate);
                        boolean isUpdated = myDB.updateDataById(student);
                        if (isUpdated) {
                            Toasty.success(SQLiteActivity.this, "Data updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toasty.error(SQLiteActivity.this, "Error: Data not updated", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toasty.error(SQLiteActivity.this, "ID does not exist", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // DELETE button by id
        deleteSQLBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String studentId = studentSQLId.getText().toString();
                if (studentId.isEmpty()) {
                    Toasty.error(SQLiteActivity.this, "Please fill ID field", Toast.LENGTH_SHORT).show();
                } else {
                    // check if id exists
                    if(myDB.isIdExist(studentId)){
                        boolean isDeleted = myDB.deleteDataByID(studentId);
                        if (isDeleted) {
                            Toasty.success(SQLiteActivity.this, "Data deleted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toasty.error(SQLiteActivity.this, "Error: Data not deleted", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toasty.error(SQLiteActivity.this, "ID does not exist", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // sync data from Firebase to SQLite
        syncDataBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStudentListFromFirebase(myDB);
                Toasty.success(SQLiteActivity.this, "Data Synced From Firebase", Toast.LENGTH_SHORT).show();
            }
        });

        // go to SQLiteFullListActivity
        goToSQListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SQLiteActivity.this, SQLiteListActivity.class);
                startActivity(intent);
            }
        });


        // go to FirebaseActivity
        goToFirebaseTN.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               // send iconUrl to DB activity using Intent
               Intent intent = new Intent(SQLiteActivity.this, FirebaseActivity.class);
               intent.putExtra("weather", iconUrl);
               intent.putExtra("city", city);
               startActivity(intent);
           }
       });

    }

    // get student list from firebase and save them into local SQLite database 
    public void getStudentListFromFirebase(SQLiteHelper db) {
        Firebase firebase = new Firebase();
        firebase.saveDataToSQLite(db);
    }

    // onclick listener for home icon in toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            Intent intent = new Intent(SQLiteActivity.this, WeatherMainActivity.class);
            intent.putExtra("city", city);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}