package soa.bookingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import soa.bookingservice.model.TicketDto;

import java.net.URI;
import java.util.Objects;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

@Service
public class ClientService {
    private final RestTemplate restTemplate;

    @Value("${ticketservice.url}")
    private String mainServiceUrl1 = "https://localhost:9099";
    private final String mainServiceUrl = System.getenv("MAIN_SERVICE_URL");

    @Autowired
    public ClientService(
            RestTemplate restTemplate
    ) {
        this.restTemplate = restTemplate;
    }

    private URI buildURIWithIntervals(String url) {
        UriComponentsBuilder builder = fromHttpUrl(url);
//        builder.queryParam("pageNumber", 0);
//        builder.queryParam("pageSize", 0);

        return builder.build().encode().toUri();
    }

    private String getMainServiceUrl() {
        if (StringUtils.isEmpty(mainServiceUrl)) {
            return mainServiceUrl1;
        } else {
            return mainServiceUrl;
        }
    }

    public TicketDto createVipTicket(Integer ticketId, Integer personId) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");

        String url = getMainServiceUrl() + "/tickets/vip/" + ticketId;
        URI uri = buildURIWithIntervals(url);

        return catchAnyException(uri, headers);
    }

    public TicketDto makeDiscountTicket(Integer ticketId, Integer personId, Double discount) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");

        String url = getMainServiceUrl() + "/tickets/discount/" + ticketId + "/" + discount;
        URI uri = buildURIWithIntervals(url);

        return catchAnyException(uri, headers);
    }

    private TicketDto catchAnyException(URI uri, MultiValueMap<String, String> headers) {
        try {
            ResponseEntity<TicketDto> response = restTemplate.exchange(uri, HttpMethod.POST,
                    new HttpEntity<>(headers), TicketDto.class);
            return Objects.requireNonNull(response.getBody());
        } catch (Exception e) {
            String formatted = e.getMessage().substring(7, e.getMessage().length() - 1);

            throw new RuntimeException(formatted);
        }
    }
}
