package com.unjust1ce.idontcare;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.unjust1ce.idontcare.config.ModConfig;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class IDontCare implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);

        String prefix = "he doesn't";
        switch (new Random().nextInt(3)) {
            case 1:
                prefix = "she doesn't";
                break;
            case 2:
                prefix = "they don't";
                break;
        }
        LOGGER.info(prefix + " care");
    }
}
