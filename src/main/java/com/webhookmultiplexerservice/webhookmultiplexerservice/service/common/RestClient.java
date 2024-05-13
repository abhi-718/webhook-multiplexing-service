package com.webhookmultiplexerservice.webhookmultiplexerservice.service.common;

import com.webhookmultiplexerservice.webhookmultiplexerservice.dto.ResponseDto.ConsumerEndPointResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class RestClient {

    @Qualifier("restTemplate1")
    @Autowired
    private RestTemplate restTemplate;
    private Logger logger = LoggerFactory.getLogger(RestClient.class);


    /**
     * This function will Call the Consumer Url and Will get the Response.
     * @param endpoint: consumerEndPoint
     * @param headers: header
     * @param requestBody: RequestBody / (Null if it is Get Request)
     * @param requestParams: Request Parameter
     * @param method: Get/Post
     * @return: Reponse of the Url and Status Code.
     */
    public ConsumerEndPointResponseDto callConsumerUrl(String endpoint, HttpHeaders headers, String requestBody, MultiValueMap<String,String> requestParams, HttpMethod method) {
        HttpEntity<Object> httpEntity = new HttpEntity<>(requestBody, headers);
        ConsumerEndPointResponseDto consumerEndPointResponseDto = new ConsumerEndPointResponseDto();
        StringBuilder urlBuilder = new StringBuilder(endpoint);
        if (requestParams != null && !requestParams.isEmpty()) {
            urlBuilder.append("?");
            requestParams.forEach((key, value) -> urlBuilder.append(key).append("=").append(value.get(0)).append("&"));
        }
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(urlBuilder.toString(), method, httpEntity, String.class);
            consumerEndPointResponseDto.setStatusCode(responseEntity.getStatusCode().toString());
            consumerEndPointResponseDto.setResponse(responseEntity.getBody());
        } catch(HttpStatusCodeException e){
            logger.error("Error in Payload to getAccountSegment due to : {}, for Application Id : {}",e.getMessage(), endpoint, e);
            consumerEndPointResponseDto.setStatusCode(HttpStatus.BAD_REQUEST.toString());
        } catch(RestClientException e){
            logger.error("No Response Payload for getAccountSegment Due to : {}, for Application Id : {}",e.getMessage(),endpoint, e);
            consumerEndPointResponseDto.setStatusCode(HttpStatus.BAD_REQUEST.toString());
        } catch(Exception e){
            logger.error("No Response Payload for getAccountSegment Due to some exception for Application Id : {}", endpoint, e);
            consumerEndPointResponseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        }


        return consumerEndPointResponseDto;
    }
}
