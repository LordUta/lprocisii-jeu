package inventaire;

public class Arme extends Objet {

	public Arme(int id) {
		super(id);
		switch(id){
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
