package backend.klasse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
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
	public List<Klasse> getAllKlassen() {
		List<Klasse> klassen = klasseDao.findAll();
		return klassen;
	}
	
	@GetMapping("/klasse/{id}")
	public Klasse getKlasseById(@PathVariable int id) {
		Klasse klasse = klasseDao.findById(id);
		if (klasse==null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return klasse;
	}

	@DeleteMapping("/klasse/{id}")
	public void deleteKlasse(@PathVariable int id) {
		int status = klasseDao.delete(id);
		if (status==0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

}
