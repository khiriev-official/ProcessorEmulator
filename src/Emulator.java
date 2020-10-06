public class Emulator {
    //Loads Processor, Loads Numbers in Memory
    //Prepares registers
    //initializes commands
    public static void main(String[] args) {
        Processor processor = new Processor(4, 17, 10);
        processor.executeProgram();
    }
}
