package com.manna.batchservice.tingthing.repository;

import com.manna.batchservice.tingthing.entity.Atmosphere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AtmosphereRepository extends JpaRepository<Atmosphere, Long> {
    Optional<Atmosphere> findByAtmosphereId(int atmosphereId);

}
