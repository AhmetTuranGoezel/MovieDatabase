package com.backend.service;


import com.backend.model.HistoryMovie;
import com.backend.model.Movie;
import com.backend.model.Review;
import com.backend.repository.HistoryMovieRepository;
import com.backend.repository.MovieRepository;
import com.backend.repository.ReviewRepository;
import com.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@Service
public class MovieService {


    @Autowired
    MovieRepository movieRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    HistoryMovieRepository historyMovieRepository;
    @Autowired
    ReviewRepository reviewRepository;



    public ResponseEntity<List<Movie>> getAllMovies() {

        return new ResponseEntity<>(movieRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<Movie> getMoviesById(@PathVariable("id") int id) {

        return new ResponseEntity<>(movieRepository.findById(id).get(), HttpStatus.OK);
    }

    public ResponseEntity<String> createMovie(Movie movie) {

        movieRepository.save(movie);

        return new ResponseEntity<>("Movie was successfully created!", HttpStatus.OK);
    }

    public ResponseEntity<String> editMovie(int id, Movie movie){

        movieRepository.save(movie);
        return new ResponseEntity<>("Film was successfully edited!",HttpStatus.OK);
    }


    public ResponseEntity<String> deleteMovie(int id) {

        movieRepository.deleteById(id);
        return new ResponseEntity<>("Movie deleted!",HttpStatus.OK);
    }

    /*public ResponseEntity<Integer> getTotalCountOfViews(int movieId){

        int count = 0;
        List<HistoryMovie> moviesForStats = movieRepository.findById(movieId).get().getMoviesForStats();
        System.out.println(moviesForStats.toString());
        for(HistoryMovie historyMovie:moviesForStats){
            if(historyMovie.getMovie().getMovieId() == movieId){
                System.out.println("Siu");
                count++;
            }
        }
        return new ResponseEntity<>(count, HttpStatus.OK);
    }*/

    public void createHistoryMovie(HistoryMovie historyMovie){
        historyMovieRepository.save(historyMovie);

        Movie movie =  movieRepository.findById(historyMovie.getMovie().getMovieId()).get();
        int views = movie.getViews();
        movie.setViews(views+1);
        movieRepository.save(movie);

    }
    /*public void addMovieToStatsList (HistoryMovie historyMovie){

        movieRepository.findById(historyMovie.getMovie().getMovieId()).get().getMoviesForStats().add(historyMovie);
        System.out.println(movieRepository.findById(historyMovie.getMovie().getMovieId()).get().getMoviesForStats().toString());
    }*/


    /*public void addReviewToStatsList (Review review){
        movieRepository.findById(review.getMovie().getMovieId()).get().getReviewsForStats().add(review);
        System.out.println("Review added to stats dikka");
    }*/

   /* public ResponseEntity<Integer> getTotalCountOfRatings(int movieId) {

        return new ResponseEntity<>(movieRepository.findById(movieId).get().getReviewsForStats().size(), HttpStatus.OK);
    }*/




    public ResponseEntity<String> resetStats(int movieId){

       /* List<HistoryMovie> resetHistoryMoviesForStats = new LinkedList<>();
        movieRepository.findById(movieId).get().setMoviesForStats(resetHistoryMoviesForStats);*/

        Movie movie = movieRepository.findById(movieId).get();

        movie.setViews(0);
        movie.setReviewCount(0);
        movie.setAverageRating(0);

       /* List<Review> resetReviewsForStats = new LinkedList<>();
        movie.setReviewsForStats(resetReviewsForStats);*/

        movieRepository.save(movie);

        for (Review review:reviewRepository.findAll()){
            if(review.getMovie().getMovieId() == movieId){
                reviewRepository.delete(review);
            }
        }
       // movieRepository.findById(movieId).get().setReviewsForStats(resetReviewsForStats);

        return new ResponseEntity<>("Total count of movie views has been reset!",HttpStatus.OK);
    }
}