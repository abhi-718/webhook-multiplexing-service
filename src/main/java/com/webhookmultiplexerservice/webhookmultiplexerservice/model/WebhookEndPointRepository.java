package com.webhookmultiplexerservice.webhookmultiplexerservice.model;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebhookEndPointRepository extends JpaRepository<WebhookEndpoint, Integer> {

    boolean existsByName(String name);

    WebhookEndpoint getByName(String name);
    WebhookEndpoint getByEndPoint(String endPoint);
}
