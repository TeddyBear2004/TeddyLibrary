package de.teddybear2004.library;

import org.bukkit.plugin.java.JavaPlugin;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.util.function.Consumer;

public final class TeddyLibrary extends JavaPlugin {

    private SessionFactoryHandler sessionFactoryHandler;

    @Override
    public void onEnable() {
        File file = new File(getDataFolder(), "hibernate.cfg.xml");

        if (!file.exists())
            saveResource("hibernate.cfg.xml", false);

        this.sessionFactoryHandler = new SessionFactoryHandler(file);
    }

    public SessionFactory getSessionFactory(Consumer<Configuration> configurationConsumer) {
        return this.sessionFactoryHandler.buildSessionFactory(configurationConsumer);
    }
    public SessionFactory getSessionFactory() {
        return this.sessionFactoryHandler.buildSessionFactory();
    }

}
