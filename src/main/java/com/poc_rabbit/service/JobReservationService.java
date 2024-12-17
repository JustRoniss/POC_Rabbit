package com.poc_rabbit.service;

import com.poc_rabbit.domain.JobData;
import com.poc_rabbit.repository.JobDataRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobReservationService {

    private final JobDataRepository jobDataRepository;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public JobData processFile(MultipartFile file) {
        try{
            List<String> reservationNumbers = new BufferedReader(
                new InputStreamReader(file.getInputStream()))
                    .lines()
                    .collect(Collectors.toList());

            JobData jobData = new JobData();
            jobData.setReservationNumbers(reservationNumbers);
            jobData = jobDataRepository.save(jobData);

            //Enviar para processamento
            rabbitTemplate.convertAndSend("reservationQueue", jobData.getId());
            return jobData;
        }catch (Exception e){
            throw new RuntimeException("Error processing file", e);
        }
    }

    @Transactional()
    public JobData getStatus(Long requestId) {
        return jobDataRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Id not found"));
    }
}
