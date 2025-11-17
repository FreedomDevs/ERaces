package dev.elysium.eraces.bootstrap;

import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.commands.AbilsCommand;
import dev.elysium.eraces.commands.MyraceCommand;
import dev.elysium.eraces.commands.RacesCommand;
import dev.elysium.eraces.exceptions.internal.InitFailedException;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;

public class CommandInitializer implements IInitializer {
    @Override
    public void setup(ERaces plugin) {
        try {
            plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
                commands.registrar().register(new MyraceCommand().getCmd());
                commands.registrar().register(new RacesCommand().getCmd());
                commands.registrar().register(new AbilsCommand().getCmd());
            });
        } catch (Exception e) {
            throw new InitFailedException("Ошибка при регистрации команд", e);
        }
    }
}
