package id.crypto;

import java.util.ArrayList;

/**
 * Created by Fatia Kusuma Dewi on 4/20/2016.
 */
public class Tuple {

    ArrayList<byte[]> arrBytes;
    int last;

    public Tuple(ArrayList<byte[]> arrBytes, int last) {
        this.arrBytes = arrBytes;
        this.last = last;
    }

    public ArrayList<byte[]> getArrBytes() {
        return arrBytes;
    }

    public void setArrBytes(ArrayList<byte[]> arrBytes) {
        this.arrBytes = arrBytes;
    }

    public int getLast() {
        return last;
    }

    public void setLast(int last) {
        this.last = last;
    }
}
