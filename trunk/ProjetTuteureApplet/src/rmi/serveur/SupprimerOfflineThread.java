package rmi.serveur;

import java.rmi.RemoteException;

import exploration.OnlineUpdateThread;
import game.MainGame;

/**
 * V�rifier toutes les 5 secondes les joueurs qui r�pondent
 * Si ils ne r�pondent pas, on les retire
 *
 */
public class SupprimerOfflineThread extends Thread{
	private Serveur serveur;
	
	public SupprimerOfflineThread(Serveur serveur){
		this.serveur = serveur;
	}
	
	public synchronized void run(){
		while(true){
					serveur.retirerReferencesNeRepondantPas();
				try {
					OnlineUpdateThread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
	}
	
}
