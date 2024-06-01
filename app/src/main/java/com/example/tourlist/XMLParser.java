package com.example.tourlist;

import com.example.tourlist.Tourist_Detail_Activity.Detail_files.TouristAttraction;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {
    public List<TouristAttraction> parse(InputStream inputStream) {
        List<TouristAttraction> attractions = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);

            NodeList nodeList = document.getElementsByTagName("record");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String name = element.getElementsByTagName("관광지명").item(0).getTextContent();
                    String address = element.getElementsByTagName("소재지도로명주소").item(0).getTextContent();

                    attractions.add(new TouristAttraction(name, address));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return attractions;
    }
}

