package ru.mipt.bit.platformer.abstractions.models;

public class HealthBarModel {
    private static boolean visible = false;
    public static boolean getVisible() {
        return visible;
    }
    public static void switchVisible() {
        visible = !visible;
    }
}
