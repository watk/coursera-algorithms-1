import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FastCollinearPoints {
    private final LineSegment[] segments;

    public FastCollinearPoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException("points must be non-null");
        if (Arrays.stream(points).anyMatch(Objects::isNull))
            throw new IllegalArgumentException("points must not contain null");

        Point[] ps = points.clone();
        Arrays.sort(ps);
        for (int i = 1; i < ps.length; i++)
            if (ps[i].compareTo(ps[i - 1]) == 0)
                throw new IllegalArgumentException("Duplicate points not allowed");

        List<LineSegment> segmentList = new ArrayList<>();
        for (int b = 0; b < ps.length - 3; b++) {
            Point base = ps[b];
            Point[] slopeOrdered = ps.clone();
            Arrays.sort(slopeOrdered, base.slopeOrder());

            // The base point is always first in slope order, so skip over it.
            int i = 1;
            while (i < slopeOrdered.length) {
                final double currentSegmentSlope = base.slopeTo(slopeOrdered[i]);
                int j = i + 1;
                for (; j < slopeOrdered.length; j++) {
                    if (base.slopeTo(slopeOrdered[j]) != currentSegmentSlope)
                        break;
                }

                // If this segment is long enough (base + 3 others) and the base point is the
                // smallest in natural order, add the segment. By only adding segments where the
                // latter property holds we avoiding adding the same segment when we find it with
                // each other point as the base.
                if (j - i >= 3 && base.compareTo(slopeOrdered[i]) < 0)
                    segmentList.add(new LineSegment(base, slopeOrdered[j - 1]));

                i = j;
            }
        }

        segments = segmentList.toArray(new LineSegment[0]);
    }

    public LineSegment[] segments() {
        return segments.clone();
    }

    public int numberOfSegments() {
        return segments.length;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
