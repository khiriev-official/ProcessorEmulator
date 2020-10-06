/* This class is fetching fields' values from Command */

public class Decipher {

    public static int getCmdType(int cmd) {
        return ((cmd >> 8) & 255);
    }

    public static String getCmd(int cmd) {
        String res = "";
        switch (getCmdType(cmd)) {
            case 0: res = "LDA1"; break;  //LDA1 - loads into AX data from <dmem address> OK
            case 1: res = "MRA"; break;   //MRA - copies from AX to <register> OK
            case 2: res = "LDA2"; break;       //LDA2 - loads into AX number OK
            case 3: res = "ADD"; break;   //ADD - returns sum of AX and given <register> OK
            case 4: res = "INC";  break;       //INC - increments <register>
            case 5: res = "LOOP"; break;
            case 6: res = "DEC"; break; //DEC - Retires from program
            case 7: res = "RET"; break;
            default:
                res = "undefied";break;
        }


        return res;
    }

    /*public static short getRegDest(short cmd) {
        return (short) ((cmd >> 8) & 15);
    }*/

    public static int getOp1(int cmd) {
        return (cmd & 255);
    }

    /*public static short getOp2(short cmd) {
        return (short) (cmd & 15);
    }*/

    public static int getCh1(int cmd) {
        return (cmd & 255);
    }
}
