package inventaire;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

/**
 * En fait utiliser une hashmap c'�tait pas le top.
 * J'aurai du utiliser une arraylist je pense mais maintenant que c'est fait..
 */
public class Inventaire extends Observable implements Serializable{
	private HashMap<Objet, Nombre> inventaire;
	public Inventaire() {
		super();
		inventaire = new HashMap<Objet, Nombre>();
		inventaire.put(new Potion(), new Nombre());
		inventaire.put(new Arme(1), new Nombre());
		inventaire.put(new Arme(2), new Nombre());
		inventaire.put(new Armure(1), new Nombre());
		inventaire.put(new Armure(2), new Nombre());
	}

	/**
	 * @return le nombre de PV r�cup�r�
	 */
	public int retirerPotion(){
		for(Objet o : inventaire.keySet())
			if(o instanceof Potion){
				if(!inventaire.get(o).isEmpty()){
					inventaire.get(o).decrementer();
					setChanged();
					notifyObservers();
					return o.valeur;
				}
			}
		return 0;
	}
	
	/**
	 * Ajoute un objet dans l'inventaire
	 * @param Objet � ajouter
	 * TODO A VOIR AVEC LES POINTEURS, MARCHE SUREMENT PAS
	 */
	public void addObjet(Objet o){
		if(inventaire.containsKey(o)){
			inventaire.get(o).incrementer();
			setChanged();
			notifyObservers();
		}
	}
	
	public void addObjets(ArrayList<Objet> listeO){
		for(Objet o : listeO)
			addObjet(o);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * �quipe une arme possed�e et d�s�quipe l'autre 
	 * @param arme � �quiper
	 * @return true si l'arme a bien �t� �quip�e
	 * TODO A VOIR AVEC LES POINTEURS, MARCHE SUREMENT PAS
	 */
	public boolean equiperArme(Arme a){
		if(inventaire.containsKey(a)){
			for (Objet possede : inventaire.keySet())
				if(possede instanceof Arme)
					inventaire.get(possede).setEquipe(false);
			inventaire.get(a).setEquipe(true);
			setChanged();
			notifyObservers();
			return true;
		}
		return false;
	}
	

	/**
	 * �quipe une armure possed�e et d�s�quipe l'autre 
	 * @param arme � �quiper
	 * @return true si l'arme a bien �t� �quip�e
	 */
	public boolean equiperArmure(Armure a){
		if(inventaire.containsKey(a)){
			for (Objet possede : inventaire.keySet())
				if(possede instanceof Armure && inventaire.get(possede).isEquipe())
					inventaire.get(possede).setEquipe(false);
			inventaire.get(a).setEquipe(true);
			System.out.println(a.getNom()+" �quip�e.");
			setChanged();
			notifyObservers();
			return true;
		}
		return false;
	}
	
	public void desequiperArmure(){
			for (Objet possede : inventaire.keySet())
				if(possede instanceof Armure && inventaire.get(possede).isEquipe()){
					inventaire.get(possede).setEquipe(false);
					System.out.println(possede.getNom()+" d�s�quip�e.");
					setChanged();
					notifyObservers();
				}
	}
	
	public void desequiperArme(){
		for (Objet possede : inventaire.keySet())
			if(possede instanceof Arme && inventaire.get(possede).isEquipe()){
				inventaire.get(possede).setEquipe(false);
				System.out.println(possede.getNom()+" d�s�quip�e.");
				setChanged();
				notifyObservers();
			}
	}
	
	/**
	 * @return l'attaque bonus procur�e par l'arme actuellement �quip�e.
	 * @return 0 si aucune arme �quip�e
	 */
	public int getAttaqueBonus(){
		for (Objet o : inventaire.keySet())
			if ((o instanceof Arme) && (inventaire.get(o)).isEquipe())
				return o.valeur;
		return 0;
	}

	/**
	 * @return les PV bonus procur�e par l'armure actuellement �quip�e.
	 * @return 0 si aucune arme �quip�e
	 */
	public int getPVBonus(){
		for (Objet o : inventaire.keySet())
			if ((o instanceof Armure) && (inventaire.get(o)).isEquipe())
				return o.valeur;
		return 0;
	}
	
	public int getArmureEquipeeId(){
		for (Objet o : inventaire.keySet())
			if ((o instanceof Armure) && (inventaire.get(o)).isEquipe())
				return o.id;
		return 0;
	}

	public int getArmeEquipeeId(){
		for (Objet o : inventaire.keySet())
			if ((o instanceof Arme) && (inventaire.get(o)).isEquipe())
				return o.id;
		return 0;
	}

	@Override
	public String toString(){
		String res="INVENTAIRE : \n";
		for(Objet o : inventaire.keySet()){
			res+="- "+o.getNom()+" NOMBRE = "+inventaire.get(o).getNombre();
			if (inventaire.get(o).isEquipe())
				res += "(Equip�)";
			res += "\n";
		}
		return res;
	}
	
	public String toStringHTML(){
		String res="<h3>INVENTAIRE :</h3><ul>";
		for(Objet o : inventaire.keySet()){
			res+="<li>"+o.getNom()+" NOMBRE = "+inventaire.get(o).getNombre();
			if (inventaire.get(o).isEquipe())
				res += "(Equip�)";
			res += "</li>";
		}
		return res+"</ul>";
	}
	
}
