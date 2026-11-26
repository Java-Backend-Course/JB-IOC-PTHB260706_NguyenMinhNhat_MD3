package com.rikkei.buildingmanagement.service;

import com.rikkei.buildingmanagement.dto.BuildingRequest;
import com.rikkei.buildingmanagement.entity.Building;
import org.springframework.data.domain.Page;
import java.util.List;

public interface BuildingService {
    Page<Building> getAllBuildings(int page, int size);
    Building createBuilding(BuildingRequest request);
    Building updateBuilding(Long id, BuildingRequest request);
    Building updateStatus(Long id, Short newStatus);
    List<Building> searchByName(String name);
    List<Building> searchByStatus(Short status);
}