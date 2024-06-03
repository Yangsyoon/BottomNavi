package com.example.tourlist.Course;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;



import java.util.List;
//

public class CourseViewModel extends AndroidViewModel {

    private MutableLiveData<List<TouristCourse>> touristCourses;

    private TouristCourseRepository repository;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        repository = TouristCourseRepository.getInstance(application);


    }

    public LiveData<List<TouristCourse>> getTouristCourses() {
        if (touristCourses == null) {
            touristCourses = new MutableLiveData<>();

            repository.loadTouristCourses(touristCourses);
        }
        return touristCourses;
    }


}
