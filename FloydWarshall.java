package com.swt.segment.common.testComu;

/*
 * The Floyd-Warshall algorithm is used to find the shortest path between
 * all pairs of nodes in a weighted graph with either positive or negative
 * edge weights but without negative edge cycles.
 *
 * The running time of the algorithm is O(n^3), being n the number of nodes in
 * the graph.
 *
 * This implementation is self contained and has no external dependencies. It
 * does not try to be a model of good Java OOP, but a simple self contained
 * implementation of the algorithm.
 */

import java.util.Arrays;

public class FloydWarshall {

    // graph represented by an adjacency matrix
    private double[][] graph;

    private boolean negativeCycle;

    public FloydWarshall(double[][] tableau) {
        this.graph = tableau.clone();
    }

    public boolean hasNegativeCycle() {
        return this.negativeCycle;
    }

    // utility function to add edges to the adjacencyList
    public void addEdge(int from, int to, int weight) {
        graph[from][to] = weight;
    }

    // all-pairs shortest path
    public double[][] floydWarshall() {
        double[][] distances;
        int n = this.graph.length;
        distances = Arrays.copyOf(this.graph, n);

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    distances[i][j] = Math.min(distances[i][j], distances[i][k] + distances[k][j]);
                }
            }

            if (distances[k][k] < 0.0) {
                this.negativeCycle = true;
            }
        }

        return distances;
    }

}

