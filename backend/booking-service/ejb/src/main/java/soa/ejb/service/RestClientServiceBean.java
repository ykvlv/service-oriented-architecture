package soa.ejb.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.jboss.ejb3.annotation.Pool;
import soa.ejb.external.HttpClientFactory;
import soa.ejb.model.TicketDto;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Stateless
@Remote(RestClientService.class)
@Pool(value = "restClientServicePool")
public class RestClientServiceBean implements RestClientService {
    private Client client;
    private final ObjectMapper objectMapper;

    public RestClientServiceBean() {
        JavaTimeModule module = new JavaTimeModule();
        LocalDateDeserializer localDateTimeDeserializer =  new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        module.addDeserializer(LocalDate.class, localDateTimeDeserializer);
        LocalDateSerializer localDateSerializer = new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        module.addSerializer(localDateSerializer);
        objectMapper = new ObjectMapper().registerModule(module).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private final String mainServiceUrl = System.getenv("MAIN_SERVICE_URL");

    public TicketDto createVipTicket(Integer ticketId, Integer personId) {
        System.out.println("HELLO MY NAME");
        System.out.println("TICKET: " + ticketId + ", PERSON: " + personId);
        client = HttpClientFactory.getJerseyHTTPSClient();

        String url = mainServiceUrl + "/tickets/vip/" + ticketId;
        System.out.println(url);
        Response response = client.target(url).request(MediaType.APPLICATION_JSON_TYPE).post(null);
        System.out.println(response.toString());
        TicketDto ticketDto = response.readEntity(TicketDto.class);
        System.out.println(ticketDto);
        try {
            System.out.println(response.readEntity(String.class));
        } catch (Exception e) {
            System.out.println("А не в строку не получилсоь");
        }

        return ticketDto;
    }

    public TicketDto makeDiscountTicket(Integer ticketId, Integer personId, Double discount) {
        client = HttpClientFactory.getJerseyHTTPSClient();

        String url = mainServiceUrl + "/tickets/discount/" + ticketId + "/" + discount;
        Response response = client.target(url).request(MediaType.APPLICATION_JSON_TYPE).post(null);
        return response.readEntity(TicketDto.class);
    }
}
