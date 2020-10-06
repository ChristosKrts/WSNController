package wsncontroller.activeMQ;

import java.util.StringTokenizer;

public class MatrixGrid implements java.io.Serializable {

    private float[][] matrix;

    public float[][] getMatrix() {
        return matrix;
    }

    public void buildMatrix(String text) {
        StringTokenizer st = new StringTokenizer(text);
        System.out.println(st.nextToken());
        int Rows = Integer.parseInt(st.nextToken());
        int Cols = Integer.parseInt(st.nextToken());
        if (Cols != 0 && Rows != 0) {
            matrix = new float[Rows][Cols];
        }
        else
        {
            System.out.println("Error on buildMatrix");
        }
        for (int i = 0; i < Rows; i++) {
            for (int j = 0; j < Cols; j++) {
                matrix[i][j] = Float.parseFloat(st.nextToken());
            }
        }
    }

    public void printGrid() {
        int rows = this.matrix.length;
        int cols = this.matrix[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(matrix[i][j] + " ");
            }
             System.out.println();
        }
    }
}
