package wsncontroller.MatrixUtil;

import wsncontroller.activeMQ.MatrixGrid;

public class matrixList {

    private MatrixGrid fusion;
    private MatrixGrid real;

    public MatrixGrid getFusion() {
        return fusion;
    }

    public MatrixGrid getReal() {
        return real;
    }
    private static matrixList mList;

    public static matrixList getList() {
        if (mList == null) {
            mList = new matrixList();
        }
        return mList;
    }

    public void setFusion(MatrixGrid f) {
        this.fusion = f;
    }

    public void setReal(MatrixGrid r) {
        this.real = r;
    }

    public void initializeFusionAndReal()
    {
        this.fusion = null;
        this.real = null;
    }
}
