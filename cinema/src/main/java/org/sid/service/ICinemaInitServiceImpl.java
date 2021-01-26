package org.sid.service;

import org.sid.dao.CategorieRepository;
import org.sid.dao.CinemaRepository;
import org.sid.dao.FilmRepository;
import org.sid.dao.PlaceRepository;
import org.sid.dao.ProjectionRepository;
import org.sid.dao.SalleRepository;
import org.sid.dao.SeanceRepository;
import org.sid.dao.TicketRepository;
import org.sid.dao.VilleRepository;
import org.sid.entities.Categorie;
import org.sid.entities.Cinema;
import org.sid.entities.Film;
import org.sid.entities.Place;
import org.sid.entities.Salle;
import org.sid.entities.Seance;
import org.sid.entities.Ticket;
import org.sid.entities.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.List;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.stream.Stream;

import javax.transaction.Transactional;
@Transactional
@Service
public class ICinemaInitServiceImpl implements ICinemaInitService {
	@Autowired
	private VilleRepository villeRepository;
	@Autowired
	private CinemaRepository cinemaRepository;
	@Autowired
	private SalleRepository salleRepository;
	@Autowired
	private PlaceRepository placeRepository;
	@Autowired
	private SeanceRepository seanceRepository;
	@Autowired
	private CategorieRepository categorieRepository;
	@Autowired
	private FilmRepository filmRepository;
	@Autowired
	private ProjectionRepository projectionRepository;
	@Autowired
	private TicketRepository ticketRepository;

	@Override
	@Transactional
	public void initVilles() {
		Stream.of("Casablanca", "Marrakech", "Rabat", "Tanger").forEach(nomVille -> {
			Ville ville = new Ville();
			ville.setName(nomVille);
			villeRepository.save(ville);
		});

	}

	@Override
	@Transactional
	public void initCinemas() {
		villeRepository.findAll().forEach(nomVille -> {
			Stream.of("Megarama", "IMAX", "Dawliz").forEach(nomCinema -> {
				Cinema cinema = new Cinema();
				cinema.setName(nomCinema);
				cinema.setVille(nomVille);
				cinema.setNombreSalles(3 + (int) (Math.random() * 7));
				cinemaRepository.save(cinema);
			});
		});
	}

	@Override
	@Transactional
	public void initSalles() {
		cinemaRepository.findAll().forEach(cinema -> {
			for (int i = 0; i < cinema.getNombreSalles(); i++) {
				Salle salle = new Salle();
				salle.setName("Salle " + (i + 1));
				salle.setCinema(cinema);
				salle.setNombrePlace(15 + (int) (Math.random() * 20));
				salleRepository.save(salle);
			}
		});

	}

	@Override
	@Transactional
	public void initPlaces() {
		salleRepository.findAll().forEach(salle -> {
			for (int i = 0; salle.getNombrePlace() > i; i++) {
				Place place = new Place();
				place.setNumero((i + 1));
				place.setSalle(salle);
				placeRepository.save(place);
			}
		});

	}

	@Override
	@Transactional
	public void initSeances() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		Stream.of("12:00", "15:00", "17:00", "19:00", "21:00").forEach(heureSeance -> {
			Seance seance = new Seance();
			try {
				seance.setHeureDebut(dateFormat.parse(heureSeance));
				seanceRepository.save(seance);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	@Transactional
	public void initCategories() {
		Stream.of("Histoire", "Action", "Fiction", "Horreur").forEach(nomCategorie -> {
			Categorie categorie = new Categorie();
			categorie.setName(nomCategorie);
			categorieRepository.save(categorie);
		});
	}

	@Override
	@Transactional
	public void initFilms() {
		double[] durees = new double[] { 1, 1.5, 2, 2.5 };
		java.util.List<Categorie> categories = categorieRepository.findAll();
		Stream.of("Wrong Turn", "Fast And Furious", "Jumanji", "The Banker").forEach(titre -> {
			Film film = new Film();
			film.setTitre(titre);
			film.setDuree(durees[new Random().nextInt(durees.length)]);
			film.setAffiche(titre.replace(" ", "")+".jpg");
			film.setCategorie(categories.get(new Random().nextInt(categories.size())));
			filmRepository.save(film);
		});
	}

	@Override
	@Transactional
	public void initProjections() {
		double[] prix=new double[] {30,40,50,60};
		villeRepository.findAll().forEach(ville->{
			ville.getCinemas().forEach(cinema->{
				cinema.getSalles().forEach(salle->{
					filmRepository.findAll().forEach(film->{
						seanceRepository.findAll().forEach(seance->{
							org.sid.entities.Projection projection=new org.sid.entities.Projection();
							projection.setDateProjection(new Date());
							projection.setFilm(film);
							projection.setPrix(prix[new Random().nextInt(prix.length)]);
							projection.setSalle(salle);
							projection.setSeance(seance);
							projectionRepository.save(projection);
						});
					});
				});
			});
		});
	}

	@Override
	@Transactional
	public void initTickets() {
		projectionRepository.findAll().forEach(projection->{
			projection.getSalle().getPlaces().forEach(place->{
				Ticket ticket=new Ticket();
				ticket.setPlace(place);
				ticket.setPrix(projection.getPrix());
				ticket.setProjection(projection);
				ticket.setReserve(false);
				ticketRepository.save(ticket);
			});
		});
	}

}
