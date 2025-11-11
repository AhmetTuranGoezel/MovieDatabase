package com.backend.service;

import com.backend.model.ClientHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class Server {

    public static ArrayList<ClientHandler> loggedInClientHandlers = new ArrayList<>();

    public static ArrayList<ArrayList<ClientHandler>> GroupChats = new ArrayList<>();
    private static BufferedReader reader;
    private static StringTokenizer tokenizer;

    public static void server() {
        try {
            ServerSocket srvS = new ServerSocket(1234);
            Socket socket = null;


            while (!srvS.isClosed()) {
                socket = srvS.accept();

                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String info = reader.readLine();
                tokenizer=new StringTokenizer(info,"#");

                int clientID=Integer.parseInt(tokenizer.nextToken());
                String nickname= tokenizer.nextToken();
                System.out.println(clientID+nickname);
                ClientHandler clientHandler = new ClientHandler(socket, clientID, nickname);

                loggedInClientHandlers.add(clientHandler);
                Thread thread = new Thread(clientHandler);
                thread.start();
                //ArrayList<ClientHandler> chatMembers = new ArrayList<ClientHandler>();
                //chatMembers.add(clientHandler);
                //GroupChats.add(chatMembers);


            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}


