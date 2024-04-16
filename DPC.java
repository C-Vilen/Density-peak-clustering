import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class DPC {
    private ArrayList<Point> dataset;
    private Map<String, Double> distanceBetweenPairsMap;
    private Map<Point, Point> nearestNeighbourMap;
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
        for (int i = 0; i < dataset.size()-1; i++) {
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

    /**
     * Finds rho for each point in the dataset within a cutoff distance (also considered close neighbours inside certain distance). 
     * @param dCut distance of which rho will be incremented if another point is closer than dCut.
     */
    public void calculateRhoForEachPoint(double dCut){
        for(Map.Entry<String, Double> pair : distanceBetweenPairsMap.entrySet()){
            if (pair.getValue() < dCut) {
                int[] pointPair = Stream.of(pair.getKey().split(" ")).mapToInt(Integer::parseInt).toArray();
                dataset.get(pointPair[0]).incrementRho();
                dataset.get(pointPair[1]).incrementRho();
            }
        }
    }

    /**
     * Find the nearest neighbour distance between points i and j where rho of J is equal or larger than rho of I 
     * @throws Exception from helper method findDistance(i, j) if the coordinates of I and J are not an equal size.
     */
    public void calculateNNDistanceWithHigherRho() throws Exception{
        sortDatasetByRho();
        nearestNeighbourMap = new HashMap<>();
        for (int i = 0; i < dataset.size(); i++) {
            if(i == 0){
                dataset.get(0).setNearestNeighbourDistance(Double.MAX_VALUE);
                nearestNeighbourMap.put(dataset.get(0), null);
            } else {
                double minimumDistanceIJ = Double.MAX_VALUE;
                int neighbourIndex = 0;
                for (int j = 0; j < i; j++) {
                    double distance = findDistance(dataset.get(i), dataset.get(j));
                    if (distance < minimumDistanceIJ) {
                        minimumDistanceIJ = distance;
                        neighbourIndex = j;
                    }
                }
                dataset.get(i).setNearestNeighbourDistance(minimumDistanceIJ);
                nearestNeighbourMap.put(dataset.get(i), dataset.get(neighbourIndex));
            }
        }
    }

    /**
     * Helper method to sort the arraylist of points by their rho value in descending order.
     */
    private void sortDatasetByRho(){
        Collections.sort(dataset, Comparator.comparing(Point::getRho).reversed());
    }

    public void clusterDataset(double deltaMinimum, double rhoMinimum){
        Point currentPoint;
        int currentClusterLabel = 1; 
        // Finds peaks/cluster centers
        for (int i = 0; i < dataset.size(); i++) {
            currentPoint = dataset.get(i);
            if (currentPoint.getNearestNeighbourDistance() >= deltaMinimum && currentPoint.getRho() >= rhoMinimum) {
                currentPoint.setClusterLabel(currentClusterLabel);
                currentClusterLabel++;
            }
        }

        // Set labels for remaining points in dataset
        for (int i = 1; i < dataset.size(); i++) {
            currentPoint = dataset.get(i);
            if(currentPoint.getClusterLabel() == 0){
                if(nearestNeighbourMap.containsKey(currentPoint)){
                    currentPoint.setClusterLabel(nearestNeighbourMap.get(currentPoint).getClusterLabel());
                } else {
                    currentPoint.setClusterLabel(-1);
                }
            }
        }
    }
}
