package game;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import ingame.Combat;
import ingame.Exploration;
import gui.Menu;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import rmi.interfaces.DispatcherInterface;

import constantes.Constantes;



public class MainGame extends StateBasedGame{
	private static DispatcherInterface remoteReference;


	public MainGame() {
		super("Projet Tuteur�");
		try { 
			if (Constantes.MODE_ONLINE){
				System.out.println("Connexion en cours...");
				System.setProperty("java.rmi.server.hostname", Constantes.IP_SERVEUR);
				Registry registry = LocateRegistry.getRegistry(Constantes.IP_SERVEUR, Constantes.REGISTRY_PORT);
				System.out.println(registry);
				// EN FAIT FAIRE UNE REFERENCE REPREND UN PORT AU HASARD :/
				remoteReference = (DispatcherInterface) Naming.lookup("rmi://"+Constantes.IP_SERVEUR+":"+Constantes.REGISTRY_PORT+"/"+Constantes.REGISTRY_NAME);
//				remoteReference = (DispatcherInterface) registry.lookup(Constantes.REGISTRY_NAME);
				System.out.println(remoteReference);
				System.out.println("Connexion �tablie, normalement.");
			}
		} catch (Exception e){
			e.printStackTrace();
			System.out.println("Erreur de connexion, s�rement. Passage en mode Hors Ligne.");
			Constantes.MODE_ONLINE=false;
		}
	}

	public void initStatesList(GameContainer container) {
		addState(new Exploration(Constantes.GAMEPLAY_MAP_STATE));
		addState(new Menu(Constantes.MENU_MAP_STATE));
		addState(new Combat(Constantes.COMBAT_STATE));
	}

	public static void main(String[] argv) { 

		try{
			AppGameContainer container = new AppGameContainer(new MainGame()); 
			container.setVSync(true);
			container.setAlwaysRender(true);
			container.setDisplayMode(640,480,false); 
			container.start(); 


		} catch (SlickException e) { 
			e.printStackTrace(); 
		}
	}

	public static DispatcherInterface getRemoteReference() {
		return remoteReference;
	} 

}
