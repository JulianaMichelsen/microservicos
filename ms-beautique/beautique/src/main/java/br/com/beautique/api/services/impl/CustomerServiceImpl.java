package br.com.beautique.api.services.impl;

import br.com.beautique.api.dtos.CustomerDTO;
import br.com.beautique.api.entities.CustomerEntity;
import br.com.beautique.api.repositories.CustomerRepository;
import br.com.beautique.api.services.BrokerService;
import br.com.beautique.api.services.CustomerService;
import br.com.beautique.api.utils.ConverterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BrokerService brokerService;

    private final ConverterUtil<CustomerEntity, CustomerDTO> converterUtil = new ConverterUtil<>(CustomerEntity.class, CustomerDTO.class);

    @Override
    public CustomerDTO create(CustomerDTO customerDTO) {
        CustomerEntity customerEntity = converterUtil.converteToSource(customerDTO);
        CustomerEntity newCustomerEntity = customerRepository.save(customerEntity);
        sendCustomerToQueue(newCustomerEntity);
        return converterUtil.converteToTarget(newCustomerEntity);
    }

    @Override
    public void delete(Long id) {
        Optional<CustomerEntity> customerEntityOptional = customerRepository.findById(id);
        if(customerEntityOptional.isEmpty()){
            throw new RuntimeException("Customer not found");
        }
        customerRepository.delete(customerEntityOptional.get());
    }

    @Override
    public CustomerDTO update(CustomerDTO customerDTO) {
        Optional<CustomerEntity> customerEntityOptional = customerRepository.findById((customerDTO.getId()));
        if(customerEntityOptional.isEmpty()){
            throw new RuntimeException("Customer not found");
        }
        CustomerEntity customerEntity = converterUtil.converteToSource(customerDTO);

        customerEntity.setAppointments(customerEntityOptional.get().getAppointments());
        customerEntity.setCreatedAt(customerEntityOptional.get().getCreatedAt());
        CustomerDTO updatedCustomerDTO = converterUtil.converteToTarget(customerRepository.save(customerEntity));
        sendCustomerToQueue(customerEntity);
        return updatedCustomerDTO;
    }

    private void sendCustomerToQueue(CustomerEntity customerEntity){
        CustomerDTO customerDTO = CustomerDTO.builder()
                .id(customerEntity.getId())
                .name(customerEntity.getName())
                .email(customerEntity.getEmail())
                .phone(customerEntity.getPhone())
                .build();
        brokerService.send("customer", customerDTO);
    }
}
