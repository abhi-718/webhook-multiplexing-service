package com.webhookmultiplexerservice.webhookmultiplexerservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "webhook_endpoint")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebhookEndpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "API_NAME")
    private String name;
    @Column(name = "API_END_POINT")
    private String endPoint;

}
