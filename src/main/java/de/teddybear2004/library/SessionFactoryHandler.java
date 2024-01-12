package de.teddybear2004.library;

import org.bukkit.plugin.java.PluginClassLoader;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class HibernateUtil {

    private final File baseConfig;

    public HibernateUtil(File baseConfig) {
        this.baseConfig = baseConfig;
    }

    public SessionFactory buildSessionFactory(File mapping) {
        Thread.currentThread().setContextClassLoader(HibernateUtil.class.getClassLoader());

        SessionFactory sessionFactory = null;
        try{
            sessionFactory = new Configuration().configure(this.baseConfig).configure(mapping).buildSessionFactory();
            System.out.println("[ChatUtilities] SessionFactory for " + mapping.getName() + " initialized!");
        }catch(Throwable ex){
            System.out.println("Initial SessionFactory creation failed for " + mapping.getName() + ". " + ex);
        }

        Thread.currentThread().setContextClassLoader(PluginClassLoader.class.getClassLoader());


        return sessionFactory;


    }

}