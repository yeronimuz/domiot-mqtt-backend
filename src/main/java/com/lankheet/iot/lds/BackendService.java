/**
 * MIT License
 * <p>
 * Copyright (c) 2017 Lankheet Software and System Solutions
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.lankheet.iot.lds;

import com.lankheet.iot.lds.config.BackendServiceConfig;
import com.lankheet.iot.datatypes.entities.Measurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * The web service has the following tasks:<BR>
 * <li>It accepts measurements from the message broker
 * <li>It saves the measurements in the database
 *
 */
public class BackendService
{
   private static final Logger LOG = LoggerFactory.getLogger(BackendService.class);


   public void run(String configurationFile)
      throws Exception
   {
      BackendServiceConfig config = BackendServiceConfig.loadConfigurationFromFile(configurationFile);
      BlockingQueue<Measurement> queue = new ArrayBlockingQueue<>(config.getInternalQueueSize());

      DatabaseAgent dbManager = new DatabaseAgent(config.getDatabaseConfig(), queue);
      MqttReader mqttReader = new MqttReader(config.getMqttConfig(), queue);
      Runtime.getRuntime().addShutdownHook(new Thread()
      {
         public void run()
         {
            LOG.warn("Shutdown Hook is running !");
            // TODO: gracefully shutdown threads
            if (mqttReader.isRunning())
            {
               mqttReader.setRunFlag(false);
            }
            if (dbManager.isRunning())
            {
               dbManager.setRunFlag(false);
            }
         }
      });

      new Thread(dbManager).start();
      new Thread(mqttReader).start();
   }
}
