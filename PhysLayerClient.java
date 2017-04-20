/**
 * Created by Narvik on 4/15/17.
 */

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class PhysLayerClient {

    public static void main ( String [] args ){

        try (Socket socket = new Socket("codebank.xyz", 38002)) {

            byte serverMassageArray[] = new byte[32];
            byte ArrayOf5B [];
            int ServerByte = 0;
            float baseline = 0;
            String receivedInfo = "";

            InputStream getIS = socket.getInputStream();
            OutputStream outStream = socket.getOutputStream();

            // Shows we are connected to the server
            System.out.println("Connected to server.");

            // Find the baseline
            for(int i=0; i < 64; i++) {
                baseline += getIS.read();
            }
            baseline = baseline/64;

            // Print out the Baseline
            System.out.printf("Baseline established from preamble: %.2f\n" , baseline);

            // Every single byte we receive from a server means its a single bit
            for ( int i = 0 ; i < 320 ; i++){
                ServerByte = getIS.read();
                if (ServerByte > baseline){
                    receivedInfo += "1";
                }else{
                    receivedInfo += "0";
                }
            }
            // Decode the NRZI
            ArrayOf5B = NRZI(receivedInfo);

            String toStringF = "",toStringS = "";
            int convertedTo4bF = 0, convertedTo4bS = 0;
            int counter = 10;

            // Separate every 5bit, convert it to 4bit, shift and add the upper and lower of a byte and save them in an array
            for ( int i = 0 ; i < 320 ;){
                while (counter != 0){
                    if(counter<=5){
                        toStringS += ArrayOf5B[i++];
                    }
                    else{
                        toStringF += ArrayOf5B[i++];
                    }
                    counter--;
                }

                // Convert it to 4bit
                convertedTo4bF = ConvertTo4Bits(toStringF);
                convertedTo4bS = ConvertTo4Bits(toStringS);

                // Shift and add the upper and lower of a byte and save them in an array
                convertedTo4bF = convertedTo4bF << 4;
                serverMassageArray[(i/10)-1] = (byte)(convertedTo4bF+convertedTo4bS);

                // Reset
                counter=10;
                toStringF = "";
                toStringS = "";
            }

            // Print out the received bytes
            System.out.print("Received 32 bytes: ");
            for(int i=0; i < 32; i++) {
                System.out.printf("%02X",serverMassageArray[i]);
            }
            System.out.println();

            // Send the result to server
            for(int i=0; i < 32; i++) {
                outStream.write(serverMassageArray[i]);
            }

            // Check the result
            if(getIS.read() == 1) {
                System.out.println("Response good.");
            } else {
                System.out.println("Response bad.");
            }

        }
        catch(IOException e) {
            e.printStackTrace();
        }

        System.out.println("Disconnected from server.");
    }

    // Convert to corect Format of 5 bit values
    private static byte[] NRZI(String string) {
        byte corectedForm [] = new byte [320];

        char prevValue = string.charAt(0);
        char curValue = '0';

        if (prevValue == '0'){
            corectedForm[0] = 0;
        }
        else{
            corectedForm[0] = 1;
        }

        for( int i = 1; i < 320; i++ ){
            curValue = string.charAt(i);

            if(curValue == prevValue){
                corectedForm[i] = 0;
            } else{
                corectedForm[i] = 1;
            }
            prevValue = curValue;

        }

        return corectedForm;
    }

    // 5bit to 4bit converter
    private static int ConvertTo4Bits(String fiveBit) {
        if(fiveBit.equals("11110"))
            return 0;
        else if(fiveBit.equals("01001"))
            return 1;
        else if(fiveBit.equals("10100"))
            return 2;
        else if(fiveBit.equals("10101"))
            return 3;
        else if(fiveBit.equals("01010"))
            return 4;
        else if(fiveBit.equals("01011"))
            return 5;
        else if(fiveBit.equals("01110"))
            return 6;
        else if(fiveBit.equals("01111"))
            return 7;
        else if(fiveBit.equals("10010"))
            return 8;
        else if(fiveBit.equals("10011"))
            return 9;
        else if(fiveBit.equals("10110"))
            return 10;
        else if(fiveBit.equals("10111"))
            return 11;
        else if(fiveBit.equals("11010"))
            return 12;
        else if(fiveBit.equals("11011"))
            return 13;
        else if(fiveBit.equals("11100"))
            return 14;
        else if(fiveBit.equals("11101"))
            return 15;
        else{
            return -1;
        }
    }
}