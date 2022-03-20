/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author acer
 */
public class CompteDB {
    
    public static final List<compte> Etudiants = new ArrayList<>();  
    public static final List<compte> all = new ArrayList<>();  
    static Connection Con = null;
    static Statement St = null;
    static ResultSet Res = null;
    static int ID=0;
    
    // Clint handler
    public static List<compte> LoginRequest(String u, String p) throws SQLException{
            Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/estsforum","root","root");
            St = Con.createStatement();
            Res= St.executeQuery("SELECT * from login");
            while(Res.next()){
                    String Mytrait = Res.getString("username");
                    String Mytrait2 = Res.getString("password");
                    if(u.equals(Mytrait) && p.equals(Mytrait2)){
                        //infos = "Event;"+Res.getString("username")+";"+Res.getString("nom")+";"+Res.getString("nom")+";"+Res.getString("isActive")+";"+Res.getString("isApproved");
                        compte e = new compte(Res.getString("nom"),Res.getString("prenom"),Res.getString("username"),Res.getString("password"),Res.getBoolean("isActive"),Res.getBoolean("isApproved"));
                        Etudiants.add(e);
                    }else{/*infos = "les informations de connexion ne sont pas correctes";*/}
            }
            Con.close();
    return Etudiants;
}    
    public static String RegisterRequest(String username,String password,String nom,String prenom) throws SQLException {
    String msg = null;
    Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/estsforum","root","root");
    boolean check = UsrChech(username);
    if(check){
            LoginCount();
            var add = Con.prepareStatement("INSERT INTO login VALUES (?,?,?,?,?,?,?,?)");
            add.setInt(1, ID);
            add.setString(2, username);
            add.setString(3, nom);
            add.setString(4, prenom);
            add.setString(5, username);
            add.setString(6, password);
            add.setString(7, "0");
            add.setString(8, "0");
            int row = add.executeUpdate();
            compte e = new compte(nom,prenom,username,password,false,false);
            Etudiants.add(e);
            msg = "Success";
    }
    else{msg = "used";}
    Con.close();
    
     return msg;
}
    
    
    // Managed by the server
    public static List<compte> List() throws SQLException{
            Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/estsforum","root","root");
            St = Con.createStatement();
            Res= St.executeQuery("SELECT * from login");
            while(Res.next()){
                    compte e = new compte(Res.getString("nom"),Res.getString("prenom"),Res.getString("username"),Res.getString("password"),Res.getBoolean("isActive"),Res.getBoolean("isApproved"));
                    all.add(e);
            }
            Con.close();
    return all;
    }
    public static boolean Activate(String usr){
        boolean b = false;
        try {
            Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/estsforum","root","root");
            var add = Con.prepareStatement("UPDATE login SET isActive =? WHERE username=?");
            add.setString(1, "1");
            add.setString(2, usr);
            add.executeUpdate();
            b = true;
        } catch (SQLException ex) {
            Logger.getLogger(CompteDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    return b;
    }
    public static boolean Desactivate(String usr){
        boolean b = false;
        try {
            Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/estsforum","root","root");
            var add = Con.prepareStatement("UPDATE login SET isActive =? WHERE username=?");
            add.setString(1, "0");
            add.setString(2, usr);
            add.executeUpdate();
            b = true;
        } catch (SQLException ex) {
            Logger.getLogger(CompteDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    return b;
    }
    public static boolean Approve(String usr){
        boolean b = false;
        try {
            Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/estsforum","root","root");
            var add = Con.prepareStatement("UPDATE login SET isApproved =? WHERE username=?");
            add.setString(1, "1");
            add.setString(2, usr);
            add.executeUpdate();
            b = true;
        } catch (SQLException ex) {
            Logger.getLogger(CompteDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    return b;
    }
    public static boolean Decline(String usr){
        boolean b = false;
        try {
            Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/estsforum","root","root");
            var add = Con.prepareStatement("UPDATE login SET isApproved =? WHERE username=?");
            add.setString(1, "0");
            add.setString(2, usr);
            add.executeUpdate();
            b = true;
        } catch (SQLException ex) {
            Logger.getLogger(CompteDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    return b;
    }
       
       // used inside this class
        private static void LoginCount(){
        try{
            Statement St1 = Con.createStatement();
            ResultSet Res1 = St1.executeQuery("SELECT MAX(idlogin) from login");
        Res1.next();
        ID = Res1.getInt(1)+1;
        }
        catch(SQLException e){}
    }
        private static boolean UsrChech(String usr) throws SQLException{
       boolean c = true; 
       try{
            Statement St1 = Con.createStatement();
            ResultSet Res1 = St1.executeQuery("SELECT username from login");
            while(Res1.next()){
                String u = Res1.getString("username");
                if(usr.equals(u)){
                    c = false;
                }
            }
        }
        catch(SQLException e){}
        return c;
   }
   
    public static List<compte> getComptes(){return Etudiants;} 
    public static List<compte> getComptesFromfile(){
    try{ 
      File file = new File("users.txt");
      //créer l'objet filereader
      FileReader fr = new FileReader(file);  
      //créer l'objet bufferreader
      BufferedReader br = new BufferedReader(fr);  
      String line;
     while((line = br.readLine()) != null){
         //split permet de récupérer tout les elements du tableau
         //ajoute la ligne au buffer
        String[] parts = line.split(";");
      Etudiants.add(new compte( parts[0],parts[1],parts[2],parts[3],true,true));  
     } fr.close(); }
    catch(IOException e){e.printStackTrace();}
return Etudiants;
}
    static Iterable<compte> getEtudiant() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
}
}

