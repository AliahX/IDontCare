package com.unjust1ce.idontcare.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "idontcare")
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Gui.Tooltip
    public boolean displayRedBackground = false;

    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Gui.Tooltip
    public boolean displayWarningText = false;

    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Gui.Tooltip
    public boolean displayWarningDescription = false;

    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Gui.Tooltip
    public boolean incompatibleConfirmation = false;
}
