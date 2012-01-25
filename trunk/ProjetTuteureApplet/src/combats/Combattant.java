package combats;

import org.newdawn.slick.Animation;

public abstract class Combattant {
	protected String nom;
	protected int pvMax, pvCourant, attaque, vitesse;
	protected int xCombat;
	protected boolean enAttaque;
	protected Animation sprite;
	
	
	public Combattant(){
		this.enAttaque = true;
	}
	
	public Combattant(String nom, int pvMax, int pvCourant, int attaque,
			int vitesse) {
		this.nom = nom;
		this.pvMax = pvMax;
		this.pvCourant = pvCourant;
		this.attaque = attaque;
		this.vitesse = vitesse;
		this.enAttaque = true;
	}

	/**
	 * @param Combattant
	 * @return true si l'ennemi est KO
	 */
	public boolean attaquer(Combattant c){
		System.out.println("\n"+ nom +" attaque "+c.getNom()+" !");
		int degatInflige = (int) (getAttaque() + (getAttaque() * (Math.random() -0.5) * 0.30));
		int pvFinaux = c.getPvCourant()-degatInflige;
		System.out.println(c.getNom()+" perd "+degatInflige+" pv!");
		c.setPvCourant(pvFinaux);
		
		if(c.getPvCourant() <= 0){
			System.out.println("Il est KO! OH MON DIEU!");
//			this.afficherDegatsRecu(degatInflige+getPvCourant());
			return true;
		}
		else{
			System.out.println("Il ne lui reste plus que "+pvFinaux+" pv.");
			return false;
		}
	}
	
	/**
	 * D�place un combattant lorsqu'il attaque
	 * @return true quand le combattant a termin� son d�placement.
	 */
	public abstract boolean deplacementAttaque(int delta, int departX, int destinationX);

	public int getPvCourant() {
		return pvCourant;
	}


	public void setPvCourant(int pvCourant) {
		this.pvCourant = pvCourant;
	}


	public String getNom() {
		return nom;
	}


	public int getPvMax() {
		return pvMax;
	}


	public int getAttaque() {
		return attaque;
	}


	public int getVitesse() {
		return vitesse;
	}

	public Animation getSprite() {
		return sprite;
	}

	public int getXCombat() {
		return xCombat;
	}

	public void setXCombat(int xCombat) {
		this.xCombat = xCombat;
	}

	
}
