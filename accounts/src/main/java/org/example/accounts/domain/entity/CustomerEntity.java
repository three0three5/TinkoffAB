package org.example.accounts.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "customers")
@NoArgsConstructor
public class CustomerEntity {
    @Id
    @GeneratedValue
    private int id;
    private String firstName;
    private String lastName;
    private LocalDate birthDay;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<AccountEntity> accounts;
}
