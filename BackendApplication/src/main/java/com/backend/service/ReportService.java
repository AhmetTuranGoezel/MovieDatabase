package com.backend.service;

import com.backend.model.Report;
import com.backend.model.Systemadministrator;
import com.backend.repository.MovieRepository;
import com.backend.repository.ReportRepository;
import com.backend.repository.SystemadministratorRepository;
import com.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    SystemadministratorRepository systemadministratorRepository;

    public ResponseEntity<String> createReport(Report report) {

        reportRepository.save(report);

        List<Systemadministrator> systemadministratorList=systemadministratorRepository.findAll();

        for (Systemadministrator systemadministrator: systemadministratorList) {

            emailService.sendCode(systemadministrator.getEmail(),"Report Alert","Hello "+systemadministrator.getForename()+",\n\n"+
                                    "a new report was created by user: "+report.getUser().getUsername()+". This user has reported the movie:\n\n "
                                    +report.getMovie().getTitle()+"\n\n"+"Please check out your reports inbox to solve the report.");

        }

        return new ResponseEntity<>("Report created", HttpStatus.OK);
    }

    public ResponseEntity<List<Report>> getReports() {
        List<Report> reportList = reportRepository.findAll();
        return new ResponseEntity<>(reportList, HttpStatus.OK);

    }


    public ResponseEntity<List<Report>> getReportsThroughMovieId(int movieId) {

        List<Report> reportList = reportRepository.findAll();
        List<Report> reportsByMovies = new LinkedList<>();

        if(reportList.isEmpty()) {

            return new ResponseEntity<>(reportList,HttpStatus.BAD_REQUEST);
        }

        for (Report report: reportList) {

            if(report.getMovie().getMovieId()==movieId) {

                reportsByMovies.add(report);
            }
        }

        return new ResponseEntity<>(reportsByMovies,HttpStatus.OK);

    }

    public ResponseEntity<String> deleteReport(int reportId, int userId, int movieId, int systemadministratorId) {

       emailService.sendCode(userRepository.findById(userId).get().getEmail(),"Solved Report",
                "Hello "+userRepository.findById(userId).get().getForename() + ",\n\n"+
                        "we have successfully processed your report. \n\n" +
                        "The movie data of: "+movieRepository.findById(movieId).get().getTitle()+" "+"has been corrected. \n\n"+
                        "Thank you very much. \n\n"+
                        "Kind regards\n" +
                        systemadministratorRepository.findById(systemadministratorId).get().getForename()+" "
                        +systemadministratorRepository.findById(systemadministratorId).get().getSurname());

        reportRepository.deleteById(reportId);

        return new ResponseEntity<>("The report was successfully deleted!",HttpStatus.OK);

    }

}

