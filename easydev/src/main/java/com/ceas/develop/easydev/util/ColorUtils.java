package com.ceas.develop.easydev.util;

import android.graphics.Color;

import java.util.Objects;
import java.util.Random;

public class ColorUtils {

    public static int[] intToRGB(int color) {
        return new int[]{
                Color.red(color), Color.green(color), Color.blue(color)
        };
    }

    public static int[] intToRGBA(int color) {
        return new int[]{
                Color.red(color), Color.green(color), Color.blue(color), Color.alpha(color)
        };
    }

    public static float[] intToHSL(int color) {
        float[] values = new float[3];
        Color.colorToHSV(color, values);
        return values;
    }

    public static float[] intToHSLA(int color) {
        float[] hsl = intToHSL(color);
        return new float[]{hsl[0], hsl[1], hsl[2], Color.alpha(color)};
    }

    public static int hslToInt(float... hsl) {
        return hslaToInt(255, hsl);
    }

    public static int hslaToInt(int alpha, float... hsl) {
        if (Objects.requireNonNull(hsl, "hsl==null").length != 3) {
            throw new IllegalArgumentException("hsl needs to have 3 values");
        }
        return Color.HSVToColor(alpha, hsl);
    }


    public static int rgbToInt(int... rgb) {
        if (Objects.requireNonNull(rgb, "rgb==null").length != 3) {
            throw new IllegalArgumentException("rgb needs to have 3 values");
        }
        return Color.rgb(rgb[0], rgb[1], rgb[2]);
    }

    public static int rgbaToInt(int... argb) {
        if (Objects.requireNonNull(argb, "argb==null").length != 4) {
            throw new IllegalArgumentException("argb needs to have 4 values");
        }
        return Color.argb(argb[0], argb[1], argb[2], argb[3]);
    }

    public static String intToHex(HexType hexType, int color) {
        return String.format(
                Objects.requireNonNull(hexType, "hexType==null").format,
                color
        );
    }

    public static String rgbToHex(int... rgb) {
        return intToHex(HexType.HEX, rgbToInt(rgb));
    }

    public static String rgbToHex(HexType hexType, int... rgb) {
        return intToHex(hexType, rgbToInt(rgb));
    }

    public static String argbToHex(int... argb) {
        return intToHex(HexType.HEX_ALPHA, rgbaToInt(argb));
    }

    public static String argbToHex(HexType hexType, int... argb) {
        return intToHex(hexType, rgbToInt(argb));
    }

    public static int getContrastColor(int color) {
        return getContrastColor(color, Color.WHITE, Color.BLACK);
    }

    public static int getContrastColor(int color, int colorLight, int colorDark) {
        return hasLightContrast(color) ? colorLight : colorDark;
    }

    public static boolean hasDarkContrast(int color) {
        return !hasLightContrast(color);
    }

    public static boolean hasLightContrast(int color) {
        int[] rgba = intToRGBA(color);
        double brightness = calculateBrightness(rgba);
        return rgba[3] > 80 || brightness <= 128;
    }

    public static int random(){
        return random(1, 1);
    }
    public static int random(float saturationMax, float saturationMin) {
        Random random = new Random();
        return hslToInt(
                random.nextFloat() * 360,
                random.nextFloat() * saturationMax,
                random.nextFloat() * saturationMin
        );
    }

    public static int random(int redMax, int greenMax, int blueMax){
        Random random = new Random();
        return rgbToInt(
                random.nextInt() * redMax,
                random.nextInt() * greenMax,
                random.nextInt() * blueMax
        );
    }


    private static double calculateBrightness(int[] argb) {
        return ((argb[1] * 299) + (argb[2] * 587) + (argb[3] * 114)) / 1000.0;
    }

    public enum HexType {
        HEX("#%06X"),
        HEX_ALPHA("#%08X");

        private final String format;

        HexType(String format) {
            this.format = format;
        }
    }

}
