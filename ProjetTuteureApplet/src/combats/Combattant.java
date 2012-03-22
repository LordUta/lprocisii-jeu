package combats;

import java.io.Serializable;

import org.newdawn.slick.Animation;

public abstract class Combattant implements Serializable {
	private static final long serialVersionUID = 924099034084323818L;
	protected String nom;
	protected int pvMax, pvCourant, attaque, vitesse;
	protected int xCombat, yCombat;
	protected boolean enAttaque;
	protected transient Animation sprite;
	
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
	 * @return le nombre de d�gats inflig�s
	 */
	public int attaquer(Combattant c){
		System.out.println("\n"+ nom +" attaque "+c.getNom()+" !");
		int degatInflige = (int) (getAttaque() + (getAttaque() * (Math.random() -0.5) * 0.30));
		int pvFinaux = c.getPvCourant()-degatInflige;
		System.out.println(c.getNom()+" perd "+degatInflige+" pv!");
		c.setPvCourant(pvFinaux);
		
		if(c.getPvCourant() <= 0){
			System.out.println("Il est KO! OH MON DIEU!");
			return degatInflige+c.getPvCourant();
		}
		else{
			System.out.println("Il ne lui reste plus que "+pvFinaux+" pv.");
			return degatInflige;
		}
	}
	
	/**
	 * Attaquer avec des d�gats pr�d�finies (online)
	 */
	public int attaquer(Combattant c, int degatsInflige){
		System.out.println("\n"+ nom +" attaque "+c.getNom()+" !");
		int pvFinaux = c.getPvCourant()-degatsInflige;
		System.out.println(c.getNom()+" perd "+degatsInflige+" pv!");
		c.setPvCourant(pvFinaux);
		
		if(c.getPvCourant() <= 0){
			System.out.println("Il est KO! OH MON DIEU!");
			return degatsInflige+c.getPvCourant();
		}
		else{
			System.out.println("Il ne lui reste plus que "+pvFinaux+" pv.");
			return degatsInflige;
		}
	}
	
	/**
	 * D�place un combattant lorsqu'il attaque
	 * @return true quand le combattant a termin� son d�placement.
	 */
	public abstract boolean deplacementAttaque(int delta, int departX, int destinationX);
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Combattant other = (Combattant) obj;
		if (nom == null) {
			if (other.nom != null)
				return false;
		} else if (!nom.equals(other.nom))
			return false;
		return true;
	}

	public boolean estEnVie(){
		return this.getPvCourant() > 0;
	}


	public int getAttaque() {
		return attaque;
	}


	public String getNom() {
		return nom;
	}


	public int getPvCourant() {
		return pvCourant;
	}


	public int getPvMax() {
		return pvMax;
	}


	public Animation getSprite() {
		return sprite;
	}

	public int getVitesse() {
		return vitesse;
	}

	public int getXCombat() {
		return xCombat;
	}

	public int getYCombat() {
		return yCombat;
	}

	/**
	 * N�cessaire de mettre �a dans une m�thode � part pour
	 * recr�er localement les animations.
	 */
	public abstract void initAnimation();

	public boolean isKO(){
		return pvCourant<=0;
	}

	public void setPvCourant(int pvCourant) {
		this.pvCourant = pvCourant;
	}

	public void setXCombat(int xCombat) {
		this.xCombat = xCombat;
	}
	
	public void setYCombat(int yCombat) {
		this.yCombat = yCombat;
	}
	
}
