package br.com.beautique.api.services;

import br.com.beautique.api.dtos.CustomerDTO;
import br.com.beautique.api.repositories.CustomerRepository;

public interface CustomerService {
    CustomerDTO create(CustomerDTO customerEntity);
    void delete(Long id);
    CustomerDTO update(CustomerDTO customerDTO);
}
