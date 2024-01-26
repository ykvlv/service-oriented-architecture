FROM quay.io/wildfly/wildfly:26.1.2.Final-jdk17


# Закидываем конфигу для https
COPY wildfly-config-standalone.xml /opt/jboss/wildfly/standalone/configuration/standalone.xml
COPY ejb/src/main/resources/keystore/2service.jks /opt/jboss/wildfly/standalone/2service.jks

RUN /opt/jboss/wildfly/bin/add-user.sh admin admin --silent
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]


# Закидываем war и jar для того чтобы, wildfly при запуске их развернул автоматически
COPY ejb/target/ejb-4.0.0.jar /opt/jboss/wildfly/standalone/deployments/
COPY service/target/service.war /opt/jboss/wildfly/standalone/deployments/

EXPOSE 9990 9100
