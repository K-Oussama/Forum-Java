/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package fserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author acer
 */
public class Fserver {

   private final ServerSocket serverSocket;
   public final Admin admn;
    /**
     *
     * @param serversocket
     * @param ad
     */
    public Fserver(ServerSocket serversocket,Admin ad) {
        this.serverSocket = serversocket;
        this.admn = ad;
    }
    
    public void Start() throws SQLException{
    
        try{
            while(!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                // System.out.println("client connected");
                admn.Logs.append("client connected > ");
                ClientHandler clienthandler = new ClientHandler(socket,admn);
                Thread thread = new Thread(clienthandler);
                thread.start();
                
            }
        }
        catch(IOException ex){}
    }
    public void CloseServer(){
        try{
            if(serverSocket != null){
                serverSocket.close();
            }
        }catch(IOException ex){ex.printStackTrace();}
    }
    
    
    public static int Server(){
        int p = 0;
        do{
            String pr =JOptionPane.showInputDialog("Enter Port: (entre 4000-65535)"); 
            p = Integer.parseInt(pr);
        }while(p <=4000 || p>65535);
    return p;
    }
    
    public static void main(String[] args) throws IOException, SQLException{
        int port = Server();
        String pr = port+"";
        Admin admn = new Admin();
        admn.portNum.setText(pr);
        admn.setVisible(true);

        ServerSocket serverSockt = new ServerSocket(port);
        admn.Logs.setText("Serveur est demarre \n");
        Fserver serverchat = new Fserver(serverSockt,admn);
        serverchat.Start();
    }
    
}
