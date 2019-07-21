package com.example.jas10022.parkingapp;

import java.util.Set;
import java.util.HashSet;
import java.util.Comparator;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

/**
 * KDTree is a CoordinateSet that can find nearest Coordinates.
 */
public class KDTree {

    protected Set<Coordinate> CoordinateSet = new HashSet();
    private Node CoordinateTree;
    private int depth = 0;
    /**
     * Comparator Lambda function that compares Coordinates' x and y values
     * based on the KDTree's current depth instantce variable.
     */
//    private Comparator<Coordinate> CoordinateComparator = (Coordinate curr, Coordinate goal) -> {
//        if (depth % 2 == 0) {
//            return Double.compare(curr.getLatitude(), goal.getLatitude());
//        }
//        return -1 * Double.compare(curr.getLongitude(), goal.getLongitude());
//    };



    /**
     * KDTree constructor that takes in a list of Coordinates.
     *
     * @param Coordinates
     * @source referenced piazza post 3557 for shuffling Coordinates
     */
    public KDTree(List<Coordinate> Coordinates) {
        if (Coordinates.size() < 1) {
            throw new IllegalArgumentException("Coordinates list is empty");
        }

        List<Coordinate> mutable = new ArrayList<Coordinate>(Coordinates);

        Collections.shuffle(mutable);
        for (Coordinate p : mutable) {
            if (!this.CoordinateSet.contains(p)) {
                this.CoordinateTree = insert(this.CoordinateTree, p, 0);
                this.CoordinateSet.add(p);
            }
        }


    }

    /**
     * Insert a Coordinate into a Node tree at depth d.
     *
     * @param n Parent node to insert the Coordinate into
     * @param p Coordinate that is being inserted
     * @param d depth the node is at
     * @return
     * @source Referenced the slides/video for KDTree for function signature
     */
    private Node insert(Node n, Coordinate p, int d) {
        if (n == null) {
            return new Node(p, d);
        }
        if (d % 2 == 0) {
            if (n.x > p.getLatitude()) {
                n.left = insert(n.left, p, d + 1);
            } else {
                n.right = insert(n.right, p, d + 1);
            }

        } else {
            if (n.y < p.getLongitude()) {
                n.up = insert(n.up, p, d + 1);
            } else {
                n.down = insert(n.down, p, d + 1);
            }

        }

        return n;
    }

    /**
     * Find the nearest Coordinate to the given input.
     *
     * @param x x coordinate of the Coordinate
     * @param y y coordinate of the Coordinate
     * @return nearest Coordinate in the KD Tree to the given Coordinate.
     */
    public Coordinate nearest(double x, double y) {
        Coordinate goalCoordinate = new Coordinate(x, y);
        Coordinate best = nearestHelper(CoordinateTree, goalCoordinate, CoordinateTree).Coordinate;
        this.depth = 0;
        return best;
    }

    public Coordinate nearest(Coordinate c) {
        Coordinate goalCoordinate = c;
        Coordinate best = nearestHelper(CoordinateTree, goalCoordinate, CoordinateTree).Coordinate;
        this.depth = 0;
        return best;
    }

    /**
     * @param n    Parent node to start at
     * @param goal goal Coordinate we are trying to find
     * @param best current best node
     * @return return the nearest Node to the goal Coordinate
     * @source Referenced the slides/video for KDTree for
     * function signature, and body
     */
    private Node nearestHelper(Node n, Coordinate goal, Node best) {
        if (n == null) {
            return best;
        }
        if (n.distance(goal) < best.distance(goal)) {
            best = n;
        }
        Node goodSide;
        Node badSide;
        if (CoordinateComparator.compare(this.depth, n.Coordinate, goal) == 1) {
            if (depth % 2 == 0) {
                goodSide = n.left;
                badSide = n.right;
            } else {
                goodSide = n.up;
                badSide = n.down;
            }
        } else {
            if (depth % 2 == 0) {
                goodSide = n.right;
                badSide = n.left;
            } else {
                goodSide = n.down;
                badSide = n.up;
            }
        }
        this.depth += 1;

        best = nearestHelper(goodSide, goal, best);

        if (badSide != null) {
            double bestDistance = best.distance(goal);
            double xDelta = Math.pow(goal.getLatitude() - n.x, 2);
            double yDelta = Math.pow(goal.getLongitude() - n.y, 2);

            if (depth % 2 == 1) {
                if (xDelta < bestDistance) {
                    best = nearestHelper(badSide, goal, best);
                }
            } else {
                if (yDelta < bestDistance) {
                    best = nearestHelper(badSide, goal, best);
                }
            }
        }


        this.depth -= 1;

        return best;
    }

    /**
     * Node private class that holds the information about the Coordinate.
     * Note: There should only be Nodes in Left and Right OR Up and DOwn
     */
    private class Node {

        protected double x;
        protected double y;
        protected Coordinate Coordinate;
        protected Node left;
        protected Node right;
        protected Node up;
        protected Node down;
        protected int d = 0;

        /**
         * Initialize a Node with a Coordinate.
         *
         * @param p     Coordinate that is held in the Node
         * @param d Used mainly for debugging and
         * records what depth the Node is at.
         */
        Node(Coordinate p, int d) {
            this.Coordinate = p;
            this.x = p.getLatitude();
            this.y = p.getLongitude();
            this.d = d;
        }

        /**
         * Allows calculation of distance from a Node to another Coordinate.
         *
         * @param p The Coordinate to compare the distance with.
         * @return Return the L2 norm squared from Node to Coordinate p
         */
        public double distance(Coordinate p) {
            return Coordinate.L2Norm(this.Coordinate, p);
        }

        /**
         * toString method used for debugging.
         *
         * @return String that represents the Coordinate in the Node
         */
        @Override
        public String toString() {
            return "Node | x: " + this.x + " y: " + this.y;
        }
    }
}