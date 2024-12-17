package com.poc_rabbit.domain;

public enum Status {
    PENDING,
    PROCESSING,
    PROCESSED,
    PROCESSED_FAILED,
    PROCESSED_WITH_ERROR,
    CANCELLED,
    ERROR
}
