import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BruteCollinearPoints {
    private final LineSegment[] segments;

    public BruteCollinearPoints(Point[] points) {
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
        for (int p = 0; p < ps.length; ++p) {
            for (int q = p + 1; q < ps.length; q++) {
                for (int r = q + 1; r < ps.length; r++) {
                    for (int s = r + 1; s < ps.length; s++) {
                        double pq = ps[p].slopeTo(ps[q]);
                        if (Double.compare(pq, ps[p].slopeTo(ps[r])) == 0
                                && Double.compare(pq, ps[p].slopeTo(ps[s])) == 0) {
                            segmentList.add(new LineSegment(ps[p], ps[s]));
                        }
                    }
                }
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
