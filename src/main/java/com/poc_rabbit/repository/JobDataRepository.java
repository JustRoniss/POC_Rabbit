package com.poc_rabbit.repository;

import com.poc_rabbit.domain.JobData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobDataRepository extends JpaRepository<JobData, Long> {

}
