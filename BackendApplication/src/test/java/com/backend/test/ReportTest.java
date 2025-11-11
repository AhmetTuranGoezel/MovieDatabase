package com.backend.test;

import com.backend.model.Movie;
import com.backend.model.Report;
import com.backend.model.User;
import com.backend.repository.ReportRepository;
import com.backend.repository.SystemadministratorRepository;
import com.backend.repository.UserRepository;
import com.backend.service.EmailService;
import com.backend.service.ReportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ReportTest {

    @InjectMocks
    ReportService reportService = new ReportService();

    @InjectMocks
    EmailService emailService;
    @Mock
    ReportRepository  reportRepository;
    @Mock
    SystemadministratorRepository systemadministratorRepository;
    @Mock
    UserRepository userRepository;

    public Report reportBuilder(int reportId,String text,int userId,String forename,String surname,String email
            ,long password,String username,String dateOfBirth,int movieId,String title,String category
            ,String releaseDate, String movieLength, String director,String writer, String cast) {

        Report report = new Report(reportId,text,new User(userId,forename,surname,email,password,username,dateOfBirth,null,null),
                        new Movie(movieId,title,category,releaseDate,movieLength,director,writer,cast,null));
        return report;
    }


    @Test
    void createReport() {
        Report report = reportBuilder(1, "There are mistakes!", 1, "Emre", "Kubat",
                "emre.kubat@stud.uni-due.de", 12345, "mQ", "24.04.1996",
                1, "Spider-Man", "Action", "10.08.2017", "120 min", "Idon't know",
                "Writers", "Not known");
        ResponseEntity<String> response= reportService.createReport(report);
        assertEquals(response.getBody(),"Report created");
        assertEquals(response.getStatusCode(), HttpStatus.OK);

    }

    @Test
    void getReports() {
        when(reportRepository.findAll()).thenReturn(List.of(new Report[]{
                reportBuilder(1, "There are mistakes!", 1, "Emre", "Kubat",
                "emre.kubat@stud.uni-due.de", 12345, "mQ", "24.04.1996",
                1, "Spider-Man", "Action", "10.08.2017", "120 min", "Idon't know",
                "Writers", "Not known"),
                reportBuilder(2, "This is a test!", 2, "Robert", "Heß",
                        "robert.hess@stud.uni-due.de", 12345, "Pun1sher", "26.12.1999",
                        2, "Batman", "Action", "10.08.2016", "140 min", "Idon't know",
                        "Writers", "Not known"),
                reportBuilder(3, "There are mistakes!", 3, "Furkan", "Bedirhanoglu",
                        "furkan.bedirhanoglu@stud.uni-due.de", 12345, "vFurkii", "13.08.1998",
                        2, "Batman", "Action", "10.08.2016", "140 min", "Idon't know",
                        "Writers", "Not known")}));

        ResponseEntity<List<Report>> response = reportService.getReports();

        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertEquals(response.getBody().size(),3);
        assertEquals(response.getBody().get(1).getUser().getUsername(),"Pun1sher");

    }

    @Test
    void getReportsThroughMovieId() {
        when(reportRepository.findAll()).thenReturn(List.of(new Report[]{
                reportBuilder(1, "There are mistakes!", 1, "Emre", "Kubat",
                        "emre.kubat@stud.uni-due.de", 12345, "mQ", "24.04.1996",
                        1, "Spider-Man", "Action", "10.08.2017", "120 min", "Idon't know",
                        "Writers", "Not known"),
                reportBuilder(2, "This is a test!", 2, "Robert", "Heß",
                        "robert.hess@stud.uni-due.de", 12345, "Pun1sher", "26.12.1999",
                        2, "Batman", "Action", "10.08.2016", "140 min", "Idon't know",
                        "Writers", "Not known"),
               reportBuilder(3, "There are mistakes!", 3, "Furkan", "Bedirhanoglu",
                        "furkan.bedirhanoglu@stud.uni-due.de", 12345, "vFurkii", "13.08.1998",
                        2, "Batman", "Action", "10.08.2016", "140 min", "Idon't know",
                        "Writers", "Not known")}));

        ResponseEntity<List<Report>> response = reportService.getReportsThroughMovieId(2);
        assertEquals(response.getBody().size(),2);
        assertEquals(response.getBody().get(1).getMovie().getTitle(),"Batman");
        assertEquals(response.getBody().get(0).getUser().getUsername(),"Pun1sher");

    }


}