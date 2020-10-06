package wsncontroller.MatrixUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Coordinates {

    private Map<String, Coord> coordMap;
     private static Coordinates coordHandler;
    //Produce random values for the quandrant and the coords

      public static Coordinates getHandler() {
        if (coordHandler == null) {
            coordHandler = new Coordinates();
            return coordHandler;
        } else {
            return coordHandler;
        }
    }

    public Coordinates() {
        coordMap = new HashMap<String, Coord>();
        this.readFile();

    }

    public Coord getCoordByID(String nodeID)
    {
        
          Coord coord = null;
          if (coordMap.containsKey(nodeID)) {
              coord = coordMap.get(nodeID);
          }
          return coord;
    }

    public void readFile() {
        String path = "";
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("configuration.properties"));
            path = prop.getProperty("coord_path");
        } catch (IOException ex) {
            Logger.getLogger(Coordinates.class.getName()).log(Level.SEVERE, null, ex);
        }

        InputStream in;
        try {
            in = new FileInputStream(new File(path));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    StringTokenizer st = new StringTokenizer(line);
                    while (st.hasMoreTokens()) {
                        String id = st.nextToken();
                        String xx = st.nextToken();
                        String yy = st.nextToken();

                        Coord coord = new Coord(id, Integer.parseInt(xx), Integer.parseInt(yy));
                        coordMap.put(id, coord);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Coordinates.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Coordinates.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
