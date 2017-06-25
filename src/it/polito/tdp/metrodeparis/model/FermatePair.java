package it.polito.tdp.metrodeparis.model;

public class FermatePair {
	private int id_linea;
	private int id_partenza;
	private int id_arrivo;
	public FermatePair(int id_linea, int id_partenza, int id_arrivo) {
		super();
		this.id_linea = id_linea;
		this.id_partenza = id_partenza;
		this.id_arrivo = id_arrivo;
	}
	public int getId_linea() {
		return id_linea;
	}
	public void setId_linea(int id_linea) {
		this.id_linea = id_linea;
	}
	public int getId_partenza() {
		return id_partenza;
	}
	public void setId_partenza(int id_partenza) {
		this.id_partenza = id_partenza;
	}
	public int getId_arrivo() {
		return id_arrivo;
	}
	public void setId_arrivo(int id_arrivo) {
		this.id_arrivo = id_arrivo;
	}

}
