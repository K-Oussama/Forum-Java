/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package forum_api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import javax.swing.JTextArea;

/**
 *
 * @author acer Oussama KORCHI
 */
public class Forum_API {
//bidaya API 
    
    private static BufferedReader br;
    private static BufferedWriter bw;
    private static Socket socket;
    
    // constructeurs 
    public Forum_API(){

    }
    public Forum_API(Socket socket) throws IOException{
            Forum_API.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            Forum_API.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Forum_API.socket = socket;
    }
        //methods    
        public void Receive(JTextArea a){ //BufferedReader br
                try {
            // TODO add your handling code here:
            new Thread(
                    new Runnable() {
                @Override
                public void run() {
                    String fromForum;
                    while (socket.isConnected()) {
                        try {
                            fromForum = br.readLine();
                            a.append(fromForum+"\n");
                        } catch (IOException ex) {
                            CloseKolchi(socket, bw, br);
                        }
                    }
                }
            }
            ).start();
        } catch (Exception ex) {}
    }
        public boolean Send(String msg,String clientUsr){ // BufferedWriter bw
        boolean etat = false;
        try {
            String o = msg;
            if (socket.isConnected()) {
                String MsgToSend = o;
                bw.write(clientUsr + ": " + MsgToSend);
                bw.newLine();
                bw.flush();
                etat = true;
            }

        } catch (IOException ex) {}
        return etat;
    }
        private void CloseKolchi(Socket socket,BufferedWriter bw, BufferedReader br){
        try{
            if(bw != null){bw.close();}
            if(br != null){br.close();}
            if(socket != null){socket.close();}
        }catch(IOException ex){}
    }
    
            //login;
            public String Slogin(String usr,String pass) {String info = "LoginRequest;"+usr+";"+pass; return info;}
            public String login(String g) throws IOException{
            //info = "LoginRequest;"+usr+";"+pass;
            BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader brr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bwr.write(g);
            bwr.newLine();
            bwr.flush(); 
            String etat = brr.readLine();
            return etat;
    }
            
            //Register
            public String SRegister(String usr,String pass,String nom,String prenom) {String info = "RegisterRequest;"+usr+";"+pass+";"+nom+";"+prenom; return info;}
            public String Register(String g) throws IOException{
            BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader brr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bwr.write(g);
            bwr.newLine();
            bwr.flush(); 
            String etat = brr.readLine();
            return etat;
    }
    
// End API
}
