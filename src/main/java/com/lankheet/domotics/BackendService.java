package com.lankheet.domotics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.lankheet.backend.resources.BackendInfoResource;
import com.lankheet.backend.resources.BackendServiceInfo;
import com.lankheet.backend.resources.MeasurementsResource;
import com.lankheet.domotics.config.BackendServiceConfig;
import com.lankheet.domotics.health.DatabaseHealthCheck;
import com.lankheet.domotics.health.MqttConnectionHealthCheck;
import com.lankheet.utils.TcpPortUtil;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * The web service has the following tasks:<BR>
 * <li>It accepts measurements from the message broker
 * <li>It saves the measurements in the database
 * <li>It serves as a resource for the measurements database
 *
 */
public class BackendService extends Application<BackendServiceConfig> {
	private static final Logger LOG = LogManager.getLogger(BackendService.class);
	
    private static final int TIMEOUT_FOR_PORTSCAN = 200;
    private static final int DEFAULT_MQTT_PORT = 1883;
    private static final String DEFAULT_MQTT_HOST = "localhost";

    private BackendServiceConfig configuration;

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			LOG.error("Missing or wrong arguments");
		} else {
			new BackendService().run(args[0], args[1]);
		}
	}

	@Override
	public void initialize(Bootstrap<BackendServiceConfig> bootstrap) {
		LOG.info("Lankheet LNB IOT web service", "");
	}

	@Override
	public void run(BackendServiceConfig configuration, Environment environment) throws Exception {
	    this.setConfiguration(configuration);
        if (!TcpPortUtil.isPortOpen(DEFAULT_MQTT_HOST, DEFAULT_MQTT_PORT, TIMEOUT_FOR_PORTSCAN)) {
            LOG.fatal("Mqtt port not accessible");
            System.exit(-1);
        } else {
            LOG.info("MQTT port available");
        }
        DatabaseManager dbManager = new DatabaseManager(configuration.getDatabaseConfig());
        MqttClientManager mqttClientManager = new MqttClientManager(configuration.getMqttConfig(), dbManager);
        BackendInfoResource webServiceInfoResource = new BackendInfoResource(new BackendServiceInfo());
        MeasurementsResource measurementsResource = new MeasurementsResource(dbManager);
        environment.getApplicationContext().setContextPath("/api");
        environment.lifecycle().manage(mqttClientManager);
        environment.lifecycle().manage(dbManager);
        environment.jersey().register(webServiceInfoResource);
        environment.jersey().register(measurementsResource);

        environment.healthChecks().register("database", new DatabaseHealthCheck(dbManager));
        environment.healthChecks().register("mqtt-server", new MqttConnectionHealthCheck(mqttClientManager));
	}

    public BackendServiceConfig getConfiguration() {
        return configuration;
    }

    public void setConfiguration(BackendServiceConfig configuration) {
        this.configuration = configuration;
    }
}
