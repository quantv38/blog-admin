package com.doomdev.admin_blog.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.Normalizer;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StrUtils {
    public static String capitalizeWord(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        char firstChar = Character.toUpperCase(input.charAt(0));

        return firstChar + input.substring(1);
    }

    public static <T> String join(List<T> objects, String delimiter) {
        return objects.stream().map(Object::toString).collect(Collectors.joining(delimiter));
    }

    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        temp = temp.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        temp = temp.replaceAll("đ", "d").replaceAll("Đ", "D");

        temp = temp.replaceAll("[^a-zA-Z0-9\\s]", "");

        return temp;
    }
}
