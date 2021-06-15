package backend;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;



@RestController
public class KlassenController {
	
	@GetMapping("/klasse")
	public List<Klasse> getAllKlassen() throws SQLException {
		List<Klasse> klassen = new LinkedList<>();
		klassen = Klasse.findAll();
		return klassen;
	}
	
	@GetMapping("/klasse/{id}")
	public Klasse getKlasseById(@PathVariable int id) throws SQLException {
		Klasse klasse = Klasse.findKlasseById(id);
		return klasse;
	}

}
