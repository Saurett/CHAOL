package com.indev.chaol.utils;

/**
 * Created by jvier on 15/06/2017.
 */

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

/**
 * Span to draw a dot centered under a section of text
 */
class MyDotSpan implements LineBackgroundSpan {

    /**
     * Default radius used
     */
    public static final float DEFAULT_RADIUS = 3;

    /**
     * Default radius used
     */
    public static final float DEFAULT_MARGIN_BETWEEN_DOTS = 3;

    private final float radius;
    private final float marginBetweenDots;
    private final int[] colors;

    /**
     * Create a span to draw a dot using default radius and color
     *
     * @see #MyDotSpan(float, float, int[])
     * @see #DEFAULT_RADIUS
     */
    public MyDotSpan() {
        this.radius = DEFAULT_RADIUS;
        this.marginBetweenDots = DEFAULT_MARGIN_BETWEEN_DOTS;
        this.colors = new int[]{0};
    }

    /**
     * Create a span to draw a dot using a specified color
     *
     * @param colors color of the dot
     * @see #MyDotSpan(float, float, int[])
     * @see #DEFAULT_RADIUS
     */
    public MyDotSpan(int... colors) {
        this.radius = DEFAULT_RADIUS;
        this.marginBetweenDots = DEFAULT_MARGIN_BETWEEN_DOTS;
        this.colors = colors;
    }

    /**
     * Create a span to draw a dot using a specified radius
     *
     * @param radius radius for the dot
     * @see #MyDotSpan(float, float, int[])
     */
    public MyDotSpan(float radius) {
        this.radius = radius;
        this.marginBetweenDots = DEFAULT_MARGIN_BETWEEN_DOTS;
        this.colors = new int[]{0};
    }

    /**
     * Create a span to draw a dot using a specified radius and color
     *
     * @param radius radius for the dots
     * @param colors colors of the dots
     */
    public MyDotSpan(float radius, float marginBetweenDots, int[] colors) {
        this.radius = radius;
        this.marginBetweenDots = marginBetweenDots;
        this.colors = colors;
    }

    @Override
    public void drawBackground(
            Canvas canvas, Paint paint,
            int left, int right, int top, int baseline, int bottom,
            CharSequence charSequence,
            int start, int end, int lineNum
    ) {
        int oldColor = paint.getColor();
        final float totalWidth = (colors.length) * (2 * radius) + ((colors.length - 1) * marginBetweenDots);
        final float centerPosition = (left + right) / 2;
        float startLeft = centerPosition - (totalWidth / 2);
        for (int color : colors) {
            if (color != 0) {
                paint.setColor(color);
            }
            canvas.drawCircle(startLeft + radius, bottom + radius, radius, paint);
            startLeft = startLeft + (radius * 2) + marginBetweenDots;
        }
        paint.setColor(oldColor);
    }
}