package id.crypto;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Fatia Kusuma Dewi on 4/18/2016.
 */

public class XTS_AES {

    private static final int BLOCK_SIZE = 16;
    public XTS_AES() {

    }

    public byte[][] encryption (ArrayList<byte[]> plaintext, ArrayList<byte[]> key, int last) throws Exception {
        int m = plaintext.size();

        byte[][] ciphertext = new byte[m][BLOCK_SIZE];
        byte[] tweak = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        byte[] additional = new byte[BLOCK_SIZE];

        //initialization encryption
        AES aes = new AES();
        byte[] init = aes.AESencryption(key.get(1), tweak);

        for (int i=0; i<m; i++) {
            //kalo m cuma 1
            if (m==1) {
                //xor1 & enkrip1
                byte[] enkrip1 = aes.AESencryption(key.get(0), xor(init, plaintext.get(i)));

                //xor2
                for (int j=0; j<BLOCK_SIZE; j++) {
                    ciphertext[i][j] = (byte)(init[j] ^ enkrip1[j]);
                }
            }
            //sebelum m-2
            else if (i<m-2) {
                //xor1 & enkrip1
                byte[] enkrip1 = aes.AESencryption(key.get(0), xor(init, plaintext.get(i)));

                //xor2
                for (int j=0; j<BLOCK_SIZE; j++) {
                    ciphertext[i][j] = (byte)(init[j] ^ enkrip1[j]);
                }
                init = multiplication(init);
            }
            //handling m-2
            else {
                //kalau block terakhir tidak penuh
                if (last != 0) {
                    for (int j=m-2; j<m; j++) {
                        //m-1 handling
                        //dua terakhir dari belakang
                        if (j != m-1){
                            //xor1 & enkrip1
                            byte[] enkrip1 = aes.AESencryption(key.get(0), xor(init, plaintext.get(m-2)));

                            //assign last value of cipher
                            byte[] akhir = new byte[last];
                            for (int k=0; k<last; k++) {
                                akhir[k] = (byte)(init[k] ^ enkrip1[k]);
                            }
                            ciphertext[j+1] = akhir;

                            //assign additional plaintext
                            for (int k=last; k<BLOCK_SIZE; k++) {
                                additional[k] = (byte)(init[k] ^ enkrip1[k]);
                            }

                            init = multiplication(init);
                        }
                        //m handling
                        else {
                            //xor1
                            byte[] xor1 = new byte[BLOCK_SIZE];
                            for (int k=0; k<BLOCK_SIZE; k++) {
                                if (k < last) {
                                    xor1[k] = (byte)(init[k] ^ plaintext.get(m-1)[k]);
                                }
                                else {
                                    xor1[k] = (byte)(init[k] ^ additional[k]);
                                }
                            }

                            //enkrip1
                            byte[] enkrip1 = aes.AESencryption(key.get(0), xor1);

                            //xor2
                            for (int k=0; k<BLOCK_SIZE; k++) {
                                ciphertext[m-2][k] = (byte)(init[k] ^ enkrip1[k]);
                            }
                        }
                    }
                    break;
                }
                //kalau elemen terakhirnya pas
                else {
                    //enkrip1
                    byte[] enkrip1 = aes.AESencryption(key.get(0), xor(init, plaintext.get(i)));

                    //xor2
                    for (int j=0; j<BLOCK_SIZE; j++) {
                        ciphertext[i][j] = (byte)(init[j] ^ enkrip1[j]);
                    }

                    //hanya dilakukan saat m-2
                    if (i!=m-1) {
                        init = multiplication(init);
                    }
                }
            }
        }

        System.out.println("--------PRINT CIPHERTEXT--------");

        for(int p=0; p<ciphertext.length; p++) {
            System.out.print("CT ke-"+p+": ");
            printByte(ciphertext[p]);
        }
        return ciphertext;
    }

    public byte[][] decryption (ArrayList<byte[]> ciphertext, ArrayList<byte[]> key, int last) throws Exception {

        int m = ciphertext.size();

        byte[][] plaintext = new byte[m][BLOCK_SIZE];
        byte[] tweak = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        byte[] additional = new byte[BLOCK_SIZE];

        //initialization encryption
        AES aes = new AES();
        byte[] init = aes.AESencryption(key.get(1), tweak);

        for (int i=0; i<m; i++) {

            //kalo m cuma 1
            if (m==1) {
                //xor1 & dekrip1
                byte[] dekrip1 = aes.AESdecryption(key.get(0), xor(init, ciphertext.get(i)));

                //xor2
                for (int j=0; j<BLOCK_SIZE; j++) {
                    plaintext[i][j] = (byte)(init[j] ^ dekrip1[j]);
                }
            }
            //sebelum m-2
            else if (i<m-2) {
                //xor1 & dekrip1
                byte[] dekrip1 = aes.AESdecryption(key.get(0), xor(init, ciphertext.get(i)));

                //xor2
                for (int j=0; j<BLOCK_SIZE; j++) {
                    plaintext[i][j] = (byte)(init[j] ^ dekrip1[j]);
                }
                init = multiplication(init);
            }
            //handling m-2
            else {
                //kalau block terakhir tidak penuh
                if (last != 0) {
                    byte[] initlama = new byte[BLOCK_SIZE];
                    for (int j=m-2; j<m; j++) {
                        //m-1 handling
                        //dua terakhir dari belakang
                        if (j != m-1){
                            initlama = Arrays.copyOf(init, BLOCK_SIZE);
                            init = multiplication(init);

                            //xor1 & dekrip1
                            byte[] dekrip1 = aes.AESdecryption(key.get(0), xor(init, ciphertext.get(m-2)));

                            byte[] akhir = new byte [last];
                            //assign last value of cipher
                            for (int k=0; k<last; k++) {
                                akhir[k] = (byte)(init[k] ^ dekrip1[k]);
                            }
                            plaintext[j+1] = akhir;

                            //assign additional plaintext
                            for (int k=last; k<BLOCK_SIZE; k++) {
                                additional[k] = (byte)(init[k] ^ dekrip1[k]);
                            }

                        }
                        //m handling
                        else {
                            //xor1
                            byte[] xor1 = new byte[BLOCK_SIZE];
                            for (int k=0; k<BLOCK_SIZE; k++) {
                                if (k < last) {
                                    xor1[k] = (byte)(initlama[k] ^ ciphertext.get(m-1)[k]);
                                }
                                else {
                                    xor1[k] = (byte)(initlama[k] ^ additional[k]);
                                }
                            }

                            //dekrip1
                            byte[] dekrip1 = aes.AESdecryption(key.get(0), xor1);

                            //xor2
                            for (int k=0; k<BLOCK_SIZE; k++) {
                                plaintext[m-2][k] = (byte)(initlama[k] ^ dekrip1[k]);
                            }
                        }
                    }
                    break;
                }
                //kalau elemen terakhirnya pas
                else {
                    //dekrip1
                    byte[] dekrip1 = aes.AESdecryption(key.get(0), xor(init, ciphertext.get(i)));

                    //xor2
                    for (int j=0; j<BLOCK_SIZE; j++) {
                        plaintext[i][j] = (byte)(init[j] ^ dekrip1[j]);
                    }

                    //hanya dilakukan saat m-2
                    if (i!=m-1) {
                        init = multiplication(init);
                    }
                }
            }
        }

//        System.out.println("--------PRINT PLAINTEXT--------");
//
//        for(int p=0; p<plaintext.length; p++) {
//            System.out.print("PT ke-"+p+": ");
//            printByte(plaintext[p]);
//        }
        return plaintext;

    }


    public static byte[] multiplication(byte[] arr) {
        byte[] out = new byte[BLOCK_SIZE];
        for (int i=0; i<BLOCK_SIZE; i++) {
            out[i] = (byte) (arr[i] << 1);
            if (i < BLOCK_SIZE - 1 && arr[i+1] < 0) {
                out[i] = (byte)(out[i] ^ ((byte) 1));
            }
        }
        if (arr[0] < 0){
            out[BLOCK_SIZE-1] = (byte)(out[BLOCK_SIZE-1] ^ ((byte) 135));
        }

        return out;
    }

    public static void printByte(byte[] arr) {

        for (int i=0; i<arr.length; i++) {
            System.out.print(arr[i] + " ");
        }

        System.out.println();
    }

    public static byte[] xor(byte[] init, byte[] plaintext) {
        byte[] res = new byte[BLOCK_SIZE];
        for (int j=0; j<BLOCK_SIZE; j++) {
            res[j] = (byte)(init[j] ^ plaintext[j]);
        }

        return res;
    }


}
