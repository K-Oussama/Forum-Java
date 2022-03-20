/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fserver;

/**
 *
 * @author acer
 */
public class compte {
     private String nom;
    private String prenom;
    private String username;
    private String password;
    private boolean isActif;
    private boolean isApproved;
    

    public compte(String nom, String prenom, String username,String password, boolean isActif,boolean isApproved) {
        this.nom = nom;
        this.prenom = prenom;
        this.username = username;
        this.password = password;
        this.isActif = isActif;
        this.isApproved = isApproved;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public boolean getIsActif() {
        return isActif;
    }

    public void setIsActif(boolean isActif) {
        this.isActif = isActif;
    }
    
    public boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }
}