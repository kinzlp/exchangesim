package com.mk.exsim.gateway.fix;

import org.quickfixj.jmx.JmxExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;
import quickfix.mina.acceptor.DynamicAcceptorSessionProvider;
import quickfix.mina.acceptor.DynamicAcceptorSessionProvider.TemplateMapping;

import javax.annotation.Nonnull;
import javax.management.JMException;
import javax.management.ObjectName;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.*;

import static quickfix.Acceptor.*;

/**
 * Fix adaptor service initial the socket acceptor for the fix session.
 */
public class FixAdaptorService {
    private static final Logger log = LoggerFactory.getLogger(FixAdaptorService.class);
    private final SocketAcceptor acceptor;
    private final JmxExporter jmxExporter;
    private final ObjectName connectorObjectName;

    private final Map<InetSocketAddress, List<TemplateMapping>> dynamicSessionMappings = new
            HashMap<>();

    /**
     * @param settingFile setting filename that will be lookup in classpath
     * @param application QuickFixJ application
     * @throws JMException
     * @throws ConfigError
     * @throws FieldConvertError
     */
    public FixAdaptorService(@Nonnull String settingFile, @Nonnull Application application)
            throws JMException, ConfigError, FieldConvertError, FileNotFoundException {
        Objects.requireNonNull(settingFile);
        Objects.requireNonNull(application);

        InputStream inputStream = FixAdaptorService.class.getClassLoader().getResourceAsStream(settingFile);
        if (inputStream==null) {
            throw new ConfigError(settingFile + " file not found in resources path.");
        }
        SessionSettings settings = new SessionSettings(inputStream);
        MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new SLF4JLogFactory(settings);
        MessageFactory messageFactory = new DefaultMessageFactory();
        acceptor = new SocketAcceptor(application, messageStoreFactory, settings, logFactory,
                messageFactory);

        configureDynamicSessions(settings, application, messageStoreFactory, logFactory,
                messageFactory);

        // Export JMX
        jmxExporter = new JmxExporter();
        connectorObjectName = jmxExporter.register(acceptor);
        log.info("Acceptor registered with JMX, name=" + connectorObjectName);
    }

    /**
     * @param settings
     * @param application
     * @param messageStoreFactory
     * @param logFactory
     * @param messageFactory
     * @throws ConfigError
     * @throws FieldConvertError
     */
    private void configureDynamicSessions(SessionSettings settings, Application application,
                                          MessageStoreFactory messageStoreFactory,
                                          LogFactory logFactory,
                                          MessageFactory messageFactory)
            throws ConfigError, FieldConvertError {

        Iterator<SessionID> sectionIterator = settings.sectionIterator();
        while (sectionIterator.hasNext()) {
            SessionID sessionID = sectionIterator.next();
            // Setup dynamic session if a Session Template is detected
            if (isSessionTemplate(settings, sessionID)) {
                InetSocketAddress address = getAcceptorSocketAddress(settings, sessionID);
                getMappings(address).add(new TemplateMapping(sessionID, sessionID));
            }
        }

        for (Map.Entry<InetSocketAddress, List<TemplateMapping>> entry : dynamicSessionMappings
                .entrySet()) {
            acceptor.setSessionProvider(entry.getKey(), new DynamicAcceptorSessionProvider(
                    settings, entry.getValue(), application, messageStoreFactory, logFactory,
                    messageFactory));
        }
    }

    private List<TemplateMapping> getMappings(InetSocketAddress address) {
        List<TemplateMapping> mappings = dynamicSessionMappings.get(address);
        if (mappings == null) {
            mappings = new ArrayList<TemplateMapping>();
            dynamicSessionMappings.put(address, mappings);
        }
        return mappings;
    }

    private InetSocketAddress getAcceptorSocketAddress(SessionSettings settings, SessionID
            sessionID)
            throws ConfigError, FieldConvertError {
        String acceptorHost = "0.0.0.0";
        if (settings.isSetting(sessionID, SETTING_SOCKET_ACCEPT_ADDRESS)) {
            acceptorHost = settings.getString(sessionID, SETTING_SOCKET_ACCEPT_ADDRESS);
        }
        int acceptorPort = (int) settings.getLong(sessionID, SETTING_SOCKET_ACCEPT_PORT);

        return new InetSocketAddress(acceptorHost, acceptorPort);
    }

    /**
     * Check if that is a SessionTemplate, defined in the QFJ config file.
     *
     * @param settings
     * @param sessionID
     * @return
     * @throws ConfigError
     * @throws FieldConvertError
     */
    private boolean isSessionTemplate(SessionSettings settings, SessionID sessionID)
            throws ConfigError, FieldConvertError {
        return settings.isSetting(sessionID, SETTING_ACCEPTOR_TEMPLATE)
                && settings.getBool(sessionID, SETTING_ACCEPTOR_TEMPLATE);
    }

    /**
     * Start the acceptor.
     *
     * @throws RuntimeError
     * @throws ConfigError
     */
    public void start() throws RuntimeError, ConfigError {
        acceptor.start();
    }

    /**
     * Stop the acceptor.
     */
    public void stop() {
        try {
            jmxExporter.getMBeanServer().unregisterMBean(connectorObjectName);
        } catch (Exception e) {
            log.error("Failed to unregister acceptor from JMX", e);
        }
        acceptor.stop();
    }

}
