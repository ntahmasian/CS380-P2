/**
 * Created by Narvik on 4/15/17.
 */

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.zip.CRC32;

public class PhysLayerClient {

    public static void main ( String [] args ){

        try (Socket socket = new Socket("codebank.xyz", 38002)) {


            InputStream getIS = socket.getInputStream();
            OutputStream outStream = socket.getOutputStream();



            // showes we are connected to the server
            System.out.println("Connected to server.\nReceived bytes:");





            System.out.println("Disconnected from server.");

        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}