package com.example.tourlist.A_Course;

import android.location.Location;
import android.util.Log;

import com.naver.maps.map.util.FusedLocationSource;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

class Course_XmlParser {
    private double currentLatitude;
    private double currentLongitude;
    private FusedLocationSource locationSource;
    public List<Course> parse(InputStream inputStream) {
        List<Course> courses = new ArrayList<>();
        Log.d("Course_XmlParser", "parse: ");

        try (BufferedReader bufreader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder xmlData = new StringBuilder();
            String line;
            while ((line = bufreader.readLine()) != null) {
                xmlData.append(line);
            }

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlData.toString()));

            int eventType = parser.getEventType();
            Course course=null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && parser.getName().equals("item")) {



                    while (!(eventType == XmlPullParser.END_TAG && parser.getName().equals("item"))) {
                        if (eventType == XmlPullParser.START_TAG) {
                            String tagName = parser.getName();
                            if (tagName.equals("item")) {
                                course = new Course();
                            } else if (course != null) {
                            switch (tagName) {

                                case "areacode":
                                    course.setAreacode(parser.nextText());
                                    break;
                                case "contentid":
                                    course.setContent_id(parser.nextText());
                                    break;
                                case "title":
                                    course.setCourse_title(parser.nextText());
                                    break;
                                case "addr1":
                                    course.setAddr1(parser.nextText());
                                    break;
                                case "addr2":
                                    course.setAddr2(parser.nextText());
                                    break;
                                case "booktour":
                                    course.setBooktour(parser.nextText());
                                    break;
                                case "cat1":
                                    course.setCat1(parser.nextText());
                                    break;
                                case "cat2":
                                    course.setCat2(parser.nextText());
                                    break;
                                case "cat3":
                                    course.setCat3(parser.nextText());
                                    break;
                                case "createdtime":
                                    course.setCreatedtime(parser.nextText());
                                    break;
                                case "firstimage":
                                    course.setFirstimage(parser.nextText());
                                    break;
                                case "firstimage2":
                                    course.setFirstimage2(parser.nextText());
                                    break;
                                case "cpyrhtDivCd":
                                    course.setCpyrhtDivCd(parser.nextText());
                                    break;
                                case "mapx":
                                    course.setMapx((parser.nextText()));
                                    break;
                                case "mapy":
                                    course.setMapy((parser.nextText()));
                                    break;
                                case "mlevel":
                                    course.setMlevel((parser.nextText()));
                                    break;
                                case "modifiedtime":
                                    course.setModifiedtime(parser.nextText());
                                    break;
                                case "sigungucode":
                                    course.setSigungucode(parser.nextText());
                                    break;
                                case "tel":
                                    course.setTel(parser.nextText());
                                    break;
                                case "zipcode":
                                    course.setZipcode(parser.nextText());
                                    break;

                            }

                            }
                        }
                        eventType = parser.next();
                    }

//                    courses.add(new TouristCourse(subname, subdetailoverview));
                    courses.add(course);
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        Log.d("Course_XmlParser", "parse:2 ");

        return courses;
    }

    public Course parseDetail(InputStream inputStream) {
        Course course = new Course();
        List<TouristCoursePlace> places = new ArrayList<>();

        try (BufferedReader bufreader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder xmlData = new StringBuilder();
            String line;
            while ((line = bufreader.readLine()) != null) {
                xmlData.append(line);
            }

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlData.toString()));

            int eventType = parser.getEventType();
            TouristCoursePlace place = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String tagName = parser.getName();
                    switch (tagName) {
                        case "contentid":
                            course.setContent_id(parser.nextText());
                            break;
//                        case "title":
//                            course.setCourse_title(parser.nextText());
//                            break;
                        case "item":
                            place = new TouristCoursePlace();
                            break;
                        case "subcontentid":
                            if (place != null) {
                                place.setSubcontentid(parser.nextText());
                            }
                            break;
                        case "subname":
                            if (place != null) {
                                place.setSubname(parser.nextText());
                            }
                            break;
                        case "subdetailoverview":
                            if (place != null) {
                                place.setSubdetailoverview(parser.nextText());
                            }
                            break;
                        case "subdetailimg":
                            if (place != null) {
                                place.setSubdetailimg(parser.nextText());
                            }
                            break;
                        case "subdetailalt":
                            if (place != null) {
                                place.setSubdetailalt(parser.nextText());
                            }
                            break;

                        case "arr1":
                            if (place != null) {
                                place.setAddr1(parser.nextText());
                            }
                            break;
                    }
                } else if (eventType == XmlPullParser.END_TAG && parser.getName().equals("item")) {
                    if (place != null) {
                        places.add(place);
                    }
                }
                eventType = parser.next();
            }
            course.setPlaces(places);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return course;
    }

    public TouristCoursePlace parseCommonInfo(InputStream inputStream, TouristCoursePlace place) {
        try (BufferedReader bufreader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder xmlData = new StringBuilder();
            String line;
            while ((line = bufreader.readLine()) != null) {
                xmlData.append(line);
            }
            Log.d("p", "mmmm");
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlData.toString()));

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String tagName = parser.getName();
                    switch (tagName) {
                        case "originimgurl":
                            place.setFirstimage(parser.nextText());
                            break;
                        case "areacode":
                            place.setAreacode(parser.nextText());
                            break;
                        case "sigungucode":
                            place.setSigungucode(parser.nextText());
                            break;
                        case "addr1":
                            place.setAddr1(parser.nextText());
                            break;
                        case "mapx":
                            place.setMapx(Double.parseDouble(parser.nextText()));
                            break;
                        case "mapy":
                            place.setMapy(Double.parseDouble(parser.nextText()));
                            break;
                    }
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return place;
    }

    // Haversine 공식을 사용하여 두 지점 사이의 거리를 계산하는 메서드
    // Location 클래스를 사용하여 두 지점 사이의 거리를 계산하는 메서드
/*    private double calculateDistance(String mapx, String mapy) {
        Location currentLocation = new Location("");
        currentLocation.setLatitude(currentLatitude);
        currentLocation.setLongitude(currentLongitude);

        Location courseLocation = new Location("");
        courseLocation.setLatitude(Double.parseDouble(mapy));
        courseLocation.setLongitude(Double.parseDouble(mapx));

        return currentLocation.distanceTo(courseLocation) / 1000.0; // 킬로미터 단위의 거리
    }*/


    /*private void getCurrentLocation() {
        try {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            currentLocation = location;
                            Toast.makeText(getContext(), "Current location acquired", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }*/


}
