package backend.klasse;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;



@RestController
public class KlassenController {

	private final KlasseDao klasseDao;

	public KlassenController(KlasseDao klasseDao) {
		this.klasseDao = klasseDao;
	}

	@PostMapping("/klasse")
	public Klasse postKlasse(@RequestBody Klasse klasse) {
		return klasseDao.insert(klasse);
	}

	@GetMapping("/klasse")
	public List<Klasse> getAllKlassen() {
		return klasseDao.findAll();
	}
	
	@GetMapping("/klasse/{id}")
	public Klasse getKlasseById(@PathVariable int id) {
		Klasse klasse = klasseDao.findById(id);
		if (klasse==null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return klasse;
	}

	@PutMapping("/klasse/{id}")
	public void putKlasse(@RequestBody Klasse klasse, @PathVariable int id) {
		int status = klasseDao.update(klasse, id);
		if (status==0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/klasse/{id}")
	public void deleteKlasse(@PathVariable int id) {
		int status = klasseDao.delete(id);
		if (status==0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

}
