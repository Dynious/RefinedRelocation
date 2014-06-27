package com.dynious.refinedrelocation.lib;

public class Strings
{
    private static final String GUI_PREFIX = "gui." + Reference.MOD_ID.toLowerCase() + ".";
    private static final String ITEM_DESC_PREFIX = "itemDesc." + Reference.MOD_ID.toLowerCase() + ".";
    private static final String FILTER_NAME_PREFIX = "filterName." + Reference.MOD_ID.toLowerCase() + ".";
    private static final String RELOCATOR_MODULE_PREFIX = "relocatorModule." + Reference.MOD_ID.toLowerCase() + ".";
    private static final String RELOCATOR_STUFFED_PREFIX = "relocatorStuffed." + Reference.MOD_ID.toLowerCase() + ".";
    private static final String DIRECTION_PREFIX = "direction." + Reference.MOD_ID.toLowerCase() + ".";

    public static final String BLACKLIST = GUI_PREFIX + "blacklist";
    public static final String WHITELIST = GUI_PREFIX + "whitelist";
    public static final String CLICK = GUI_PREFIX + "click";
    public static final String SHIFT_CLICK = GUI_PREFIX + "shiftClick";
    public static final String FILTERED_EXTRACT = GUI_PREFIX + "filteredExtr";
    public static final String UNFILTERED_EXTRACT = GUI_PREFIX + "unfilteredExtr";
    public static final String MAX_STACK_SIZE = GUI_PREFIX + "maxStackSize";
    public static final String MODE = GUI_PREFIX + "mode";
    public static final String ROUND_ROBIN = GUI_PREFIX + "roundRobin";
    public static final String ROUND_ROBIN_INFO = GUI_PREFIX + "roundRobinInfo";
    public static final String GREEDY = GUI_PREFIX + "greedy";
    public static final String GREEDY_INFO = GUI_PREFIX + "greedyInfo";
    public static final String SPREAD = GUI_PREFIX + "spread";
    public static final String STACK = GUI_PREFIX + "stack";
    public static final String DISGUISED = GUI_PREFIX + "disguised";
    public static final String UNDISGUISED = GUI_PREFIX + "undisguised";
    public static final String DISGUISED_INFO = GUI_PREFIX + "disguisedInfo";
    public static final String UNDISGUISED_INFO = GUI_PREFIX + "undisguisedInfo";
    public static final String CONNECTED = GUI_PREFIX + "connected";
    public static final String NOT_CONNECTED = GUI_PREFIX + "notConnected";
    public static final String BUFFER_INSERTION_INFO = GUI_PREFIX + "bufferInsInfo";
    public static final String BLOCK_EXTENDER_INSERTION_INFO = GUI_PREFIX + "blockExtenderInsInfo";
    public static final String REDSTONE_TRANSMISSION = GUI_PREFIX + "redstoneTrans";
    public static final String ENABLED = GUI_PREFIX + "enabled";
    public static final String DISABLED = GUI_PREFIX + "disabled";
    public static final String ACTIVE = GUI_PREFIX + "active";
    public static final String INACTIVE = GUI_PREFIX + "inactive";
    public static final String CUSTOM_FILTER = GUI_PREFIX + "customFilter";
    public static final String NAME_MATCHING = GUI_PREFIX + "nameMatching";
    public static final String WILDCARD_CHARACTER = GUI_PREFIX + "wildcardChar";
    public static final String OREDICT_CHARACTER = GUI_PREFIX + "oredictChar";
    public static final String COMMA_SEPARATION = GUI_PREFIX + "commaSeparation";
    public static final String WIRELESS_LINK = GUI_PREFIX + "wirelessLink";
    public static final String LINKED_TO_AT = GUI_PREFIX + "linkedToAt";
    public static final String CONNECTIONS = GUI_PREFIX + "connections";
    public static final String ORE_DICT_CON = GUI_PREFIX + "oreDictConversion";
    public static final String MAX_ENERGY = GUI_PREFIX + "maxEnergy";
    public static final String RS_ON = GUI_PREFIX + "RSOn";
    public static final String RS_PULSE = GUI_PREFIX + "RSPulse";
    public static final String PRIORITY = GUI_PREFIX + "priority";
    public static final String OUTDATED = GUI_PREFIX + "outdated";
    public static final String CHANGE_LOG = GUI_PREFIX + "changeLog";
    public static final String LATEST = GUI_PREFIX + "latest";
    public static final String TAB = GUI_PREFIX + "tab";
    public static final String TICKS_BETWEEN_EXT = GUI_PREFIX + "ticksBetween";
    public static final String SNEAKY = GUI_PREFIX + "sneaky";
    public static final String NONE = GUI_PREFIX + "none";
    public static final String PLAYER_RELOCATOR_LINK = GUI_PREFIX + "playerRelocatorLink";
    public static final String TOO_FAR = GUI_PREFIX + "tooFar";
    public static final String LINKED_WITH = GUI_PREFIX + "linkedWith";
    public static final String CONNECTED_TO = GUI_PREFIX + "connectedTo";
    public static final String LINKED_TO = GUI_PREFIX + "linkedTo";
    public static final String FACING = GUI_PREFIX + "facing";
    public static final String REDSTONE = GUI_PREFIX + "redstone";

    public static final String LINKED_POS = ITEM_DESC_PREFIX + "linkedPos";
    public static final String UNLINKED = ITEM_DESC_PREFIX + "unlinked";
    public static final String DISGUISED_AS = ITEM_DESC_PREFIX + "disguisedAs";
    public static final String CANNOT_DISGUISE_AS = ITEM_DESC_PREFIX + "cannotDisguiseAs";
    public static final String LINKER_SET = ITEM_DESC_PREFIX + "linkerSet";
    public static final String NO_LONGER_LINKED = ITEM_DESC_PREFIX + "noLongerLinked";
    public static final String INTER_DIMENSIONAL = ITEM_DESC_PREFIX + "interDimensional";
    public static final String BROKEN_LINK = ITEM_DESC_PREFIX + "brokenLink";
    public static final String COOLDOWN = ITEM_DESC_PREFIX + "cooldown";
    public static final String REL_CONT_DESC = ITEM_DESC_PREFIX + "relContDesc";
    public static final String RELOCATOR_MODULE = ITEM_DESC_PREFIX + "relocatorModule";
    public static final String RELOCATOR_MODULE_INFO = ITEM_DESC_PREFIX + "relocatorModuleInfo";
    public static final String TOOLBOX_INFO = ITEM_DESC_PREFIX + "toolBoxInfo";
    public static final String TOOLBOX_WRENCH_LIST_START = ITEM_DESC_PREFIX + "toolBoxWrenchListStart";

    public static final String INGOT_FILTER = FILTER_NAME_PREFIX + "filterIngot";
    public static final String ORE_FILTER = FILTER_NAME_PREFIX + "filterOres";
    public static final String LOG_FILTER = FILTER_NAME_PREFIX + "filterLogs";
    public static final String PLANK_FILTER = FILTER_NAME_PREFIX + "filterPlanks";
    public static final String DUST_FILTER = FILTER_NAME_PREFIX + "filterDusts";
    public static final String CRUSHED_ORE_FILTER = FILTER_NAME_PREFIX + "filterCrushedOres";
    public static final String PURIFIED_ORE_FILTER = FILTER_NAME_PREFIX + "filterPurifiedOres";
    public static final String PLATE_FILTER = FILTER_NAME_PREFIX + "filterPlates";
    public static final String GEM_FILTER = FILTER_NAME_PREFIX + "filterGems";
    public static final String FOOD_FILTER = FILTER_NAME_PREFIX + "filterFood";
    public static final String DYE_FILTER = FILTER_NAME_PREFIX + "filterDyes";
    public static final String NUGGET_FILTER = FILTER_NAME_PREFIX + "filterNuggets";

    public static final String REDSTONE_BLOCK_ENABLED = RELOCATOR_MODULE_PREFIX + "redstoneBlockEnabled";
    public static final String REDSTONE_BLOCK_DISABLED = RELOCATOR_MODULE_PREFIX + "redstoneBlockDisabled";

    public static final String RELOCATOR_STUFFED = RELOCATOR_STUFFED_PREFIX + "stuffed";

    public static final String DIRECTION = DIRECTION_PREFIX + "direction";
}
