package com.example.tourlist.Course;

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
    public List<TouristCourse> parse(InputStream inputStream) {
        List<TouristCourse> courses = new ArrayList<>();

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
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && parser.getName().equals("item")) {
                    String contentid = "";
                    String course_title = "";
                    String subdetailoverview = "";
                    while (!(eventType == XmlPullParser.END_TAG && parser.getName().equals("item"))) {
                        if (eventType == XmlPullParser.START_TAG) {
                            String tagName = parser.getName();
                            switch (tagName) {

//                                case "subname":
//                                    subname = parser.nextText();
//                                    break;
//                                case "subdetailoverview":
//                                    subdetailoverview = parser.nextText();
//                                    break;
                                case "contentid":
                                    contentid = parser.nextText();
                                    break;
                                case "title":
                                    course_title = parser.nextText();
                                    break;

                            }
                        }
                        eventType = parser.next();
                    }
//                    courses.add(new TouristCourse(subname, subdetailoverview));
                    courses.add(new TouristCourse(contentid, course_title));
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return courses;
    }

    public TouristCourse parseDetail(InputStream inputStream) {
        TouristCourse course = new TouristCourse();
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
                        case "title":
                            course.setCourse_title(parser.nextText());
                            break;
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

    public void parseCommonInfo(InputStream inputStream, TouristCoursePlace place) {
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
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String tagName = parser.getName();
                    switch (tagName) {
                        case "firstimage":
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

    }




}
