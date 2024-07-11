package de.teddybear2004.library;

import jakarta.persistence.Entity;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class DatabaseConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConnector.class);
    private final Configuration configuration1;
    private final String url;
    private final String user;
    private final String password;

    public DatabaseConnector(String host, String port, String database, String user, String password, String... packages) {
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        this.user = user;
        this.password = password;

        Configuration configuration1 = new Configuration();
        configuration1.configure();

        configuration1.setProperty("hibernate.connection.url", url);
        configuration1.setProperty("hibernate.connection.username", user);
        configuration1.setProperty("hibernate.connection.password", password);

        for (String aPackage : packages) {
            Package definedPackage = Thread.currentThread().getContextClassLoader().getDefinedPackage(aPackage);
            if (definedPackage != null) {
                Reflections reflections = new Reflections(new ConfigurationBuilder()
                                                                  .setUrls(ClasspathHelper.forPackage(aPackage))
                                                                  .setScanners(Scanners.TypesAnnotated, Scanners.SubTypes));

                Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Entity.class);
                for (Class<?> aClass : typesAnnotatedWith) {
                    configuration1.addAnnotatedClass(aClass);
                }
            }
        }

        this.configuration1 = configuration1;
    }

    public boolean establishConnection(int maxAttempts) {
        int attempts = 0;
        while (attempts < maxAttempts) {
            try{
                SessionFactory sessionFactory = createConnection();
                if (sessionFactory != null) {
                    sessionFactory.close();
                    LOGGER.info("Established connection to database");
                    return true;
                }
            }catch(Exception e){
                LOGGER.error("Failed to establish connection to database", e);
                try{
                    Thread.sleep(500 + (long) (Math.random() * 500));
                }catch(InterruptedException ie){
                    Thread.currentThread().interrupt();
                    LOGGER.error("Connection attempt interrupted", ie);
                    return false;
                }
            }
            attempts++;
        }
        return false;
    }

    public SessionFactory createConnection() {
        return configuration1.buildSessionFactory();
    }


    public void migrate() {
        Flyway flyway = Flyway
                .configure()
                .validateMigrationNaming(true)
                .locations("classpath:db/migration")
                .createSchemas(true)
                .defaultSchema("public")
                .dataSource(url, user, password)
                .load();

        try{
            flyway.migrate();
        }catch(FlywayException e){
            LOGGER.error("Failed to run migrations", e);
        }
    }

}