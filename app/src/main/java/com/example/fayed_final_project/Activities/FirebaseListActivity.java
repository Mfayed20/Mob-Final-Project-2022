package com.example.fayed_final_project.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fayed_final_project.Adapters.FirebaseAdapter;
import com.example.fayed_final_project.R;
import com.example.fayed_final_project.Student.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class FirebaseListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseAdapter firebaseAdapter;
    private ArrayList<Student> list;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    // first time I don't need to check if data is the same or not
    private boolean firstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_full_list);

        // change the text of the app bar title
        getSupportActionBar().setTitle("Firebase Student List");

        // display home button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // onclick action for the home button
        getSupportActionBar().setHomeButtonEnabled(true);

        // add subtitle to the app bar
        getSupportActionBar().setSubtitle("Fayed - Final Project");

        // change icon of the home button
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon);



        database = FirebaseDatabase.getInstance("https://fayed---final-project-default-rtdb.asia-southeast1.firebasedatabase.app/");
        myRef = database.getReference("Students-List");
        recyclerView = findViewById(R.id.studentList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        firebaseAdapter = new FirebaseAdapter(this,list);
        recyclerView.setAdapter(firebaseAdapter);

        // get data from firebase and update in realtime
        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Student student = dataSnapshot.getValue(Student.class);
                    // compare the student id and name from firebase with the list student
                    // if different remove from the list and add student from firebase
                    // if same do nothing

                    // check if the student is not null
                    if (student != null) {
                        if (firstTime) {
                            list.add(student);
                        } else {
                            if (!list.get(i).getId().equals(student.getId()) || !list.get(i).getName().equals(student.getName())) {
                                list.remove(i);
                                list.add(i, student);
                            }
                        }
                        i++;
                    }
                }
                firstTime=false;
                firebaseAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // onclick listener for home icon in toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
//            Intent intent = new Intent(FirebaseListActivity.this, WeatherMainActivity.class);
//            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}