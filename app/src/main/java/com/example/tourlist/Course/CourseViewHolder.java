package com.example.tourlist.Course;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tourlist.R;

class CourseViewHolder extends RecyclerView.ViewHolder {

    Button courseButton;

    CourseViewHolder(View itemView) {
        super(itemView);
        courseButton = itemView.findViewById(R.id.courseButton);
    }

    void bind(final TouristCourse course) {
        courseButton.setText(course.getCourse_title());
        courseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(itemView.getContext(), CourseDetail_Activity.class);
                intent.putExtra("CONTENT_ID", course.getContent_id());
                itemView.getContext().startActivity(intent);
            }
        });
    }
}
