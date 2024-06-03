package com.example.tourlist.Course;

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
        courseButton.setText(course.getName());
        courseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Intent intent = new Intent(itemView.getContext(), TouristPlaceDetailActivity.class);
//                    intent.putExtra("PLACE_NAME", course.getName());
//                    intent.putExtra("PLACE_ADDRESS", course.getAddress());
//                    itemView.getContext().startActivity(intent);
            }
        });
    }
}
