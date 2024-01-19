package soa.controller;

import soa.api.OrgdirectoryApi;
import soa.ejb.service.RestClientServiceBean;
import soa.util.JndiUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import soa.ejb.service.RestClientService;

import java.io.InputStream;
import java.util.Properties;

@CrossOrigin(origins = "*")
@RestController
public class OrgdirectoryController implements OrgdirectoryApi {
    private static final String EJB_ARTIFACT_GROUP = "soa";
    private static final String EJB_ARTIFACT_NAME = "ejb";

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
        return "ejb:/" + EJB_ARTIFACT_NAME
                + "-" + getVersion()
                + "/" + RestClientServiceBean.class.getSimpleName()
                + "!" + RestClientService.class.getName();
    }

    public synchronized String getVersion() {
        String version = null;

        // try to load from maven properties first
        try {
            Properties p = new Properties();
            InputStream is = getClass().getResourceAsStream(
                    "/META-INF/maven/" + EJB_ARTIFACT_GROUP + "/" + EJB_ARTIFACT_NAME + "/pom.properties"
            );
            if (is != null) {
                p.load(is);
                version = p.getProperty("version", "");
            }
        } catch (Exception e) {
            // ignore
        }

        // fallback to using Java API
        if (version == null) {
            Package aPackage = getClass().getPackage();
            if (aPackage != null) {
                version = aPackage.getImplementationVersion();
                if (version == null) {
                    version = aPackage.getSpecificationVersion();
                }
            }
        }

        if (version == null) {
            // we could not compute the version so use a blank
            version = "";
        }

        return version;
    }
}
