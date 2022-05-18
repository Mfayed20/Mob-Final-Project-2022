package com.example.fayed_final_project.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fayed_final_project.Adapters.SQLiteAdapter;
import com.example.fayed_final_project.Databases.SQLiteHelper;
import com.example.fayed_final_project.Interface.OnClickInterface;
import com.example.fayed_final_project.R;
import com.example.fayed_final_project.Student.Student;
import java.util.ArrayList;
import es.dmoral.toasty.Toasty;

public class SQLiteListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSQLite;
    private SQLiteHelper dbHelper;
    private SQLiteAdapter StudentAdapter;
    private ArrayList<Student> studentList;
    private OnClickInterface onclickInterface;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_full_list);

        // change the text of the app bar title
        getSupportActionBar().setTitle("SQLite Student List");

        // display home button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // onclick action for the home button
        getSupportActionBar().setHomeButtonEnabled(true);

        // change icon of the home button
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon);

        // add subtitle to the app bar
        getSupportActionBar().setSubtitle("Fayed - Final Project");



        onclickInterface = position -> {
            Toasty.info(SQLiteListActivity.this, "Full Name: " + studentList.get(position).getName() + " " + studentList.get(position).getSurname(), Toast.LENGTH_SHORT).show();
            StudentAdapter.notifyDataSetChanged();
        };

        dbHelper = new SQLiteHelper(this);
        studentList = new ArrayList<>();

        recyclerViewSQLite = findViewById(R.id.recyclerViewSQLite);
        recyclerViewSQLite.setHasFixedSize(true);
        recyclerViewSQLite.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSQLite.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSQLite.addItemDecoration(new DividerItemDecoration(recyclerViewSQLite.getContext(), DividerItemDecoration.VERTICAL));
        fillListView();
    }

    // fills the RecycleView List
    public void fillListView() {
        studentList = dbHelper.getAllData();
        StudentAdapter = new SQLiteAdapter(this,studentList, onclickInterface);
        recyclerViewSQLite.setAdapter(StudentAdapter);
    }

    // onclick listener for home icon in toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
//            Intent intent = new Intent(SQLiteListActivity.this, WeatherMainActivity.class);
//            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}