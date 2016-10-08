package com.controls;

/**
 * Created by igor on 26.04.16.
 */

import com.objects.*;

import java.util.ArrayList;

public class Convex {
  ArrayList<Point> points;
  ArrayList<Point> convex;

  public Convex(ArrayList<Point> points) {
    this.points = points;
    this.find();
  }

  private void find() {
    this.convex = new ArrayList<>();
    Point t = this.points.get(0);
    int it = -1;
    for (int i = 1; i < this.points.size(); i++) {
      if (this.points.get(i).Y() <= t.Y()) {
        if (this.points.get(i).Y() < t.Y() || this.points.get(i).X() < t.X()) {
          t = this.points.get(i);
          it = i;
        }
      }
    }
    this.convex.add(t);
    if (it != -1) this.points.remove(it);
    else this.points.remove(0);
    if (this.points.size() == 0) return;
    do {
      it = -1;
      t = this.convex.get(this.convex.size() - 1);

      double phi_min = 361;
      Segment s1, s2;
      for (int i = 0; i < this.points.size(); i++) {
        if (this.convex.size() == 1) {
          Point gx = new Point(this.convex.get(0).X() + 1.0, this.convex.get(0).Y());
          s1 = new Segment(this.convex.get(0), gx);
          s2 = new Segment(this.convex.get(0), this.points.get(i));
        } else {
          s1 = new Segment(this.convex.get(this.convex.size() - 2), this.convex.get(this.convex.size() - 1));
          s2 = new Segment(this.convex.get(this.convex.size() - 1), this.points.get(i));
        }
        Angle ao = new Angle(s1, s2);
        if (phi_min >= ao.getFi()) {
          if (phi_min > ao.getFi() || s2.abs() < (new Segment(this.convex.get(this.convex.size() - 1), this.points.get(it))).abs()) {
            phi_min = ao.getFi();
            it = i;
          }
        }
      }
      if (this.convex.size() > 1) {
        s1 = new Segment(this.convex.get(this.convex.size() - 2), this.convex.get(this.convex.size() - 1));
        s2 = new Segment(this.convex.get(this.convex.size() - 1), this.convex.get(0));
        if ((new Angle(s1, s2)).getFi() < phi_min) {
          break;
        } else {
          this.convex.add(this.points.get(it));
          this.points.remove(it);
        }
      } else {
        this.convex.add(this.points.get(it));
        this.points.remove(it);
      }
    } while (true);
  }

  public ArrayList<Point> getPoints() { return this.points; }
  public ArrayList<Point> getConvex() { return this.convex; }
}
