package com.rikkei.buildingmanagement.repository;

import com.rikkei.buildingmanagement.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {
    boolean existsByBuildingName(String buildingName);
    boolean existsByBuildingNameAndIdNot(String buildingName, Long id);
    List<Building> findByBuildingNameContainingIgnoreCase(String buildingName);
    List<Building> findByStatus(Short status);
}