package com.objects;

/**
 * Created by igor on 26.04.16.
 */


import java.io.IOException;
import java.util.ArrayList;

public class Polygon {
  private ArrayList<Point> points;

  public Polygon(ArrayList<Point> points) throws IOException {
    if (points.size() < 3) throw new IOException("Polygon must consists more than 3 points.");
    else
      this.points = points;
  }

  public Point get(int i) {
    if (i > points.size()) return null;
    return points.get(i);
  }

  public int size() {
    return points.size();
  }
}
