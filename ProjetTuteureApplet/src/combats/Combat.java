package combats;


import inventaire.Objet;

import java.applet.Applet;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import netscape.javascript.JSObject;

import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tests.xml.Entity;

import constantes.Constantes;
import exploration.Exploration;
import exploration.Player;
import game.AppletGameContainer;
import gui.GUIValeursCombats;



public class Combat extends BasicGameState{
	private int stateID;
	private ArrayList<Monstre> listeMonstre;

	private boolean attaquer, selectionCible, tourJoueur, tourEnnemi;
	private int definirCible, tour;
	private Combattant combattantPlusRapide;

	private int attaqueJoueurX, destinationAttaqueJoueurX; // peut pas mettre dans constante car par rapport au bord..
	private boolean joueurAttaque;
	private Image background;

	private GUIValeursCombats degatsDisplay, dropDisplay;

	public Combat(int stateID){
		this.stateID = stateID;
	}

	public Combat(){
	}

	@Override
	public int getID() {
		return stateID;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		background = new Image(Constantes.BATTLE_BACKGROUND_LOCATION+"battleBackground.jpg");
	}



	public void leave(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.leave(container, game);
		// r�activaiton des boutons
		if(container instanceof AppletGameContainer.Container){
			Applet applet = ((AppletGameContainer.Container) container).getApplet();
			JSObject jso = JSObject.getWindow(applet);
			jso.call("activerBoutons", null);
		}
		// fin de l'affichage
		degatsDisplay = null;
		dropDisplay = null;
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);

		//on d�sactive les boutons de la page
		if(container instanceof AppletGameContainer.Container){
			Applet applet = ((AppletGameContainer.Container) container).getApplet();
			JSObject jso = JSObject.getWindow(applet);
			jso.call("desactiverBoutons", null);
		}

		listeMonstre = new ArrayList<Monstre>();
		for (int i=0; i<Math.random()*4; i++){
			listeMonstre.add(new Monstre());
		}
		attaquer = true;
		selectionCible = false;
		tourJoueur=false;
		tourEnnemi=false;
		tour=0;
		definirCible=0;

		attaqueJoueurX = container.getWidth()-80;
		destinationAttaqueJoueurX = container.getWidth()-80 - 30;
		joueurAttaque = false;

		Exploration.getPlayer().setXCombat(attaqueJoueurX);
		Exploration.getPlayer().setYCombat(container.getHeight()/2);

		int espace = 30;
		for(Monstre m : listeMonstre){
			m.setXCombat(Constantes.POSX_COMBAT_MONSTRE);
			m.setYCombat(espace+=60);
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		background.draw(0, 0, container.getWidth(), container.getHeight()-Constantes.HAUTEUR_MENU_BAS_COMBAT);
		Exploration.getPlayer().getSprite().draw(Exploration.getPlayer().getXCombat(),Exploration.getPlayer().getYCombat());


		for(Monstre m : listeMonstre){
			m.getSprite().draw(m.getXCombat(), m.getYCombat());

			//afficher PV
			g.setColor(Color.black);
			g.drawString(m.getPvCourant()+"/"+m.getPvMax(), m.getXCombat(), m.getYCombat()+46);
			g.setColor(Color.lightGray);
			g.drawString(m.getPvCourant()+"/"+m.getPvMax(), m.getXCombat(), m.getYCombat()+45);
		}

		// Menu combat
		int positionH = container.getHeight()-Constantes.HAUTEUR_MENU_BAS_COMBAT;
		g.drawLine(0, positionH, container.getWidth(), positionH);
		if (attaquer){
			g.setColor(Color.white);
			g.drawString(Constantes.SELECTION+"Attaquer", 50, positionH+10);
			g.setColor(Color.gray);
			g.drawString("Utiliser potion", 50, positionH+30);
			if(selectionCible && !this.listeMonstre.isEmpty()){
				g.drawString(Constantes.SELECTION, this.listeMonstre.get(definirCible).getXCombat(), this.listeMonstre.get(definirCible).getYCombat()+20);
			}
		}
		else{
			g.drawString("Attaquer", 50, positionH+10);
			g.setColor(Color.white);
			g.drawString(Constantes.SELECTION+"Utiliser potion", 50, positionH+30);
			g.setColor(Color.gray);
		}

		g.setColor(Color.black);
		g.drawString(Exploration.getPlayer().getPvCourant()+"/"+Exploration.getPlayer().getPvMax(), Exploration.getPlayer().getXCombat()-20, Exploration.getPlayer().getYCombat()+35);
		g.setColor(Color.lightGray);
		g.drawString(Exploration.getPlayer().getPvCourant()+"/"+Exploration.getPlayer().getPvMax(), Exploration.getPlayer().getXCombat()-20, Exploration.getPlayer().getYCombat()+34);
		//affichage des d�gats

		if(degatsDisplay != null){
			degatsDisplay.render(container, game, g);
		}
		if(dropDisplay != null){
			dropDisplay.render(container, game, g);
		}

	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();

		// animation joueur
		Exploration.getPlayer().updateCombat(container, game, delta);

		// drops eventuels
		if(dropDisplay != null && dropDisplay.isAffiche())
			dropDisplay.update(container, game, delta);
		
		//on attend la fin d'affichage des d�gats pour continuer
		if(degatsDisplay != null && degatsDisplay.isAffiche())
			degatsDisplay.update(container, game, delta);
		else {
			//si tous les ennemis sont morts, on arr�te le combat
			if(listeMonstre.isEmpty()){
				Exploration.getPlayer().finCombat();
				game.enterState(Constantes.GAMEPLAY_MAP_STATE);
			}
			
			// si le joueur (seul pour l'instant) est ko, on stop aussi en fait
			if(Exploration.getPlayer().getPvCourant()<=0){
				Exploration.getPlayer().finCombat();
				Exploration.getPlayer().setPvCourant(Exploration.getPlayer().getPvMax());
				game.enterState(Constantes.GAMEPLAY_MAP_STATE);
			}
			
			
			//avant de naviguer dans le menu, on regarde si on est le plus rapide
			//-- liste de tous les combattants :
			ArrayList<Combattant> listeCombattant = new ArrayList<Combattant>();
			listeCombattant.add(Exploration.getPlayer());
			listeCombattant.addAll(this.listeMonstre);

			// on d�fini qui doit commencer � jouer
			if(!tourJoueur && !tourEnnemi){
				if(tour>=listeCombattant.size())
					tour=0;
				combattantPlusRapide = getPlusRapide(listeCombattant, tour);
				if(combattantPlusRapide instanceof Player){
					tourJoueur=true;
					tourEnnemi=false;
					choisirAction(input, delta);
				}
				else{
					tourEnnemi=true;
				}

				//fin du tour
				tour++;
			}

			// c'est au tour de l'ennemi d'attaquer
			if(tourEnnemi){
				if(combattantPlusRapide.deplacementAttaque(delta, Constantes.POSX_COMBAT_MONSTRE, Constantes.POSX_ATTAQUE_MONSTRE)){
					tourEnnemi = false;
					int degats = combattantPlusRapide.attaquer(Exploration.getPlayer());
					degatsDisplay = new GUIValeursCombats(Exploration.getPlayer().getXCombat(), container.getHeight()/2, Integer.toString(degats));
				}
			}
			if(tourJoueur){
				choisirAction(input, delta);
			}

		}




		//DEBUG : sortir du combat
		if (input.isKeyPressed(Input.KEY_F1)){
			Exploration.getPlayer().finCombat();
			game.enterState(Constantes.GAMEPLAY_MAP_STATE);
		}
	}


	/**
	 * @param listeCombattant, la liste de combattant
	 * @param numero
	 * @return le Nieme combattant le plus rapide de la liste
	 * Par exemple, getPlusRapide(liste, 0) retourne le plus rapide.
	 */
	private Combattant getPlusRapide(ArrayList<Combattant> listeCombattant,  int numero){
		ArrayList<Combattant> temp = (ArrayList<Combattant>) listeCombattant.clone();
		ArrayList<Combattant> sortedList = new ArrayList<Combattant>();
		Combattant rapide = null;

		for (int i=0; i<listeCombattant.size(); i++){
			int plusRapide=0;
			for (Combattant c : temp){
				if(c.getVitesse() > plusRapide){
					plusRapide = c.getVitesse();
					rapide = c;
				}
			}
			sortedList.add(rapide);
			temp.remove(rapide);
		}
		System.out.println("Le plus rapide c'est "+sortedList.get(numero).getNom());
		return sortedList.get(numero);
	}


	/**
	 * Autorise le joueur � choisir une action dans le menu
	 * (c'est donc � son tour de jouer)
	 */
	public void choisirAction(Input input, int delta){
		// naviguation dans le "menu"

		if(!joueurAttaque){
			if(!selectionCible){
				if(input.isKeyPressed(Input.KEY_DOWN) || input.isKeyPressed(Input.KEY_UP))
					attaquer = !attaquer;

				if(input.isKeyPressed(Input.KEY_ENTER)){
					// s'il choisit d'attaquer
					if(attaquer && !selectionCible){
						selectionCible = true;
					}
					else {
						int soin = Exploration.getPlayer().utiliserPotion();
						tourJoueur=false;
						degatsDisplay = new GUIValeursCombats(Exploration.getPlayer().getXCombat(), Exploration.getPlayer().getYCombat(), Integer.toString(soin), 2);
					}
				}
			}

			//selection d'une cible
			else {
				if(input.isKeyPressed(Input.KEY_DOWN))
					if(definirCible < listeMonstre.size()-1)
						definirCible++;
					else definirCible=0;
				else if(input.isKeyPressed(Input.KEY_UP))
					if(definirCible>0)
						definirCible--;
					else definirCible=listeMonstre.size()-1;
				else if(input.isKeyPressed(Input.KEY_ENTER)){
					// on attaque
					joueurAttaque = true;
				}
				else if (input.isKeyPressed(Input.KEY_ESCAPE)){
					//on annule
					selectionCible = false;
				}
			}
		}
		else {
			if (Exploration.getPlayer().deplacementAttaque(delta, attaqueJoueurX, destinationAttaqueJoueurX)){
				joueurAttaque = false;
				int degats = Exploration.getPlayer().attaquer(listeMonstre.get(definirCible));
				degatsDisplay = new GUIValeursCombats(listeMonstre.get(definirCible).getXCombat(), listeMonstre.get(definirCible).getYCombat(), Integer.toString(degats));
				if(!listeMonstre.get(definirCible).estEnVie()){
					// si l'ennemi est KO
					ArrayList<Objet> drops = listeMonstre.get(definirCible).drop();
					// si drop
					if(drops!=null && !drops.isEmpty()){
						String drop = "";
						if(drops.size()>1){
							for (Objet o : drops)
								drop+=o.getNom()+", ";
						drop+=" dropp�s!";
						}
						else drop+=drops.get(0).getNom()+" dropp�!";
						dropDisplay = new GUIValeursCombats(listeMonstre.get(definirCible).getXCombat(), listeMonstre.get(definirCible).getYCombat(), drop, 3);
						Exploration.getPlayer().getInventaire().addObjets(drops);
					}
					listeMonstre.remove(definirCible);
					definirCible=0;
				}
				selectionCible=false;
				tourJoueur=false;
			}
		}
	}

}
