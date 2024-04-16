public class Main {
    public static void main(String[] args) throws Exception {
        // 1. Initialise dataset and create the points
        FileIO file = new FileIO();
        file.inputData("s1.txt");
        
        // 2. Initialise the density peak clustering class with the dataset
        DPC dpc = new DPC(file.getData());

        // 3. Find distance between all pairs
        dpc.calculateDistanceBetweenPairs();

        // 4. Find dCut
    }
}