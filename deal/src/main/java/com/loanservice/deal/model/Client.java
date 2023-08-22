package com.loanservice.deal.model;

import com.loanservice.deal.openapi.dto.EmploymentDTO;
import com.loanservice.deal.openapi.dto.Gender;
import com.loanservice.deal.openapi.dto.MaritalStatus;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
@Table(name = "client")
@TypeDef(name = "json", typeClass = JsonBinaryType.class)
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "birth_date")
    private LocalDate birthdate;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status")
    private MaritalStatus maritalStatus;

    @Column(name = "dependent_amount")
    private Integer dependentAmount;

    @Type(type = "json")
    @Column(columnDefinition = "jsonb", name = "passport")
    private Passport passport;

    @Type(type = "json")
    @Column(columnDefinition = "jsonb", name = "employment")
    private EmploymentDTO employment;

    private String account;

}
