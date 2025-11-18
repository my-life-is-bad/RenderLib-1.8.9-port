package meldexun.renderlib.config;

import net.minecraft.client.gui.GuiScreen;
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
                getAllCategories(),
                RenderLib.MODID,
                false,
                false,
                "RenderLib"
        );
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        RenderLibConfig.config.save();
        RenderLibConfig.loadConfig(RenderLib.configFile);
    }

    private static List getAllCategories() {
        List categories = new ArrayList();
        Configuration config = RenderLibConfig.config;
        categories.addAll(new ConfigElement(config.getCategory("general")).getChildElements());
        categories.addAll(new ConfigElement(config.getCategory("opengldebugoutput")).getChildElements());
        return categories;
    }
}
