package com.backend.model;

import com.backend.service.Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ClientHandler implements Runnable {


    private Socket socket;
    private BufferedReader bfreader;
    private PrintWriter writer;
    private StringTokenizer tokenizer;
    private int clientID;
    private String nickname;
    private int groupID;


    public ClientHandler(Socket socket, int clientID, String nickname) {
        try {
            this.socket = socket;
            this.bfreader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            this.clientID = clientID;
            this.nickname = nickname;

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void run() {

        int receivedID;
        while (socket.isConnected()) {

            try {
                int currGroupID = -1;
                int alertID;
                ArrayList<ClientHandler> chatMembers = new ArrayList<>();
                String receivedMessage = bfreader.readLine();
                System.out.println(receivedMessage);
                tokenizer = new StringTokenizer(receivedMessage, "#");


                switch (tokenizer.nextToken()) {


                    //Method#senderusername#targetID
                    case "Invite":
                        System.out.println("Bin im invite");
                        currGroupID = Server.GroupChats.size();
                        groupID = currGroupID;
                        chatMembers = new ArrayList<>();
                        chatMembers.add(this);
                        Server.GroupChats.add(chatMembers);
                        nickname = tokenizer.nextToken();
                        alertID = Integer.parseInt(tokenizer.nextToken());
                        for (ClientHandler loggedInCh : Server.loggedInClientHandlers) {
                            if (loggedInCh.clientID == alertID) {
                                loggedInCh.writer.println("Alert#" + this.nickname + "#" + this.clientID + "#" + currGroupID);

                            }
                        }
                        System.out.println("aus invite raus");
                        break;

                    //Method#Senderusername#MultipleTargetIDs#CurrentChattingGroupID
                    case "InviteGroup":
                        System.out.println("Bin im InviteGroup");
                        //erstellt gruppe und packt sich selbst hinein
                        currGroupID = Server.GroupChats.size();
                        groupID = currGroupID;
                        chatMembers = new ArrayList<>();
                        chatMembers.add(this);
                        Server.GroupChats.add(chatMembers);
                        nickname = tokenizer.nextToken();
                        //schickt an alle ids eine einladung
                        String[] receivedIDs = tokenizer.nextToken().split("\\*");
                        String currentChattingGroupID = tokenizer.nextToken();
                        for (String currString : receivedIDs) {
                            int currInt = Integer.parseInt(currString);
                            for (ClientHandler loggedInCh : Server.loggedInClientHandlers) {
                                if (currInt == loggedInCh.clientID) {
                                    loggedInCh.writer.println("Alert#" + this.nickname + "#" + this.clientID + "#" + currGroupID + "#" + currentChattingGroupID);
                                }
                            }
                        }
                        System.out.println("aus invite raus");
                        break;


                    case "Chat":
                        System.out.println("Bin im Chat");
                        String message = tokenizer.nextToken();
                        broadcastmessage("Chat#" + nickname + ": " + message);
                        break;

                    case "disableButtons":
                        broadcastmessageToAll("disableButtons");
                        break;


                    //Method#GroupID#ClientID
                    case "StartChat":
                        System.out.println("Bin im StartChat");
                        groupID = Integer.parseInt(tokenizer.nextToken());
                        Server.GroupChats.get(groupID).add(this);
                        broadcastmessageToAll("Chat#" + nickname + " has joined the Live-Chat.");
                        /*

                        receivedID = Integer.parseInt(tokenizer.nextToken());
                        for (ClientHandler loggedInCh : Server.loggedInClientHandlers) {
                            if (loggedInCh.clientID == receivedID) {
                                loggedInCh.writer.println("Chat#" + this.nickname + " accepted");
                                Server.GroupChats.get(groupID).add(loggedInCh);
                                loggedInCh.setGroupID(groupID);


                            }
                        }*/
                        System.out.println("Bin raus aus StartChat");
                        break;


                    case "Decline":
                        System.out.println("Bin im decline");
                        receivedID = Integer.parseInt(tokenizer.nextToken());
                        for (ClientHandler loggedInCh : Server.loggedInClientHandlers) {
                            if (loggedInCh.clientID == receivedID) {
                                loggedInCh.writer.println("Chat#" + this.nickname + " declined.");

                            }
                        }
                        break;


                    //Method#SenderUsername#targetID
                    case "InviteToGroup":

                        chatMembers = new ArrayList<>();
                        chatMembers.add(this);
                        Server.GroupChats.add(chatMembers);
                        nickname = tokenizer.nextToken();
                        alertID = Integer.parseInt(tokenizer.nextToken());

                        for (ClientHandler loggedInCh : Server.loggedInClientHandlers) {
                            if (loggedInCh.clientID == alertID) {
                                loggedInCh.writer.println("Alert#" + this.nickname + "#" + this.clientID + "#" + groupID);

                            }
                        }

                        break;

                    case "InviteToDiscussionGroup":

                        chatMembers = new ArrayList<>();
                        chatMembers.add(this);
                        Server.GroupChats.add(chatMembers);
                        nickname = tokenizer.nextToken();
                        alertID = Integer.parseInt(tokenizer.nextToken());

                        for (ClientHandler loggedInCh : Server.loggedInClientHandlers) {
                            if (loggedInCh.clientID == alertID) {
                                loggedInCh.writer.println("Alert#" + this.nickname + "#" + this.clientID + "#" + groupID + "#" + tokenizer.nextToken());

                            }
                        }

                        break;

                    case "CloseChat":
                        if (Server.GroupChats.size() > 0 && Server.GroupChats.get(groupID).contains(this)) {
                            broadcastmessage("Chat#" + this.nickname + " disconnected");
                            Server.GroupChats.get(groupID).remove(this);

                        }
                        break;

                    case "LogOut":
                        // int removeID = Integer.parseInt(tokenizer.nextToken());
                        System.out.println(Server.loggedInClientHandlers.contains(this));
                        Server.loggedInClientHandlers.remove(this);
                        System.out.println(Server.loggedInClientHandlers.contains(this));
                        //Server.loggedInClientHandlers.removeIf(clientHandler -> clientHandler.clientID == removeID);
                        break;

                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


    public void broadcastmessage(String broadcastmessage) throws IOException {
        for (ClientHandler ch : Server.GroupChats.get(groupID)) {

            if (ch != this) {
                ch.writer.println(broadcastmessage);
            }
        }
    }

    public void broadcastmessageToAll(String broadcastmessage) throws IOException {
        for (ClientHandler ch : Server.GroupChats.get(groupID)) {
            ch.writer.println(broadcastmessage);

        }
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }


}
