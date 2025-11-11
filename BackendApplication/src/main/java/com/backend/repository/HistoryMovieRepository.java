package com.backend.repository;

import com.backend.model.HistoryMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


    @Repository
    public interface HistoryMovieRepository extends JpaRepository<HistoryMovie,Integer> {}

