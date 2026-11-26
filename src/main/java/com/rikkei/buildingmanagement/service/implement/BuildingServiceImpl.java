package com.rikkei.buildingmanagement.service.implement;

import com.rikkei.buildingmanagement.dto.BuildingRequest;
import com.rikkei.buildingmanagement.entity.Building;
import com.rikkei.buildingmanagement.exception.BadRequestException;
import com.rikkei.buildingmanagement.exception.ResourceNotFoundException;
import com.rikkei.buildingmanagement.repository.BuildingRepository;
import com.rikkei.buildingmanagement.service.BuildingService;
import com.rikkei.buildingmanagement.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BuildingServiceImpl implements BuildingService {
    private final BuildingRepository buildingRepository;
    private final FileUploadService fileUploadService;
    @Override
    public Page<Building> getAllBuildings(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return buildingRepository.findAll(pageable);
    }
    @Override
    public Building createBuilding(BuildingRequest request) {
        if (buildingRepository.existsByBuildingName(request.getBuildingName())) {
            throw new BadRequestException("Tên tòa nhà đã tồn tại");
        }

        String imagePath = fileUploadService.uploadFile(request.getDesignFile());
        Building building = Building.builder()
                .buildingName(request.getBuildingName())
                .buildingArea(request.getBuildingArea())
                .areaUnit(request.getAreaUnit())
                .startDate(request.getStartDate())
                .time(request.getTime())
                .timeUnit(request.getTimeUnit())
                .content(request.getContent())
                .design(imagePath)
                .status((short) 1)
                .build();

        return buildingRepository.save(building);
    }
    @Override
    public Building updateBuilding(Long id, BuildingRequest request) {
        Building existing = buildingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tòa nhà có mã: " + id));

        if (buildingRepository.existsByBuildingNameAndIdNot(request.getBuildingName(), id)) {
            throw new BadRequestException("Tên tòa nhà đã trùng với tòa nhà khác");
        }
        existing.setBuildingName(request.getBuildingName());
        existing.setBuildingArea(request.getBuildingArea());
        existing.setAreaUnit(request.getAreaUnit());
        existing.setStartDate(request.getStartDate());
        existing.setTime(request.getTime());
        existing.setTimeUnit(request.getTimeUnit());
        existing.setContent(request.getContent());
        if (request.getDesignFile() != null && !request.getDesignFile().isEmpty()) {
            String newImagePath = fileUploadService.uploadFile(request.getDesignFile());
            existing.setDesign(newImagePath);
        }

        return buildingRepository.save(existing);
    }
    @Override
    public Building updateStatus(Long id, Short newStatus) {
        if (newStatus == null || newStatus < 0 || newStatus > 2) {
            throw new BadRequestException("Trạng thái không hợp lệ (chỉ nhận 0, 1 hoặc 2)");
        }

        Building existing = buildingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tòa nhà có mã: " + id));

        if (existing.getStatus() == 2) {
            throw new BadRequestException("Tòa nhà đã hoàn thành (status = 2), không thể thay đổi trạng thái nữa");
        }

        existing.setStatus(newStatus);
        return buildingRepository.save(existing);
    }
    @Override
    public List<Building> searchByName(String name) {
        return buildingRepository.findByBuildingNameContainingIgnoreCase(name);
    }
    @Override
    public List<Building> searchByStatus(Short status) {
        return buildingRepository.findByStatus(status);
    }
}