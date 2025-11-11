package com.backend.repository;

import com.backend.model.Systemadministrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemadministratorRepository extends JpaRepository<Systemadministrator,Integer> {
}
