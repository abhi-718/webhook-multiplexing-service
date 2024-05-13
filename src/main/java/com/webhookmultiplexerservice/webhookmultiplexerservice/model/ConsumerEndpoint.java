package com.webhookmultiplexerservice.webhookmultiplexerservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "consumer_endpoint")
public class ConsumerEndpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "WEBHOOK_ID")
    private Integer webhookId;
    @Column(name = "API_NAME")
    private String name;
    @Column(name = "API_END_POINT")
    private String endPoint;
}
