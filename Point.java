public class Point {
    private final double[] coordinates;
    private int clusterLabel = 0;
    private int rho = 1;
    private double nearestNeighbourDistance;

    public Point(double[] coordinates){
        this.coordinates = coordinates;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public int getClusterLabel() {
        return clusterLabel;
    }

    public void setClusterLabel(int clusterLabel) {
        this.clusterLabel = clusterLabel;
    }

    public int getRho() {
        return rho;
    }

    public void incrementRho() {
        this.rho += 1;
    }

    public double getNearestNeighbourDistance() {
        return nearestNeighbourDistance;
    }

    public void setNearestNeighbourDistance(double nearestNeighbourDistance) {
        this.nearestNeighbourDistance = nearestNeighbourDistance;
    }
}
