package wsncontroller.node;

public class Node {

    private String nodeID;
    private int frequency;
    private boolean state;
    private int posX;
    private int posY;
    private int quadrant;

    public Node(String nodeID, int frequency, boolean state, int posX, int posY, int quadrant) {
        this.nodeID = nodeID;
        this.frequency = frequency;
        this.state = state;
        this.posX = posX;
        this.posY = posY;
        this.quadrant = quadrant;
    }

    //dimiourgei ena command gia na xupnisei to node
    public String awakeNodeCMD()
    {
        StringBuilder msg = new StringBuilder();
         msg.append(this.nodeID);
         msg.append(" ");
         msg.append("Energy");
         msg.append(" ");
         msg.append("Wake");
         return msg.toString();
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setQuadrant(int quadrant) {
        this.quadrant = quadrant;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getFrequency() {
        return frequency;
    }

    public String getNodeID() {
        return nodeID;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getQuadrant() {
        return quadrant;
    }

    public boolean isState() {
        return state;
    }
}
