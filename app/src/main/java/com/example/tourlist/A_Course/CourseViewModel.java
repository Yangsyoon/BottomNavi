package com.example.tourlist.A_Course;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;

public class CourseViewModel extends AndroidViewModel {

    private MutableLiveData<List<Course>> touristCourses;
    private TouristCourseRepository repository;
    private FusedLocationProviderClient fusedLocationClient;
    private double currentLatitude;
    private double currentLongitude;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        repository = TouristCourseRepository.getInstance(application);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(application);
    }

    public LiveData<List<Course>> getTouristCourses() {
        if (touristCourses == null) {
            touristCourses = new MutableLiveData<>();
            repository.loadTouristCourses(touristCourses);


        }
        return touristCourses;
    }

    public void filterCoursesByAreaCode(String areaCode) {
        repository.loadFilteredCourses(areaCode, touristCourses);
    }

    public void filterCoursesByGps(String latitude, String longitude) {
        Log.d("p", "5");
        repository.loadFilteredGps(latitude, longitude, touristCourses);
    }



/*

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




    public LiveData<List<Course>> getFilteredCourses(String areaCode) {
//        MutableLiveData<List<TouristCourse>> filteredCourses = new MutableLiveData<>();
//        repository.loadFilteredCourses(areaCode, filteredCourses);
//        return filteredCourses;
        if (touristCourses == null) {
            touristCourses = new MutableLiveData<>();
            repository.loadFilteredCourses(areaCode,touristCourses);
        }
        return touristCourses;
    }

*/



}
