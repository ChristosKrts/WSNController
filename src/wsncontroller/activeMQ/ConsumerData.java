package wsncontroller.activeMQ;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import java.io.File;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import org.xml.sax.SAXException;
import wsncontroller.dataBaseUtil.Queries;

public class ConsumerData extends Thread {

    public void msgReceirer() {
        while (true) {
            String str1 = MQHandler.getHandler().getData();

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder;
            try {
                documentBuilder = documentBuilderFactory.newDocumentBuilder();
                try {
                    Document document = null;
                    PrintWriter writer = new PrintWriter("the-file-name.txt", "UTF-8");
                    writer.println(str1);
                    writer.close();

                    File file = new File("the-file-name.txt");
                    document = documentBuilder.parse(file);
                    String value = document.getElementsByTagName("swe:values").item(0).getTextContent();
                     //System.out.println("Received from queue: Data Queue:  " + value);
                    this.parceMesurementsValues(value);

                } catch (SAXException ex) {
                    Logger.getLogger(ConsumerData.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ConsumerData.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(ConsumerData.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @Override
    public void run() {
        msgReceirer();
    }

    private void parceMesurementsValues(String str) {
        StringTokenizer st = new StringTokenizer(str, " ,_;");

        st.nextToken();
        String timeStamp = st.nextToken();
        st.nextToken();
        String nodeID = st.nextToken() + "_" + (st.nextToken()).toLowerCase(); //we want the format of the ID be like "SunSPOT_101a" , NOT "SunSPOT_101A"...
        st.nextToken();
        String temperature = st.nextToken();
        String light = st.nextToken();
        //System.out.println("1:"+timeStamp + " 2:" + nodeID+" "+temperature+" "+light);

        //Send mesurements to database
        Queries.InsertData(nodeID, timeStamp, temperature, light);
    }
}
