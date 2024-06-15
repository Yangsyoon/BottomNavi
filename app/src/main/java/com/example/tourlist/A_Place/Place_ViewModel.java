package com.example.tourlist.A_Place;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class Place_ViewModel extends AndroidViewModel {

    private MutableLiveData<List<Place>> touristPlaces;
    private MutableLiveData<Place> placeDetail;
    private TouristPlaceRepository_call_parser repository;

    public Place_ViewModel(@NonNull Application application) {
        super(application);
        repository = TouristPlaceRepository_call_parser.getInstance(application);
    }

    public LiveData<List<Place>> getTouristPlaces() {
        if (touristPlaces == null) {
            touristPlaces = new MutableLiveData<>();
            repository.loadTouristPlaces(touristPlaces);
        }
        return touristPlaces;
    }

    public LiveData<Place> getPlacedetail(String contentId) {
        placeDetail = new MutableLiveData<>();
        repository.loadCommonInfo(contentId, placeDetail);
        return placeDetail;
    }

//    public LiveData<Place> getPlacedetail() {
//        if (touristPlaces == null) {
//            touristPlaces = new MutableLiveData<>();
//            repository.loadcommoninfo(touristPlaces);
//        }
//        return touristPlaces;
//    }

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




    /*public LiveData<List<Course>> getFilteredCourses(String areaCode) {
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
    }*/
}
