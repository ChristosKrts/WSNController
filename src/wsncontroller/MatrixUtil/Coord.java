package wsncontroller.MatrixUtil;

public final class Coord {

    private String id;
    private int x;
    private int y;
    private int quadrant;

    public Coord(String id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.findquadrant();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getQuadrant() {
        return quadrant;
    }
    
    public int findquadrant()
    {
        if(x <= 101 &&  y <= 101)
        {
            quadrant = 1;
        }
        else if(x <= 101 &&  y > 101)
        {
            quadrant = 2;
        }
        else if(x > 101 &&  y <= 101)
        {
            quadrant = 3;
        }
        else
        {
            quadrant = 4;
        }

        return quadrant;
    }
}
