package br.com.beautique.api.controllers;

import br.com.beautique.api.dtos.CustomerDTO;
import br.com.beautique.api.entities.CustomerEntity;
import br.com.beautique.api.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping()
    ResponseEntity<CustomerDTO> create(@RequestBody CustomerDTO customerDTO){
        return ResponseEntity.ok(customerService.create(customerDTO));
    }

    @DeleteMapping("{id}")
    ResponseEntity<Void> delete(@PathVariable Long id){
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping()
    ResponseEntity<CustomerDTO> update(@RequestBody CustomerDTO customerDTO){
        return ResponseEntity.ok(customerService.update(customerDTO));
    }
}
