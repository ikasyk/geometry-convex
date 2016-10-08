package com.controls;

/**
 * Created by igor on 26.04.16.
 */

import java.io.IOException;
import java.util.ArrayList;

import com.objects.Segment;

import com.objects.Point;

public class Intersection {
  // Intersection result:
  // 1 - intersection was found as a point
  // 2 - intersection was found as a segment
  // 0 - segments haven't intersection
  int result;

  Point a, b;

  public Intersection(Segment s1, Segment s2) {
    double d_x = det(s1.getA(), s1.getB(), s2.getA(), s2.getB());
    double d_x1 = det(-s1.getC(), s1.getB(), -s2.getC(), s2.getB());
    double d_x2 = det(s1.getA(), -s1.getC(), s2.getA(), -s2.getC());

    if (Double.compare(d_x, 0) == 0) {
      if (Double.compare(d_x1,  d_x2) == 0) {
        ArrayList <Point> IntersectArray = new ArrayList<>();
        if (s1.inLimit(s2.getP1())) IntersectArray.add(s2.getP1());
        if (s1.inLimit(s2.getP2())) IntersectArray.add(s2.getP2());
        if (s2.inLimit(s1.getP1())) IntersectArray.add(s1.getP1());
        if (s2.inLimit(s1.getP2())) IntersectArray.add(s1.getP2());
        int s = IntersectArray.size();
        if (s == 4) s = 2;
        if (s == 3) {
          if (IntersectArray.get(0).equals(IntersectArray.get(1))) IntersectArray.remove(0);
          else if (IntersectArray.get(1).equals(IntersectArray.get(2))) IntersectArray.remove(1);
          else IntersectArray.remove(2);
          s = 2;
        }
        if (s > 0) {
          a = IntersectArray.get(0);
          if (s == 2 && !IntersectArray.get(0).equals(IntersectArray.get(1))) {
            b = IntersectArray.get(1);
          } else if (s == 2) {
            IntersectArray.remove(--s);
          }
        }
        result = (s == 1 || s == 2) ? s : 0;
      } else {
        result = 0;
      }
    } else {
      a = new Point(d_x1 / d_x, d_x2 / d_x);
      result = s1.inLimit(a) && s2.inLimit(a) ? 1 : 0;
    }
  }

  private double det(double a, double b, double c, double d) {
    return a*d - b*c;
  }

  public String getResult() {
    switch (result) {
      case 1:
        return "Point" + a;
      case 2:
        return "Segment(" + a + ", " + b + ")";
      default:
        return "None";
    }
  }

  public Point getPointA() { return a; }

  public int getResultCode() {
    return result;
  }
}
