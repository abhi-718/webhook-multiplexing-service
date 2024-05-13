package com.webhookmultiplexerservice.webhookmultiplexerservice.service.impl;

import com.webhookmultiplexerservice.webhookmultiplexerservice.dto.ConsumerEndPointDto;
import com.webhookmultiplexerservice.webhookmultiplexerservice.dto.RequestDto;
import com.webhookmultiplexerservice.webhookmultiplexerservice.dto.ResponseDto.ConsumerEndPointResponseDto;
import com.webhookmultiplexerservice.webhookmultiplexerservice.dto.WebhookEndPointDto;
import com.webhookmultiplexerservice.webhookmultiplexerservice.enums.ErrorCode;
import com.webhookmultiplexerservice.webhookmultiplexerservice.model.*;
import com.webhookmultiplexerservice.webhookmultiplexerservice.service.ConsumerEndPointMapper;
import com.webhookmultiplexerservice.webhookmultiplexerservice.service.WebhookEndpointMapper;
import com.webhookmultiplexerservice.webhookmultiplexerservice.service.WebhookMultiplexerService;
import com.webhookmultiplexerservice.webhookmultiplexerservice.service.common.RestClient;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class WebhookMultiplexerServiceImpl implements WebhookMultiplexerService {

    private static final Logger logger = LoggerFactory.getLogger(WebhookMultiplexerServiceImpl.class);
    @Autowired
    private WebhookEndpointMapper webhookEndpointMapper;

    @Autowired
    private WebhookEndPointRepository webhookEndPointRepository;

    @Autowired
    private ConsumerEndPointRepository consumerEndPointRepository;

    @Autowired
    private ConsumerEndPointMapper consumerEndPointMapper;

    @Autowired
    private ConsumerEndPointResponseRepository consumerEndPointResponseRepository;

    @Autowired
    private RestClient restClient;

    /**
     * This function will verify if a WebHook Endpoint with the given name exists.
     * If it does exist, it will raise a BadRequest Exception.
     * However, if it doesn't exist, it will create a new entry in the webhook_endpoint table.
     *
     * @param webhookEndPointDto: This Dto Contains all the Information required to create webhook Endpoint i.e apiName and endPoint
     * @throws BadRequestException: If the Api Name is already exist in the DB itself then it is going to throw BadRequest Exception
     */
    @Override
    public ResponseEntity<WebhookEndpoint> createWebhookEndpoint(WebhookEndPointDto webhookEndPointDto) throws BadRequestException {

        WebhookEndpoint webhookEndpoint = webhookEndpointMapper.toEntity(webhookEndPointDto);
        WebhookEndpoint savedWebhookEndpoint = null;
        try{
            savedWebhookEndpoint = webhookEndPointRepository.save(webhookEndpoint);
        } catch (DataIntegrityViolationException dive){
            logger.error("Webhook api exists for the webhook api with endPoint: {}",webhookEndpoint.getEndPoint());
            throw new BadRequestException(ErrorCode.ENTITY_ALREADY_EXIST.toString());
        } catch(Exception e){
            logger.error("Webhook api save request {} failed with error: ",webhookEndpoint,e);
            throw e;
        }
        return new ResponseEntity<WebhookEndpoint>(savedWebhookEndpoint, HttpStatus.CREATED);
    }

    /**
     * This function will take Webhook Id and Consumer Dto as Input
     * and it will create an entry on Consumer_endPoint Table
     * @param webHookId:
     * @param consumerEndPointDto
     * @return
     * @throws BadRequestException
     */
    @Override
    public ResponseEntity<ConsumerEndpoint> createConsumerEndpoint(Integer webHookId, ConsumerEndPointDto consumerEndPointDto) throws BadRequestException {
        WebhookEndpoint webhookEndpoint = webhookEndPointRepository.findById(webHookId)
                .orElseThrow(() -> new EntityNotFoundException("WebHook Entity not found"));
        consumerEndPointDto.setWebHookEndPoint(webhookEndpoint);
        ConsumerEndpoint savedConsumerEndPoint = null;
        ConsumerEndpoint consumerEndpoint = consumerEndPointMapper.toEntity(consumerEndPointDto);
        try {
            savedConsumerEndPoint = consumerEndPointRepository.save(consumerEndpoint);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            logger.error("Webhook api exists for the webhook api with endPoint: {}",webhookEndpoint.getEndPoint());
            throw new BadRequestException(ErrorCode.ENTITY_ALREADY_EXIST.toString());
        }catch(Exception e){
            logger.error("Webhook api save request {} failed with error: ",webhookEndpoint,e);
            throw e;
        }

        return new ResponseEntity<ConsumerEndpoint>(savedConsumerEndPoint, HttpStatus.CREATED);

    }

    /**
     *
     * @param webHookId
     * @param requestDto
     * @return
     * @throws BadRequestException
     */
    @Override
    public List<ConsumerEndPointResponse> forwardRequest(Integer webHookId, RequestDto requestDto) throws BadRequestException {

        WebhookEndpoint webhookEndpoint = webhookEndPointRepository.getById(webHookId);
        String endPoint = webhookEndpoint.getEndPoint();
        logger.info(webHookId.toString());
        if (Objects.isNull(webhookEndpoint)) {
            throw new BadRequestException();
        }
        List<ConsumerEndpoint> consumerEndpointList = consumerEndPointRepository.findAllByWebhookId(webHookId);
        List<ConsumerEndPointResponse> consumerResponseList = new ArrayList<>();
        for (ConsumerEndpoint consumerEndpoint: consumerEndpointList) {
            logger.info(consumerEndpoint.getEndPoint());
            String consumerEndPoint = consumerEndpoint.getEndPoint();
            HttpHeaders headers = parseToHttpHeaders(requestDto.getHeaders());
            MultiValueMap<String,String> requestParameterList = Objects.nonNull(requestDto.getRequestParameter()) ?
                    convertToRequestParameter(requestDto.getRequestParameter()): null;
            String requestBody = requestDto.getRequestBody();
            HttpMethod httpMethod = convertToHttpMethod(requestDto.getRequestMethod());
            ConsumerEndPointResponseDto consumerEndPointResponseDto = restClient.callConsumerUrl(consumerEndPoint,headers,requestBody,requestParameterList, httpMethod);
            ConsumerEndPointResponse consumerEndPointResponse = saveDataToDb(consumerEndPointResponseDto, requestDto, consumerEndPoint, endPoint);
            consumerResponseList.add(consumerEndPointResponse);
        }

        return consumerResponseList;

    }

    public  HttpMethod convertToHttpMethod(String method) {
        try {
            return HttpMethod.valueOf(method.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid HTTP method: " + method);
        }
    }

    public ConsumerEndPointResponse saveDataToDb(ConsumerEndPointResponseDto consumerEndPointResponseDto,
                                                  RequestDto requestDto, String consumerEndPoint, String webHookEndPoint) {
        ConsumerEndPointResponse consumerEndPointResponse = new ConsumerEndPointResponse();
        consumerEndPointResponse.setConsumerApiResponseStatusCode(consumerEndPointResponseDto.getStatusCode());
        consumerEndPointResponse.setHeaders(requestDto.getHeaders());
        consumerEndPointResponse.setWebHookEndPoint(webHookEndPoint);
        consumerEndPointResponse.setConsumerEndPoint(consumerEndPoint);
        consumerEndPointResponse.setRequestParam(requestDto.getRequestParameter());
        consumerEndPointResponse.setRequestBody(requestDto.getRequestBody());
        consumerEndPointResponse.setRequestMethod(requestDto.getRequestMethod());
        consumerEndPointResponse.setConsumerApiResponse(consumerEndPointResponseDto.getResponse());
        consumerEndPointResponse.setConsumerApiResponseStatus(consumerEndPointResponseDto.getStatusCode().toString().equals(HttpStatus.OK.toString())?"Success":"Failed");
        return consumerEndPointResponseRepository.save(consumerEndPointResponse);
    }

    public MultiValueMap<String, String> convertToRequestParameter(String paramString) {
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        String[] keyValuePairs = paramString.split("&");
        for (String pair : keyValuePairs) {
            String[] keyValue = pair.split("=", 2); // Split once
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = keyValue[1];
                paramMap.add(key, value);
            }
        }
        return paramMap;
    }

    public HttpHeaders parseToHttpHeaders(String headersString) {
        HttpHeaders headers = new HttpHeaders();
        String[] headerLines = headersString.split("\n");
        for (String headerLine : headerLines) {
            String[] headerParts = headerLine.split(":", 2); // Split once
            if (headerParts.length == 2) {
                String headerName = headerParts[0].trim();
                String headerValue = headerParts[1].trim();
                headers.add(headerName, headerValue);
            }
        }
        return headers;
    }
}
