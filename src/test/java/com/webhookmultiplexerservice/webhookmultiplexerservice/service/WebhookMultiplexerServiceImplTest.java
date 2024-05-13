package com.webhookmultiplexerservice.webhookmultiplexerservice.service;

import com.webhookmultiplexerservice.webhookmultiplexerservice.dto.ConsumerEndPointDto;
import com.webhookmultiplexerservice.webhookmultiplexerservice.dto.RequestDto;
import com.webhookmultiplexerservice.webhookmultiplexerservice.dto.ResponseDto.ConsumerEndPointResponseDto;
import com.webhookmultiplexerservice.webhookmultiplexerservice.dto.WebhookEndPointDto;
import com.webhookmultiplexerservice.webhookmultiplexerservice.model.*;
import com.webhookmultiplexerservice.webhookmultiplexerservice.service.common.RestClient;
import com.webhookmultiplexerservice.webhookmultiplexerservice.service.impl.WebhookMultiplexerServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WebhookMultiplexerServiceImplTest {

    @InjectMocks
    private WebhookMultiplexerServiceImpl webhookMultiplexerService;

    @Mock
    private WebhookEndPointRepository webhookEndPointRepository;

    @Mock
    private WebhookEndpointMapper webhookEndpointMapper;
    @Mock
    private ConsumerEndPointMapper consumerEndPointMapper;
    @Mock
    private ConsumerEndPointRepository consumerEndPointRepository;

    @Mock
    private RestClient restClient;

    @Mock
    private ConsumerEndPointResponseRepository consumerEndPointResponseRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testCreateWebhookEndpoint_Success() throws BadRequestException {
        // Arrange
        WebhookEndPointDto webhookEndPointDto = new WebhookEndPointDto();
        WebhookEndpoint webhookEndpoint = new WebhookEndpoint();
        Mockito.when(webhookEndpointMapper.toEntity(webhookEndPointDto)).thenReturn(webhookEndpoint);
        Mockito.when(webhookEndPointRepository.save(webhookEndpoint)).thenReturn(webhookEndpoint);

        // Act
        ResponseEntity<WebhookEndpoint> responseEntity = webhookMultiplexerService.createWebhookEndpoint(webhookEndPointDto);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    public void testCreateWebhookEndpoint_WebhookEndpointAlreadyExists() {
        // Arrange
        WebhookEndPointDto webhookEndPointDto = new WebhookEndPointDto();
        WebhookEndpoint webhookEndpoint = new WebhookEndpoint();
        Mockito.when(webhookEndpointMapper.toEntity(webhookEndPointDto)).thenReturn(webhookEndpoint);
        Mockito.when(webhookEndPointRepository.save(webhookEndpoint)).thenThrow(DataIntegrityViolationException.class);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> {
            webhookMultiplexerService.createWebhookEndpoint(webhookEndPointDto);
        });
    }

    @Test
    public void testCreateWebhookEndpoint_Exception() {
        // Arrange
        WebhookEndPointDto webhookEndPointDto = new WebhookEndPointDto();
        WebhookEndpoint webhookEndpoint = new WebhookEndpoint();
        Mockito.when(webhookEndpointMapper.toEntity(webhookEndPointDto)).thenReturn(webhookEndpoint);
        Mockito.when(webhookEndPointRepository.save(webhookEndpoint)).thenThrow(RuntimeException.class);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            webhookMultiplexerService.createWebhookEndpoint(webhookEndPointDto);
        });
    }
    @Test
    public void testCreateConsumerEndpoint_Success() throws BadRequestException {
        WebhookEndpoint webhookEndpoint = new WebhookEndpoint();
        ConsumerEndPointDto consumerEndPointDto = new ConsumerEndPointDto();
        Mockito.when(webhookEndPointRepository.findById(1)).thenReturn(java.util.Optional.of(webhookEndpoint));
        Mockito.when(consumerEndPointMapper.toEntity(consumerEndPointDto)).thenReturn(new ConsumerEndpoint());
        Mockito.when(consumerEndPointRepository.save(any())).thenReturn(new ConsumerEndpoint());

        ResponseEntity<ConsumerEndpoint> responseEntity = webhookMultiplexerService.createConsumerEndpoint(1, consumerEndPointDto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }


    @Test
    public void testCreateConsumerEndpoint_WebhookEndpointNotFound() {
        Mockito.when(webhookEndPointRepository.findById(1)).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            webhookMultiplexerService.createConsumerEndpoint(1, new ConsumerEndPointDto());
        });
    }

    @Test
    public void testCreateConsumerEndpoint_ConsumerEndpointAlreadyExists() {
        WebhookEndpoint webhookEndpoint = new WebhookEndpoint();
        ConsumerEndPointDto consumerEndPointDto = new ConsumerEndPointDto();
        Mockito.when(webhookEndPointRepository.findById(1)).thenReturn(java.util.Optional.of(webhookEndpoint));
        Mockito.when(consumerEndPointMapper.toEntity(consumerEndPointDto)).thenReturn(new ConsumerEndpoint());
        Mockito.when(consumerEndPointRepository.save(any())).thenThrow(DataIntegrityViolationException.class);

        assertThrows(BadRequestException.class, () -> {
            webhookMultiplexerService.createConsumerEndpoint(1, consumerEndPointDto);
        });
    }

    @Test
    public void testCreateConsumerEndpoint_Exception() {
        WebhookEndpoint webhookEndpoint = new WebhookEndpoint();
        ConsumerEndPointDto consumerEndPointDto = new ConsumerEndPointDto();
        Mockito.when(webhookEndPointRepository.findById(1)).thenReturn(java.util.Optional.of(webhookEndpoint));
        Mockito.when(consumerEndPointMapper.toEntity(consumerEndPointDto)).thenReturn(new ConsumerEndpoint());
        Mockito.when(consumerEndPointRepository.save(any())).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> {
            webhookMultiplexerService.createConsumerEndpoint(1, consumerEndPointDto);
        });
    }

    @Test
    public void testSaveDataToDb_Success() {
        ConsumerEndPointResponse consumerEndPointResponse = new ConsumerEndPointResponse();
        consumerEndPointResponse.setConsumerApiResponseStatus("Success");
        when(consumerEndPointResponseRepository.save(any())).thenReturn(consumerEndPointResponse);

        ConsumerEndPointResponse result = webhookMultiplexerService.saveDataToDb(new ConsumerEndPointResponseDto(HttpStatus.OK.toString(),""), new RequestDto(), "consumerEndPoint", "webHookEndPoint");

        assertEquals("Success", result.getConsumerApiResponseStatus());
    }

    @Test
    public void testConvertToRequestParameter_Success() {
        MultiValueMap<String, String> result = webhookMultiplexerService.convertToRequestParameter("param1=value1&param2=value2");

        assertEquals("value1", result.getFirst("param1"));
        assertEquals("value2", result.getFirst("param2"));
    }

    @Test
    public void testParseToHttpHeaders_Success() {
        HttpHeaders result = webhookMultiplexerService.parseToHttpHeaders("Content-Type: application/json\nAuthorization: Bearer token123");

        assertEquals("application/json", result.getFirst("Content-Type"));
        assertEquals("Bearer token123", result.getFirst("Authorization"));
    }

    @Test
    public void testForwardRequest_Success() throws BadRequestException {
        RequestDto requestDto = new RequestDto( "headers", "requestParam", "requestBody", HttpMethod.POST.toString());
        WebhookEndpoint webhookEndpoint = new WebhookEndpoint();
        when(webhookEndPointRepository.getById(anyInt())).thenReturn(webhookEndpoint);
        List<ConsumerEndpoint> consumerEndpointList = new ArrayList<>();
        when(consumerEndPointRepository.findAllByWebhookId(anyInt())).thenReturn(consumerEndpointList);
        ConsumerEndPointResponseDto responseDto = new ConsumerEndPointResponseDto();
        when(restClient.callConsumerUrl(anyString(), any(), anyString(), any(), any())).thenReturn(responseDto);
        when(consumerEndPointResponseRepository.save(any())).thenReturn(new ConsumerEndPointResponse());

        List<ConsumerEndPointResponse> result = webhookMultiplexerService.forwardRequest(1, requestDto);

        assertEquals(0, result.size()); // Assuming no consumer endpoints
    }


    @Test
    public void testConvertToHttpMethod_ValidMethod() {
        String methodString = "GET";

        HttpMethod httpMethod = webhookMultiplexerService.convertToHttpMethod(methodString);

        assertEquals(HttpMethod.GET, httpMethod);
    }


}
