package com.cfgtest.services.riskevaluation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class RiskTransactionDetails  extends BaseDTO {

    @Id
    @GeneratedValue(generator = "riskTransactionIdGen")
//    Hibernate Annotation to generate a value
    @GenericGenerator(name="riskTransactionIdGen", strategy = "org.hibernate.id.UUIDGenerator")
//    Needed to fix an issue with mapping UUID to object.
    @Type(type="uuid-char")
    @Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    private UUID riskTxnId;

    // JPA will use Enum.name() when persisting a given entity in database.
    @Enumerated(EnumType.STRING)
    private RiskStatus riskStatus;

    public enum RiskStatus {
        APPROVED , REVIEW, DECLINED
    }
}
