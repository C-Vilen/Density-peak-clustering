public class Main {
    public static void main(String[] args) throws Exception {
        // 1. Initialise dataset and create the points
        FileIO file = new FileIO();
        file.inputData("s1.txt");
        
        // 2. Initialise the density peak clustering class with the dataset
        DPC dpc = new DPC(file.getData());

        // 3. Find distance between all pairs
        dpc.calculateDistanceBetweenPairs();

        // 4. Find dCut for each point over 100 iterations
        // double dCut = dpc.findDCut();
        // System.out.println("dCut = " + dCut);

        // 5. Calculate the rho for each point
        dpc.calculateRhoForEachPoint(37_000);
        System.out.println(file.getData().get(2500).getRho());

        // 6. Calculate nearest neighbour distance and populate map
        dpc.calculateNNDistanceWithHigherRho();

        // 7. Set cluster labels for all points 
        dpc.clusterDataset(77_000, 5);
    }
}