package br.com.beautique.api.services;

import br.com.beautique.api.dtos.BeautyProcedureDTO;

public interface BeautyProcedureService {

    BeautyProcedureDTO create(BeautyProcedureDTO beautyProcedureDTO);
    void delet(Long id);
    BeautyProcedureDTO update(BeautyProcedureDTO beautyProcedureDTO);
}
