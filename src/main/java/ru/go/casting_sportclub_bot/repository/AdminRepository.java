package ru.go.casting_sportclub_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.go.casting_sportclub_bot.model.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {
}
