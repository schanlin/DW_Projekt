package backend;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.LinkedList;
import java.util.List;



@RestController
public class KlassenController {
	
	@GetMapping("/klasse")
	public List<Klasse> getAllKlassen(){
		List<Klasse> klassen = new LinkedList<>();
		klassen = Klasse.findAllKlassen();
		return klassen;
	}
	
	@GetMapping("/klasse/{id}")
	public Klasse getKlasseById(@PathVariable int id) {
		Klasse klasse = Klasse.findKlasseById(id);
		return klasse;
	}

}
