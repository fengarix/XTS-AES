package id.crypto;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Controller {
    @FXML
    private Button sourceButton;

    @FXML
    private Button keyButton;

    @FXML
    private Button enc;

    @FXML
    private Button dec;

    private TargetFiles tf = new TargetFiles();
    private ArrayList<byte[]> key;
    private static final int BLOCK_SIZE = 16;

    public void sourceAction(ActionEvent event){
        FileChooser fc = new FileChooser();
        File f = fc.showOpenDialog(null);

        if(f == null){
            System.out.println("not valid");
        }else {
            tf.setSourcePath(f.getAbsolutePath());
            tf.setSourceName(f.getName());
            System.out.println(tf.getSourcePath());
        }
    }

    public void keyAction(ActionEvent event){
        FileChooser fc = new FileChooser();
        File def = new File(System.getProperty("user.dir")+"\\res");
        fc.setInitialDirectory(def);
        File f = fc.showOpenDialog(null);

        if(f == null){
            System.out.println("not valid");
        }else {
            tf.setKeyPath(f.getAbsolutePath());
            System.out.println(tf.getKeyPath());
        }
    }

    public void encAction(ActionEvent event) throws Exception {
        if (tf.getSourceName()==null || tf.getKeyPath()==null){
            System.out.println("File is not set yet");
            return;
        }
        enc.setDisable(true);
        dec.setDisable(true);
        readKey();
        encrypt();
        enc.setDisable(false);
        dec.setDisable(false);
    }

    public void decAction(ActionEvent event) throws Exception {
        if (tf.getSourceName()==null || tf.getKeyPath()==null){
            System.out.println("File is not set yet");
            return;
        }
        enc.setDisable(true);
        dec.setDisable(true);
        readKey();
        decrypt();
        enc.setDisable(false);
        dec.setDisable(false);
    }

    private void readKey() throws IOException {
        //get path key
        String pathKey = tf.getKeyPath();

        //read key from hexadecimal to byte
        BufferedReader breader = new BufferedReader(new FileReader(pathKey));
        String keyhex = breader.readLine();
        byte[] keys = DatatypeConverter.parseHexBinary(keyhex);

        key = new ArrayList<>();
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
    }

    private void encrypt() throws Exception {
        //ENCRYPTION
        String pathSource = tf.getSourcePath();
        Tuple plaintext = readInput(pathSource);
        printBytes(plaintext.getArrBytes(), "PT");
        XTS_AES xts_aes = new XTS_AES();
        byte[][] enkripsi = xts_aes.encryption(plaintext.getArrBytes(), key, plaintext.getLast());
        String targetPath = "res\\encrypted-"+tf.getSourceName();
        FileOutputStream writer = new FileOutputStream(targetPath);
        for (byte[] en : enkripsi){
            writer.write(en);
        }
        System.out.println(System.getProperty("user.dir")+"\\"+targetPath);
        Runtime.getRuntime().exec("explorer.exe /select," + System.getProperty("user.dir")+"\\"+targetPath);
    }

    private void decrypt() throws Exception{
        //DECRYPTION
        String pathSource = tf.getSourcePath();
        Tuple ciphertext = readInput(pathSource);
        printBytes(ciphertext.getArrBytes(), "CT");
        XTS_AES xts_aes = new XTS_AES();
        byte[][] dekripsi = xts_aes.decryption(ciphertext.getArrBytes(), key, ciphertext.getLast());
        String targetPath = "res\\decrypted-"+tf.getSourceName();
        FileOutputStream writer = new FileOutputStream(targetPath);
        for (byte[] de : dekripsi){
            writer.write(de);
        }

        System.out.println("------HASIL DEKRIP COY------");
        for (byte[] pt : dekripsi) {
            printByte(pt);
        }
        System.out.println(System.getProperty("user.dir")+"\\"+targetPath);
        Runtime.getRuntime().exec("explorer.exe /select," + System.getProperty("user.dir")+"\\"+targetPath);
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
