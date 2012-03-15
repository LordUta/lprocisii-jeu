package rmi.serveur;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UID;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;

import combats.Combattant;
import combats.Monstre;

import constantes.Constantes;
import game.Callbacker;
import game.Player;

import rmi.interfaces.ChatRemoteInterface;
import rmi.interfaces.DispatcherInterface;
import rmi.interfaces.ReceiverInterface;

/**
 * LE SERVEUR RMI
 * NE PAS IMPORTER DANS LE JEU
 */
public class Serveur implements DispatcherInterface {
	private ArrayList<ReceiverInterface> listeRefJoueurs;
	private ArrayList<Player> listeJoueurs;

	public Serveur(){
		listeRefJoueurs = new ArrayList<ReceiverInterface>();
		listeJoueurs = new ArrayList<Player>();
		new SupprimerOfflineThread(this).start();
	}

	public static void main(String[] args){
		System.out.println("Dispatcher");
		System.setSecurityManager (null);
		try {
			//			System.setProperty("java.rmi.server.hostname", Constantes.IP_SERVEUR);
			DispatcherInterface server = new Serveur();
			DispatcherInterface proxy = (DispatcherInterface) UnicastRemoteObject.exportObject(server, 25465);
			Registry registry = LocateRegistry.createRegistry(Constantes.REGISTRY_PORT);
			registry.rebind(Constantes.REGISTRY_NAME, proxy);

			System.out.println("Serveur du Jeu : OK");

			ChatRemoteInterface remoteReference = (ChatRemoteInterface) UnicastRemoteObject.exportObject(new Chat(), 7777);
			registry.rebind(Constantes.REGISTRY_NAME_CHAT, remoteReference);

			System.out.println("Serveur du Chat : OK");

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void inscription(Player p, ReceiverInterface client) throws RemoteException {
		this.listeRefJoueurs.add(client);
		synchronized(listeJoueurs){
			this.listeJoueurs.add(p);
		}
	}

	/**
	 * @return les joueurs d'une m�me map et toujours le leader du groupe auquel appartient le joueur
	 */
	@Override
	public ArrayList<Player> updateListe(UID id, String idMap) throws RemoteException {
		ArrayList<Player> res = new ArrayList<Player>();
		for (Player p : listeJoueurs)
			if(!p.getId().equals(id) && (p.getMapId().equals(idMap) || (p.getGroupe()!=null && p.getGroupe().contains(id))))
				res.add(p);
		return res;
	}

	/**
	 * On update le joueur dans notre liste
	 */
	@Override
	public void updatePosition(Player recu) throws RemoteException {
		//		Player update;
		if(listeJoueurs.contains(recu)){
			//			update = listeJoueurs.get(listeJoueurs.indexOf(recu));
			listeJoueurs.set(listeJoueurs.indexOf(recu), recu);
			//			update.setPosition(recu.getPosition());
			//			update.setDirection(recu.getDirection());
			//			update.setMapId(recu.getMapId());
			//			update.setGroupe(recu.getGroupe());
		}
	}

	@Override
	public boolean inviterJoueur(Player leader, Player invite) throws RemoteException{
		for(Player p : listeJoueurs){
			if(invite.equals(p)){
				ReceiverInterface ri = getReferenceCorrespondante(p);
				if(!ri.getPlayer().containsInvitation(leader.getGroupe())){
					ri.addInvitation(leader.getGroupe());
					return true;
				}
			}
		}
		return false;
	}

	private ReceiverInterface getReferenceCorrespondante(Player p){
		return listeRefJoueurs.get(listeJoueurs.indexOf(p));
	}

	@Override
	public void accepterInvitation(Player leader, Player invite) throws RemoteException {
		for(Player p : listeJoueurs){
			if(leader.equals(p)){
				ReceiverInterface ri = getReferenceCorrespondante(p);
				ri.invitationAcceptee(invite);
			}
		}
	}

	@Override
	public void disbandGroup(UID groupID) throws RemoteException {
		for(ReceiverInterface ri : listeRefJoueurs)
			for(Player p : listeJoueurs)
				if(p.equals(ri.getPlayer()))
					ri.disbandGroup();
	}

	public synchronized void retirerReferencesNeRepondantPas(){
		synchronized(listeJoueurs){
			int i=-1;
			for (Iterator<ReceiverInterface> iterator = listeRefJoueurs.iterator(); iterator.hasNext();) {
				ReceiverInterface ri = (ReceiverInterface) iterator.next();
				i++;
				try {
					ri.getPlayer();
				} catch (RemoteException e) {
					listeJoueurs.remove(i);
					i--;
					iterator.remove();
					System.out.println("Un joueur ne r�pondant pas a �t� supprim�.");
				}
			}
		}
	}

	@Override
	public void entreEnModeCombat(Player leader, ArrayList<Player> listeJoueurs, ArrayList<Monstre> listeMonstre) throws RemoteException {
		// on r�cup�re les joueurs du groupe du leader
		for(Player p : listeJoueurs){
			if(!p.equals(leader))
			//			if(p.getGroupe()!=null && p.getGroupe().equals(leader.getGroupe())){
				getReferenceCorrespondante(p).entrerEnCombat(listeJoueurs, listeMonstre);
			//			}
		}
	}

	@Override
	public void attaquer(Player emetteur, Combattant cible, int degats) throws RemoteException {
		// on r�cup�re les joueurs du groupe de l'emetteur
		System.out.println("Methode attaquer d�tect�e");
		for(Player p : emetteur.getListeJoueursCombatEnCours()){
			System.out.println(p.getId());
			if(!p.equals(emetteur)){
//			if(!p.equals(emetteur) && p.getGroupe()!=null && p.getGroupe().equals(emetteur.getGroupe())){
				getReferenceCorrespondante(p).attaquer(cible, degats);
//			}
			}
		}
	}

	@Override
	public void seSoigner(Player emetteur, int soin) throws RemoteException {
		// on r�cup�re les joueurs du groupe de l'emetteur
		for(Player p : listeJoueurs){
			if(!p.equals(emetteur) && p.getGroupe()!=null && p.getGroupe().equals(emetteur.getGroupe())){
				getReferenceCorrespondante(p).seSoigner(emetteur, soin);
			}
		}
	}

}
