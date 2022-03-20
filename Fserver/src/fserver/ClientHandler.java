/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fserver;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author acer
 */
public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler> clienthandlers = new ArrayList<>();
    public Admin admin;
    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;
    private String clientUsr;
  //  private String clientPass;
    String p_nom = "";
    boolean approved = false;

    public ClientHandler(Socket socket,Admin ad) throws SQLException {
        try {
            this.admin = ad;
            this.socket = socket;
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsr = br.readLine();

            String[] elements = this.clientUsr.split(";");
            ad.Logs.append(elements[0]);
            //System.out.println(elements[0]);
            
            HndlrRegister(elements);
            HndlrLogin(elements);
            
        } catch (IOException ex) {
            CloseKolchi(socket, bw, br);
        }
    }
    
    private void HndlrLogin(String[] e) throws SQLException, IOException{
                    List<compte> Etudiants = CompteDB.LoginRequest(e[1],e[2]); // getComptes() getComptesFromfile()
                    for(int i=0; i<Etudiants.size(); i++){
                        if(Etudiants.get(i).getUsername().equals(e[1]) && Etudiants.get(i).getPassword().equals(e[2])){
                            p_nom = Etudiants.get(i).getPrenom();
                            approved = Etudiants.get(i).getIsApproved();
                            if(Etudiants.get(i).getIsActif()==true){
                                clienthandlers.add(this);
                                //PrivateMessage("Success");
                                broadcastMessage("Server: "+ p_nom+" has entred the froum");
                            }
                            admin.Logs.append(" > Success");
                            this.bw.write("Success");
                            this.bw.newLine();
                            this.bw.flush();
                            admin.Logs.append("\nServer: "+ p_nom+" has entred the froum\n"); 
                        }
                    }
        
    }
    private void HndlrRegister(String[] e) throws SQLException, IOException{
        if(e[0].equals("RegisterRequest")){ 
            String msg = CompteDB.RegisterRequest(e[1],e[2],e[3],e[4]);
            if("Success".equals(msg)){
                p_nom = e[3];
                PrivateMessage("Success;Server: Salut "+ p_nom+" attendez que votre compte soit activé");   
            }
                this.bw.write(msg);
                this.bw.newLine();
                this.bw.flush();
            
        }
    }
    
    
    @Override
    public void run() {
        String MsgFromClient;
        while(socket.isConnected()){
            try{
                MsgFromClient = br.readLine();
                String[] elements = MsgFromClient.split(":");
                for(ClientHandler clienthandler : clienthandlers){
                    if(clienthandler.clientUsr.equals(elements[0]) && clienthandler.approved == true){
                        String[] elem = clienthandler.clientUsr.split(";");
                        broadcastMessage(elem[1]+": "+elements[1]);
                    }
                    else if(clienthandler.clientUsr.equals(elements[0])&& clienthandler.approved == false){
                        PrivateMessage("Server: Vous ne pouvez pas envoyer des messages !");}
                }
            }catch(IOException ex){CloseKolchi(socket, bw, br); break;}
        }
    }
    
    private void PrivateMessage(String MsgFromServer) {
        for(ClientHandler clienthandler : clienthandlers){
            try{
                if(clienthandler.clientUsr.equals(clientUsr)){
                    clienthandler.bw.write(MsgFromServer);
                    clienthandler.bw.newLine();
                    clienthandler.bw.flush();
                }
            }catch(IOException ex){CloseKolchi(socket, bw, br);}
        } 
    }
    private void broadcastMessage(String MsgFromClient) {
        for(ClientHandler clienthandler : clienthandlers){
            try{
                if(!clienthandler.clientUsr.equals(clientUsr)){
                    clienthandler.bw.write(MsgFromClient);
                    clienthandler.bw.newLine();
                    clienthandler.bw.flush();
                }
            }catch(IOException ex){CloseKolchi(socket, bw, br);}
        } 
    }
    public void DelClientHndlr(){
        clienthandlers.remove(this);
        broadcastMessage(p_nom+" has left the forum"); // clientUsr
        admin.Logs.append("Server: "+ p_nom+" has left the forum\n");
    }
    private void CloseKolchi(Socket socket,BufferedWriter bw, BufferedReader br){
        DelClientHndlr();
        try{
            if(bw != null){bw.close();}
            if(br != null){br.close();}
            if(socket != null){socket.close();}
        }catch(IOException ex){ex.printStackTrace();}
    }
}
