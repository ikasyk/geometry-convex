package com.controls;

/**
 * Created by igor on 26.04.16.
 */
import com.objects.Segment;

public class Angle {
  double fi;

  public Angle(Segment s1, Segment s2) {
    fi = s2.angleOX() - s1.angleOX();
    if (Double.compare(fi, 0.0) < 0)
      fi += 360.0;
  }

  public String getResult() {
    return Double.toString(Math.round(fi * 10.) / 10.) + "deg";
  }

  public double getFi() {
    return fi;
  }
}
