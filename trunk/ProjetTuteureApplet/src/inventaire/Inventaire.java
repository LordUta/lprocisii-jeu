package inventaire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Inventaire extends HashMap<Objet, Nombre> {

	public Inventaire() {
		super();
		put(new Potion(), new Nombre());
		put(new Arme(1), new Nombre());
		put(new Arme(2), new Nombre());
		put(new Armure(1), new Nombre());
		put(new Armure(2), new Nombre());
	}

	public Inventaire(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public Inventaire(int initialCapacity) {
		super(initialCapacity);
	}

	
	public Inventaire(Map<? extends Objet, ? extends Nombre> m) {
		super(m);
	}

	/**
	 * @return le nombre de PV r�cup�r�
	 */
	public int retirerPotion(){
		for(Objet o : this.keySet())
			if(o instanceof Potion){
				if(!this.get(o).isEmpty()){
					this.get(o).decrementer();
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
		if(this.containsKey(o)){
			this.get(o).incrementer();
		}
	}
	
	public void addObjets(ArrayList<Objet> listeO){
		for(Objet o : listeO)
			addObjet(o);
	}
	
	/**
	 * �quipe une arme possed�e et d�s�quipe l'autre 
	 * @param arme � �quiper
	 * @return true si l'arme a bien �t� �quip�e
	 * TODO A VOIR AVEC LES POINTEURS, MARCHE SUREMENT PAS
	 */
	public boolean equiperArme(Arme a){
		if(this.containsKey(a)){
			for (Objet possede : this.keySet())
				if(possede instanceof Arme)
					this.get(possede).setEquipe(false);
			this.get(a).setEquipe(true);
			return true;
		}
		return false;
	}

	/**
	 * �quipe une armure possed�e et d�s�quipe l'autre 
	 * @param arme � �quiper
	 * @return true si l'arme a bien �t� �quip�e
	 * TODO A VOIR AVEC LES POINTEURS, MARCHE SUREMENT PAS
	 */
	public boolean equiperArmure(Armure a){
		if(this.containsKey(a)){
			for (Objet possede : this.keySet())
				if(possede instanceof Armure)
					this.get(possede).setEquipe(false);
			this.get(a).setEquipe(true);
			return true;
		}
		return false;
	}
	
	/**
	 * @return l'attaque bonus procur�e par l'arme actuellement �quip�e.
	 * @return 0 si aucune arme �quip�e
	 */
	public int getAttaqueBonus(){
		for (Objet o : this.keySet())
			if ((o instanceof Arme) && (this.get(o)).isEquipe())
				return o.valeur;
		return 0;
	}

	/**
	 * @return les PV bonus procur�e par l'armure actuellement �quip�e.
	 * @return 0 si aucune arme �quip�e
	 */
	public int getPVBonus(){
		for (Objet o : this.keySet())
			if ((o instanceof Armure) && (this.get(o)).isEquipe())
				return o.valeur;
		return 0;
	}

	public String toString(){
		String res="INVENTAIRE : \n";
		for(Objet o : this.keySet())
			res+="- "+o.getNom()+" NOMBRE = "+this.get(o).getNombre()+"\n";
		return res;
	}
	
}
