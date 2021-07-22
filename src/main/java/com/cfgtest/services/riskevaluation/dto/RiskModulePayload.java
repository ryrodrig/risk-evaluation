package com.cfgtest.services.riskevaluation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class RiskModulePayload extends BaseDTO {

    @Id
    @GeneratedValue(generator = "riskModuleIdGen")
//    Hibernate Annotation to generate a value
    @GenericGenerator(name="riskModuleIdGen", strategy = "org.hibernate.id.UUIDGenerator")
//    Needed to fix an issue with mapping UUID to object.
    @Type(type="uuid-char")
    @Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    private UUID riskModuleId;

    private String requestPayload;

    private String responsePayload;

    // Unidirectional join..
    // This entity is the owner entity and references ROskModulePayload table.
    @ManyToOne(fetch = FetchType.LAZY)
    // Name of the column that hold foreign key reference to primary key in RiskModulePayload..
    // https://www.baeldung.com/jpa-join-column
    @JoinColumn(name = "risk_transaction_details_id")
    private RiskTransactionDetails riskTransactionDetails;

}
