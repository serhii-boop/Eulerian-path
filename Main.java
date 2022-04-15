package nulp;

import java.util.Arrays;

public class Main {

    static int NUM_OF_VERTEX = ReadFile.vertex();
    static int K = 100000;

    public static void main(String[] args) {
        int[][] array = ReadFile.matrix();
        int[][] connectivityMatrix = new int[NUM_OF_VERTEX][NUM_OF_VERTEX];
        int[] vertexWeight = new int[NUM_OF_VERTEX];

        System.out.println("Number of vertex: " + NUM_OF_VERTEX);
        System.out.println("Matrix of weight: ");
        ReadFile.printMatrix(array);
        getConnectivityMatrix(connectivityMatrix, array);
        getVertexDegree(connectivityMatrix, vertexWeight, array);
    }

    static int sumEdgesVertexes(int[][] connectivityMatrix) {
        int sum = 0;
        for (int[] row : connectivityMatrix) {
            sum += Arrays.stream(row).sum();
        }
        return sum / 2;
    }

    static void euler(int[][] array, int[][] connectivityMatrixNew) {
        int[][] connectivityMatrix = new int[NUM_OF_VERTEX][NUM_OF_VERTEX];
        for (int i = 0; i < NUM_OF_VERTEX; i++) {
            System.arraycopy(connectivityMatrixNew[i], 0, connectivityMatrix[i], 0, NUM_OF_VERTEX);
        }
        int sumEdgesVertexes = sumEdgesVertexes(connectivityMatrix);
        int[] p = new int[K + 1];
        int p1 = 0;
        int p2 = sumEdgesVertexes + 1;
        int weight = 0;

        p[p1] = 0;

        while (p1 >= 0) {
            int i, v = p[p1];
            for (i = 0; i < NUM_OF_VERTEX; ) {
                if (connectivityMatrix[v][i] != 0) {
                    connectivityMatrix[v][i] = connectivityMatrix[v][i] - 1;
                    connectivityMatrix[i][v] = connectivityMatrix[i][v] - 1;
                    p[++p1] = i;
                    v = i;
                    i = 0;
                } else {
                    i++;
                }
            }
            p[--p2] = p[p1--];
        }

        if (p2 > 0) {
            System.out.println("The graph is not Euler");
        } else {
            System.out.println(" Edge : Weight");
            for (int i = 0; i < sumEdgesVertexes; i++) {
                System.out.println(" " + p[i] + " - " + p[i + 1] + " : " + array[p[i]][p[i + 1]]);
                weight += array[p[i]][p[i + 1]];
            }
            System.out.println("\n Total path weight: " + weight);
        }

    }

    static void addEdges(int[][] connectivityMatrix, int[] vertexDegree, int[][] array) {
        for (int i = 0; i < NUM_OF_VERTEX; i++) {
            if (vertexDegree[i] % 2 != 0) {
                for (int j = 0; j < NUM_OF_VERTEX; j++) {
                    if (vertexDegree[j] % 2 != 0 && connectivityMatrix[i][j] != 0) {
                        connectivityMatrix[i][j] = connectivityMatrix[i][j] + 1;
                    }
                }
            }
        }

        System.out.println("\nChanged connectivity matrix: ");
        for (int i = 0; i < NUM_OF_VERTEX; i++) {
            for (int j = 0; j < NUM_OF_VERTEX; j++) {
                System.out.print(connectivityMatrix[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println();
        euler(array, connectivityMatrix);
    }

    static void getConnectivityMatrix(int[][] connectivityMatrix, int[][] array) {
        System.out.println("\nconnectivity matrix: ");
        for (int i = 0; i < NUM_OF_VERTEX; i++) {
            for (int j = 0; j < NUM_OF_VERTEX; j++) {
                if (array[i][j] > 0) {
                    connectivityMatrix[i][j] = 1;
                } else {
                    connectivityMatrix[i][j] = array[i][j];
                }
                System.out.print(connectivityMatrix[i][j] + "\t");
            }
            System.out.println();
        }
    }


    static void ifEuler(int[][] connectivityMatrix, int[] vertexDegree, int[][] array) {
        for (int i = 0; i < NUM_OF_VERTEX; i++) {
            if (vertexDegree[i] != 0) {
                addEdges(connectivityMatrix, vertexDegree, array);
                break;
            } else {
                euler(array, connectivityMatrix);
            }
        }
    }

    static void getVertexDegree(int[][] connectivityMatrix, int[] vertexDegree, int[][] array) {
        System.out.println();
        for (int i = 0; i < NUM_OF_VERTEX; i++) {
            for (int j = 0; j < NUM_OF_VERTEX; j++) {
                vertexDegree[i] += connectivityMatrix[i][j];
            }
            if (vertexDegree[i] % 2 != 0) {
                System.out.println("Vertex " + i + " - odd");
            }
        }

        ifEuler(connectivityMatrix, vertexDegree, array);
    }
}
