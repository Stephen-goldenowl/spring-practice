package com.gscores.backend.utils;

public class SlugConverter {
    public static String slugify(String str) {
        str = str.toLowerCase();

        // Xóa kí tự "/"
        str = str.replace("/", "-");

        // Thay thế dấu gạch ngang trước và sau khoảng trắng bằng khoảng trắng
        str = str.replaceAll("\\s*\\-\\s*", " ");

        // Thay thế khoảng trắng bằng dấu gạch ngang
        str = str.replaceAll("\\s+", "-");

        return str;
    }
}
