package me.wisp.kirbean.framework.repository;

import me.wisp.kirbean.framework.SlashCommand;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

// scans packageName for SlashCommand, may replace with either ClassGraph, Spring, or ClassIndex later
public class CommandLoader {
    public CommandLoader() {};

    public List<? extends Class<?>> getClasses(String packageName) {
        String packagePath = packageName.replace('.', File.separatorChar);
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        URI uri = getResource(loader, packagePath);

        Path root;
        if (uri.toString().startsWith("jar:")) {
            try {
                root = FileSystems.newFileSystem(uri, Collections.emptyMap()).getPath(packagePath);
            } catch (IOException e) {
                throw new RuntimeException("Could not create file system for jar", e);
            }
        } else {
            root = Path.of(uri);
        }

        List<Class<?>> classes = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(root)) {
            paths.filter(Files::isRegularFile)
                    .filter(file -> file.toString().endsWith(".class"))
                    .forEach(file -> {
                        Class<?> command = loadClass(loader, getClassPath(file.toString(), packagePath));
                        if (command != null) {
                            classes.add(command);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException("Error walking files");
        }
        return classes;
    }

    private URI getResource(ClassLoader loader, String packageName) {
        try {
            return loader.getResource(packageName).toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(packageName + "cannot be converted to URI", e);
        }
    }

    private String getClassPath(String file, String packagePath) {
        return file.substring(file.indexOf(packagePath), file.length() - 6).replace(File.separatorChar, '.');
    }

    private Class<?> loadClass(ClassLoader loader, String className) {
        try {
            Class<?> clazz = loader.loadClass(className);
            if (SlashCommand.class.isAssignableFrom(clazz)) {
                return clazz;
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not load class from class " + className);
        }
        return null;
    }
}