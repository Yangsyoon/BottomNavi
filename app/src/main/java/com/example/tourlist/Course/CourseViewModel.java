package com.example.tourlist.Course;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

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

//    public LiveData<List<TouristCourse>> getTouristCoursesByGps(String latitude, String longitude) {
//        if (touristCourses == null) {
//            touristCourses = new MutableLiveData<>();
//            Log.d("CourseViewModel", "getTouristCoursesByGps: ");
//            repository.loadFilteredGps(latitude, longitude,touristCourses);
//        }
//        return touristCourses;
//    }


//    public LiveData<List<TouristCourse>> getGpsCourses() {
//        if (touristCourses == null) {
//            touristCourses = new MutableLiveData<>();
//            repository.loadTouristCourses(touristCourses);
//        }
//        return touristCourses;
//    }




    public LiveData<List<TouristCourse>> getFilteredCourses(String areaCode) {
//        MutableLiveData<List<TouristCourse>> filteredCourses = new MutableLiveData<>();
//        repository.loadFilteredCourses(areaCode, filteredCourses);
//        return filteredCourses;
        if (touristCourses == null) {
            touristCourses = new MutableLiveData<>();
            repository.loadFilteredCourses(areaCode,touristCourses);
        }
        return touristCourses;
    }



    public void filterCoursesByAreaCode(String areaCode) {
        repository.loadFilteredCourses(areaCode, touristCourses);
    }

    public void filterCoursesByGps(String latitude, String longitude) {
//        Log.d("CourseViewModel", latitude);
        repository.loadFilteredGps(latitude, longitude, touristCourses);
    }
}
