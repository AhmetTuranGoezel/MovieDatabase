package com.backend.repository;

import com.backend.model.DiscussionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscussionGroupRepository extends JpaRepository<DiscussionGroup,Integer> {



}
