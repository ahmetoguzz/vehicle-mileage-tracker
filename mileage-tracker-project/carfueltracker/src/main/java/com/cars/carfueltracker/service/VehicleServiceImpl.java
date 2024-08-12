package com.cars.carfueltracker.service;

import com.cars.carfueltracker.exception.GlobalException;
import com.cars.carfueltracker.exception.VehicleNotFoundException;
import com.cars.carfueltracker.model.Employee;
import com.cars.carfueltracker.model.EmployeeDto;
import com.cars.carfueltracker.model.Vehicle;
import com.cars.carfueltracker.model.VehicleDto;
import com.cars.carfueltracker.repository.IEmployeeRepository;
import com.cars.carfueltracker.repository.IVehicleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class VehicleServiceImpl implements IVehicleService {


    @Autowired
    private IVehicleRepository vehicleRepository;

    @Autowired
    private IEmployeeRepository employeeRepository;

    public Vehicle createVehicle(VehicleDto vehicleDto) {

        log.info("[{}][createVehicle] - Yeni araç oluşturuluyor: {}", this.getClass().getSimpleName(), vehicleDto);

        try {
            Vehicle vehicle = convertToEntity(vehicleDto);

            if (vehicleDto.getEmployeeId() != null) {
                Employee employee = employeeRepository.findById(vehicleDto.getEmployeeId()).orElse(null);
                if (employee != null) {
                    vehicle.setEmployee(employee);
                    employee.setVehicle(vehicle);
                    log.info("Araç, çalışan ile ilişkilendirildi: {}", employee);
                }
            }

            Vehicle savedVehicle = vehicleRepository.save(vehicle);
            log.info("Araç başarıyla oluşturuldu: {}", savedVehicle);
            return savedVehicle;

        } catch (Exception e) {
            log.error("[{}][createVehicle] - Araç oluşturulurken bir hata oluştu.", this.getClass().getSimpleName(), e);
            throw new GlobalException("Bir hata oluştu.");
        }

    }

    @Override
    public Vehicle updateVehicle(Long id, VehicleDto vehicleDto) {
        log.info("[{}][updateVehicle] - Araç güncelleniyor: {}", this.getClass().getSimpleName(), vehicleDto);

        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> {
            log.warn("Araç bulunamadı: {}", id);
            return new VehicleNotFoundException("Araç bulunamadı.");
        });

        try {
            updateVehicleFields(vehicle, vehicleDto);

            if (vehicleDto.getEmployeeId() != null) {
                Employee employee = employeeRepository.findById(vehicleDto.getEmployeeId()).orElse(null);
                if (employee != null) {
                    vehicle.setEmployee(employee);
                    employee.setVehicle(vehicle);
                }
            }

            Vehicle updatedVehicle = vehicleRepository.save(vehicle);
            log.info("Araç başarıyla güncellendi: {}", updatedVehicle);
            return updatedVehicle;
        } catch (Exception e) {
            log.error("Araç güncellenirken bir hata oluştu.", e);
            throw new GlobalException("Araç güncellenirken bir hata oluştu.");
        }


    }

    @Override
    @Transactional
    public void deleteVehicle(Long id) {
        log.info("[{}][deleteVehicle] - Araç siliniyor: {}", this.getClass().getSimpleName(), id);

        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> {
            log.warn("Araç bulunamadı: {}", id);
            return new VehicleNotFoundException("Araç bulunamadı.");
        });

        try {
                if (vehicle.getEmployee() != null) {
                    Employee employee = vehicle.getEmployee();
                    employee.setVehicle(null);
                    employeeRepository.save(employee);
                }
            vehicleRepository.delete(vehicle);
            log.info("Araç başarıyla silindi: {}", id);
        } catch (Exception e) {
            log.error("Araç silinirken bir hata oluştu.", e);
            throw new GlobalException("Bir hata oluştu.");
        }
    }


    @Override
    public VehicleDto getVehicleById(Long id) {
        log.info("[{}][getVehicleById] - Araç getiriliyor: {}", this.getClass().getSimpleName(), id);

        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> {
            log.warn("Araç bulunamadı: {}", id);
            return new VehicleNotFoundException("Araç bulunamadı.");
        });

        try {
            VehicleDto vehicleDto = convertToDto(vehicle);
            log.info("Araç başarıyla getirildi: {}", vehicleDto);
            return vehicleDto;
        } catch (Exception e) {
            log.error("Araç getirilirken bir hata oluştu.", e);
            throw new GlobalException("Bir hata oluştu.");
        }
    }

    @Override
    public List<VehicleDto> getAllVehicles() {
        log.info("Fetching all vehicles");
        try {
            List<Vehicle> vehicles = vehicleRepository.findAll();
            if (vehicles.isEmpty()) {
                throw new VehicleNotFoundException("No vehicles found");
            }

            List<VehicleDto> vehicleDtos = new ArrayList<>();
            for (Vehicle vehicle : vehicles) {
                EmployeeDto employeeDto = null;
                if (vehicle.getEmployee() != null) {
                    employeeDto = EmployeeDto.builder()
                            .employeeId(vehicle.getEmployee().getId())
                            .name(vehicle.getEmployee().getName())
                            .surname(vehicle.getEmployee().getSurname())
                            .email(vehicle.getEmployee().getEmail())
                            .personalPhone(vehicle.getEmployee().getPersonalPhone())
                            .title(vehicle.getEmployee().getTitle())
                            .department(vehicle.getEmployee().getDepartment())
                            .employeeStartDate(vehicle.getEmployee().getEmployeeStartDate())
                            .build();
                }

                VehicleDto vehicleDto = VehicleDto.builder()
                        .id(vehicle.getId())
                        .employeeId(vehicle.getEmployee() != null ? vehicle.getEmployee().getId() : null)
                        .plateNumber(vehicle.getPlateNumber())
                        .brand(vehicle.getBrand())
                        .model(vehicle.getModel())
                        .color(vehicle.getColor())
                        .hgsNumber(vehicle.getHgsNumber())
                        .lastKm(vehicle.getLastKm())
                        .createDate(vehicle.getCreateDate())
                        .modelYear(vehicle.getModelYear())
                        .employee(employeeDto)  // Set the EmployeeDto
                        .build();

                vehicleDtos.add(vehicleDto);
            }
            log.info("Successfully fetched all vehicles");
            return vehicleDtos;
        } catch (Exception e) {
            log.error("Error occurred while fetching all vehicles", e);
            throw new VehicleNotFoundException("Error occurred while fetching all vehicles");
        }
    }



    // *********************************_METHODS_******************************************** //

    private VehicleDto convertToDto(Vehicle vehicle) {
        EmployeeDto employeeDto = null;
        if (vehicle.getEmployee() != null) {
            employeeDto = EmployeeDto.builder()
                    .employeeId(vehicle.getEmployee().getId())
                    .name(vehicle.getEmployee().getName())
                    .surname(vehicle.getEmployee().getSurname())
                    .email(vehicle.getEmployee().getEmail())
                    .personalPhone(vehicle.getEmployee().getPersonalPhone())
                    .title(vehicle.getEmployee().getTitle())
                    .department(vehicle.getEmployee().getDepartment())
                    .employeeStartDate(vehicle.getEmployee().getEmployeeStartDate())
                    .build();
        }

        return VehicleDto.builder()
                .id(vehicle.getId())
                .employeeId(vehicle.getEmployee() != null ? vehicle.getEmployee().getId() : null)
                .plateNumber(vehicle.getPlateNumber())
                .brand(vehicle.getBrand())
                .model(vehicle.getModel())
                .color(vehicle.getColor())
                .hgsNumber(vehicle.getHgsNumber())
                .lastKm(vehicle.getLastKm())
                .createDate(vehicle.getCreateDate())
                .modelYear(vehicle.getModelYear())
                .employee(employeeDto)  // Set the EmployeeDto
                .build();
    }




    private static Vehicle convertToEntity(VehicleDto vehicleDto) {
        Vehicle vehicle = new Vehicle();
        vehicle.setPlateNumber(vehicleDto.getPlateNumber());
        vehicle.setBrand(vehicleDto.getBrand());
        vehicle.setModel(vehicleDto.getModel());
        vehicle.setColor(vehicleDto.getColor());
        vehicle.setHgsNumber(vehicleDto.getHgsNumber());
        vehicle.setLastKm(vehicleDto.getLastKm());
        vehicle.setCreateDate(vehicleDto.getCreateDate());
        vehicle.setModelYear(vehicleDto.getModelYear());
        return vehicle;
    }

    private static void updateVehicleFields(Vehicle vehicle, VehicleDto vehicleDto) {
        vehicle.setPlateNumber(vehicleDto.getPlateNumber());
        vehicle.setBrand(vehicleDto.getBrand());
        vehicle.setModel(vehicleDto.getModel());
        vehicle.setColor(vehicleDto.getColor());
        vehicle.setHgsNumber(vehicleDto.getHgsNumber());
        vehicle.setLastKm(vehicleDto.getLastKm());
        vehicle.setCreateDate(vehicleDto.getCreateDate());
        vehicle.setModelYear(vehicleDto.getModelYear());
    }

}



