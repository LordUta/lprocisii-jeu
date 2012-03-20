/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package command;


import beans.InventaireBean;
import beans.InventaireDB;
import beans.JoueurBean;
import beans.JoueurDB;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Loic
 */
public class TestCommand implements Command {

    @Override
    public String getCommandName() {
        return "testCommand";
    }

    @Override
    public ActionFlow actionPerform(HttpServletRequest request) {
        /*
        try {
            //Class.forName("org.gjt.mm.mysql.Driver");
            Class.forName("com.mysql.jdbc.Driver");
            
            String url = "jdbc:mysql://localhost:3306";
            String user = "root";
            String password = null;
            Connection con = DriverManager.getConnection( url, user, password ) ;
            System.out.println("GG");
        }
        catch(Exception ex) 
        { 
            System.out.println("Fail du chargement du driver J mysql");
        }*/
        
        
        JoueurBean j = new JoueurBean("test", "ee", "dd");
        JoueurDB jdb = new JoueurDB();
        jdb.creerJoueur(j);
        
        InventaireBean inv = new InventaireBean(j.getIdJoueur());
        InventaireDB idb = new InventaireDB();
        idb.createInvenaire(inv);
        
        InventaireBean inv2 = idb.jsonInventaire(j.getIdJoueur());
        System.out.println (inv2.getIdJoueur());
        
        String vue = "index";
        return new ActionFlow(vue, vue+".jsp", false);
    }
    
}
