package com.example.tourlist.Course;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourlist.R;
import com.example.tourlist.Tourist_Detail_Activity.TouristPlaceDetailActivity;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseViewHolder> {

    public List<TouristCourse> courses;

    public CourseAdapter(List<TouristCourse> courses) {
        this.courses = courses;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frag4_item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        TouristCourse course = courses.get(position);
        holder.bind(course);
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }


}
