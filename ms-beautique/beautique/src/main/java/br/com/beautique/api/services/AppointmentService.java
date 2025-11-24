package br.com.beautique.api.services;

import br.com.beautique.api.dtos.AppointmentDTO;

public interface AppointmentService {

    AppointmentDTO create(AppointmentDTO appointmentDTO);
    AppointmentDTO update(AppointmentDTO appointmentDTO);
    void deleteById(Long id);
    AppointmentDTO setCustomerToAppointment(AppointmentDTO appointmentDTO);
}
