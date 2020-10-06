package wsncontroller.commander;

import wsncontroller.dataBaseUtil.Queries;
import wsncontroller.MatrixUtil.CompareGrids;
import wsncontroller.MatrixUtil.matrixList;
import wsncontroller.node.Node;
import java.io.IOException;
import java.util.List;
import wsncontroller.activeMQ.MQHandler;
import wsncontroller.activeMQ.MatrixGrid;

public class Commander extends Thread {

    private matrixList list;
    private CompareGrids cmprG;
    final int step = 5000; // The standard step to change the frequency on a node...
    final int minFreq = 5000; // The minimum value for the frequency of a node
    final int maxFreq = 120000; // The maximum value for the frequency of a node
    public void setList() {
        this.list = matrixList.getList();
    }

    public void listenForGrids() {

        while (true) {
            //diavasma munimatos apo tis oures twn grids pou mas stelnonte
            MatrixGrid matrix = new MatrixGrid();
            MatrixGrid matrix2 = new MatrixGrid();
            String str1 = MQHandler.getHandler().getGridFusion(); //fusion matrix
            matrix.buildMatrix(str1);
            list.setFusion(matrix);


            String str2 = MQHandler.getHandler().getGrid(); //real matrix
            matrix2.buildMatrix(str2);
            list.setReal(matrix2);

            try {
                this.cmprG.compareM(list.getFusion().getMatrix(), list.getReal().getMatrix());
            } catch (IOException ex) {
                System.out.println("An IO Exception at CompareGrids.... Life goes on ...");
            }
            CheckQuadrant(this.cmprG.getMeanValue0(), this.cmprG.getFusionValue0(), 1);
            CheckQuadrant(this.cmprG.getMeanValue1(), this.cmprG.getFusionValue1(), 2);
            CheckQuadrant(this.cmprG.getMeanValue2(), this.cmprG.getFusionValue2(), 3);
            CheckQuadrant(this.cmprG.getMeanValue3(), this.cmprG.getFusionValue3(), 4);

            //arxikopoihsh twn duo grids se null
            list.initializeFusionAndReal();
        }
    }

    //Tsekarei gia to sygekrimeno tetartimorio, poia nodes epireazontai...
    public void CheckQuadrant(float meanVal, float fusionValue , int quadrant) {

        //Meiwsi tis deigmatolipsias sto tetartimorio ayto
        System.out.println("_________quadrant= " + quadrant + " meanVal= " + meanVal+ " fusionValue= " + fusionValue);
        if (meanVal < 0) {
           //meanVal = Math.abs(meanVal);
            //....Ayxisi praktika tou frequency tou node, analoga me tin mesi timi tis diaforas twn grids...px apo 5000 se 10000
            if ( fusionValue < 0.02) {
               FrequencyChange(5000, quadrant);
            } else if ( fusionValue < 0.04) {
                 FrequencyChange(10000, quadrant);
            } else if ( fusionValue < 0.06) {
                 FrequencyChange(15000, quadrant);
            } else if  ( fusionValue < 0.1) {
                FrequencyChange(20000, quadrant);
            } else if ( fusionValue < 0.5) {
                 FrequencyChange(25000, quadrant);
            } else if ( fusionValue < 0.8) {
                 FrequencyChange(30000, quadrant);
            }
        } else if (meanVal > 0) {
              if ( fusionValue < 0.02) {
               FrequencyChange(-5000, quadrant);
            } else if ( fusionValue < 0.04) {
                 FrequencyChange(-10000, quadrant);
            } else if ( fusionValue < 0.06) {
                 FrequencyChange(-15000, quadrant);
            } else if  ( fusionValue < 0.1) {
                FrequencyChange(-20000, quadrant);
            } else if ( fusionValue < 0.5) {
                 FrequencyChange(-25000, quadrant);
            } else if ( fusionValue < 0.8) {
                 FrequencyChange(-30000, quadrant);
            }
        }
    }

    public void NodesAwake(int quadrant, List<Node> list) {
        for (Node node : list) {
            if (node.isState() == false) {
                MQHandler.getHandler().SendCommand(node.awakeNodeCMD()); // Send message to node that it needs to be awaken
                wsncontroller.dataBaseUtil.Queries.UpdateState(node.getNodeID(), "Wake"); // Change the state of the node in the DB
            }
        }
    }

    public void FrequencyChange(int freq, int quadrant) {
        List<Node> list = Queries.ReturnNodesQ(quadrant);

        NodesAwake(quadrant, list); //Awake up nodes , if necessary

        if (list.isEmpty() == true) {
            System.out.println("den exei kanena Node to quadrant: " + quadrant);
        }
        for (Node node : list) {
            int dbFreqVal = wsncontroller.dataBaseUtil.Queries.getNodeFrequency(node.getNodeID());
            int checkFreq = dbFreqVal + freq; // The value of frequency to be checked, in case we exceed the limits of the frequecy of a node
            int resultFreq = 0; // The value of frequency we should give as command to a node
            int finalFreq; //The value of frequency a node will have, after the command
            if ( checkFreq < minFreq) {
                resultFreq = minFreq - dbFreqVal;
                finalFreq = minFreq;
            } else if ( checkFreq > maxFreq) {
                resultFreq = maxFreq - dbFreqVal;
                finalFreq = maxFreq;
            } else {
                resultFreq = freq;
                finalFreq = checkFreq;
            }
             String message = node.getNodeID() + " Sampling  " + resultFreq;
             MQHandler.getHandler().SendCommand(message); //Send message to node
             //System.out.print("Sending message: " + message);
             wsncontroller.dataBaseUtil.Queries.UpdateFreq(node.getNodeID(), finalFreq); //We store the value of the frequency of a node to the db...
        }
    }

    @Override
    public void run() {
        MQHandler.getHandler(); //Dimiourgei ti sundesi  gia tin lista MQ
        setList(); //Dimiourgei mia "domi" apo 2 matrices gia na ta sygrinei
        this.cmprG = CompareGrids.getHandler();
        listenForGrids();
    }
}
