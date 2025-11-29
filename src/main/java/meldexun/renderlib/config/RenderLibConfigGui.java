package meldexun.renderlib.config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import meldexun.renderlib.RenderLib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RenderLibConfigGui extends GuiConfig {
    public RenderLibConfigGui(GuiScreen parent) {
        super(
                parent,
                new ConfigElement(RenderLibConfig.config.getCategory(RenderLibConfig.GENERAL.getName())).getChildElements(),
                RenderLib.MODID,
                false,
                false,
                "RenderLib"
        );
    }

    @Override
    protected void actionPerformed(GuiButton button){
        super.actionPerformed(button);
        
        if (button.id == 2000) {
            RenderLibConfig.config.save();
            RenderLibConfig.loadConfig(RenderLib.configFile);
        }
    }
}
