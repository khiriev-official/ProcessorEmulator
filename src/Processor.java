import java.util.Scanner;

/*
* TO-DO:
* * Create loader for programm
* * Automate Calculation of CommandMemorySize
* */


public class Processor {

    private int pc; // Program Counter
    private int[] reg; // All Registers in Processor
    private int[] memory; // Command Memory and Data Memory
    private int commandMemorySize; // Command Memory Size
    private final int ax = 0; // Index of ax register in reg[] Array
    private final int bx = 1; // Index of bx register in reg[] Array
    private final int cx = 2; // Index of cx register in reg[] Array
    private final int dx = 3; // Index of dx register in reg[] Array

    /*Creation and getting ready Processor for work*/
    public Processor(int numOfRegisters, int memorySize, int commandMemorySize){

        /*Setting Number of Commands in Memory*/
        this.commandMemorySize = commandMemorySize;


        System.out.println("Starting Initialization... Done!");
        System.out.print("Preparing PC, Registers, Memory for work... ");


        /*Setting up Dmem and Registers*/
        loadRegisters(numOfRegisters);
        loadData(memorySize);


        System.out.println("Done!");


        System.out.print("Loading Commands for the Processor... ");

        /*Setting up program
         * Program loads here to execute
         * */
        calculateSum();

        System.out.println("Done!");
        System.out.println("Processor is ready to go!");
    }


    /*
     * This method executes preloaded instructions
     * */
    public void executeProgram() {
        /*
         * Created auxiliary variables for convenience
         * */
        int cmdtype;
        int operand;
        int ch1;
        int cmd;
        int loop;

        Scanner scan = new Scanner(System.in); // Scanner is created for stopping program on each iteration

        showAll(); // get Starting state of Processor

        System.out.println("Programm begins!");

        while (pc != commandMemorySize) {
            /*Copying command from Command Memory*/
            cmd = memory[pc];

            /*Getting FIELDS' values*/
            cmdtype = Decipher.getCmdType(cmd);
            operand = Decipher.getOp1(cmd);
            ch1 = Decipher.getCh1(cmd);
            loop = Decipher.getOp1(cmd);

            System.out.println("\nCMDtype["+ pc +"]: " + Decipher.getCmd(cmd));


            /*Executing given command depending on its type*/
            switch (cmdtype) {
                case 0: LDA1(ch1); pc++; break;
                case 1: MRA(operand); pc++; break;
                case 2: LDA2(operand); pc++; break;
                case 3: ADD(dx); pc++; break;
                case 4: INC(operand); pc++; break;
                case 5: LOOP(loop); break;
                case 6: DEC(operand); pc++; break; // NEW COMMAND!!!
                case 7: System.out.println("RET"); pc++; break;
            }

            /*Showing results after execution*/
            showRegisters();

            /*Getting time to observe the results*/
            System.out.print("Type any key...\n");
            // scan.nextLine();
        }
        System.out.println("\nProgram ended...\nSum = " + reg[ax]);
    }

    /*
     * This methods outputs in Console view Registers' Values
     * Out[]: "pc: 0
     *         Registers: ax: 0 bx: 0 cx: 0 dx: 0"
     * */
    public void showRegisters() {

        System.out.print("pc: " + pc + "\n");
        System.out.println("Registers: " + "ax: " + reg[ax] + " bx: " + reg[bx] + " cx: " + reg[cx] + " dx: " + reg[dx]);

    }

    /*
     * This methods outputs in Console view Command Memory Values
     * Out[]: "Cmem: [ 9 258 522 259 512 771 1027 1285 1536 ]"
     * */
    public void showCommandMemory() {

        System.out.print("Cmem: " + "[ ");
        for (int i = 0; i < commandMemorySize; i++) System.out.print(memory[i] + " ");
        System.out.println("]");

    }

    /*
     * This methods outputs in Console view Data Values
     * Out[]: "Dmem: [ 11 60 30 3 31 95 56 4 97 60 80 11 3 ]"
     * */
    public void showDataMemory() {

        System.out.print("Dmem: " + "[ ");
        int dMemSum = 0;
        for (int i = commandMemorySize; i < memory.length; i++) {
            System.out.print(memory[i] + " ");
            if (i != commandMemorySize) {
                dMemSum += memory[i];
            }
        }
        System.out.println("]");
        System.out.println("Data Memory Sum = " + dMemSum);

    }

    /*
     * showAll() - method for showing info of all components: PC, Registers, Command Memory, Data Memory
     * Out[]:    Showing all info.
     *           ==============================================================
     *           pc: 0
     *           Registers: ax: 0 bx: 0 cx: 0 dx: 0
     *           Cmem: [ 9 258 522 259 512 771 1027 1285 1536 ]
     *           Dmem: [ 11 60 30 3 31 95 56 4 97 60 80 11 3 ]
     *           Data Memory Sum = 530
     *           ==============================================================
     * */
    private void showAll() {
        System.out.println("\nShowing all info.");
        System.out.println("==============================================================");

        showRegisters();
        showCommandMemory();
        showDataMemory();

        System.out.println("==============================================================\n");
    }

    /*
     * This Method Loads commands to execute for Processor
     * int[] commands - array with commands
     * */
    public void setCommands(int[] commands) {

        System.arraycopy(commands, 0, memory, 0, commands.length);

    }

    /*Initializing Commands into Command Memory*/
    public void calculateSum() {
        memory[0] = 0xa; //LDA1 [ch1]
        memory[1] = 0x102; //MRA cx
        memory[2] = 0x20b; //LDA2 11
        memory[3] = 0x103; //MRA dx
        memory[4] = 0x602; //DEC cx NEW!!!
        memory[5] = 0x200; //LDA2 0
        memory[6] = 0x303; //ADD dx
        memory[7] = 0x403; //INC dx
        memory[8] = 0x506; //LOOP 6
        memory[9] = 0x700; // RET
    }

    /*Initializing Data Memory Numbers
     * First Number is a size of a Data Memory
     * Other Numbers are operands
     * */
    public void loadData(int memorySize) {

        memory = new int[memorySize];
        memory[commandMemorySize] = (memory.length - commandMemorySize - 1);
        for (int i = commandMemorySize; i < memory.length - 1; i++) {
            memory[i + 1] = (int) (Math.random() * 100);
        }
    }

    /*Initializing Data Memory Numbers
     * First Number is a size of a Data Memory
     * Other Numbers are operands
     * */
    public void loadRegisters(int numOfRegisters) {
        pc = 0;
        //checks if we have at least 4 registers: ax, bx, cx, dx.
        if (numOfRegisters < 4) numOfRegisters = 4;
        reg = new int[numOfRegisters];
    }

    /*
     * Loads in AX value by address in Data Memory
     * int dataMemoryAddress - 16 bit address of number in Memory
     * Result is stored in ax
     * */
    public void LDA1(int dataMemoryAddress) {
        reg[ax] = memory[dataMemoryAddress];
    }

    /*
     * Loads in AX value by pure Value(not address)
     * int dataMemoryAddress - 16 bit address of number in Memory
     * Result is stored in ax
     * */
    public void LDA2(int operandValue) {
        reg[ax] = operandValue;
    }

    /*
     * Copies AX value into chosen register
     * int register - 16 bit address of register in Register Memory
     *                i.e. ax, bx, cx, dx.
     * Result is stored in <register>
     * */
    public void MRA(int register) {
        reg[register] = reg[ax];
    }

    /*
     * Adds two values: ax-value and value from address stored in dx
     * Result is stored in ax
     * int dataMemoryAddress - 16 bit address of number in Memory
     * Result is stored in ax
     * */
    public void ADD(int register) {
        reg[ax] = reg[ax] + memory[reg[register]];
    }
    /*
     * Increments value of chosen register
     * Result is stored in chosen register
     * int op1 - 16 bit address of number in Memory
     * Result is stored in ax
     * */
    public void INC(int op1) {

        reg[op1]++;
    }

    /*
    * Decrements value in a given register
    * register - address of a register in reg[] array
    * */
    public void DEC(int register) {
        reg[register]--;
    }


    /*
     * Creates for loop
     * Number of iterations is stored in cx register
     * Each iteration cx decrements by one
     * If cx != 0 -> Jumps to given command in Command Memory
     * When cx == 0 -> pc increments by 1
     * */
    public void LOOP(int commandToJump) {
        if (reg[cx] == 0) {
            pc++;
        } else {
            reg[cx]--;
            pc = commandToJump;
        }
    }





    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    public int[] getReg() {
        return reg;
    }

    public void setReg(int[] reg) {
        this.reg = reg;
    }

    public int[] getMemory() {
        return memory;
    }

    public void setMemory(int[] memory) {
        this.memory = memory;
    }

    public int getCommandMemorySize() {
        return commandMemorySize;
    }

    public void setCommandMemorySize(int commandMemorySize) {
        this.commandMemorySize = commandMemorySize;
    }


}
