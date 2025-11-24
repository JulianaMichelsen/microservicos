package br.com.beautique.api.utils;

import org.modelmapper.ModelMapper;

public class ConverterUtil<S,T>{

    private final ModelMapper modelMapper;
    private final Class<S> sourceType;
    private final Class<T> targetType;

    public ConverterUtil(Class<S> sourceType, Class<T> targetType) {
        this.modelMapper = new ModelMapper();
        this.sourceType = sourceType;
        this.targetType = targetType;
    }

    public T converteToTarget(S source){
        return modelMapper.map(source, targetType);
    }

    public S converteToSource(T target){
        return modelMapper.map(target, sourceType);
    }


}
