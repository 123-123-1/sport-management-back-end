package com.tongji.sportmanagement.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "venue_manager")
public class VenueManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", length = 20)
    private String name;

    @Column(name = "image", length = 100)
    private String image;

    @Column(name = "description")
    private String description;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "allow_reserve")
    private Boolean allowReserve;

}