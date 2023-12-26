package com.example.ProyectoAWS.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name= "Empleados")
public class Empleado {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "clave")
    private String clave;

    @Column(name = "email")
    private String email;

    @Column(name = "estado")
    private Boolean estado;

    private String imagenPath;
    private String cedulaPath;

    @Transient
    private String imagenUrl;

    @Transient
    private String cedulaUrl;

}
