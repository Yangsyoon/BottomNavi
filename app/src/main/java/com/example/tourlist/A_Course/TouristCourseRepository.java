package com.example.tourlist.A_Course;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class TouristCourseRepository {

    private static TouristCourseRepository instance;
    private RequestQueue requestQueue;
    private Course_XmlParser courseXmlParser;

    private TouristCourseRepository(Application application) {
        requestQueue = Volley.newRequestQueue(application);
        courseXmlParser = new Course_XmlParser();
    }

    public static synchronized TouristCourseRepository getInstance(Application application) {
        if (instance == null) {
            instance = new TouristCourseRepository(application);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

//    jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D




    //코스 제목. 아이디
    public void loadTouristCourses(final MutableLiveData<List<Course>> touristCourses) {
        try {
        // 코스 키
        //String requestUrl = "https://apis.data.go.kr/B551011/KorService1/areaBasedList1?numOfRows=30&pageNo=1&MobileOS=ETC&MobileApp=AppTest&ServiceKey=jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D&listYN=Y&arrange=O&contentTypeId=25&areaCode=&sigunguCode=&cat1=&cat2=&cat3=";
//        String requestUrl ="https://apis.data.go.kr/B551011/KorService1/areaBasedList1?numOfRows=12&pageNo=1&MobileOS=ETC&MobileApp=AppTest&ServiceKey=jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D&listYN=Y&arrange=A&contentTypeId=39&areaCode=4&sigunguCode=5&cat1=A05&cat2=A0502&cat3=A05020100";
        String requestUrl ="https://apis.data.go.kr/B551011/KorService1/";
//         홈페이지에서 받은 키
        String serviceKey = "jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D";
        Log.d("tt", "12");


        String serviceType = "areaBasedList1";
        String numOfRows = "10";
        String pageNo = "1";
        String mobileOS = "AND";
        String mobileApp = "AppTest";
        String listYN = "Y";
        String arrange = "O";
        String contentTypeId = "25";//
        String areaCode = "";
        String sigunguCode = "";
        String cat1 = "";
        String cat2 = "";
        String cat3 = "";


            StringBuilder urlBuilder = new StringBuilder(requestUrl);
            urlBuilder.append(URLEncoder.encode(serviceType, "UTF-8"));
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "="+serviceKey);
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=" + URLEncoder.encode(mobileOS, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "=" + URLEncoder.encode(mobileApp, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("listYN", "UTF-8") + "=" + URLEncoder.encode(listYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("arrange", "UTF-8") + "=" + URLEncoder.encode(arrange, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("contentTypeId", "UTF-8") + "=" + URLEncoder.encode(contentTypeId, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("areaCode", "UTF-8") + "=" + URLEncoder.encode(areaCode, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("sigunguCode", "UTF-8") + "=" + URLEncoder.encode(sigunguCode, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("cat1", "UTF-8") + "=" + URLEncoder.encode(cat1, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("cat2", "UTF-8") + "=" + URLEncoder.encode(cat2, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("cat3", "UTF-8") + "=" + URLEncoder.encode(cat3, "UTF-8"));

            requestUrl = urlBuilder.toString();


        InputStreamRequest request = new InputStreamRequest(Request.Method.GET, requestUrl,
                new Response.Listener<InputStream>() {
                    @Override
                    public void onResponse(InputStream response) {
                        List<Course> courses = courseXmlParser.parse(response);

                        for (Course course : courses) {
                            TouristCoursePlace place = new TouristCoursePlace();
//                            place
//                            loadCommonInfo1(course.getContent_id(),);
                        }
                        touristCourses.setValue(courses);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });

        requestQueue.add(request);

        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            // 예외를 처리할 다른 방법을 추가할 수 있습니다.
            Log.d("error", "UnsupportedEncodingException");
        }

//        Log.d("tt", requestUrl);
    }

    //코스 반복 정보.각 장소들 나옴(사진 없다.)
    public void loadTouristCourseDetails(final String contentId, final MutableLiveData<Course> touristCourse) {
        String requestUrl = "https://apis.data.go.kr/B551011/KorService1/detailInfo1?serviceKey=jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D&MobileOS=ETC&MobileApp=AppTest&contentId=" + contentId + "&contentTypeId=25&numOfRows=10&pageNo=1";
//        String requestUrl = "https://apis.data.go.kr/B551011/KorService1/detailInfo1?serviceKey=YOUR_API_KEY&MobileOS=ETC&MobileApp=AppTest&contentId=" + contentId + "&contentTypeId=25&numOfRows=10&pageNo=1";

        InputStreamRequest request = new InputStreamRequest(Request.Method.GET, requestUrl,
                new Response.Listener<InputStream>() {
                    @Override
                    public void onResponse(InputStream response) {
                        Course course = courseXmlParser.parseDetail(response);

                        if(course!=null){
                            Log.d("p", course.getContent_id());
                        }

                        touristCourse.setValue(course);
//                        Log.d("p", course.getCourse_title());
                        for (TouristCoursePlace place : course.getPlaces()) {
                            loadDetailImage(place.getSubcontentid(), place, touristCourse);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });

        requestQueue.add(request);
    }

    //장소 하나의 이미지. 상세이미지 요청 서비스. 공통정보 아님.
    public void loadDetailImage(final String subcontentid, final TouristCoursePlace place, final MutableLiveData<Course> touristCourse) {
//        String requestUrl = "https://apis.data.go.kr/B551011/KorService1/detailCommon1?serviceKey=YOUR_API_KEY&MobileOS=ETC&MobileApp=AppTest&contentId=" + subcontentid + "&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&addrinfoYN=Y&mapinfoYN=Y";
//        String  = "https://apis.data.go.kr/B551011/KorService1/detailImage1?serviceKey=jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D&MobileOS=ETC&MobileApp=AppTest&contentId=" + subcontentid + "&imageYN=Y&subImageYN=Y&numOfRows=10&pageNo=1";
try {
// 베이스 URL 설정
    String requestUrl = "https://apis.data.go.kr/B551011/KorService1/";


    String serviceKey = "jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D";
    String serviceType = "detailImage1";

    String mobileOS = "ETC";
    String mobileApp = "AppTest";
    String contentId = subcontentid; // subcontentid 변수를 사용

    String imageYN = "Y";
    String subImageYN = "Y";
    String numOfRows = "10";
    String pageNo = "1";

// URL 빌더 사용하여 URL 구성
    StringBuilder urlBuilder = new StringBuilder(requestUrl);
    urlBuilder.append(serviceType);
    urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);

    urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=" + URLEncoder.encode(mobileOS, "UTF-8"));
    urlBuilder.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "=" + URLEncoder.encode(mobileApp, "UTF-8"));
    urlBuilder.append("&" + URLEncoder.encode("contentId", "UTF-8") + "=" + URLEncoder.encode(contentId, "UTF-8"));
    urlBuilder.append("&" + URLEncoder.encode("imageYN", "UTF-8") + "=" + URLEncoder.encode(imageYN, "UTF-8"));
    urlBuilder.append("&" + URLEncoder.encode("subImageYN", "UTF-8") + "=" + URLEncoder.encode(subImageYN, "UTF-8"));
    urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8"));
    urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8"));

// 완성된 URL 출력
    requestUrl = urlBuilder.toString();
    Log.d("Generated URL", requestUrl);


    Log.d("j", requestUrl);
    InputStreamRequest request = new InputStreamRequest(Request.Method.GET, requestUrl,
            new Response.Listener<InputStream>() {
                @Override
                public void onResponse(InputStream response) {
                    Log.d("firstimage1", place.getSubname() + place.getFirstimage());

                    courseXmlParser.parseDetailImg(response, place);

                    Log.d("firstimage1", place.getSubname() + place.getFirstimage());
                    touristCourse.setValue(touristCourse.getValue());
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Handle error
                }
            });

    requestQueue.add(request);
}
catch(Exception e){


}
    }

//새로만든 공통요청함수 _ 코스하나를 장소로 요청
    public void loadCommonInfo1(final String subcontentid, final MutableLiveData<TouristCoursePlace> courseMutableLiveData) {
        try {
            // 베이스 URL 설정
            String requestUrl = "https://apis.data.go.kr/B551011/KorService1/detailCommon1";

            // 서비스 키
            String serviceKey = "jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D";
            String mobileOS = "ETC";
            String mobileApp = "AppTest";
            String contentTypeId = "25";
            String defaultYN = "Y";
            String firstImageYN = "Y";
            String areacodeYN = "Y";
            String catcodeYN = "Y";
            String addrinfoYN = "Y";
            String mapinfoYN = "Y";
            String overviewYN = "Y";
            String numOfRows = "10";
            String pageNo = "1";

            // URL 빌더 사용하여 URL 구성
            StringBuilder urlBuilder = new StringBuilder(requestUrl);
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
            urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=" + URLEncoder.encode(mobileOS, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "=" + URLEncoder.encode(mobileApp, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("contentId", "UTF-8") + "=" + URLEncoder.encode(subcontentid, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("contentTypeId", "UTF-8") + "=" + URLEncoder.encode(contentTypeId, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("defaultYN", "UTF-8") + "=" + URLEncoder.encode(defaultYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("firstImageYN", "UTF-8") + "=" + URLEncoder.encode(firstImageYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("areacodeYN", "UTF-8") + "=" + URLEncoder.encode(areacodeYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("catcodeYN", "UTF-8") + "=" + URLEncoder.encode(catcodeYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("addrinfoYN", "UTF-8") + "=" + URLEncoder.encode(addrinfoYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("mapinfoYN", "UTF-8") + "=" + URLEncoder.encode(mapinfoYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("overviewYN", "UTF-8") + "=" + URLEncoder.encode(overviewYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8"));

            requestUrl = urlBuilder.toString();
            Log.d("Generated URL", requestUrl);

            InputStreamRequest request = new InputStreamRequest(Request.Method.GET, requestUrl,
                    new Response.Listener<InputStream>() {
                        @Override
                        public void onResponse(InputStream response) {
                            TouristCoursePlace newPlace = courseXmlParser.parseCommonInfo(response, new TouristCoursePlace());
                            courseMutableLiveData.setValue(newPlace);
//                            Log.d("p1", newPlace.getSubname());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle error
                        }
                    });

            requestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("error", "Exception in loadCommonInfo2");
        }
    }

    //장소
    public void loadCommonInfo2(final String subcontentid, final MutableLiveData<TouristCoursePlace> placeLiveData) {
        try {
            // 베이스 URL 설정
            String requestUrl = "https://apis.data.go.kr/B551011/KorService1/detailCommon1";

            // 서비스 키
            String serviceKey = "jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D";
            String mobileOS = "ETC";
            String mobileApp = "AppTest";
            String contentTypeId = "12";
            String defaultYN = "Y";
            String firstImageYN = "Y";
            String areacodeYN = "Y";
            String catcodeYN = "Y";
            String addrinfoYN = "Y";
            String mapinfoYN = "Y";
            String overviewYN = "Y";
            String numOfRows = "10";
            String pageNo = "1";

            // URL 빌더 사용하여 URL 구성
            StringBuilder urlBuilder = new StringBuilder(requestUrl);
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
            urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=" + URLEncoder.encode(mobileOS, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "=" + URLEncoder.encode(mobileApp, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("contentId", "UTF-8") + "=" + URLEncoder.encode(subcontentid, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("contentTypeId", "UTF-8") + "=" + URLEncoder.encode(contentTypeId, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("defaultYN", "UTF-8") + "=" + URLEncoder.encode(defaultYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("firstImageYN", "UTF-8") + "=" + URLEncoder.encode(firstImageYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("areacodeYN", "UTF-8") + "=" + URLEncoder.encode(areacodeYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("catcodeYN", "UTF-8") + "=" + URLEncoder.encode(catcodeYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("addrinfoYN", "UTF-8") + "=" + URLEncoder.encode(addrinfoYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("mapinfoYN", "UTF-8") + "=" + URLEncoder.encode(mapinfoYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("overviewYN", "UTF-8") + "=" + URLEncoder.encode(overviewYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8"));

            requestUrl = urlBuilder.toString();
            Log.d("Generated URL", requestUrl);

            InputStreamRequest request = new InputStreamRequest(Request.Method.GET, requestUrl,
                    new Response.Listener<InputStream>() {
                        @Override
                        public void onResponse(InputStream response) {
                            TouristCoursePlace newPlace = courseXmlParser.parseCommonInfo(response, new TouristCoursePlace());
                            placeLiveData.setValue(newPlace);
//                            Log.d("p1", newPlace.getSubname());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle error
                        }
                    });

            requestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("error", "Exception in loadCommonInfo2");
        }
    }



    public void loadFilteredCourses(final String areaCode, final MutableLiveData<List<Course>> filteredCourses) {

        String requestUrl = "https://apis.data.go.kr/B551011/KorService1/areaBasedList1?serviceKey=jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D&numOfRows=30&pageNo=1&MobileOS=ETC&MobileApp=AppTest&listYN=Y&arrange=O&contentTypeId=25&areaCode=" + areaCode;
//        String requestUrl = "https://apis.data.go.kr/B551011/KorService1/areaBasedList1?serviceKey=YOUR_API_KEY&numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=AppTest&listYN=Y&arrange=O&contentTypeId=25&areaCode=" + areaCode;

        InputStreamRequest request = new InputStreamRequest(Request.Method.GET, requestUrl,
                new Response.Listener<InputStream>() {
                    @Override
                    public void onResponse(InputStream response) {
                        List<Course> courses = courseXmlParser.parse(response);
                        filteredCourses.setValue(courses);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });

        requestQueue.add(request);
    }

    public void loadFilteredGps(final String latitude, final String longitude, final MutableLiveData<List<Course>> filteredCourses) {
         try{

            String requestUrl = "https://apis.data.go.kr/B551011/KorService1/";
            String serviceKey = "jkZr%2BH8GxnzGB9LAB%2BDG0t%2B7xV6YZeF%2BiOqlC%2Fx3%2BdTAkBnoUim7KC6DdfyDdQ3%2FqnOgQQWhWHlHyrQGOLKobw%3D%3D";
            String serviceType = "locationBasedList1";
            String numOfRows = "10";
            String pageNo = "1";
            String mobileOS = "ETC";
            String mobileApp = "AppTest";
            String listYN = "Y";
            String arrange = "O";
             // 예시 latitude, 실제 사용 시 동적으로 설정 필요
            String radius = "20000";
            String contentTypeId = "25";

            StringBuilder urlBuilder = new StringBuilder(requestUrl);
            urlBuilder.append(URLEncoder.encode(serviceType, "UTF-8"));
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=" + URLEncoder.encode(mobileOS, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "=" + URLEncoder.encode(mobileApp, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("listYN", "UTF-8") + "=" + URLEncoder.encode(listYN, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("arrange", "UTF-8") + "=" + URLEncoder.encode(arrange, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("mapX", "UTF-8") + "=" + URLEncoder.encode(longitude, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("mapY", "UTF-8") + "=" + URLEncoder.encode(latitude, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("radius", "UTF-8") + "=" + URLEncoder.encode(radius, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("contentTypeId", "UTF-8") + "=" + URLEncoder.encode(contentTypeId, "UTF-8"));

//             urlBuilder.append(URLEncoder.encode(serviceType, "UTF-8"));
//             urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "="+serviceKey);
//             urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8"));
//             urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8"));
//             urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=" + URLEncoder.encode(mobileOS, "UTF-8"));
//             urlBuilder.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "=" + URLEncoder.encode(mobileApp, "UTF-8"));
//             urlBuilder.append("&" + URLEncoder.encode("listYN", "UTF-8") + "=" + URLEncoder.encode(listYN, "UTF-8"));
//             urlBuilder.append("&" + URLEncoder.encode("arrange", "UTF-8") + "=" + URLEncoder.encode(arrange, "UTF-8"));
//             urlBuilder.append("&" + URLEncoder.encode("contentTypeId", "UTF-8") + "=" + URLEncoder.encode(contentTypeId, "UTF-8"));
//            urlBuilder.append("&" + URLEncoder.encode("mapX", "UTF-8") + "=" + URLEncoder.encode(latitude, "UTF-8"));
//            urlBuilder.append("&" + URLEncoder.encode("mapY", "UTF-8") + "=" + URLEncoder.encode(longitude, "UTF-8"));


            requestUrl = urlBuilder.toString();

             Log.d("p", requestUrl);



            InputStreamRequest request = new InputStreamRequest(Request.Method.GET, requestUrl,
                    new Response.Listener<InputStream>() {
                        @Override
                        public void onResponse(InputStream response) {
                            Log.d("p", "123");

                        List<Course> courses = courseXmlParser.parse(response);
                        filteredCourses.setValue(courses);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle error
                        }
                    });

            requestQueue.add(request);

        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            // 예외를 처리할 다른 방법을 추가할 수 있습니다.
            Log.d("error", "UnsupportedEncodingException");
        }
    }


}
