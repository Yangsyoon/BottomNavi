package com.example.tourlist.Course;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class PlaceViewModel extends AndroidViewModel {

    private MutableLiveData<List<TouristCoursePlace>> places;
    private TouristCourseRepository repository;

    public PlaceViewModel(@NonNull Application application) {
        super(application);
        repository = TouristCourseRepository.getInstance(application);
    }

    public LiveData<List<TouristCoursePlace>> getPlaces() {
        if (places == null) {
            places = new MutableLiveData<>();
        }
        return places;
    }

    public void loadTouristCoursePlaces(String contentId) {
        MutableLiveData<TouristCourse> touristCourse = new MutableLiveData<>();
        repository.loadTouristCourseDetails(contentId, touristCourse);

        touristCourse.observeForever(course -> {
            if (course != null) {
                places.setValue(course.getPlaces());
            }
        });
    }
}
