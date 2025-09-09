package ru.go.casting_sportclub_bot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Usercard {
    @Id
    @Column(nullable = false)
    private Long tgid;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Faculties faculty;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private Integer age;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(nullable = false)
    private String course;
    @ElementCollection(targetClass = Choice.class)
    @CollectionTable(name = "user_choices", joinColumns = @JoinColumn(name = "tgid"))
    @Enumerated(EnumType.STRING)
    @Column(name = "choice")
    private Set<Choice> choices;
    @Column(nullable = false)
    private String eventmaking;
    @Column(nullable = false)
    private String eventpart;
}
