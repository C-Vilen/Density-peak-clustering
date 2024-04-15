public class Point {
    private final double[] coordinates;
    private int clusterLabel = 0;

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
}
