package it.scigot.DB;

public class Farmacie_trento {

	private int _id;
	private String indirizzo;
	private String nome;
	private int cap;
	private String comune;
	private String frazione;
	private String latitudine;
	private String longitudine;

//	public Farmacie_trento(int _id, String indirizzo, String nome, int cap, String comune, String frazione, String latitudine, String longitudine) {
//		super();
//		this.set_id(_id);
//		this.setIndirizzo(indirizzo);
//		this.setNome(nome);
//		this.setCap(cap);
//		this.setComune(comune);
//		this.setFrazione(frazione);
//		this.setLatitudine(latitudine);
//		this.setLongitudine(longitudine);
//	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getCap() {
		return cap;
	}

	public void setCap(int cap) {
		this.cap = cap;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getFrazione() {
		return frazione;
	}

	public void setFrazione(String frazione) {
		this.frazione = frazione;
	}

	public String getLatitudine() {
		return latitudine;
	}

	public void setLatitudine(String latitudine) {
		this.latitudine = latitudine;
	}

	public String getLongitudine() {
		return longitudine;
	}

	public void setLongitudine(String longitudine) {
		this.longitudine = longitudine;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

}
