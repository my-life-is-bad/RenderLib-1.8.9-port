package meldexun.renderlib.config;

import java.util.Arrays;
import java.io.File;

import meldexun.renderlib.RenderLib;
import meldexun.renderlib.opengl.debug.GLDebugMessageFilter;
import meldexun.renderlib.opengl.debug.LogStackTraceMode;
import meldexun.renderlib.opengl.debug.Severity;
import meldexun.renderlib.opengl.debug.Source;
import meldexun.renderlib.opengl.debug.Type;
import meldexun.renderlib.util.ResourceLocationMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;


import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Property;

import net.minecraft.util.ResourceLocation;

public class RenderLibConfig {

    public static Configuration config;
	public static final ConfigCategory GENERAL = new ConfigCategory("general");
	public static final ConfigCategory OPENGL_DEBUG = new ConfigCategory("opengldebugoutput", GENERAL);

	/*=====GENERAL=====*/
	public static boolean debugRenderBoxes;
	public static int mainMenuFPS;
	public static boolean mainMenuFPSSynced;
	public static boolean openGLLogExtensions;
	public static boolean showFrameTimes;
	public static String[] entityBoundingBoxGrowthList;
	public static String[] tileEntityBoundingBoxGrowthList;
	public static String[] tileEntityCachedBoundingBoxBlacklist;
	public static boolean tileEntityCachedBoundingBoxEnabled;
	public static int tileEntityCachedBoundingBoxUpdateInterval;
	public static ResourceLocationMap<TileEntity, Vec3> tileEntityBoundingBoxGrowthListImpl = new ResourceLocationMap<>(RenderLibConfig::getTileEntityStringHelper, null, s -> {
		double x = s.length >= 1 ? Double.parseDouble(s[0]) : 0.0D;
		double y = s.length >= 2 ? Double.parseDouble(s[1]) : 0.0D;
		double z = s.length >= 3 ? Double.parseDouble(s[2]) : 0.0D;
		return x != 0.0D || y != 0.0D || z != 0.0D ? new Vec3(x, y, z) : null;
	});
	public static ResourceLocationMap<TileEntity, Boolean> tileEntityCachedBoundingBoxBlacklistImpl = new ResourceLocationMap<>(RenderLibConfig::getTileEntityStringHelper, false, s -> true);
	public static ResourceLocationMap<Entity, Vec3> entityBoundingBoxGrowthListImpl = new ResourceLocationMap<>(RenderLibConfig::getEntityStringHelper, null, s -> {
		double x = s.length >= 1 ? Double.parseDouble(s[0]) : 0.0D;
		double y = s.length >= 2 ? Double.parseDouble(s[1]) : 0.0D;
		double z = s.length >= 3 ? Double.parseDouble(s[2]) : 0.0D;
		return x != 0.0D || y != 0.0D || z != 0.0D ? new Vec3(x, y, z) : null;
	});

	private static Property mainMenuFPSProp;
	private static Property mainMenuFPSSyncedProp;
	private static Property showFrameTimesProp;
	private static Property debugRenderBoxesProp;
	private static Property openGLLogExtensionsProp;
	private static Property entityBoundingBoxGrowthListProp;
	private static Property tileEntityBoundingBoxGrowthListProp;
	private static Property tileEntityCachedBoundingBoxBlacklistProp;
	private static Property tileEntityCachedBoundingBoxEnabledProp;
	private static Property tileEntityCachedBoundingBoxUpdateIntervalProp;

	/*=====OPENGL DEBUG=====*/
	public static class OpenGLDebugConfiguration {
		public boolean crashOnError;
		public boolean enabled;
		public LogStackTraceMode logStackTrace;
		public String[] messageFilters = { new GLDebugMessageFilter(Source.ANY, Type.ERROR, Severity.ANY, true).toString() };
		public boolean setContextDebugBit;
		public GLDebugMessageFilter[] getMessageFilters() {
			return Arrays.stream(messageFilters).map(GLDebugMessageFilter::new).toArray(GLDebugMessageFilter[]::new);
		}
	}
	public static final String[] options = Arrays.stream(LogStackTraceMode.values()).map(Enum::name).toArray(String[]::new);
	public static OpenGLDebugConfiguration openGLDebugOutput = new OpenGLDebugConfiguration();
	private static Property openGLDebugCrashOnErrorProp;
	private static Property openGLDebugEnabledProp;
	private static Property openGLDebugLogStackTraceProp;
	private static Property openGLDebugMessageFiltersProp;
	private static Property openGLDebugSetContextDebugBitProp;


	public static void loadConfig(File file) {
		config = new Configuration(file);
		config.load();

		/*=====GENERAL=====*/

		/*-Properties-*/
		mainMenuFPSProp = config.get(GENERAL.getQualifiedName(), "mainMenuFPS", 60, "The Fps Cap in main menu.");
		mainMenuFPSSyncedProp = config.get(GENERAL.getQualifiedName(), "mainMenuFPSSynced", false, "If set to true the main menu FPS is synced to the in game FPS (but clamped between 30 and 240).");
		showFrameTimesProp = config.get(GENERAL.getQualifiedName(), "showFrameTimes", false, "If set to true. Frame times will be shown on screen.");
		debugRenderBoxesProp = config.get(GENERAL.getQualifiedName(), "debugRenderBoxes", false, "If set to true Debug boxes will render around entities.");
		openGLLogExtensionsProp = config.get(GENERAL.getQualifiedName(), "openGLLogExtensions", false, "Log OpenGL Extenstions.");
		entityBoundingBoxGrowthListProp = config.get(GENERAL.getQualifiedName(), "entityBoundingBoxGrowthList", new String[0],"Allows you to increase the render bounding boxes of entities (or all entities of a mod). Width increases the size on the X and Z axis. Top increases the size in the positive Y direction. Bottom increases the size in the negative Y direction. (Accepts entity=width,top,bottom').");
		tileEntityBoundingBoxGrowthListProp = config.get(GENERAL.getQualifiedName(), "tileEntityBoundingBoxGrowthList", new String[0],"Allows you to increase the render bounding boxes of tile entities (or all entities of a mod). Width increases the size on the X and Z axis. Top increases the size in the positive Y direction. Bottom increases the size in the negative Y direction. (Accepts tileentity=width,top,bottom').");
		tileEntityCachedBoundingBoxBlacklistProp = config.get(GENERAL.getQualifiedName(), "tileEntityCachedBoundingBoxBlacklist", new String[0],"Tile entities whose bounding boxes won't be cached (Accepts {tile_entity_name} e.g: Chest, EnderChest).");
		tileEntityCachedBoundingBoxEnabledProp = config.get(GENERAL.getQualifiedName(), "tileEntityCachedBoundingBoxEnabled", true,"Most tile entities have static bounding boxes and thus they can be cached. Tile entities whose bounding boxes are likely to change every frame or so should be added to the blacklist. Tile entities whose bounding only change every once in a while should be covered by cache updates (update speed adjustable through tileEntityCachedBoundingBoxUpdateInterval)");
		tileEntityCachedBoundingBoxUpdateIntervalProp = config.get(GENERAL.getQualifiedName(), "tileEntityCachedBoundingBoxUpdateInterval", 100,"Every frame there is a 1 in x chance to update the cached bounding box. Higher = better performance, Lower = tile entities with dynamic bounding boxes get updated faster." + "\n" + "Min: 1" + "\n" + "Max: 1000000");
		tileEntityCachedBoundingBoxUpdateIntervalProp.setMinValue(1);
		tileEntityCachedBoundingBoxUpdateIntervalProp.setMaxValue(1_000_000);

		/*-Variable assigning-*/
		mainMenuFPS = mainMenuFPSProp.getInt();
		mainMenuFPSSynced = mainMenuFPSSyncedProp.getBoolean();
		showFrameTimes = showFrameTimesProp.getBoolean();
		debugRenderBoxes = debugRenderBoxesProp.getBoolean();
		openGLLogExtensions = openGLLogExtensionsProp.getBoolean();
		entityBoundingBoxGrowthList = entityBoundingBoxGrowthListProp.getStringList();
		tileEntityBoundingBoxGrowthList = tileEntityBoundingBoxGrowthListProp.getStringList();
		tileEntityCachedBoundingBoxBlacklist = tileEntityCachedBoundingBoxBlacklistProp.getStringList();
		tileEntityCachedBoundingBoxEnabled = tileEntityCachedBoundingBoxEnabledProp.getBoolean();
		tileEntityCachedBoundingBoxUpdateInterval = tileEntityCachedBoundingBoxUpdateIntervalProp.getInt();

		/*=====OPENGL DEBUG=====*/

		/*-Properties-*/
		openGLDebugCrashOnErrorProp = config.get(OPENGL_DEBUG.getQualifiedName(), "crashOnError", false,"Enable/Disable crashing when an OpenGL error occurs. If disabled OpenGL errors are likely to go unnoticed unless the log is checked manually.");
		openGLDebugEnabledProp = config.get(OPENGL_DEBUG.getQualifiedName(), "enabled", false,"Better debugging of OpenGL errors. Might not be supported by your hardware/driver.");
		openGLDebugLogStackTraceProp = config.get(OPENGL_DEBUG.getQualifiedName(), "logStackTrace", "ERRORS_ONLY","Enable/Disable appending of the stack trace when logging a debug message.");
		openGLDebugMessageFiltersProp = config.get(OPENGL_DEBUG.getQualifiedName(), "messageFilters",
				openGLDebugOutput.messageFilters,
				"Enable/Disable debug messages matching the specified filters." +
				"\nFormat: 'source, type, severity, enabled'" +
				"\nValid source values: [ ANY, API, WINDOW_SYSTEM, SHADER_COMPILER, THIRD_PARTY, APPLICATION, OTHER ]" +
				"\nValid type values: [ ANY, ERROR, DEPRECATED_BEHAVIOR, UNDEFINED_BEHAVIOR, PORTABILITY, PERFORMANCE, MARKER, PUSH_GROUP, POP_GROUP, OTHER ]" +
				"\nValid severity values: [ ANY, HIGH, MEDIUM, LOW, NOTIFICATION ]");
		openGLDebugSetContextDebugBitProp = config.get(OPENGL_DEBUG.getQualifiedName(), "setContextDebugBit", false,"May be required by some systems to generate OpenGL debug output. (Enabling might have a negative impact on performance)");
		openGLDebugLogStackTraceProp.setValidValues(options);
		openGLDebugSetContextDebugBitProp.setRequiresMcRestart(true);
		
		/*-Variable assigning-*/
		openGLDebugOutput.crashOnError = openGLDebugCrashOnErrorProp.getBoolean();
		openGLDebugOutput.enabled = openGLDebugEnabledProp.getBoolean();
		openGLDebugOutput.logStackTrace = LogStackTraceMode.valueOf(openGLDebugLogStackTraceProp.getString());
		openGLDebugOutput.messageFilters = openGLDebugMessageFiltersProp.getStringList();
		openGLDebugOutput.setContextDebugBit = openGLDebugSetContextDebugBitProp.getBoolean();

		tileEntityCachedBoundingBoxBlacklistImpl.load(tileEntityCachedBoundingBoxBlacklist);
		entityBoundingBoxGrowthListImpl.load(entityBoundingBoxGrowthList);
		tileEntityBoundingBoxGrowthListImpl.load(tileEntityBoundingBoxGrowthList);
	}


	public static void saveConfig() {
		/*=====GENERAL=====*/
		mainMenuFPSProp.set(mainMenuFPS);
		mainMenuFPSSyncedProp.set(mainMenuFPSSynced);
		showFrameTimesProp.set(showFrameTimes);
		debugRenderBoxesProp.set(debugRenderBoxes);
		openGLLogExtensionsProp.set(openGLLogExtensions);
		entityBoundingBoxGrowthListProp.set(entityBoundingBoxGrowthList);
		tileEntityBoundingBoxGrowthListProp.set(tileEntityBoundingBoxGrowthList);
		tileEntityCachedBoundingBoxBlacklistProp.set(tileEntityCachedBoundingBoxBlacklist);
		tileEntityCachedBoundingBoxEnabledProp.set(tileEntityCachedBoundingBoxEnabled);
		tileEntityCachedBoundingBoxUpdateIntervalProp.set(tileEntityCachedBoundingBoxUpdateInterval);

		/*=====OPENGL DEBUG=====*/
		openGLDebugCrashOnErrorProp.set(openGLDebugOutput.crashOnError);
		openGLDebugEnabledProp.set(openGLDebugOutput.enabled);
		openGLDebugLogStackTraceProp.set(openGLDebugOutput.logStackTrace.name());
		openGLDebugMessageFiltersProp.set(openGLDebugOutput.messageFilters);
		openGLDebugSetContextDebugBitProp.set(openGLDebugOutput.setContextDebugBit);
		
		if (config.hasChanged()) {
            config.save();
        }
    }

	@SuppressWarnings("unchecked")
	public static String getEntityStringHelper(Class<? extends Entity> cls) {
		return (String) EntityList.classToStringMapping.get(cls);
	}

	@SuppressWarnings("unchecked")
	public static String getTileEntityStringHelper(Class<? extends TileEntity> cls) {
		return (String) TileEntity.classToNameMap.get(cls);
	}

}