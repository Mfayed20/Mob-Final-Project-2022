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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_full_list);

        getSupportActionBar().setTitle("Firebase Student List");
        getSupportActionBar().setSubtitle("Fayed - 200002");

        // display home button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


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
                list.clear(); //* added this
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Student student = dataSnapshot.getValue(Student.class);
                    list.add(student); //* added this
                }
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
        }
        return super.onOptionsItemSelected(item);
    }
}