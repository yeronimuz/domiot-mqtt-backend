# domiot-backend

* Subscribe to all data from an MQTT broker
* Convert measurements to an hourly, daily, monthly graph. This conversion will be scheduled periodically.
* Register new devices and provide feedback about sensor id's and configuration parameters.

## Starting the service

Dockerized, TODO

## It has the following features:

* Read device data from MQTT broker, such as device data, config data
* Store the received data in a database
* Provide MQTT broker with (new) config data

## Technical specs

The backend uses 3 class types for the same object:

* Entity: Used in the database domain
* Dto: Used in the MQTT domain
* No extension: Used in the REST interface

The backend converts MQTT (Dto) to Entities to store in the database
It converts Entities to REST objects for use in the frontend.

# How to setup the domotics ecosystem:

The following items are required:

* An mqtt message broker, like mosquitto
* Docker
* Some mqtt devices

## Simple setup

## Remote setup

In this setup all devices and an mqtt message broker are running locally and a bridged one is running remotely. All
other services have to be running in the cloud.

# Installing mosquitto

You could do this several ways. I installed mosquitto on

1. Install MQTT message broker on a local or remote system. In my case I installed Mosquitto on the local (VPS) system
   and one on the Raspberry Pi at home. These two are bridged together.
1. Configure the central Mosquitto as follows: In /etc/mosquitto/conf.d/default.conf:
   <code>
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
</code>

1. Create a database with a user that has full permissions on the database.
1. Configure the user credentials in the application.yml configuration file. FIXME: No config file
1. Give your JRE enough permission to read from your mosquitto mqtt broker. Open
   /usr/lib/jvm/java-8-oracle/jre/lib/security/java.policy (if this is the location of your current JVM).
1. Add a line:
   <code>
   // allows anyone to listen on dynamic ports
   permission java.net.SocketPermission "localhost:0", "listen";
   permission java.net.SocketPermission "locahost:1883", "listen,resolve";

</code>
1. In this case, the mqtt broker listens on local host, port 1883
1. Don't forget the semi-colon at the end, or you will not receive any measurements.
1. You are ready to start the service
Please review the settings in the application.yml configuration file for logging, mqtt and database settings.

# What's next?

Now you have a micro service that handles your home's sensor data.
See the power-meter repo: https://github.com/yeronimuz/PowerMeter for reading measurements from your smart power meter.
and the web-service repo: https://github.com/yeronimuz/lnb-iot-webservice

## misc code snippets

docker context create remote --docker host=ssh://jeroen@192.168.2.75

docker-compose --context remote up -d
