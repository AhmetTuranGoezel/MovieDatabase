package com.backend.controller;

import com.backend.model.Movie;
import com.backend.service.MovieService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MovieController {

    @Autowired
    MovieService movieService;


    @GetMapping(value="/movies/")
    public ResponseEntity<List<Movie>> getAllMovies() {

        return movieService.getAllMovies();
    }


    @GetMapping(value="/movies/{id}")
    public ResponseEntity<Movie> getMoviesById(@PathVariable("id") int id) {

        return movieService.getMoviesById(id);
    }

    @PostMapping(value = "/movies/create/")
    public ResponseEntity<String> createMovie(@RequestBody Movie movie) {

        return movieService.createMovie(movie);

    }


    @PutMapping(value = "/movies/update/{id}")
    public ResponseEntity<String> editMovie(@PathVariable int id,@RequestBody Movie movie) {

        return movieService.editMovie(id, movie);
    }

    @DeleteMapping("/movies/delete/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable int id){

        return movieService.deleteMovie(id);
    }


    @DeleteMapping("movie/resetStats/{movieId}")
    public ResponseEntity<String> resetStats(@PathVariable int movieId){
        return movieService.resetStats(movieId);
    }

}
