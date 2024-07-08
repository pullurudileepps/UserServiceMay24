package org.example.userservicemay24.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @ManyToOne
    private User user;
}
