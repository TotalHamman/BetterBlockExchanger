package totalhamman.betterblockexchanger.utils;

import totalhamman.betterblockexchanger.BetterBlockExchanger;

import static totalhamman.betterblockexchanger.BetterBlockExchanger.debugOn;

public class LogHelper {

    public static void logHelper(String msg) {
        if (debugOn) BetterBlockExchanger.log.info(msg);
    }

}
