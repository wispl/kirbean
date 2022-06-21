package me.wisp.kirbean.framework.repository;

import me.wisp.kirbean.framework.SlashCommand;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CommandLoader {
    public CommandLoader() {};

    public List<? extends Class<?>> getClasses(String packageName) {
        String packagePath = packageName.replace('.', File.separatorChar);
        ClassLoader loader = ClassLoader.getSystemClassLoader();

        URL url = loader.getResource(packagePath);
        if (url == null) throw new RuntimeException("No resource found for " + packageName);
        String path = url.getPath();

        Path root = Path.of(path);
        if (path.startsWith("jar:")) {
            try {
                root = FileSystems.newFileSystem(root).getPath(packagePath);
            } catch (IOException e) {
                throw new RuntimeException("Could not create filesystem for jar");
            }
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
