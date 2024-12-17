package com.poc_rabbit.controller;

import com.poc_rabbit.domain.JobData;
import com.poc_rabbit.service.JobReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class JobReservationController {

    @Autowired
    private JobReservationService jobReservationService;

    @PostMapping("/process")
    public ResponseEntity<JobData> processJob(@RequestParam("file")MultipartFile file){
        return ResponseEntity.ok(jobReservationService.processFile(file));
    }

    @GetMapping("/status/{requestId}")
    public ResponseEntity<JobData> getStatus(@PathVariable("requestId") Long requestId){
        return ResponseEntity.ok(jobReservationService.getStatus(requestId));
    }
}
