package com.meplus.fancy.utils;

/**
 * Created by dandanba on 3/2/16.
 */
public class ISBNUtils {

    // http://mtnt2008.iteye.com/blog/666311
    public static String getISBN13(String isbn) {
        if (isbn.length() != 10) {
            return isbn;
        }
        isbn = isbn.substring(0, isbn.length() - 1);
        isbn = "978" + isbn;
        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        for (int i = 0; i < isbn.length(); i++) {
            int x = Integer.parseInt(isbn.substring(i, i + 1));
            if (i % 2 == 0) {
                a += x;
            } else {
                b += x;
            }
        }
        c = a + 3 * b;
        d = 10 - c % 10;
        isbn = isbn + (d % 10);
        return isbn;
    }
}
