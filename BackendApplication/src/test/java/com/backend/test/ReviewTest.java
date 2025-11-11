package com.backend.test;

import com.backend.model.Movie;
import com.backend.model.Review;
import com.backend.model.User;
import com.backend.repository.ReviewRepository;
import com.backend.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewTest {
    @InjectMocks
    ReviewService reviewService = new ReviewService();
    @Mock
    ReviewRepository reviewRepositoryMock;



    public Review reviewBuilder(int reviewId,int rating,String comment,int userId,String forename,String surname,String email
                                ,long password,String username,String dateOfBirth,int movieId,String title,String category,
                                String releaseDate, String movieLength, String director,String writer, String cast) {

        Review review =
                new Review(reviewId,rating,comment,
                        new User(userId,forename,surname,email,password,username,dateOfBirth, null, null),
                        new Movie(movieId,title,category,releaseDate,movieLength,director,writer,cast, null));

        return review;
    }


    //Verweis auf AdminStatsTest createReview()-Methode, da diese im 3. Zyklus erweitert wurde.
    /*@Test
    void createReview() {
        Review review = reviewBuilder(1,5,"Nice one",1,"Emre","Kubat",
        "emre.kubat@stud.uni-due.de",12345,"mQ","24.04.1996",1,"Spider-Man",
        "Action","10.05.2009","120 min","Irgendjemand","Weißichnicht","Keine Ahnung");
        ResponseEntity<String> response = reviewService.createReview(review);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(),"Review created!");
    }*/

    @Test
    void getReviewsByUserId() {
        when(reviewRepositoryMock.findAll()).thenReturn(List.of(new Review[]{
                reviewBuilder(1,5,"Nice one",1,"Emre","Kubat",
                "emre.kubat@stud.uni-due.de",12345,"mQ","24.04.1996",1,"Spider-Man",
                "Action","10.05.2009","120 min","Irgendjemand","Weißichnicht","Keine Ahnung"),
                reviewBuilder(2,5,"Nice one",1,"Emre","Kubat",
                "emre.kubat@stud.uni-due.de",12345,"mQ","24.04.1996",1,"Batman",
                "Action","10.05.2009","130 min","Irgendjemand","Weißichnicht","Keine Ahnung"),
                reviewBuilder(3,5,"Nice one",2,"Furkan","Bedirhanoglu",
                        "furkan.bedirhanoglu@stud.uni-due.de",12345,"vFurkii","13.08.1998",1,"Spider-Man",
                        "Action","10.05.2009","120 min","Irgendjemand","Weißichnicht","Keine Ahnung")}));
        ResponseEntity<List<Review>> response = reviewService.getReviewsByUserId(1);
        ResponseEntity<List<Review>> secondResponse = reviewService.getReviewsByUserId(2);
        assertEquals(response.getStatusCode(),HttpStatus.OK );
        assertEquals(response.getBody().size(), 2);
        assertEquals(response.getBody().get(1).getUser().getUsername(), "mQ");
        assertEquals(secondResponse.getBody().get(0).getUser().getUsername(), "vFurkii");
    }

    @Test
    void getReviewsByMovieId() {
        when(reviewRepositoryMock.findAll()).thenReturn(List.of(new Review[]{
                reviewBuilder(1,5,"Nice one",1,"Emre","Kubat",
                "emre.kubat@stud.uni-due.de",12345,"mQ","24.04.1996",1,"Spider-Man",
                "Action","10.05.2009","120 min","Irgendjemand","Weißichnicht","Keine Ahnung"),
                reviewBuilder(2,2,"Boring!",1,"Furkan","Bedirhanoglu",
                "furkan.bedirhanoglu@stud.uni-due.de",12345,"vFurkii","13.08.1998",1,"Spider-Man",
                "Action","10.05.2009","120 min","Irgendjemand","Weißichnicht","Keine Ahnung"),}));
        ResponseEntity<List<Review>> response = reviewService.getReviewsByMovieId(1);
        assertEquals(response.getStatusCode(),HttpStatus.OK );
        assertEquals(response.getBody().size(), 2);
        assertEquals(response.getBody().get(0).getUser().getUsername(), "mQ");
        assertEquals(response.getBody().get(0).getRating(),5);
        assertEquals(response.getBody().get(0).getMovie().getTitle(),"Spider-Man");
        assertEquals(response.getBody().get(1).getUser().getUsername(), "vFurkii");

    }

    @Test
    void getReviewById() {
        when(reviewRepositoryMock.findById(1)).thenReturn(Optional.ofNullable(reviewBuilder(1, 5, "Nice one", 1, "Emre", "Kubat",
                "emre.kubat@stud.uni-due.de", 12345, "mQ", "24.04.1996", 1, "Spider-Man",
                "Action", "10.05.2009", "120 min", "Irgendjemand", "Weißichnicht", "Keine Ahnung")));
        ResponseEntity<Review> response = reviewService.getReviewById(1);
        assertEquals(response.getStatusCode(),HttpStatus.OK );
        assertEquals(response.getBody().getUser().getUsername(), "mQ");
        assertEquals(response.getBody().getComment(), "Nice one");

    }


}