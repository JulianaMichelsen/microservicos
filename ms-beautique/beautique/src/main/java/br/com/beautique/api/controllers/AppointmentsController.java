package br.com.beautique.api.controllers;

import br.com.beautique.api.dtos.AppointmentDTO;
import br.com.beautique.api.entities.AppointmentsEntity;
import br.com.beautique.api.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appointments")
public class AppointmentsController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    ResponseEntity<AppointmentDTO> create(@RequestBody AppointmentDTO appointmentDTO){
        return ResponseEntity.ok(appointmentService.create(appointmentDTO));
    }

    @PatchMapping
    ResponseEntity<AppointmentDTO> update(@RequestBody AppointmentDTO appointmentDTO){
        return ResponseEntity.ok(appointmentService.update(appointmentDTO));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteById(@PathVariable Long id){
        appointmentService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    ResponseEntity<AppointmentDTO> setCustomerToAppointment(@RequestBody AppointmentDTO appointmentDTO){
        return ResponseEntity.ok(appointmentService.setCustomerToAppointment(appointmentDTO));
    }
}
