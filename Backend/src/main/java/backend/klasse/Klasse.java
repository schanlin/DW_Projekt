package backend.klasse;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import backend.Database;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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

//	public List<Student> getStudents() {
//		
//	}

	@JsonGetter
	public int getKlassenID() {
		return klassenID;
	}

}
