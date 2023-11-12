package com.antongoranov.demojwt.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "tokens")
public class TokenEntity extends BaseEntity {

    @OneToOne
    private UserEntity user;

    @Column(unique = true)
    private String token;
}
