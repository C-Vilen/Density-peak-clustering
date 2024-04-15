import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileIO {
    private ArrayList<Point> dataset;

    public void inputData(String fileName){
        this.dataset = new ArrayList<>();
        String fileLocation = "datasets/" + fileName;
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileLocation));
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                String[] stringCoordinates = inputLine.split("\s");
                double[] doubleCoordinates = new double[stringCoordinates.length];
                for (int i = 0; i < doubleCoordinates.length; i++) {
                    doubleCoordinates[i] = Double.parseDouble(stringCoordinates[i]);
                }
                Point point = new Point(doubleCoordinates);
                dataset.add(point);

            }
            br.close();
        } catch (IOException e) {
            System.err.println("There was an error trying to input the dataset. Ensure the fileName is corret.");            
            e.printStackTrace();
        }
    }

    public ArrayList<Point> getData() {
        return dataset;
    }
}
