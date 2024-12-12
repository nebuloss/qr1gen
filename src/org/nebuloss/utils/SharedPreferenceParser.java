package org.nebuloss.utils;

import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NameNotFoundException;

public class SharedPreferenceParser {

    private final Map<String, String> preferences = new HashMap<>();

    /**
     * Constructor to parse preferences from an InputStream.
     * 
     * @param inputStream The input stream of the SharedPreferences XML file.
     * @throws Exception If there is an error parsing the input stream.
     */
    public SharedPreferenceParser(InputStream inputStream) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream, "UTF-8");

        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String tagName = parser.getName();

                if ("string".equals(tagName)) {
                    String key = parser.getAttributeValue(null, "name");
                    parser.next(); // Move to the text content
                    String value = parser.getText();
                    preferences.put(key, value);
                }
                // Add support for other types (int, boolean) if needed
            }
            eventType = parser.next();
        }
    }

    /**
     * Retrieves the value associated with the given key.
     * 
     * @param name The key to look up.
     * @return The value as a String, or null if the key does not exist.
     */
    public String getString(String name) throws NameNotFoundException{
        if (!preferences.containsKey(name))
            throw new NameNotFoundException(String.format("Cannot find key %s",name));
        return preferences.get(name);
    }

}
