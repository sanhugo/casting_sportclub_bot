package ru.go.casting_sportclub_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.go.casting_sportclub_bot.model.Choice;
import ru.go.casting_sportclub_bot.model.Usercard;

import java.time.LocalDate;

@Repository
public interface UserCardRepository extends JpaRepository<Usercard,Long> {
    boolean existsByTgid(Long tgid);

    long countByRegdate(LocalDate regdate);

    @Query("""
        SELECT COUNT(u)
        FROM Usercard u
        WHERE :choice MEMBER OF u.choices
    """)
    long countByChoice(@Param("choice") Choice choice);
}
