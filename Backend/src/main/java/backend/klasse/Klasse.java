package backend.klasse;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import backend.Datenbank;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonGetter;

public class Klasse {
	private int klassenID;
	private String klassenName;

	public Klasse(int klassenID, String klassenName) {
		this.klassenID = klassenID;
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
