package br.com.beautique.api.services.impl;

import br.com.beautique.api.dtos.AppointmentDTO;
import br.com.beautique.api.dtos.BeautyProcedureDTO;
import br.com.beautique.api.dtos.CustomerDTO;
import br.com.beautique.api.dtos.FullAppointmentDTO;
import br.com.beautique.api.entities.AppointmentsEntity;
import br.com.beautique.api.entities.BeautyProceduresEntity;
import br.com.beautique.api.entities.CustomerEntity;
import br.com.beautique.api.repositories.AppointmentRepository;
import br.com.beautique.api.repositories.BeautyProcedureRepository;
import br.com.beautique.api.repositories.CustomerRepository;
import br.com.beautique.api.services.AppointmentService;
import br.com.beautique.api.services.BrokerService;
import br.com.beautique.api.utils.ConverterUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AppointmensServiceImpl implements AppointmentService {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private BeautyProcedureRepository beautyProcedureRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BrokerService brokerService;

    private final ConverterUtil<AppointmentsEntity, AppointmentDTO> converterUtil = new ConverterUtil<>(AppointmentsEntity.class, AppointmentDTO.class);

    @Override
    public AppointmentDTO create(AppointmentDTO appointmentDTO) {
        AppointmentsEntity appointmentsEntity = converterUtil.converteToSource(appointmentDTO);
        AppointmentsEntity newAppointmentsEntity = appointmentRepository.save(appointmentsEntity);
        sendAppointmentToQueue(newAppointmentsEntity);
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
        sendAppointmentToQueue(updatedAppointmentEntity);
        return converterUtil.converteToTarget(updatedAppointmentEntity);
    }

    private void sendAppointmentToQueue(AppointmentsEntity appointmentsEntity) {
        CustomerDTO customerDTO = appointmentsEntity.getCustomer() != null ? modelMapper.map(appointmentsEntity.getCustomer(), CustomerDTO.class) : null;
        BeautyProcedureDTO beautyProcedureDTO = appointmentsEntity.getBeautyProcedure() != null ? modelMapper.map(appointmentsEntity.getBeautyProcedure(), BeautyProcedureDTO.class) : null;
        FullAppointmentDTO fullAppointmentDTO = FullAppointmentDTO.builder()
                .id(appointmentsEntity.getId())
                .dateTime(appointmentsEntity.getDateTime())
                .appointmentsOpen(appointmentsEntity.getAppointmentsOpen())
                .customer(customerDTO)
                .beautyProcedure(beautyProcedureDTO)
                .build();
        brokerService.send("appointments", fullAppointmentDTO);
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
        sendAppointmentToQueue(updateAppointmentEntity);
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
