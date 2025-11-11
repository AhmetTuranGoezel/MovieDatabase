package com.backend.test;

import com.backend.model.Movie;
import com.backend.repository.MovieRepository;
import com.backend.service.MovieService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieTest {

    @InjectMocks
    MovieService movieService = new MovieService();
    @Mock
    MovieRepository movieRepository;

    public Movie movieBuilder(int movieId,String title,String category,String releaseDate,String movieLength,String director,
                                String writer,String cast){
        Movie movie= new Movie(movieId,title,category,releaseDate,movieLength,director,writer,cast,null);

        return movie;
    }

    @Test
    void getAllMovies() {
        when(movieRepository.findAll()).thenReturn(List.of(new Movie[]{(
                 movieBuilder(1,"Spider Man 3","Action","10.06.2009","120min",
                 "unknown","unknown","unknownToo")),
                 movieBuilder(2,"Batman","Action","10.06.2010","130min",
                                "unknown","unknown","unknownToo"),
                 movieBuilder(3,"Jujutsu Kaisen 0","Action","10.06.2010","130min",
                        "unknown","unknown","unknownToo")}));

        ResponseEntity<List<Movie>> response = movieService.getAllMovies();

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().size(),3);

    }

    @Test
    void getMoviesById() {
        when(movieRepository.findById(1)).thenReturn(Optional.ofNullable(movieBuilder(1,"Spider Man 3","Action","10.06.2009","120min",
                "unknown","unknown","unknownToo")));
        ResponseEntity<Movie> response = movieService.getMoviesById(1);

        assertEquals(response.getBody().getTitle(),"Spider Man 3");
        assertEquals(response.getStatusCode(),HttpStatus.OK);

    }

    @Test
    void createMovie() {
        Movie movie= movieBuilder(1,"Spider Man 3","Action","10.06.2009","120min",
                "unknown","unknown","unknownToo");
        ResponseEntity<String> response = movieService.createMovie(movie);

        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertEquals(response.getBody(),"Movie was successfully created!");
    }

    @Test
    void editMovie() {
        Movie movie= movieBuilder(1,"Batman","Action","10.06.2010","140min",
                "unknown","unknown","unknownToo");
        ResponseEntity<String> response = movieService.editMovie(1,movie);
        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertEquals(response.getBody(),"Film was successfully edited!");

    }

    @Test
    void deleteMovie() {
        Movie movie= movieBuilder(1,"Batman","Action","10.06.2010","140min",
                "unknown","unknown","unknownToo");
        ResponseEntity<String> response=movieService.deleteMovie(movie.getMovieId());
        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertEquals(response.getBody(),"Movie deleted!");
    }
}