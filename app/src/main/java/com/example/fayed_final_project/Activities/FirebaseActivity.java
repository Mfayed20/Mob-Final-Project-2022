package com.example.fayed_final_project.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.fayed_final_project.R;
import com.example.fayed_final_project.Student.Student;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.DateFormat;
import java.util.Calendar;
import es.dmoral.toasty.Toasty;

public class FirebaseActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);

        // change the text of the app bar title
        getSupportActionBar().setTitle("Firebase");

        // display home button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // onclick action for the home button
        getSupportActionBar().setHomeButtonEnabled(true);

        // change icon of the home button
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon);

        // add subtitle to the app bar
        getSupportActionBar().setSubtitle("Fayed - Final Project");


        //  icon object
        ImageView weatherIconImageVDb = findViewById(R.id.weatherIconInDb);

        //  get iconURL from WeatherActivity using Intent
        Intent intent = getIntent();
        city = intent.getStringExtra("city");
        String iconUrl = intent.getStringExtra("weather");
        Glide.with(this).load(iconUrl).into(weatherIconImageVDb);

        EditText studentId = findViewById(R.id.studentId);
        EditText studentName = findViewById(R.id.studentName);
        EditText studentFathersName = findViewById(R.id.studentFathersName);
        EditText studentSurname = findViewById(R.id.studentSurname);
        EditText studentNationalId = findViewById(R.id.studentNationalId);

        RadioButton mRadioBTN = findViewById(R.id.mRadioBTN);
        RadioButton fRadioBTN = findViewById(R.id.fRadioBTN);
        TextView dataDisplay = findViewById(R.id.dataDisplay);
        Button dateBTN = findViewById(R.id.dateBTN);
        Button viewByIdBTN = findViewById(R.id.viewByIdBTN);
        Button viewAllBTN = findViewById(R.id.viewAllBTN);

        Button insertBTN = findViewById(R.id.insertBTN);
        Button updateBTN = findViewById(R.id.updateBTN);
        Button deleteBTN = findViewById(R.id.deleteBTN);

        Button goToFbListBTN = findViewById(R.id.goToFbListBTN);
        Button goToSQLiteBTN = findViewById(R.id.goToSQLiteBTN);

        database = FirebaseDatabase.getInstance("https://fayed---final-project-default-rtdb.asia-southeast1.firebasedatabase.app/");
        myRef = database.getReference("Students-List");

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
                dataDisplay.setText(txtDate.format(c.getTime()));
            }
        };

        // pick date button
        dateBTN.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new DatePickerDialog(FirebaseActivity.this, d,
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // viewAllBTN
        viewAllBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayAllData();
            }
        });

        // viewByIdBTN
        viewByIdBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = studentId.getText().toString();
                if (id.isEmpty()) {
                    Toasty.error(FirebaseActivity.this, "Please enter student id", Toast.LENGTH_SHORT).show();
                } else {
                    displayDataById(id);
                }
            }
        });

        // insert button
        insertBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _studentID = studentId.getText().toString();
                String _studentName = studentName.getText().toString();
                String _studentFathersName = studentFathersName.getText().toString();
                String _studentSurname = studentSurname.getText().toString();
                String _studentNationalId = studentNationalId.getText().toString();
                String _studentGender = "";
                    if (mRadioBTN.isChecked()) {
                        _studentGender = " M";
                    } else if (fRadioBTN.isChecked()) {
                        _studentGender = " F";
                    }
                String _studentBirthDate = dataDisplay.getText().toString();

                // check if all fields are filled
                if (_studentID.isEmpty() || _studentName.isEmpty() || _studentFathersName.isEmpty() || _studentSurname.isEmpty() || _studentNationalId.isEmpty() || _studentGender.isEmpty() || _studentBirthDate.isEmpty()) {
                    Toasty.error(FirebaseActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    insertData(_studentID, _studentName, _studentFathersName, _studentSurname, _studentNationalId, _studentGender, _studentBirthDate);
                }
            }
        });

        // update button
        updateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _studentID = studentId.getText().toString();
                String _studentName = studentName.getText().toString();
                String _studentFathersName = studentFathersName.getText().toString();
                String _studentSurname = studentSurname.getText().toString();
                String _studentNationalId = studentNationalId.getText().toString();
                String _studentGender = "";
                if (mRadioBTN.isChecked()) {
                    _studentGender = " M";
                } else if (fRadioBTN.isChecked()) {
                    _studentGender = " F";
                }
                String _studentBirthDate = dataDisplay.getText().toString();

                // check if all fields are filled
                if (_studentID.isEmpty() || _studentName.isEmpty() || _studentFathersName.isEmpty() || _studentSurname.isEmpty() || _studentNationalId.isEmpty() || _studentGender.isEmpty() || _studentBirthDate.isEmpty()) {
                    Toasty.error(FirebaseActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    updateById(_studentID, _studentName, _studentFathersName, _studentSurname, _studentNationalId, _studentGender, _studentBirthDate);
                }

            }
        });

        // delete button by id
        deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _studentID = studentId.getText().toString();

                // check if id field is empty
                if (_studentID.isEmpty()) {
                    Toasty.error(FirebaseActivity.this, "Please fill id field", Toast.LENGTH_SHORT).show();
                } else {
                    deleteById(_studentID);
                }
            }
        });

        // goToFbListBTN
        goToFbListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirebaseActivity.this, FirebaseListActivity.class);
                startActivity(intent);
            }
        });

        // goToSQLiteBTN
        goToSQLiteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send iconUrl to DB activity using Intent
                Intent intent = new Intent(FirebaseActivity.this, SQLiteActivity.class);
                intent.putExtra("weather", iconUrl);
                intent.putExtra("city", city);
                startActivity(intent);
            }
        });

        // goToWeatherBTN
//        goToWeatherBTN.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(FirebaseActivity.this, WeatherMainActivity.class);
//                intent.putExtra("city", city);
//                startActivity(intent);
//            }
//        });

    }

    // insert student
    public void insertData(String _studentID, String _studentName, String _studentFathersName, String _studentSurname, String _studentNationalId, String studentGender, String _studentBirthDate) {
        String stuNum = _studentID.substring(3);
        // loop through all students
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Student student = ds.getValue(Student.class);
                    String _stuNum = student.getId().substring(3);
                    if (stuNum.equals(_stuNum)) {
                        Toasty.error(FirebaseActivity.this, "ID already exists", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                // student object
                Student student = new Student(_studentID, _studentName, _studentFathersName, _studentSurname, _studentNationalId, studentGender, _studentBirthDate);
                myRef.child("NUM " + stuNum).setValue(student).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toasty.success(FirebaseActivity.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toasty.error(FirebaseActivity.this, "Data not inserted", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(FirebaseActivity.this, "Data not inserted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // update student by ID
    public void updateById(String _studentID, String _studentName, String _studentFathersName, String _studentSurname, String _studentNationalId, String studentGender, String _studentBirthDate) {
        // loop through all students
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Student student = ds.getValue(Student.class);
                    if (student.getId().equals(_studentID)) {
                        // update student
                        student.setName(_studentName);
                        student.setFathers_name(_studentFathersName);
                        student.setSurname(_studentSurname);
                        student.setNational_id(_studentNationalId);
                        student.setGender(studentGender);
                        student.setDateOfBirth(_studentBirthDate);

                        // update student
                        myRef.child(ds.getKey()).setValue(student).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toasty.success(FirebaseActivity.this, "Data updated successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toasty.error(FirebaseActivity.this, "Data not updated", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                }
                Toasty.error(FirebaseActivity.this, "ID not found", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(FirebaseActivity.this, "Data not updated", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // delete student by ID
    public void deleteById(String _studentID) {
        // loop through all students
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Student student = ds.getValue(Student.class);
                    if (student.getId().equals(_studentID)) {
                        ds.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toasty.success(FirebaseActivity.this, "Data deleted successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toasty.error(FirebaseActivity.this, "Data not deleted", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                    Toasty.error(FirebaseActivity.this, "ID not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(FirebaseActivity.this, "Data not deleted", Toast.LENGTH_SHORT).show();
            }
        });


    }

    // get data from firebase
    public void displayAllData() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StringBuilder sb = new StringBuilder();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String buffer = ds.getValue().toString();
                    // format data
                    String[] data = buffer.split(",");

                    // index 0 has two = sign, so we need to remove it
                    String[] data1 = data[0].split("=");
                    String fathersName = data1[1];

                    String gender = data[1].substring(data[1].indexOf("=") + 1);
                    String nationalId = data[2].substring(data[2].indexOf("=") + 1);
                    String surname = data[3].substring(data[3].indexOf("=") + 1);
                    String name = data[4].substring(data[4].indexOf("=") + 1);
                    String dateOfBirth = data[5].substring(data[5].indexOf("=") + 1);
                    String year = data[6];
                    // don't take the last "}" for id
                    String id = data[7].substring(data[7].indexOf("=") + 1, data[7].length() - 1);

                    // save them in StringBuffer
                    sb.append("Id: ").append(id).append("\n");
                    sb.append("Name: ").append(name).append("\n");
                    sb.append("Father's Name: ").append(fathersName).append("\n");
                    sb.append("Surname: ").append(surname).append("\n");
                    sb.append("National Id: ").append(nationalId).append("\n");
                    sb.append("Gender: ").append(gender).append("\n");
                    sb.append("Date of birth: ").append(dateOfBirth+", "+year).append("\n\n");
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(FirebaseActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Student List");
                builder.setMessage(sb.toString());
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error", "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    //  get data from firebase by ID
    public void displayDataById(String _studentID) {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Student student = ds.getValue(Student.class);
                    if (student.getId().equals(_studentID)) {
                        String buffer = ds.getValue().toString();
                        // format data
                        String[] data = buffer.split(",");

                        // index 0 has two = sign, so we need to remove it
                        String[] data1 = data[0].split("=");
                        String fathersName = data1[1];

                        String gender = data[1].substring(data[1].indexOf("=") + 1);
                        String nationalId = data[2].substring(data[2].indexOf("=") + 1);
                        String surname = data[3].substring(data[3].indexOf("=") + 1);
                        String name = data[4].substring(data[4].indexOf("=") + 1);
                        String dateOfBirth = data[5].substring(data[5].indexOf("=") + 1);
                        String year = data[6].substring(data[6].indexOf("=") + 1);
                        // don't take the last "}" for id
                        String id = data[7].substring(data[7].indexOf("=") + 1, data[7].length() - 1);

                        // save them in StringBuffer
                        StringBuffer sb = new StringBuffer();
                        sb.append("Id: ").append(id).append("\n");
                        sb.append("Name: ").append(name).append("\n");
                        sb.append("Father's Name: ").append(fathersName).append("\n");
                        sb.append("Surname: ").append(surname).append("\n");
                        sb.append("National Id: ").append(nationalId).append("\n");
                        sb.append("Gender: ").append(gender).append("\n");
                        sb.append("Date of birth: ").append(dateOfBirth+", "+year).append("\n\n");


                        AlertDialog.Builder builder = new AlertDialog.Builder(FirebaseActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle("Student Data");
                        builder.setMessage(sb.toString());
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                        return;
                    }
                }
                Toasty.error(FirebaseActivity.this, "ID not found", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error", "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    // onclick listener for home icon in toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            Intent intent = new Intent(FirebaseActivity.this, WeatherMainActivity.class);
            intent.putExtra("city", city);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}