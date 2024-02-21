package de.teddybear2004.library;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.util.function.Consumer;

public class SessionFactoryHandler {

    private final File baseConfig;

    public SessionFactoryHandler(File baseConfig) {
        this.baseConfig = baseConfig;
    }

    public SessionFactory buildSessionFactory(Consumer<Configuration> configurationConsumer) {
        return ClassLoaderUtil.getFromStandardClassLoader(() -> {
            Configuration configuration = new Configuration().configure(this.baseConfig);
            configurationConsumer.accept(configuration);
            return configuration.buildSessionFactory();
        });
    }
    public SessionFactory buildSessionFactory() {
        return ClassLoaderUtil.getFromStandardClassLoader(() -> new Configuration().configure(this.baseConfig).buildSessionFactory());
    }

}