package com.backend.controller;

import com.backend.model.Report;
import com.backend.model.Review;
import com.backend.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReportController {

@Autowired
ReportService reportService;


    @PostMapping(value = "/reports/create/")
    public ResponseEntity<String> createReport(@RequestBody Report report) {
        return reportService.createReport(report);
    }

    @GetMapping(value="/reports/")
    public ResponseEntity<List<Report>> getReports() {
        return reportService.getReports();
    }



    @DeleteMapping(value="/reports/movie/{reportId}/{userId}/{movieId}/{systemadministratorId}")
     public ResponseEntity<String> deleteReport(@PathVariable("reportId") int reportId,@PathVariable("userId") int userId,
                                                @PathVariable("movieId") int movieId, @PathVariable("systemadministratorId") int systemadministratorId) {

       return reportService.deleteReport(reportId,userId,movieId,systemadministratorId);

    }


}
