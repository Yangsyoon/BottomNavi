package com.example.tourlist.A_Course;

import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tourlist.R;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    public List<com.example.tourlist.A_Course.Course> courses;



    private Location currentLocation;

    public CourseAdapter(List<com.example.tourlist.A_Course.Course> courses) {
        this.courses = courses;
    }

    public CourseAdapter(List<com.example.tourlist.A_Course.Course> courses, Location currentLocation) {
        this.courses = courses;
//        this.currentLocation = currentLocation;
//        Log.d("P", "CourseAdapter: "+currentLocation.getLatitude());
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        com.example.tourlist.A_Course.Course course = courses.get(position);
        Log.d("P", "onBindViewHolder: ");

        holder.bind(course);
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }
    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvDescription;
        TextView tvAddress;
        TextView tvdist;
        ImageView ivPicture;
        CardView courseButton;

        CourseViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvdist = itemView.findViewById(R.id.tv_dist);
            ivPicture = itemView.findViewById(R.id.iv_picture);
            courseButton = itemView.findViewById(R.id.courseButton);

        }

        void bind(final Course course) {
            tvName.setText(course.getCourse_title());
//            tvDescription.setText(course.getCourse_description());
            tvAddress.setText(course.getAddr1());

// 거리 계산
            // 거리 계산
            if (currentLocation != null && course.getMapx() != null && course.getMapy() != null) {
                Location courseLocation = new Location("");
                courseLocation.setLatitude(Double.parseDouble(course.getMapy()));
                courseLocation.setLongitude(Double.parseDouble(course.getMapx()));
                float distanceInMeters = currentLocation.distanceTo(courseLocation);
                float distanceInKm = distanceInMeters / 1000;

                if(distanceInKm >=1){
                    tvdist.setText(String.format("%.0f km", distanceInKm));

                }
                else{
                    tvdist.setText(String.format("%.1f m", distanceInKm*1000));
                }
            } else {
                tvdist.setText("Unknown");
            }


            // 예를 들어, 이미지 URL이 있을 경우 Glide 또는 Picasso를 사용하여 이미지 로드
            if (course.getFirstimage() != null && !course.getFirstimage().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(course.getFirstimage())
                        .into(ivPicture);
            }

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
}
