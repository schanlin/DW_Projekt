package backend.klasse;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;



@RestController
public class KlassenController {

	private final KlasseDao klasseDao;

	public KlassenController(KlasseDao klasseDao) {
		this.klasseDao = klasseDao;
	}

	@GetMapping("/klasse")
	public List<Klasse> getAllKlassen() throws SQLException {
		List<Klasse> klassen = klasseDao.findAll();
		return klassen;
	}
	
	@GetMapping("/klasse/{id}")
	public Klasse getKlasseById(@PathVariable int id) throws SQLException {
		Klasse klasse = klasseDao.findKlasseById(id);
		return klasse;
	}

}
