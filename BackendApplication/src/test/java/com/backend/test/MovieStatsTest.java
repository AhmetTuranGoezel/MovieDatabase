package com.backend.test;

import com.backend.model.Movie;
import com.backend.model.Review;
import com.backend.model.User;
import com.backend.repository.MovieRepository;
import com.backend.repository.ReviewRepository;
import com.backend.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;


import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovieStatsTest {

    @InjectMocks
    ReviewService reviewService = new ReviewService();
    @Mock
    ReviewRepository reviewRepository;
    @Mock
    MovieRepository movieRepository;
    @InjectMocks
    List<Review> reviewList = new LinkedList<>();


    public Review reviewbuilder(int reviewId, int rating, String comment, String dateAdded, User user, Movie movie){
        Review review = new Review();
        review.setReviewId(reviewId);
        review.setRating(rating);
        review.setComment(comment);
        review.setDateAdded(dateAdded);
        review.setUser(user);
        review.setMovie(movie);

        return review;
    }



    public User userBuilder(int accountId, String forename, String surname, String username){
        User user = new User();
        user.setAccountID(accountId);
        user.setForename(forename);
        user.setSurname(surname);
        user.setUsername(username);
        return user;
    }

    public Movie movieBuilder(int movieId,String title){

        Movie movie = new Movie();
        movie.setMovieId(movieId);
        movie.setTitle(title);
        return movie;
    }

    public Movie movieBuilder2(int movieId,String title,int views, int reviewCount, double averageRating){

        Movie movie = new Movie();
        movie.setMovieId(movieId);
        movie.setTitle(title);
        movie.setViews(views);
        movie.setReviewCount(reviewCount);
        movie.setAverageRating(averageRating);
        return movie;
    }

    @Test
    void createReview(){


        Movie movie =movieBuilder2(1,"Spider Man 1",10,4,4.5);

        Review review = reviewbuilder(5,2,"Awesome","2022-07-10",
                userBuilder(5,"Emre","Kubat","mQ")
        ,movieBuilder2(1,"Spider Man 1",10,4,4.5));

        reviewList.add(reviewbuilder(1,5,"Awesome","2022-07-10",
                userBuilder(1,"Turan","Gözel","Owl")
                ,movieBuilder(1,"Spider Man 1")));
        reviewList.add(reviewbuilder(2,5,"Great!","2022-07-10",
                userBuilder(2,"Furkan","Be","Furkiboy98")
                ,movieBuilder(1,"Spider Man 1")));
        reviewList.add(reviewbuilder(3,4,"It was okay","2022-07-10",
                userBuilder(3,"Dilan","Özyol","Dilo62")
                ,movieBuilder(1,"Spider Man 1")));
        reviewList.add(reviewbuilder(4,4,"It was okay","2022-07-10",
                userBuilder(5,"Robert","Heß","Pun1sher")
                ,movieBuilder(1,"Spider Man 1")));



        when(movieRepository.findById(1)).thenReturn(Optional.ofNullable(movie));
        when(reviewRepository.findAll()).thenReturn(reviewList);

        ResponseEntity<String> response = reviewService.createReview(review,5);

        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody(),"You already posted a Review to that Movie!");
        assertEquals(movieRepository.findById(1).get().getReviewCount(),4);
        assertEquals(movieRepository.findById(1).get().getAverageRating(),4.5);

    }


    @Test
    void  getReviewsByMovieId(){
        reviewList.add(reviewbuilder(1,5,"Awesome","2022-07-10",
                userBuilder(1,"Turan","Gözel","Owl")
                ,movieBuilder(1,"Spider Man 1")));
        reviewList.add(reviewbuilder(2,5,"Great!","2022-07-10",
                userBuilder(2,"Furkan","Be","Furkiboy98")
                ,movieBuilder(1,"Spider Man 1")));
        reviewList.add(reviewbuilder(3,4,"It was okay","2022-07-10",
                userBuilder(3,"Dilan","Özyol","Dilo62")
                ,movieBuilder(1,"Spider Man 1")));
        reviewList.add(reviewbuilder(3,4,"It was okay","2022-07-10",
                userBuilder(4,"Robert","Heß","Pun1sher")
                ,movieBuilder(1,"Spider Man 1")));

        when(reviewRepository.findAll()).thenReturn(reviewList);
        ResponseEntity<List<Review>> reviewsByMovieId = reviewService.getReviewsByMovieId(1);
        assertEquals(reviewsByMovieId.getBody().size(),4);
        assertEquals(reviewsByMovieId.getStatusCode(),HttpStatus.OK);

    }

    @Test
    void setAverageRatingOfMovie(){


        Movie movie = movieBuilder(1,"Spider Man 1");

        reviewList.add(reviewbuilder(1,5,"Awesome","2022-07-10",
                userBuilder(1,"Turan","Gözel","Owl")
                ,movieBuilder(1,"Spider Man 1")));
        reviewList.add(reviewbuilder(2,5,"Great!","2022-07-10",
                userBuilder(2,"Furkan","Be","Furkiboy98")
                ,movieBuilder(1,"Spider Man 1")));
        reviewList.add(reviewbuilder(3,4,"It was okay","2022-07-10",
                userBuilder(3,"Dilan","Özyol","Dilo62")
                ,movieBuilder(1,"Spider Man 1")));
        reviewList.add(reviewbuilder(3,4,"It was okay","2022-07-10",
                userBuilder(4,"Robert","Heß","Pun1sher")
                ,movieBuilder(1,"Spider Man 1")));

        when(reviewRepository.findAll()).thenReturn(reviewList);
        Double average =reviewService.setAverageRatingOfMovie(movie);

        assertEquals(average,4.5);
    }


}
