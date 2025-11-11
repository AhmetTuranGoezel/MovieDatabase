package com.backend.repository;

import com.backend.model.Watchparty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchpartyRepository extends JpaRepository<Watchparty,Integer> {

}
