package com.example.fayed_final_project.Databases;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fayed_final_project.Student.Student;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Firebase extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private final String LOG = "Fayed";

    public Firebase() {
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance("https://fayed---final-project-default-rtdb.asia-southeast1.firebasedatabase.app/");
        myRef = database.getReference("Students-List");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Save Data From Firebase to SQLite Android
    public void saveDataToSQLite(SQLiteHelper db) {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    // clear the db
                    db.deleteAllData();
                    // loop through the data and insert it into the database
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
                        String date = data[5].substring(data[5].indexOf("=") + 1);
                        String year = data[6];

                        // don't take the last "}" for id
                        String id = data[7].substring(data[7].indexOf("=") + 1, data[7].length() - 1);

                        String dateOfBirth = date + ", " + year;

                        Student student = new Student(id, name, fathersName, surname, nationalId, gender, dateOfBirth);

                        db.insertData(student);
                        Log.d(LOG, student.toString());
                    }
                    Log.d(LOG+"-saveDataToSQLite","Successfully saved data to SQLite");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(LOG+"-saveDataToSQLite","Failed to save data to SQLite: "+e.getMessage());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}