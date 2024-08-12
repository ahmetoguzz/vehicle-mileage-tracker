package com.cars.carfueltracker.service;

import com.cars.carfueltracker.exception.EmployeeNotFoundException;
import com.cars.carfueltracker.exception.GlobalException;
import com.cars.carfueltracker.exception.VehicleNotFoundException;
import com.cars.carfueltracker.model.Employee;
import com.cars.carfueltracker.model.EmployeeDto;
import com.cars.carfueltracker.model.Vehicle;
import com.cars.carfueltracker.model.VehicleDto;
import com.cars.carfueltracker.repository.IEmployeeRepository;
import com.cars.carfueltracker.repository.IVehicleRepository;
import com.cars.carfueltracker.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements IEmployeeService {

    @Autowired
    private IVehicleRepository vehicleRepository;

    @Autowired
    private IEmployeeRepository employeeRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    @Override
    public Employee createEmployee(EmployeeDto employeeDto) {
        log.info("[{}][createEmployee] - Yeni çalışan oluşturuluyor: {}", this.getClass().getSimpleName(), employeeDto);

        try {
            Employee employee = convertToEntity(employeeDto);

            Employee savedEmployee = employeeRepository.save(employee);
            log.info("Yeni çalışan başarıyla oluşturuldu: {}", savedEmployee);
            return savedEmployee;
        } catch (Exception e) {
            log.error("Çalışan oluşturulurken bir hata oluştu.", e);
            throw new GlobalException("Bir şeyler yanlış gitti.");
        }

    }

    @Override
    public Employee updateEmployee(Long id, EmployeeDto employeeDto) {
        log.info("[{}][updateEmployee] - Çalışan güncelleniyor: {}", this.getClass().getSimpleName(), employeeDto);

        Employee employee = employeeRepository.findById(id).orElseThrow(() -> {
            log.warn("Çalışan bulunamadı: {}", id);
            return new EmployeeNotFoundException("Çalışan bulunamadı");
        });

        try {
            updateEmployeeFields(employee, employeeDto);

            Employee updatedEmployee = employeeRepository.save(employee);
            log.info("Çalışan başarıyla güncellendi: {}", updatedEmployee);
            return updatedEmployee;
        } catch (Exception e) {
            log.error("Çalışan güncellenirken bir hata oluştu.", e);
            throw new GlobalException("Bir şeyler yanlış gitti.");
        }

    }


    @Override
    public void deleteEmployee(Long id) {
        log.info("[{}][deleteEmployee] - Çalışan siliniyor: {}", this.getClass().getSimpleName(), id);

        Employee employee = employeeRepository.findById(id).orElseThrow(() -> {
            log.warn("Çalışan bulunamadı: {}", id);
            return new EmployeeNotFoundException("Çalışan bulunamadı");
        });

        try {
                if (employee.getVehicle() != null) {
                    Vehicle vehicle = employee.getVehicle();
                    vehicle.setEmployee(null);
                    vehicleRepository.save(vehicle);
                }
                employeeRepository.delete(employee);
            log.info("Çalışan başarıyla silindi: {}", id);
        } catch (Exception e) {
            log.error("Çalışan silinirken bir hata oluştu.", e);
            throw new GlobalException("Bir şeyler yanlış gitti.");
        }

    }

    @Override
    public EmployeeDto getEmployeeById(Long id) {
        log.info("[{}][getEmployeeById] - Çalışan getiriliyor: {}", this.getClass().getSimpleName(), id);

        Employee employee = employeeRepository.findById(id).orElseThrow(() -> {
            log.warn("Çalışan bulunamadı: {}", id);
            return new EmployeeNotFoundException("Çalışan bulunamadı");
        });

        try {
            EmployeeDto employeeDto = convertToDto(employee);
            log.info("Çalışan başarıyla getirildi: {}", employeeDto);
            return employeeDto;
        } catch (Exception e) {
            log.error("Çalışan getirilirken bir hata oluştu.", e);
            throw new GlobalException("Bir şeyler yanlış gitti.");
        }
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        log.info("[{}][getAllEmployees] - Bütün çalışanlar getiriliyor.", this.getClass().getSimpleName());

        try {
            List<Employee> employees = employeeRepository.findAll();
            List<EmployeeDto> employeeDtos = employees.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            log.info("Bütün çalışanlar başarıyla getirildi.");
            return employeeDtos;
        } catch (Exception e) {
            log.error("Bütün çalışanlar getirilirken bir hata oluştu.", e);
            throw new GlobalException("Bir şeyler yanlış gitti.");
        }
    }

    @Override
    public Optional<Employee> findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    @Override
    public EmployeeDto getUserDetailsByToken(String token) {
        log.info("[{}][getUserDetailsByToken] - Getting user details by token", this.getClass().getSimpleName());

        String email = jwtUtil.extractEmail(token);
        Employee employee = employeeRepository.findByEmail(email).orElseThrow(() -> {
            log.warn("Employee not found for email: {}", email);
            return new EmployeeNotFoundException("Employee not found");
        });

        try {
            EmployeeDto employeeDto = convertToDto(employee);
            log.info("User details successfully retrieved: {}", employeeDto);
            return employeeDto;
        } catch (Exception e) {
            log.error("Error occurred while retrieving user details.", e);
            throw new GlobalException("Something went wrong.");
        }
    }

    @Override
    public EmployeeDto updateKm(String token, String currentKm) {
        String email = jwtUtil.extractEmail(token);
        Employee employee = employeeRepository.findByEmail(email).orElseThrow(() -> {
            log.warn("Employee not found for email: {}", email);
            return new EmployeeNotFoundException("Employee not found");
        });

        Vehicle vehicle = employee.getVehicle();
        if (vehicle != null) {
            vehicle.setLastKm(currentKm);
            vehicleRepository.save(vehicle);
        } else {
            throw new VehicleNotFoundException("Vehicle not found for employee");
        }

        return convertToDto(employee);
    }

    public void sendUpdateLinksToAllEmployees(){
        List<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees) {
            String email = employee.getEmail();
            String token = jwtUtil.generateToken(email);
            String link = "http://localhost:3000/?id=" + token;
            String subject = "Update Your Vehicle Kilometer Status";
            String body = "Please update your vehicle's kilometer status using the following link: " + link;
            emailService.sendEmailNew(email, subject, body);
        }
    }

    @Override
    public void sendUpdateLinkToEmployee(Long id){

        Employee employee = employeeRepository.findById(id).orElseThrow(() -> {
                log.warn("Çalışan bulunamadı: {}", id);
                return new EmployeeNotFoundException("Çalışan bulunamadı");
        });

        String email = employee.getEmail();
        String token = jwtUtil.generateToken(email);
        String link = "http://localhost:3000/?id=" + token;
        String subject = "Update Your Vehicle Kilometer Status";
        String body = "Please update your vehicle's kilometer status using the following link: " + link;
        emailService.sendEmailNew(email, subject, body);
    }



    // *******************************************_METHODS_********************************************************* //

    private EmployeeDto convertToDto(Employee employee) {
        VehicleDto vehicleDto = null;
        if (employee.getVehicle() != null) {
            Vehicle vehicle = employee.getVehicle();
            vehicleDto = VehicleDto.builder()
                    .employeeId(vehicle.getEmployee() != null ? vehicle.getEmployee().getId() : null)
                    .plateNumber(vehicle.getPlateNumber())
                    .brand(vehicle.getBrand())
                    .model(vehicle.getModel())
                    .color(vehicle.getColor())
                    .hgsNumber(vehicle.getHgsNumber())
                    .lastKm(vehicle.getLastKm())
                    .createDate(vehicle.getCreateDate())
                    .modelYear(vehicle.getModelYear())
                    .build();
        }

        return EmployeeDto.builder()
                .employeeId(employee.getId())
                .name(employee.getName())
                .surname(employee.getSurname())
                .email(employee.getEmail())
                .personalPhone(employee.getPersonalPhone())
                .title(employee.getTitle())
                .department(employee.getDepartment())
                .employeeStartDate(employee.getEmployeeStartDate())
                .vehicle(vehicleDto)
                .build();
    }





    private static Employee convertToEntity(EmployeeDto employeeDto){
        Employee employee = new Employee();
        employee.setName(employeeDto.getName());
        employee.setSurname(employeeDto.getSurname());
        employee.setEmail(employeeDto.getEmail());
        employee.setPersonalPhone(employeeDto.getPersonalPhone());
        employee.setTitle(employeeDto.getTitle());
        employee.setDepartment(employeeDto.getDepartment());
        employee.setEmployeeStartDate(employeeDto.getEmployeeStartDate());

        // Set the vehicle field to null
        employee.setVehicle(null);

        return employee;
    }



    private static void updateEmployeeFields(Employee employee, EmployeeDto employeeDto){
        employee.setName(employeeDto.getName());
        employee.setSurname(employeeDto.getSurname());
        employee.setEmail(employeeDto.getEmail());
        employee.setPersonalPhone(employeeDto.getPersonalPhone());
        employee.setTitle(employeeDto.getTitle());
        employee.setDepartment(employeeDto.getDepartment());
        employee.setEmployeeStartDate(employeeDto.getEmployeeStartDate());
    }
}
