package rs.ac.singidunum;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Client started");
        try {
            Socket socket = new Socket(InetAddress.getLocalHost(), 6000);
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            // Inicijalna poruka
            System.out.println(dis.readUTF());

            System.out.println("Connected successfully");
            System.out.println("Type /logout to exit");
            Scanner scn = new Scanner(System.in);

            Thread sendMessage = new Thread(() -> {
                while (true) {
                    try {
                        String msg = scn.nextLine().trim();
                        dos.writeUTF(msg);
                        if (msg.equals("/logout")) {
                            System.exit(0);
                        }
                    } catch (IOException e) {
                        System.out.println("Doslo do greske u slanju poruka");
                        break;
                    }
                }
            });
            sendMessage.start();

            Thread readMessage = new Thread(()->{
                while (true) {
                    try{
                        System.out.println(dis.readUTF());
                    } catch (IOException e) {
                        System.out.println("Doslo je do greske prilikom citanja poruka");
                        break;
                    }
                }
            });
            readMessage.start();

        } catch (IOException io) {
            System.out.println("Doslo je do greske prilikom inicijalizacije");
            io.printStackTrace();
            System.exit(0);
        }
    }
}