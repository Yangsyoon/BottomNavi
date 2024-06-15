package com.example.tourlist.A_Course;

import java.util.ArrayList;
import java.util.List;

class XmlParser1 {

    public List<Course> parse(String xmlData) {
        List<Course> courses = new ArrayList<>();

//        try {
//            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//            XmlPullParser parser = factory.newPullParser();
//            parser.setInput(new StringReader(xmlData));
//
//            int eventType = parser.getEventType();
//            while (eventType != XmlPullParser.END_DOCUMENT) {
//                if (eventType == XmlPullParser.START_TAG && parser.getName().equals("record")) {
//                    final String[] placeName = {""};
//                    final double[] latitude = {0.0};
//                    final double[] longitude = {0.0};
//                    final String[] address = {""};
//                    final String[] description = {""};
//                    final String[] phone = {""};
//                    while (!(eventType == XmlPullParser.END_TAG && parser.getName().equals("record"))) {
//                        if (eventType == XmlPullParser.START_TAG) {
//                            String tagName = parser.getName();
//                            switch (tagName) {
//                                case "관광지명":
//                                    placeName[0] = parser.nextText();
//                                    break;
//                                case "위도":
//                                    latitude[0] = Double.parseDouble(parser.nextText());
//                                    break;
//                                case "경도":
//                                    longitude[0] = Double.parseDouble(parser.nextText());
//                                    break;
//                                case "소재지도로명주소":
//                                    address[0] = parser.nextText();
//                                    break;
//                                case "관광지소개":
//                                    description[0] = parser.nextText();
//                                    break;
//                                case "관리기관전화번호":
//                                    phone[0] = parser.nextText();
//                                    break;
//                            }
//                        }
//                        eventType = parser.next();
//                    }
//                    courses.add(new TouristCourse(placeName[0], latitude[0], longitude[0], address[0], description[0], phone[0]));
//                }
//                eventType = parser.next();
//            }
//        } catch (XmlPullParserException | IOException e) {
//            e.printStackTrace();
//        }

        return courses;
    }
}
