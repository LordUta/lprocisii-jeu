package rmi.interfaces;


import game.Groupe;
import game.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UID;
import java.util.ArrayList;

import combats.Combattant;
import combats.Monstre;

/**
 * Les m�thode que le serveur va appeler � distance.
 */
public interface ReceiverInterface extends Remote{
	
	public void addInvitation(Groupe g) throws RemoteException;
	public void attaquer(Combattant cible, int degats) throws RemoteException;
	public void disbandGroup()  throws RemoteException;
	// -------------
	// COMBAT
	// -------------
	/**
	 * Rejoint le leader en combat
	 */
	public void entrerEnCombat(ArrayList<Player> listeJoueurs, ArrayList<Monstre> listeMonstre) throws RemoteException;
	
	
	
	public void equiperArmure(Player emetteur, int armure) throws RemoteException;
    
	public Player getPlayer() throws RemoteException;
	
	public void invitationAcceptee(Player invite) throws RemoteException;

	public void invitationRefusee(Player refus) throws RemoteException;

	public void kicked() throws RemoteException;
	
	/**
	 * Si un groupe se fait dissoudre alors que le joueur �tait invit� dans ce groupe
	 * @param groupID
	 */
	public void removeInvitation(UID groupID) throws RemoteException;
	
	public void seSoigner(Player emetteur, int soin) throws RemoteException;
}
