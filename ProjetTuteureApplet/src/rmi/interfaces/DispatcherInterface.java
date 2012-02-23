package rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UID;
import java.util.ArrayList;

import combats.Combattant;
import combats.Monstre;

import game.Player;


/**
 * Les m�thodes qu'on appelle du serveur.
 */
public interface DispatcherInterface extends Remote
{
    /**
     * Connecte un joueur au serveur
     * @param joueur
     */
    public void inscription(Player player, ReceiverInterface client) throws RemoteException;
    
    
    /**
     * Permet au joueur de r�cup�rer la liste des autres joueurs updat�
     */
    public ArrayList<Player> updateListe(UID id, String idMap) throws RemoteException;
    
    /**
     * Permet au serveur de mettre � jour un joueur
     * @param p
     * @throws RemoteException
     */
    public void updatePosition(Player p) throws RemoteException;
    
    /**
     * @param leader
     * @param invite
     * @return true si l'invitation a �t� envoy�e
     * @throws RemoteException
     */
    public boolean inviterJoueur(Player leader, Player invite) throws RemoteException;
    
    public void accepterInvitation(Player leader, Player invite) throws RemoteException;
    
    /**
     * Suppression du groupe : tous les membres quittent ce groupe
     * @param groupID
     * @throws RemoteException
     */
    public void disbandGroup(UID groupID) throws RemoteException;
    
    /**
     * Lorsque le leader d'un groupe non vide entre en combat : on pr�vient les autres
     * @param leader
     * @param listeMonstre 
     * @throws RemoteException
     */
    public void entreEnModeCombat(Player leader, ArrayList<Monstre> listeMonstre) throws RemoteException;

    /**
     * Utilis� en combat lorsqu'un montre ou un joueur attaque
     * (si monstre, appel� par leader.)
     * @param cible
     */
    public void attaquer(Player emetteur, Combattant cible, int degats) throws RemoteException;
}
