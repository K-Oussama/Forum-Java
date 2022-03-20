/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cforum;

import javax.swing.JOptionPane;



/**
 *
 * @author Oussama KORCHI 30
 */
public class cforum {
    
    // server host + port
    public static String Client() {
        int p = 0;
        String host = JOptionPane.showInputDialog("Enter host adress: ");
        do {
            String pr = JOptionPane.showInputDialog("Enter Port: (entre 4000-65535)");
            p = Integer.parseInt(pr);
        } while (p <= 4000 || p > 65535);
        return host + ";" + p;
    }
    
    
    public static void main(String[] args){
        String find = Client();
        String[] ee = find.split(";");
        int p = Integer.parseInt(ee[1]);
        Home pre = new Home(ee[0],p);
        Register r = new Register();
        r.setVisible(true);
    }
}
