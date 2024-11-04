mosquitto_pub -h localhost -p 1883 -u user -P password -t 'register' -m '{"manufacturerId":null,"modelId":null,"firmwareVersion":null,"hardwareVersion":"RaspberryPi B+","macAddress":"B8:27:EB:7E:3C:F4","parameters":[{"name":"internalQueueSize","parameterType":"NUMBER","value":10000,"readonly":false},{"name":"repeatValuesAfter","parameterType":"NUMBER","value":3600000,"readonly":false},{"name":"HW:logicalProcessors","parameterType":"NUMBER","value":4,"readonly":false},{"name":"jvm:maxMemory(B)","parameterType":"NUMBER","value":235274240,"readonly":false},{"name":"jvm:totalMemory(B)","parameterType":"NUMBER","value":16515072,"readonly":false},{"name":"OS","parameterType":"STRING","value":"GNU/Linux Raspbian GNU/Linux 11 (bullseye) build 6.1.21-v7+","readonly":false},{"name":"usb devices","parameterType":"STRING","value":"DWC OTG Controller","readonly":false},{"name":": file system root","parameterType":"STRING","value":"/","readonly":false},{"name":": total space(B)","parameterType":"NUMBER","value":15288197120,"readonly":false},{"name":": free space(B)","parameterType":"NUMBER","value":8770912256,"readonly":false},{"name":": usable space(B)","parameterType":"NUMBER","value":8116449280,"readonly":false}],"sensors":[{"sensorId":0,"deviceMac":null,"topic":{"type":"power","path":"meterbox/sensor/power/pt1"},"type":"POWER_PT1","parameters":null},{"sensorId":0,"deviceMac":null,"topic":{"type":"power","path":"meterbox/sensor/power/pt2"},"type":"POWER_PT2","parameters":null},{"sensorId":0,"deviceMac":null,"topic":{"type":"power","path":"meterbox/sensor/power/ct1"},"type":"POWER_CT1","parameters":null},{"sensorId":0,"deviceMac":null,"topic":{"type":"power","path":"meterbox/sensor/power/ct2"},"type":"POWER_CT2","parameters":null},{"sensorId":0,"deviceMac":null,"topic":{"type":"power","path":"meterbox/sensor/power/ac"},"type":"POWER_AC","parameters":null},{"sensorId":0,"deviceMac":null,"topic":{"type":"power","path":"meterbox/sensor/power/ap"},"type":"POWER_AP","parameters":null},{"sensorId":0,"deviceMac":null,"topic":{"type":"gas","path":"meterbox/sensor/gas/c"},"type":"GAS_METER","parameters":null}],"actuators":null}'