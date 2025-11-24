package br.com.beautique.api.configurations;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopicConfig {

    public final String exchangeName = "beautiqueExchange";

    @Bean
    TopicExchange exchange(){
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Queue customerQueue(){
        return new Queue("customerQueue", true);
    }

    @Bean
    Binding bindingCustomer(Queue customerQueue, TopicExchange exchange){
        return BindingBuilder.bind(customerQueue).to(exchange).with("customer.#");
    }
}
