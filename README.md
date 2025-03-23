# domiot-backend

* Subscribe to all data from sensors handled by an MQTT broker
* Register new devices and provide feedback about sensor id's and configuration parameters.

## registration
* The device reports itself without deviceId and sensorIds and uses the 'register' subject.
* Backend stores device with all known info (at least brand, model, hardware and firmware version) and stores all sensors (types are known to the backend)
* Backend publishes the new configuration including the deviceId and sensorId(s) on the 'config' topic.

Exceptions: 
* Device already known: Just update hardware and firmware version (in case an update was finished)
* Sensor already known: Just return the sensor information

Rationale: Further sensor and device information should be managed by the management interface (responsibility of the webservice)

## Starting the service

Dockerized, TODO

## It has the following features:

* Read device data from MQTT broker, such as device data, config data
* Store the received data in a database
* Provide Devicesr with (new) config data

## Technical specs

The backend uses 3 class types for the same domain object:

* Entity: Used in the database domain
* Dto: Used in the MQTT domain
* No extension: Used in the REST interface

The backend converts MQTT (Dto) to Entities to store in the database
It converts Entities to REST objects for use in the frontend or MTM communication.

# How to setup the domotics ecosystem:

The following items are required:

* An mqtt message broker, like mosquitto
* Some mqtt devices

## Simple setup

TODO

# Installing mosquitto

You could do this several ways. I installed mosquitto on

1. Install MQTT message broker on a local or remote system. In my case I installed Mosquitto on the local (VPS) system
   and one on the Raspberry Pi at home. These two are bridged together.
1. Configure the central Mosquitto as follows: In /etc/mosquitto/conf.d/default.conf:
```
   allow_anonymous false
   password_file /etc/mosquitto/passwd

listener 1883 localhost

listener 8883
certfile /etc/letsencrypt/live/mqtt.example.com/cert.pem
cafile /etc/letsencrypt/live/mqtt.example.com/chain.pem
keyfile /etc/letsencrypt/live/mqtt.example.com/privkey.pem
</code>

Reference: https://www.digitalocean.com/community/tutorials/how-to-install-and-secure-the-mosquitto-mqtt-messaging-broker-on-ubuntu-16-04
Configuring the mosquitto (on site):
<code>
connection mqtt_example_com
address mqtt.example.com:8883
topic test both 0 "" ""
remote_username your_user
remote_password your_passwd
bridge_cafile /etc/pki/ca-trust/extracted/pem/tls-ca-bundle.pem
```

1. Create a database with a user that has full permissions on the database.
1. Configure the user credentials in the application.yml configuration file. FIXME: No config file
1. Give your JRE enough permission to read from your mosquitto mqtt broker. Open
   /usr/lib/jvm/java-8-oracle/jre/lib/security/java.policy (if this is the location of your current JVM).
1. Add a line:
```
   // allows anyone to listen on dynamic ports
   permission java.net.SocketPermission "localhost:0", "listen";
   permission java.net.SocketPermission "locahost:1883", "listen,resolve";

```
1. In this case, the mqtt broker listens on local host, port 1883
1. Don't forget the semi-colon at the end, or you will not receive any measurements.
1. You are ready to start the service
Please review the settings in the application.yml configuration file for logging, mqtt and database settings.

Now you have a service that handles your home's sensor data.
See the power-meter repo: https://github.com/yeronimuz/PowerMeter for reading measurements from your smart power meter.
The web-service handles the domotics environment, see the web-service repo: https://github.com/yeronimuz/lnb-iot-webservice

# What's next?
* Database robustness, reconnection ability.
* Storing data locally (Redis) while no database connection is possible
* Database health check
* MQTT broker health check


## misc code snippets



### Registering a device 
mosquitto_pub -h localhost -p 1883 -u user -P pwd -t 'register' -m '{"manufacturerId":null,"modelId":null,"firmwareVersion":null,"hardwareVersion":"RaspberryPi B+","macAddress":"B8:27:EB:7E:3C:F4","parameters":[{"name":"internalQueueSize","parameterType":"NUMBER","value":10000,"readonly":false},{"name":"repeatValuesAfter","parameterType":"NUMBER","value":3600000,"readonly":false},{"name":"HW:logicalProcessors","parameterType":"NUMBER","value":4,"readonly":false},{"name":"jvm:maxMemory(B)","parameterType":"NUMBER","value":235274240,"readonly":false},{"name":"jvm:totalMemory(B)","parameterType":"NUMBER","value":16515072,"readonly":false},{"name":"OS","parameterType":"STRING","value":"GNU/Linux Raspbian GNU/Linux 11 (bullseye) build 6.1.21-v7+","readonly":false},{"name":"usb devices","parameterType":"STRING","value":"DWC OTG Controller","readonly":false},{"name":": file system root","parameterType":"STRING","value":"/","readonly":false},{"name":": total space(B)","parameterType":"NUMBER","value":15288197120,"readonly":false},{"name":": free space(B)","parameterType":"NUMBER","value":8770912256,"readonly":false},{"name":": usable space(B)","parameterType":"NUMBER","value":8116449280,"readonly":false}],"sensors":[{"sensorId":0,"deviceMac":null,"topic":{"type":"power","path":"meterbox/sensor/power/pt1"},"type":"POWER_PT1","parameters":null},{"sensorId":0,"deviceMac":null,"topic":{"type":"power","path":"meterbox/sensor/power/pt2"},"type":"POWER_PT2","parameters":null},{"sensorId":0,"deviceMac":null,"topic":{"type":"power","path":"meterbox/sensor/power/ct1"},"type":"POWER_CT1","parameters":null},{"sensorId":0,"deviceMac":null,"topic":{"type":"power","path":"meterbox/sensor/power/ct2"},"type":"POWER_CT2","parameters":null},{"sensorId":0,"deviceMac":null,"topic":{"type":"power","path":"meterbox/sensor/power/ac"},"type":"POWER_AC","parameters":null},{"sensorId":0,"deviceMac":null,"topic":{"type":"power","path":"meterbox/sensor/power/ap"},"type":"POWER_AP","parameters":null},{"sensorId":0,"deviceMac":null,"topic":{"type":"gas","path":"meterbox/sensor/gas/c"},"type":"GAS_METER","parameters":null}],"actuators":null}'
