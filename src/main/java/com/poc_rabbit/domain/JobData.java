package com.poc_rabbit.domain;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class JobData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "reservation_numbers",
            joinColumns = @JoinColumn(name = "request_id")
    )
    @Column(name = "reservation_number")
    private List<String> reservationNumbers;

    private Status status;
    private LocalDateTime createdAt;
    private String resultFilePath;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = Status.PENDING;
    }
}
