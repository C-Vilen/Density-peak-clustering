import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DPC {
    private ArrayList<Point> dataset;
    private Map<String, Double> distanceBetweenPairsMap;
    private Double maxDistance = Double.MAX_VALUE;
    private Double minDistance = Double.MIN_VALUE; 
    
    public DPC(ArrayList<Point> dataset){
        this.dataset = dataset;    
    }

    /**
     * Populates the map distanceBetweenPairsMap and finds the minimum and maximum distance between all pairs.
     * @throws Exception if the dataset is not initialised.
     */
    public void calculateDistanceBetweenPairs() throws Exception{
        if (dataset == null) throw new IllegalArgumentException("The dataset must be initialised before calculating the distance between pairs");
        
        distanceBetweenPairsMap = new HashMap<>();
        for (int i = 0; i < dataset.size(); i++) {
            for (int j = i+1; j < dataset.size(); j++) {
                String pair = i + " " + j;
                double distance = findDistance(dataset.get(i), dataset.get(j));
                distanceBetweenPairsMap.put(pair, distance);

                if(distance < minDistance) minDistance = distance;
                if(distance > maxDistance) maxDistance = distance;
            }    
        }
    }

    /**
     * Finds the distance between two points in D-dimensional space.
     * @param i a point in the dataset.
     * @param j other point in the dataset.
     * @return the distance between the given points
     * @throws Exception if the coordinates of I and J are not an equal size.
     */
    private double findDistance(Point i, Point j) throws Exception{
        double[] iCoordinates = i.getCoordinates();
        double[] jCoordinates = j.getCoordinates();
        if(iCoordinates.length != jCoordinates.length) throw new Exception(i + " and " + j + " does not have an equal size of coordinates. Check the input data and try again.");

        double distance = 0;
        for (int k = 0; k < iCoordinates.length; k++) {
            distance += Math.pow(jCoordinates[k] - iCoordinates[k], 2);
        }
        return distance;
    }

    /**
     * Finds the cutoff distance which is used to calculate the rho of each point that is closer than that. 
     * @return cutoff distance (dCut)
     */
    public double findDCut() {
        double minTmp = minDistance;
        double maxTmp = maxDistance;
        double dCut = (minTmp + maxTmp) * 0.5;
        int totalPairs = dataset.size() * (dataset.size() - 1) / 2; // total number of pairs
    
        for(int iteration = 0; iteration < 100; iteration ++) {
            int neighbourNum = 0;
            for(Map.Entry<String, Double> pair : distanceBetweenPairsMap.entrySet()) {
                if(pair.getValue() < dCut) neighbourNum += 2;
            }
            double neighborPercentage = (double) neighbourNum / totalPairs;
            if(neighborPercentage >= 0.01 && neighborPercentage <= 0.02) break;
            if(neighborPercentage > 0.02) {
                maxTmp = dCut;
            } else {
                minTmp = dCut;
            }
            dCut = 0.5 * (maxTmp + minTmp);
        }
        return dCut;
    }
}
