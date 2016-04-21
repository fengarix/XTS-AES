package id.crypto;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    private static final int BLOCK_SIZE = 16;

    public static void main(String[] args) throws Exception {
        //DUMMY
        String pathKey = "res/key.txt";
        String pathPlaintext = "res/plaintext.txt";
        String pathCiphertext = "res/outputEnkripsi.txt";
        boolean isEncryption = true;

        //BACA KEY
        //Tuple key = readInput(pathKey);
        //printBytes(key.getArrBytes(), "KEY");

        //read key from hexadecimal to byte
        BufferedReader breader = new BufferedReader(new FileReader(pathKey));
        String keyhex = breader.readLine();
        byte[] keys = DatatypeConverter.parseHexBinary(keyhex);

        ArrayList<byte[]> key = new ArrayList<>();
        byte[] key1 = new byte[BLOCK_SIZE];
        byte[] key2 = new byte[BLOCK_SIZE];
        breader.close();

        int j=BLOCK_SIZE;
        for (int i=0; i<BLOCK_SIZE; i++) {
            key1[i] = keys[i];
            key2[i] = keys[j];
            j++;
        }
        key.add(key1);
        key.add(key2);

        System.out.println("PRINT BACA KEY");
        printBytes(key, "key");


        //HANDLING IF ELSE ENKRIPSI DEKRIPSI
        Tuple plaintext = readInput(pathPlaintext);
        printBytes(plaintext.getArrBytes(), "PT");
        XTS_AES xts_aes = new XTS_AES();
        byte[][] enkripsi = xts_aes.encryption(plaintext.getArrBytes(), key, plaintext.getLast());

        FileOutputStream writer = new FileOutputStream("res/outputEnkripsi.txt");
        for (byte[] en : enkripsi){
            writer.write(en);
        }

        Tuple ciphertext = readInput(pathCiphertext);
        printBytes(ciphertext.getArrBytes(), "CT");
        xts_aes = new XTS_AES();
        byte[][] dekripsi = xts_aes.decryption(ciphertext.getArrBytes(), key, ciphertext.getLast());

        FileOutputStream writer1 = new FileOutputStream("res/outputDekripsi.txt");
        for (byte[] de : dekripsi){
            writer1.write(de);
        }

        System.out.println("------HASIL DEKRIP COY------");
        for (byte[] pt : dekripsi) {
            printByte(pt);
        }

    }

    public static void printByte(byte[] arr) {

        for (int i=0; i<arr.length; i++) {
            System.out.print(arr[i] + " ");
        }

        System.out.println();
    }

    public static void printBytes(ArrayList<byte[]> arr, String types) {

        System.out.println("PRINT BACA "+ types);
        for (int i=0; i<arr.size(); i++) {
            System.out.print(types + "ke-"+i+": ");
            printByte(arr.get(i));
        }

    }

    public static Tuple readInput(String path) throws Exception {
        byte[] baca = new byte[BLOCK_SIZE];
        ArrayList<byte[]> input = new ArrayList<>();
        FileInputStream reader = new FileInputStream(path);

        int length = 1;
        int last = 0;

        length = reader.read(baca);
        while(length > 0) {
            if (length < BLOCK_SIZE){
                last = length;
            }
            input.add(Arrays.copyOf(baca, baca.length));
            length = reader.read(baca);
        }

        return new Tuple(input,last);
    }
}
