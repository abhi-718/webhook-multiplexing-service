package com.webhookmultiplexerservice.webhookmultiplexerservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table(name = "consumer_endpoint_response")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerEndPointResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer Id;
    @Column(name = "WEB_HOOK_END_POINT")
    private String webHookEndPoint;
    @Column(name = "CONSUMER_END_POINT")
    private String consumerEndPoint;
    @Column(name = "REQUEST_BODY")
    private String requestBody;
    @Column(name = "HEADERS")
    private String headers;
    @Column(name = "REQUEST_METHOD")
    private String requestMethod;
    @Column(name = "REQUEST_PARAM")
    private String requestParam;
    @Column(name = "CONSUMER_API_RESPONSE")
    private String consumerApiResponse;
    @Column(name = "CONSUMER_API_RESPONSE_STATUS_CODE")
    private String consumerApiResponseStatusCode;
    @Column(name = "CONSUMER_API_RESPONSE_STATUS")
    private String consumerApiResponseStatus;


}
