package com.controls;

/**
 * Created by igor on 26.04.16.
 */
import java.util.ArrayList;

import com.objects.Point;

public class Task {
  ArrayList<Point> points;
  Convex convTask;

  public Task(ArrayList<Point> points) {
    this.points = points;
    this.convTask = new Convex(this.points);
  }

  // Is  point in polygon.

  public String getResult() {
    return "Convex.";
  }
}
