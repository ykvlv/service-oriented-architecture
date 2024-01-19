package soa.controller;

import soa.api.OrgdirectoryApi;
import soa.util.JndiUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import soa.ejb.service.RestClientService;

@CrossOrigin(origins = "*")
@RestController
public class OrgdirectoryController implements OrgdirectoryApi {

    @Override
    public ResponseEntity<?> increaseStepsCount(Integer ticketId, Integer personId) {
        return ResponseEntity.ok(getService().createVipTicket(ticketId, personId));
    }

    @Override
    public ResponseEntity<?> makeDiscount(Integer ticketId, Integer personId, Double discount) {
        return ResponseEntity.ok(getService().makeDiscountTicket(ticketId, personId, discount));
    }

    private RestClientService getService() {
        return JndiUtils.getFromContext(RestClientService.class, getFullName());
    }

    @SuppressWarnings("rawtypes")
    private String getFullName() {
        return "ejb:/ejb-3.0.0/RestClientServiceBean!soa.ejb.service.RestClientService";
    }


}
