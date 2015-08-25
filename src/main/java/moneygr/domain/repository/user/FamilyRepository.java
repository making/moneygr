package moneygr.domain.repository.user;

import moneygr.domain.model.Family;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyRepository extends JpaRepository<Family, Integer> {
}
