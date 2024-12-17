package com.poc_rabbit.service;

import com.poc_rabbit.domain.JobData;
import com.poc_rabbit.domain.Status;
import com.poc_rabbit.repository.JobDataRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProcessJobService {

    @Value("${app.file.upload.dir}")
    private String uploadDir;

    private final JobDataRepository repository;

    @Transactional
    @RabbitListener(queues = "reservationQueue")
    public void processReservation(Long requestId){
        JobData jobData = repository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        try{
            jobData.setStatus(Status.PROCESSING);
            repository.save(jobData);

            for(String reservationNumber : jobData.getReservationNumbers()){
                processReservation(reservationNumber);
            }

            String zipPath = createZipFile(jobData);

            jobData.setStatus(Status.PROCESSED);
            jobData.setResultFilePath(zipPath);
            repository.save(jobData);
        } catch (Exception e) {
            log.error("Deu ruim meu Fi: ", e);
            jobData.setStatus(Status.ERROR);
            repository.save(jobData);
        }

    }
    private void processReservation(String reservationNumber){
        //Simulacao
        try{
            Thread.sleep(2000); // Simulando delay forcado de brincadeirinha hahaha
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    private String createZipFile(JobData jobData) throws IOException {
        //Criando simulacao de result

        String fileName = "reservations-" + jobData.getId() + ".zip";
        Path filePath = Path.of(uploadDir, fileName);

        try(ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(filePath))) {
            for(String reservationNumber : jobData.getReservationNumbers()){
                ZipEntry zipEntry = new ZipEntry(reservationNumber + "/info.txt");
                zos.putNextEntry(zipEntry);
                String content = "BRUBINNNNN" + reservationNumber;
                zos.write(content.getBytes());
                zos.closeEntry();
            }
        }
        return filePath.toString();
    }
}
