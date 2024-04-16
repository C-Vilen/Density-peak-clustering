import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class FileIO {
    private String currentDataset;
    private ArrayList<Point> dataset;

    public void inputData(String fileName){
        currentDataset = fileName;
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

    /**
     * outputs all data points with their coordinates and cluster label from the given dataset to the folder "dataoutput"
     */
    public void outputData() {
        if(dataset == null) throw new IllegalArgumentException("The dataset can be null before outputting the file.");
        Path path = Paths.get("dataoutput/").resolve(currentDataset);
        try{
            Files.deleteIfExists(path);
            Files.createFile(path);
        } catch (IOException e){
            e.printStackTrace();
        }

        try(BufferedWriter writer = Files.newBufferedWriter(path)){
            for (int i = 1; i <= dataset.get(0).getCoordinates().length; i++) {
                writer.write("c" + i + " ");
            }
            writer.write("clusterLabel");
            writer.newLine();
            for (int i = 0; i < dataset.size(); i++) {
                Point currentPoint = dataset.get(i);
                double[] coordinates = currentPoint.getCoordinates();
                for (int j = 0; j < coordinates.length; j++) {
                    writer.write( coordinates[j] + " ");
                }
                writer.write(currentPoint.getClusterLabel());
                writer.newLine();
            }

        } catch(IOException e) {
            System.err.println("There was an error trying to output the dataset.");
            e.printStackTrace();
        }
    }
}
