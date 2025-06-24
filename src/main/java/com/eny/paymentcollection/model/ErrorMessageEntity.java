package com.eny.paymentcollection.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@Entity
@Table(name = "error_messages")
@EqualsAndHashCode(callSuper = true)
public class ErrorMessageEntity extends BaseEntity {
    private static final long serialVersionUID = 5210442167387592397L;

    private int errorCode;

    @Column(length = 60)
    private String message;

    public ErrorMessageEntity(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public ErrorMessageEntity() {

    }
}
