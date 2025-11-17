//package com.github.ysbbbbbb.kaleidoscopecookery.compat.carryon;
//
//import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
//
//public class CarryOnBlackList {
//    public static final String CARRY_ON_ID = "carryon";
//
//    public static void addBlackList() {
//        ForgeRegistries.BLOCKS.getKeys().stream().filter(id -> id.getNamespace().equals(KaleidoscopeCookery.MOD_ID))
//                .forEach(id -> InterModComms.sendTo(CARRY_ON_ID, "blacklistBlock", id::toString));
//    }
//}
