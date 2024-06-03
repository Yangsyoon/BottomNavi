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

class XmlParser2 {
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
                    String subname = "";
                    String subdetailoverview = "";
                    while (!(eventType == XmlPullParser.END_TAG && parser.getName().equals("item"))) {
                        if (eventType == XmlPullParser.START_TAG) {
                            String tagName = parser.getName();
                            switch (tagName) {
                                case "subname":
                                    subname = parser.nextText();
                                    break;
                                case "subdetailoverview":
                                    subdetailoverview = parser.nextText();
                                    break;
                            }
                        }
                        eventType = parser.next();
                    }
//                    courses.add(new TouristCourse(subname, subdetailoverview));
                    courses.add(new TouristCourse(subname));
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return courses;
    }
}
