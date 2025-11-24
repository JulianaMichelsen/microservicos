package br.com.beautique.api.services.impl;

import br.com.beautique.api.dtos.AppointmentDTO;
import br.com.beautique.api.entities.AppointmentsEntity;
import br.com.beautique.api.entities.BeautyProceduresEntity;
import br.com.beautique.api.entities.CustomerEntity;
import br.com.beautique.api.repositories.AppointmentRepository;
import br.com.beautique.api.repositories.BeautyProcedureRepository;
import br.com.beautique.api.repositories.CustomerRepository;
import br.com.beautique.api.services.AppointmentService;
import br.com.beautique.api.utils.ConverterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AppointmensServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private BeautyProcedureRepository beautyProcedureRepository;

    @Autowired
    private CustomerRepository customerRepository;


    private final ConverterUtil<AppointmentsEntity, AppointmentDTO> converterUtil = new ConverterUtil<>(AppointmentsEntity.class, AppointmentDTO.class);

    @Override
    public AppointmentDTO create(AppointmentDTO appointmentDTO) {
        AppointmentsEntity appointmentsEntity = converterUtil.converteToSource(appointmentDTO);
        AppointmentsEntity newAppointmentsEntity = appointmentRepository.save(appointmentsEntity);
        return converterUtil.converteToTarget(newAppointmentsEntity);
    }

    @Override
    public AppointmentDTO update(AppointmentDTO appointmentDTO) {
        Optional<AppointmentsEntity> currentAppointment = appointmentRepository.findById(appointmentDTO.getId());
        if(currentAppointment.isEmpty()){
            throw new RuntimeException("Appointment not found");
        }
        AppointmentsEntity appointmentsEntity = converterUtil.converteToSource(appointmentDTO);
        appointmentsEntity.setCreatedAt(currentAppointment.get().getCreatedAt());
        AppointmentsEntity updatedAppointmentEntity = appointmentRepository.save(appointmentsEntity);
        return converterUtil.converteToTarget(updatedAppointmentEntity);
    }

    @Override
    public void deleteById(Long id) {
        AppointmentsEntity appointmentsEntity = appointmentRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Appointment not found"));
        appointmentRepository.delete(appointmentsEntity);
    }

    @Override
    public AppointmentDTO setCustomerToAppointment(AppointmentDTO appointmentDTO) {
        CustomerEntity customerEntity = findCustomerById(appointmentDTO.getCustomer());
        BeautyProceduresEntity beautyProceduresEntity = findBeautyProcedureById(appointmentDTO.getBeautyProcedure());
        AppointmentsEntity appointmentsEntity = findAppointmentById(appointmentDTO.getId());
        appointmentsEntity.setCustomer(customerEntity);
        appointmentsEntity.setBeautyProcedure(beautyProceduresEntity);
        appointmentsEntity.setAppointmentsOpen(false);

        AppointmentsEntity updateAppointmentEntity = appointmentRepository.save(appointmentsEntity);
        return buildAppointmentsDTO(updateAppointmentEntity);
    }

    private AppointmentsEntity findAppointmentById(Long id){
        return appointmentRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Appointment not found"));
    }

    private BeautyProceduresEntity findBeautyProcedureById(Long id){
        return beautyProcedureRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Beauty procedure not found"));
    }

    private CustomerEntity findCustomerById(Long id){
        return customerRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Customer not found"));
    }

    private AppointmentDTO buildAppointmentsDTO(AppointmentsEntity appointmentsEntity){
        return AppointmentDTO.builder()
                .id(appointmentsEntity.getId())
                .beautyProcedure(appointmentsEntity.getBeautyProcedure().getId())
                .dateTime(appointmentsEntity.getDateTime())
                .appointmentsOpen(appointmentsEntity.getAppointmentsOpen())
                .customer(appointmentsEntity.getCustomer().getId())
                .build();
    }
}
