package com.example.tourlist.A_Place;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

public class Place_ViewModel extends AndroidViewModel {

    private MutableLiveData<List<Place>> touristPlaces;
    private MutableLiveData<Place> placeDetail;
    private TouristPlaceRepository_call_parser repository;

    public Place_ViewModel(@NonNull Application application) {
        super(application);
        repository = TouristPlaceRepository_call_parser.getInstance(application);
    }

///저장된 데이터 반환.
    public LiveData<List<Place>> getPlaces() {
        if (touristPlaces == null) {
            touristPlaces = new MutableLiveData<>();
        }
        return touristPlaces;
    }



/*    public void getTouristPlaces() {

        MutableLiveData<List<Place>> newTouristPlaces = new MutableLiveData<>();

        if (touristPlaces == null) {
            touristPlaces = new MutableLiveData<>();
            //밑에 해제하면 중간에 상세 뜸.
//            repository.loadTouristPlaces(newTouristPlaces);
            repository.loadTouristPlaces(touristPlaces);
//이게 첫 load.
        }

        //이거에 48시간 썼다.
        newTouristPlaces.observeForever(new Observer<List<Place>>() {
            @Override
            public void onChanged(List<Place> places) {
                List<Place> newPlaces = new ArrayList<>();
                MutableLiveData<Place> place_mut = new MutableLiveData<>();
//                Log.d("Place_ViewModel1", String.valueOf(places.size()));
                for(Place place:places){
//                    place_mut.setValue(place);
                    repository.loadCommonInfo2(place.getContentid(),place_mut);

                }
                place_mut.observeForever(place->{
                    Log.d("Place_ViewModel1", "onChanged: "+place.getOverview());

                    newPlaces.add(place);


                    if (newPlaces.size() == places.size()) {
                        touristPlaces.setValue(newPlaces);
                    }

                });
//                Log.d("Place_ViewModel2", "onChanged: "+newPlaces.get(0).getOverview());



            }

        });


        touristPlaces.setValue(touristPlaces.getValue());
    }*/

    public LiveData<List<Place>> getTouristPlaces_observe() {

        MutableLiveData<List<Place>> newTouristPlaces = new MutableLiveData<>();

        if (touristPlaces == null) {
            touristPlaces = new MutableLiveData<>();
            //밑에 해제하면 중간에 상세 뜸.
            repository.loadTouristPlaces_first(newTouristPlaces);
//            repository.loadTouristPlaces_first(touristPlaces);
//이게 첫 load.
        }

        //이거에 48시간 썼다.
        newTouristPlaces.observeForever(new Observer<List<Place>>() {
            @Override
            public void onChanged(List<Place> places) {
                List<Place> newPlaces = new ArrayList<>();
                MutableLiveData<Place> place_mut = new MutableLiveData<>();
//                Log.d("Place_ViewModel1", String.valueOf(places.size()));
                for(Place place:places){
                    place_mut.setValue(place);
                    repository.loadCommonInfo2(place.getContentid(),place_mut);

                }
                place_mut.observeForever(place->{
                    Log.d("Place_ViewModel1", "onChanged: "+place.getOverview());

                    newPlaces.add(place);


                    if (newPlaces.size() == places.size()) {
                        touristPlaces.setValue(newPlaces);
                    }

                });
                Log.d("Place_ViewModel2", "onChanged: "+newPlaces.get(0).getOverview());



            }

        });




        return touristPlaces;
    }

//

    public LiveData<Place> getPlacedetail(String contentId,String contenttypeId) {
        placeDetail = new MutableLiveData<>();
        Log.d("url5", "content"+contenttypeId);
        repository.loadCommonInfo(contentId,contenttypeId, placeDetail);

        return placeDetail;
    }

//    public LiveData<Place> getPlacedetail() {
//        if (touristPlaces == null) {
//            touristPlaces = new MutableLiveData<>();
//            repository.loadcommoninfo(touristPlaces);
//        }
//        return touristPlaces;
//    }

    /*public LiveData<List<TouristCourse>> getTouristCoursesByGps(String latitude, String longitude) {
        if (touristCourses == null) {
            touristCourses = new MutableLiveData<>();
            Log.d("CourseViewModel", "getTouristCoursesByGps: ");
            repository.loadFilteredGps(latitude, longitude,touristCourses);
        }
        return touristCourses;
    }


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
    }*/



    public void filterPlacesByAreaCode(String url) {


        repository.loadFilteredPlaces(url, touristPlaces);



    }

    public void filterPlacesByGps(String latitude, String longitude,String contenttype) {
//        Log.d("CourseViewModel", latitude);
        repository.loadFilteredGps(latitude, longitude, touristPlaces,contenttype);
    }
}
