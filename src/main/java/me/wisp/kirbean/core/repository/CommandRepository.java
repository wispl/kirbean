package me.wisp.kirbean.core.repository;

import me.wisp.kirbean.core.SlashCommand;
import me.wisp.kirbean.core.annotations.Command;
import me.wisp.kirbean.core.reflect.CommandInfo;
import me.wisp.kirbean.core.tree.CommandTree;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandRepository {

    private final Logger logger = LoggerFactory.getLogger(CommandRepository.class);
    private final CommandTree tree = new CommandTree();
    private final Map<String, SlashCommand> commandMap = new HashMap<>();

    public CommandRepository() { }

    public void index() {
        CommandLoader loader = new CommandLoader();

        long start = System.currentTimeMillis();
        List<? extends Class<?>> slashCommands = loader.getClasses("me.wisp.kirbean.commands");
        long end = System.currentTimeMillis() - start;
        logger.info("Indexed " + slashCommands.size() + " commands in " + end + " milliseconds!");

        for (Class<?> c : slashCommands) {
            Method method = extractMethod(c);
            Command command = method.getAnnotation(Command.class);
            if (command == null) {
                throw new RuntimeException("Class "+ c.getName() + " does not have @Command annotation on execute()");
            }

            SlashCommand instance = createInstance(c);
            commandMap.put(command.name(), instance);

            tree.addCommand(getParentPackage(c.getPackageName()), CommandInfo.build(method));
        }
    }

    private Method extractMethod(Class<?> clazz) {
        try {
            return clazz.getDeclaredMethod("execute", SlashCommandInteractionEvent.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Class " + clazz.getName() + " does not have execute() method");
        }
    }

    private SlashCommand createInstance(Class<?> c) {
        try {
            return (SlashCommand) c.getConstructors()[0].newInstance();
        } catch (InstantiationException | InvocationTargetException e) {
            throw new RuntimeException("Error when constructing class " + c.getName(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Could no access constructor of " + c.getName(), e);
        }
    }

    private String getParentPackage(String packageName) {
        String parent = packageName.substring(packageName.lastIndexOf(".") + 1);
        return parent.equals("command") ? "other" : parent;
    }

    public Map<String, SlashCommand> asMap() {
        return commandMap;
    }

    public CommandTree asTree() {
        return tree;
    }
}
