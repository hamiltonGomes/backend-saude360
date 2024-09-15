package com.saude360.backendsaude360.services.external;

import com.saude360.backendsaude360.dtos.ImgurDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ImgurService {

    @Value("${imgur.token}")
    private String token;

    @Value("${imgur.client.id}")
    private String clientId;

    @Value("${imgur.client.secret}")
    private String clientSecret;

    @Value("${imgur.url}")
    private String url;

    private final WebClient webClient;

    @Autowired
    public ImgurService(WebClient.Builder webClientBuilder) {

        webClient = webClientBuilder.build();
    }

    public ImgurDto uploadImage(String base64Image) {

        Mono<ImgurDto> response = webClient.post()
                .uri(url)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("Authorization", "Bearer " + token)
                .body(BodyInserters.fromMultipartData("image", base64Image))
                .retrieve()
                .bodyToMono(ImgurDto.class);

        return response.block();
    }
}
