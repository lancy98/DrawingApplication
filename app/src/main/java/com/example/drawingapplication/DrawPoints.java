package com.example.drawingapplication;

import android.graphics.Canvas;
import android.graphics.PointF;

import java.util.ArrayList;

public class DrawPoints {

    private ArrayList<DrawPoint> points = new ArrayList<DrawPoint>();
    private ArrayList<DrawPoint> undonePoints = new ArrayList<DrawPoint>();

    public CAPACITY undo() {
        if (points.size() == 0) {
            return CAPACITY.EMPTY;
        } else if (undonePoints.size() == 10) {
            return CAPACITY.FULL;
        }


        int lastElementIndex = points.size() -1;
        DrawPoint lastPoint = points.get(lastElementIndex);
        points.remove(lastPoint);
        undonePoints.add(lastPoint);
        return CAPACITY.SPACE_LEFT;
    }

    public boolean redo() {
        if (undonePoints.size() == 0) {
            return false;
        }

        int lastUndonePointIndex = undonePoints.size() -1;
        DrawPoint lastUndonePoint = undonePoints.get(lastUndonePointIndex);
        undonePoints.remove(lastUndonePoint);
        points.add(lastUndonePoint);
        return true;
    }

    public void draw(Canvas canvas) {
        for (DrawPoint point: points) {
            point.draw(canvas);
        }
    }

    public void addPoint(PointF point, int strokeWidth) {
        DrawPoint drawPoint = new DrawPoint(point, strokeWidth);
        points.add(drawPoint);
    }

    public void addPoint(PointF point, int strokeWidth, CustomColor customColor) {
        DrawPoint drawPoint = new DrawPoint(point, strokeWidth, customColor.paint);
        points.add(drawPoint);
    }

    public void addPoints(DrawPoints inputPoints) {
        points.addAll(inputPoints.points);
    }

    public void useColorForAllPoints(CustomColor customColor) {
        for (DrawPoint point : points) {
            point.paint = customColor.paint;
        }
    }

    public void clearStoredPoints() {
        points.clear();
    }

    public double strokeMaxDistance() {
        if (points.size() == 0) {
            return 0;
        }

        DrawPoint firstPoint = points.get(0);
        DrawPoint lastPoint = points.get(points.size() -1);
        return firstPoint.distanceToPoint(lastPoint);
    }
}
