package com.rikkei.buildingmanagement.controller;

import com.rikkei.buildingmanagement.dto.ApiResponse;
import com.rikkei.buildingmanagement.dto.BuildingRequest;
import com.rikkei.buildingmanagement.entity.Building;
import com.rikkei.buildingmanagement.service.BuildingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/buildings")
@RequiredArgsConstructor
public class BuildingController {
    private final BuildingService buildingService;
    @GetMapping
    public ResponseEntity<ApiResponse<Page<Building>>> getAllBuildings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<Building> data = buildingService.getAllBuildings(page, size);
        return ResponseEntity.ok(ApiResponse.<Page<Building>>builder()
                .success(true)
                .message("Lấy danh sách tòa nhà thành công")
                .data(data)
                .errors(null)
                .httpStatus(HttpStatus.OK.value())
                .build());
    }
    @PostMapping
    public ResponseEntity<ApiResponse<Building>> createBuilding(@Valid @ModelAttribute BuildingRequest request) {
        Building created = buildingService.createBuilding(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<Building>builder()
                .success(true)
                .message("Thêm mới tòa nhà thành công")
                .data(created)
                .errors(null)
                .httpStatus(HttpStatus.CREATED.value())
                .build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Building>> updateBuilding(
            @PathVariable Long id,
            @Valid @ModelAttribute BuildingRequest request) {

        Building updated = buildingService.updateBuilding(id, request);
        return ResponseEntity.ok(ApiResponse.<Building>builder()
                .success(true)
                .message("Cập nhật tòa nhà thành công")
                .data(updated)
                .errors(null)
                .httpStatus(HttpStatus.OK.value())
                .build());
    }
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Building>> updateStatus(
            @PathVariable Long id,
            @RequestParam Short status) {

        Building updated = buildingService.updateStatus(id, status);
        return ResponseEntity.ok(ApiResponse.<Building>builder()
                .success(true)
                .message("Cập nhật trạng thái thành công")
                .data(updated)
                .errors(null)
                .httpStatus(HttpStatus.OK.value())
                .build());
    }
    @GetMapping("/search/by-name")
    public ResponseEntity<ApiResponse<List<Building>>> searchByName(@RequestParam String name) {
        List<Building> list = buildingService.searchByName(name);
        return ResponseEntity.ok(ApiResponse.<List<Building>>builder()
                .success(true)
                .message("Tìm kiếm theo tên thành công")
                .data(list)
                .errors(null)
                .httpStatus(HttpStatus.OK.value())
                .build());
    }
    @GetMapping("/search/by-status")
    public ResponseEntity<ApiResponse<List<Building>>> searchByStatus(@RequestParam Short status) {
        List<Building> list = buildingService.searchByStatus(status);
        return ResponseEntity.ok(ApiResponse.<List<Building>>builder()
                .success(true)
                .message("Tìm kiếm theo trạng thái thành công")
                .data(list)
                .errors(null)
                .httpStatus(HttpStatus.OK.value())
                .build());
    }
}