package com.cvs.opencv;

/**
 * Created by gregor.horvat on 31. 01. 2017.
 */
public class Utils {

    public static String toHtmlUrl(String url, String displayLabel) {
        return String.format("<a href='%s'>%s</a>",url, displayLabel);
    }
}
