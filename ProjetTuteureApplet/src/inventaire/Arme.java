package inventaire;

public class Arme extends Objet {
	private static final long serialVersionUID = -2848016707692117469L;

	public Arme(int id) {
		super(id);
		switch(id){
			case 0 :
				nom = "Poings";
				valeur = 0;
			break;
			case 1 :
				nom = "�p�e l�g�re";
				valeur = 25;
				break;
			case 2 :
				nom = "�p�e sacr�e";
				valeur = 130;
				break;
		}
	}

}
