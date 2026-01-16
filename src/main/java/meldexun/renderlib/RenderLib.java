package meldexun.renderlib;

import meldexun.renderlib.util.timer.TimerEventHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import meldexun.matrixutil.MathUtil;
import meldexun.renderlib.config.RenderLibConfig;
import meldexun.renderlib.opengl.debug.OpenGLDebugMode;
import meldexun.renderlib.util.GLUtil;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import my_life_is_bad.configurationsbackport.common.config.Config;
import my_life_is_bad.configurationsbackport.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = RenderLib.MODID, acceptableRemoteVersions = "*")
public class RenderLib {

    public static final String MODID = "renderlib";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    @EventHandler
    public void onFMLConstructionEvent(FMLConstructionEvent event) {
        MathUtil.setSinFunc(a -> MathHelper.sin((float) a));
        MathUtil.setCosFunc(a -> MathHelper.cos((float) a));

        GLUtil.init();

        RenderLibConfig.onConfigChanged();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new TimerEventHandler());
    }

    @SubscribeEvent
    public void onConfigChangedEvent(OnConfigChangedEvent event) {
        if (event.modID.equals(MODID)) {
            ConfigManager.sync(MODID, Config.Type.INSTANCE);
            RenderLibConfig.onConfigChanged();
            OpenGLDebugMode.setupDebugOutput(RenderLibConfig.openGLDebugOutput);
        }
    }

}