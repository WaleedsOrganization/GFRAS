package com.example.gfras_app.Activities.Instructor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.gfras_app.Activities.CourseHomeUI.CourseHomeMainActivity;
import com.example.gfras_app.Data.Course.Course;
import com.example.gfras_app.Data.Course.CourseListItemAdapter;
import com.example.gfras_app.Data.User.User;
import com.example.gfras_app.Data.User.UserServices;
import com.example.gfras_app.R;
import com.example.gfras_app.util.CollectionsName;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;

import pp.facerecognizer.FirstFRActicity;

public class InstructorCoursesList extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    ProgressBar prgsBarHomePage;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private CourseListItemAdapter.RecyclerViewClickListener listener;
    List<Course> courseList;
    User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_courses_list);
        setTitle("Your Courses");
        setOnclickListener();
        mRecyclerView = findViewById(R.id.CourseListRecyclerView);
        courseList = new LinkedList<Course>();
        prgsBarHomePage = findViewById(R.id.prgsBarHomePage);
        prgsBarHomePage.setVisibility(View.VISIBLE);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        currentUser = UserServices.getCurrentUser(getApplicationContext());

        showEitherLoadingOrReady();

    }
    private void showEitherLoadingOrReady() {


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference courseRef = db.collection(CollectionsName.COURSES);
        courseRef.whereEqualTo("InstructorID", "iO2qJRViPJ6rfoh1KzQW").get()
                .addOnCompleteListener(task -> {
                    Log.d("TAG", task.isSuccessful()+"");

                    if (task.isSuccessful()) {
                        Log.d("TAG", task.getResult()+" getResults");

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("TAG", document.getId() + " => " + document.getData());
                            Course c = document.toObject(Course.class);
                            courseList.add(c);
                        }

                        mAdapter = new CourseListItemAdapter(courseList, listener);
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        prgsBarHomePage.setVisibility(View.INVISIBLE);
                        mRecyclerView.setAdapter(mAdapter);
                        Log.d("TAG", "this is the list: " + courseList.toString());

                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                });
        Log.d("TAG", "this is the list: " + courseList.toString());


    }

    private void setOnclickListener() {
        listener = new CourseListItemAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), FirstFRActicity.class);
                Bundle bundle = new Bundle();
                Gson g = new Gson();

                bundle.putString("COURSE_ID",g.toJson(courseList.get(position)));
                intent.putExtras(bundle);
                startActivity(intent);


            }
        };

    }

}