package de.teddybear2004.library;

import org.bukkit.plugin.java.PluginClassLoader;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class ClassLoaderUtil {
    private static final ClassLoader NORMAL_CLASS_LOADER = ClassLoaderUtil.class.getClassLoader();
    private static final ClassLoader PLUGIN_CLASS_LOADER = PluginClassLoader.class.getClassLoader();
    public static void runInStandardClassLoader(Runnable runnable) {
        Thread.currentThread().setContextClassLoader(NORMAL_CLASS_LOADER);
        try{
            runnable.run();
        }catch(Exception e){
            Thread.currentThread().setContextClassLoader(PLUGIN_CLASS_LOADER);
            throw e;
        }
        Thread.currentThread().setContextClassLoader(PLUGIN_CLASS_LOADER);
    }

    public static <T> T getFromStandardClassLoader(Supplier<T> tSupplier){
        Thread.currentThread().setContextClassLoader(NORMAL_CLASS_LOADER);
        T t;
        try{
            t = tSupplier.get();
        }catch(Exception e){
            Thread.currentThread().setContextClassLoader(PLUGIN_CLASS_LOADER);
            throw e;
        }
        Thread.currentThread().setContextClassLoader(PLUGIN_CLASS_LOADER);

        return t;
    }

}
