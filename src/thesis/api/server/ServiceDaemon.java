/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thesis.api.server;

import org.apache.log4j.Logger;

import thesis.api.common.Config;

/**
 *
 * @author huynct
 */
public class ServiceDaemon {

    private static final String DEFAULT_CONFIGURATION_FILE = "happy-drive.conf";
    private static final Logger logger = Logger.getLogger(ServiceDaemon.class);
    private static WebServer webServer = null;
            
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        try {
            Config.init(DEFAULT_CONFIGURATION_FILE);
          
            webServer = WebServer.getInstance();
            new Thread(webServer).start();
            
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        logger.info("Shutdown thread before webserver getinstance");
                        if (webServer != null) {
                            webServer.stop();
                        }
                    
                    } catch (Exception e) {
                    }
                }
            }, "Stop Jetty Hook"));
        } catch (Throwable e) {
            String msg = "Exception encountered during startup.";
            logger.error(msg, e);
            System.out.println(msg);
            logger.error("Uncaught exception: " + e.getMessage(),e);
            System.exit(3);
        }
    }
    
}
