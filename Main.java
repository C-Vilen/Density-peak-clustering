public class Main {
    public static void main(String[] args) {
        // 1. Initialise dataset and create the points
        FileIO file = new FileIO();
        file.inputData("iris.txt");
        
        // 2. Initialise the density peak clustering class with the dataset
        DPC dpc = new DPC(file.getData());
    }
}