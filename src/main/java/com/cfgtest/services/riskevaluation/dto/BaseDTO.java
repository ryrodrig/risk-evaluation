package com.cfgtest.services.riskevaluation.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.sql.Time;
import java.sql.Timestamp;

// Annotation copies properties from baseDTO to classes extending this class.
@MappedSuperclass
// lombok does not allow properties from parent class to be accessible within child's builder class..
// Use Experimental Super Builder
// Alternate options available at https://www.baeldung.com/lombok-builder-inheritance
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseDTO {

    @Version
    private Integer version;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createDate;

    @UpdateTimestamp
    private Timestamp updateDate;

    private Boolean deleteIndicator;
}
