package io.github.piscescup.mc.fabric;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <h2>Description</h2>
 *
 * <h2>Usages</h2>
 *
 * @author REN YuanTong
 * @Date 2025-11-17
 * @since
 */
public final class References {
    public static final String MOD_ID = "pc-dev-lib";
    public static final String MOD_NAME = "PC Develop Lib";
    public static final String MOD_VERSION = "1.0.0";
    public static final String MC_VERSION = "1.21.10";

    public static final Logger MOD_LOGGER = LogManager.getLogger(MOD_NAME);

    public static final String MOD_FLAG = """
        \n
        ___________________   ________                     .__                  .____    ._____.   \s
        \\______   \\_   ___ \\  \\______ \\   _______  __ ____ |  |   ____ ______   |    |   |__\\_ |__ \s
         |     ___/    \\  \\/   |    |  \\_/ __ \\  \\/ // __ \\|  |  /  _ \\\\____ \\  |    |   |  || __ \\\s
         |    |   \\     \\____  |    `   \\  ___/\\   /\\  ___/|  |_(  <_> )  |_> > |    |___|  || \\_\\ \\
         |____|    \\______  / /_______  /\\___  >\\_/  \\___  >____/\\____/|   __/  |_______ \\__||___  /
                          \\/          \\/     \\/          \\/            |__|             \\/       \\/\s
        """;

    public static final String THANKS = "Thank for you using the lib: " + MOD_NAME;

    public static final String MOD_FINISH = "The lib has been initialized.";
}
