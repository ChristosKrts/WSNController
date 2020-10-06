package wsncontroller.MatrixUtil;

import java.io.IOException;

public class CompareGrids {

    private int rows;
    private int cols;
    private float meanValue0;
    private float meanValue1;
    private float meanValue2;
    private float meanValue3;
    private float fusionValue0;
    private float fusionValue1;
    private float fusionValue2;
    private float fusionValue3;
    private float simulatorValue0;
    private float simulatorValue1;
    private float simulatorValue2;
    private float simulatorValue3;
    private static CompareGrids cmrG = null;

    public static CompareGrids getHandler() {
        if (cmrG == null) {
            cmrG = new CompareGrids();
        }
        return cmrG;
    }

    public void compareM(float[][] t0, float[][] t1) throws IOException {

        float[][] sum = new float[4][3];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                sum[i][j] = 0;
            }
        }

        this.rows = t0.length;
        this.cols = t0[0].length;
        int length1;
        int length2;
        float mesitimi = 0;


        if ((rows % 2 == 0) && (cols % 2 == 0)) {
            length1 = rows / 2;
            length2 = cols / 2;
        } else if ((rows % 2 == 0) && (cols % 2 == 1)) {
            length1 = rows / 2;
            length2 = cols / 2 + 1;
        } else if ((rows % 2 == 1) && (cols % 2 == 0)) {
            length1 = rows / 2 + 1;
            length2 = cols / 2;
        } else {
            length1 = rows / 2 + 1;
            length2 = cols / 2 + 1;
        }

        for (int i = 0; i < length1; i++) {
            for (int j = 0; j < length2; j++) {
                //1os ypopinakas
                sum[0][0] += t0[i][j];
                sum[0][1] += t1[i][j];
                sum[0][2]++;
               
                //2os ypopinakas
                if (j + length2 < cols) {
                    sum[1][0] += t0[i][j + length2];
                    sum[1][1] += t1[i][j + length2];
                    sum[1][2]++;
                }
                //3os ypopinakas
                if (i + length1 < rows) {
                    sum[2][0] += t0[i + length1][j];
                    sum[2][1] += t1[i + length1][j];
                    sum[2][2]++;
                    mesitimi += Math.abs(t0[i + length1][j] - t1[i + length1][j]);
                }
                //4os ypopinakas
                if ((i + length1 < rows) && (j + length2 < cols)) {
                    sum[3][0] += t0[i + length1][j + length2];
                    sum[3][1] += t1[i + length1][j + length2];
                    sum[3][2]++;
                }
            }
        }

        this.meanValue0 = calculateMeanValue(sum[0][0], sum[0][1], sum[0][2]); //1os ypopinakas
        this.meanValue1 = calculateMeanValue(sum[1][0], sum[1][1], sum[1][2]); //2os
        this.meanValue2 = calculateMeanValue(sum[2][0], sum[2][1], sum[2][2]); //3os
        this.meanValue3 = calculateMeanValue(sum[3][0], sum[3][1], sum[3][2]); //4os

        initFusionValues(sum);
        initSimulatorValues(sum);
    }

    /*
     *  Function that calculates the difference between the mean values of
     * a quardant of the fusion grid to the respectively quadrant of the simulator grid
     */
    public float calculateMeanValue(float tetart1, float tetart2, float counter) {
        float meanValue = tetart1 / counter - tetart2 / counter;
        return meanValue;
    }

    /*
     * Function to initialize the mean values of every fusion's quadrant
     */
    private void initFusionValues(float[][] sum) {
        this.fusionValue0 = sum[0][0] / sum[0][2];
        this.fusionValue1 = sum[1][0] / sum[1][2];
        this.fusionValue2 = sum[2][0] / sum[2][2];
        this.fusionValue3 = sum[3][0] / sum[3][2];
    }

    /*
     * Function to initialize the mean values of every simurator's quadrant
     */
    private void initSimulatorValues(float[][] sum) {
        this.simulatorValue0 = sum[0][1] / sum[0][2];
        this.simulatorValue1 = sum[1][1] / sum[1][2];
        this.simulatorValue2 = sum[2][1] / sum[2][2];
        this.simulatorValue3 = sum[3][1] / sum[3][2];
    }

    public float getMeanValue0() {
        return meanValue0;
    }

    public float getMeanValue1() {
        return meanValue1;
    }

    public float getMeanValue2() {
        return meanValue2;
    }

    public float getMeanValue3() {
        return meanValue3;
    }

    public float getFusionValue0() {
        return fusionValue0;
    }

    public float getFusionValue1() {
        return fusionValue1;
    }

    public float getFusionValue2() {
        return fusionValue2;
    }

    public float getFusionValue3() {
        return fusionValue3;
    }

    public float getSimulatorValue0() {
        return simulatorValue0;
    }

    public float getSimulatorValue1() {
        return simulatorValue1;
    }

    public float getSimulatorValue2() {
        return simulatorValue2;
    }

    public float getSimulatorValue3() {
        return simulatorValue3;
    }
}
