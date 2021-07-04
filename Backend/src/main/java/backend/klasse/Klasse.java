package backend.klasse;

import com.fasterxml.jackson.annotation.JsonGetter;

public class Klasse {
	private Integer klassenID;
	private String klassenName;

	Klasse(int klassenId, String klassenName) {
		this.klassenID = klassenId;
		this.klassenName = klassenName;
	}
	
	public Klasse(String klassenName) {
		this.klassenID = null;
		this.klassenName = klassenName;
	}

	@JsonGetter
	public String getKlassenName() {
		return klassenName;
	}


	@JsonGetter
	public int getKlassenID() {
		return klassenID;
	}

}
