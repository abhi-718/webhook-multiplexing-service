package com.webhookmultiplexerservice.webhookmultiplexerservice.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ConsumerEndPointRepository extends JpaRepository<ConsumerEndpoint, Integer> {

    List<ConsumerEndpoint> findAllByWebhookId(Integer webHookId);
}
