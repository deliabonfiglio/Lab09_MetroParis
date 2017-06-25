package it.polito.tdp.metrodeparis.model;

public class FermataConLinea {

	private Fermata fermata;
	private int id_linea;
	
	public FermataConLinea(Fermata fermata, int id_linea) {
		super();
		this.fermata = fermata;
		this.id_linea = id_linea;
	}

	public Fermata getFermata() {
		return fermata;
	}

	public void setFermata(Fermata fermata) {
		this.fermata = fermata;
	}

	public int getId_linea() {
		return id_linea;
	}

	public void setId_linea(int id_linea) {
		this.id_linea = id_linea;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fermata == null) ? 0 : fermata.hashCode());
		result = prime * result + id_linea;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FermataConLinea other = (FermataConLinea) obj;
		if (fermata == null) {
			if (other.fermata != null)
				return false;
		} else if (!fermata.equals(other.fermata))
			return false;
		if (id_linea != other.id_linea)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return fermata+" "+id_linea;
	}


	
	
}
