package meldexun.renderlib;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import meldexun.matrixutil.MathUtil;
import meldexun.renderlib.config.RenderLibConfig;
import meldexun.renderlib.opengl.debug.OpenGLDebugMode;
import meldexun.renderlib.util.GLUtil;
import meldexun.renderlib.util.timer.TimerEventHandler;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
// import net.minecraftforge.common.config.Config;
// import net.minecraftforge.common.config.ConfigManager;
//import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = RenderLib.MODID, acceptableRemoteVersions = "*", guiFactory = "meldexun.renderlib.config.RenderLibConfigGuiFactory")
public class RenderLib {

	public static final String MODID = "renderlib";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	public static File configFile;
	public static boolean isFairyLightsInstalled;
	public static boolean isValkyrienSkiesInstalled;
	public static boolean isVampirismInstalled;
	public static boolean isMenuLibInstalled;

	@EventHandler
	public void onFMLConstructionEvent(FMLConstructionEvent event) {
		MathUtil.setSinFunc(a -> MathHelper.sin((float) a));
		MathUtil.setCosFunc(a -> MathHelper.cos((float) a));
		GLUtil.init();
		MinecraftForge.EVENT_BUS.register(this);
	}

	@EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new TimerEventHandler());
    }

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		System.out.print("Preinit config load");
		configFile = new File(event.getModConfigurationDirectory(), MODID + ".cfg");

		try {
				RenderLibConfig.loadConfig(configFile);	//load config
			} catch (Exception e) {
				e.printStackTrace();
			}

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				RenderLibConfig.saveConfig();	//save config on shutdown
			} catch (Exception e) {
				e.printStackTrace();
			}
		}));
	}
}
