package it.scigot.DB;

public class Farmaci_a {

	private int _id;
	private String principio_attivo;
	private String denominazione;
	private String prezzo;
	private String ditta;

	public Farmaci_a(int _id, String principio_attivo, String denominazione, String prezzo, String ditta) {

		super();
		this.set_id(_id);
		this.setPrincipio_attivo(principio_attivo);
		this.setDenominazione(denominazione);
		this.setPrezzo(prezzo);
		this.setDitta(ditta);

	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getPrincipio_attivo() {
		return principio_attivo;
	}

	public void setPrincipio_attivo(String principio_attivo) {
		this.principio_attivo = principio_attivo;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(String prezzo) {
		this.prezzo = prezzo;
	}

	public String getDitta() {
		return ditta;
	}

	public void setDitta(String ditta) {
		this.ditta = ditta;
	}
}
