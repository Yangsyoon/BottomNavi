package com.example.tourlist.A_Place;

import android.util.Log;

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

class Place_XmlParser {

    //공통정보 xml.
    public List<Place> parseCommon_return_List_Place(InputStream inputStream) {
        List<Place> places = new ArrayList<>();

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
                    String contenttypeid = "";
                    String title = "";
                    String createdtime = "";
                    String modifiedtime = "";
                    String tel = "";
                    String homepage = "";
                    String booktour = "";
                    String firstimage = "";
                    String firstimage2 = "";
                    String cpyrhtDivCd = "";
                    String areacode = "";
                    String sigungucode = "";
                    String cat1 = "";
                    String cat2 = "";
                    String cat3 = "";
                    String addr1 = "";
                    String zipcode = "";
                    double mapx = 0.0;
                    double mapy = 0.0;
                    String mlevel = "";
                    String overview = "";

                    while (!(eventType == XmlPullParser.END_TAG && parser.getName().equals("item"))) {
                        if (eventType == XmlPullParser.START_TAG) {
                            String tagName = parser.getName();
                            switch (tagName) {

                                case "contentid":
                                    contentid = parser.nextText();
                                    break;
                                case "contenttypeid":
                                    contenttypeid = parser.nextText();
                                    break;
                                case "title":
                                    title = parser.nextText();
                                    break;
                                case "createdtime":
                                    createdtime = parser.nextText();
                                    break;
                                case "modifiedtime":
                                    modifiedtime = parser.nextText();
                                    break;
                                case "tel":
                                    tel = parser.nextText();
                                    break;
                                case "homepage":
                                    homepage = parser.nextText();
                                    break;
                                case "booktour":
                                    booktour = parser.nextText();
                                    break;
                                case "firstimage":
                                    firstimage = parser.nextText();
                                    break;
                                case "firstimage2":
                                    firstimage2 = parser.nextText();
                                    break;
                                case "cpyrhtDivCd":
                                    cpyrhtDivCd = parser.nextText();
                                    break;
                                case "areacode":
                                    areacode = parser.nextText();
                                    break;
                                case "sigungucode":
                                    sigungucode = parser.nextText();
                                    break;
                                case "cat1":
                                    cat1 = parser.nextText();
                                    break;
                                case "cat2":
                                    cat2 = parser.nextText();
                                    break;
                                case "cat3":
                                    cat3 = parser.nextText();
                                    break;
                                case "addr1":
                                    addr1 = parser.nextText();
                                    break;
                                case "zipcode":
                                    zipcode = parser.nextText();
                                    break;
                                case "mapx":
                                    mapx = Double.parseDouble(parser.nextText());
                                    break;
                                case "mapy":
                                    mapy = Double.parseDouble(parser.nextText());
                                    break;
                                case "mlevel":
                                    mlevel = parser.nextText();
                                    break;
                                case "overview":
                                    overview = parser.nextText();
                                    break;


                            }
                        }
                        eventType = parser.next();
                    }
//                    courses.add(new TouristCourse(subname, subdetailoverview));
                    places.add(new Place(contentid, contenttypeid, title, createdtime, modifiedtime, tel, homepage, booktour, firstimage, firstimage2, cpyrhtDivCd, areacode, sigungucode, cat1, cat2, cat3, addr1, zipcode, mapx, mapy, mlevel, overview));
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return places;
    }

    /*public Course parseDetail(InputStream inputStream) {
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
    }*/

    public Place parseCommonInfo_return_Place(InputStream inputStream) {
        Place place = new Place();

        try (BufferedReader bufreader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder xmlData = new StringBuilder();
            String line;
            Log.d("P8", "1");
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
                        case "contentid":

                            place.setContentid(parser.nextText());
                            Log.d("P8", place.getContentid());
                            break;
                        case "contenttypeid":
                            place.setContenttypeid(parser.nextText());
                            break;
                        case "title":

                            place.setTitle(parser.nextText());
                            break;
                        case "createdtime":
                            place.setCreatedtime(parser.nextText());
                            break;
                        case "modifiedtime":
                            place.setModifiedtime(parser.nextText());
                            break;
                        case "tel":
                            place.setTel(parser.nextText());
                            break;
                        case "homepage":
                            place.setHomepage(parser.nextText());
                            break;
                        case "booktour":
                            place.setBooktour(parser.nextText());
                            break;
                        case "firstimage":
                            place.setFirstimage(parser.nextText());
                            break;
                        case "firstimage2":
                            place.setFirstimage2(parser.nextText());
                            break;
                        case "cpyrhtDivCd":
                            place.setCpyrhtDivCd(parser.nextText());
                            break;
                        case "areacode":
                            place.setAreacode(parser.nextText());
                            break;
                        case "sigungucode":
                            place.setSigungucode(parser.nextText());
                            break;
                        case "cat1":
                            place.setCat1(parser.nextText());
                            break;
                        case "cat2":
                            place.setCat2(parser.nextText());
                            break;
                        case "cat3":
                            place.setCat3(parser.nextText());
                            break;
                        case "addr1":
                            place.setAddr1(parser.nextText());
                            break;
//                        case "zipcode":
//                            place.setZipcode(parser.nextText());
//                            break;
                        case "mapx":
                            place.setMapx(Double.parseDouble(parser.nextText()));
                            break;
                        case "mapy":
                            place.setMapy(Double.parseDouble(parser.nextText()));
                            break;
//                        case "mlevel":
//                            place.setMlevel(parser.nextText());
//                            break;
                        case "overview":
                            place.setOverview(parser.nextText());
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




}
