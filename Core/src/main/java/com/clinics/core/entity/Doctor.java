package com.clinics.core.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Doctor implements BaseModel {
    @Id
    private String id;
    private @NotEmpty String firstName;
    private @NotEmpty String lastName;
    private @NotEmpty String phone;

}
