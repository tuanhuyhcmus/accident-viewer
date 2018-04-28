/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thesis.api.server;

import thesis.api.common.Config;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import thesis.api.controller.AccidentController;
import thesis.api.controller.ScheduleController;
import thesis.api.controller.TrafficController;
import thesis.api.controller.UserController;

/**
 *
 * @author huynct
 */
public class WebServer implements Runnable {

    private static final Logger logger = Logger.getLogger(WebServer.class);
    private Server server = new Server();
    private static WebServer _instance = null;
    private static final Lock createLock_ = new ReentrantLock();

    public static WebServer getInstance() {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new WebServer();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }

    @Override
    public void run() {
        try {
            int http_port = Integer.valueOf(Config.getParam("server", "http_port"));

            ServerConnector connector = new ServerConnector(server);
            connector.setPort(http_port);
            connector.setIdleTimeout(30000);

            server.setConnectors(new Connector[]{connector});
            logger.info("Start server...");

            ServletHandler servletHandler = new ServletHandler();

            servletHandler.addServletWithMapping(UserController.class, "/api/user/*");
            servletHandler.addServletWithMapping(ScheduleController.class, "/api/schedule/*");
            servletHandler.addServletWithMapping(AccidentController.class, "/api/accident/*");
            servletHandler.addServletWithMapping(TrafficController.class, "/api/traffic/*");

            ServletContextHandler servletContext = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
            servletContext.setContextPath("/");
            servletContext.setHandler(servletHandler);

            // Specify the Session ID Manager
            HashSessionIdManager idmanager = new HashSessionIdManager();
            server.setSessionIdManager(idmanager);

            // Create the SessionHandler (wrapper) to handle the sessions
            HashSessionManager manager = new HashSessionManager();
            SessionHandler sessions = new SessionHandler(manager);

            HandlerList handlers = new HandlerList();
            handlers.setHandlers(new Handler[]{sessions, servletContext, new DefaultHandler()});
            server.setHandler(handlers);

            server.start();
            server.join();
        } catch (Exception e) {
            logger.error("Cannot start web server: " + e.getMessage());
            System.exit(1);
        }
    }

    public void stop() throws Exception {
        server.stop();
    }
}
