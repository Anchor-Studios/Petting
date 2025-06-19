package com.anchorstudios.petting;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class Config {
    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class Common {
        public final ForgeConfigSpec.BooleanValue allowFriendlyPetCombat;
        public final ForgeConfigSpec.BooleanValue petsAttackPlayers;
        public final ForgeConfigSpec.BooleanValue areaDamageHitsOtherPets;
        public final ForgeConfigSpec.BooleanValue areaDamageHitsPlayers;
        public final ForgeConfigSpec.BooleanValue allowUntaming;
        public final ForgeConfigSpec.IntValue petLimitPerPlayer;
        public final ForgeConfigSpec.BooleanValue allowRetaming;
        public final ForgeConfigSpec.BooleanValue showPetOwnershipName;
        public final ForgeConfigSpec.IntValue cleanupIntervalTicks;

        Common(ForgeConfigSpec.Builder builder) {
            builder.push("Petting Settings");

            allowFriendlyPetCombat = builder
                    .comment("Should tamed pets under the same owner be able to attack each other?")
                    .define("allowFriendlyPetCombat", false);

            petsAttackPlayers = builder
                    .comment("Can tamed pets attack players?")
                    .define("petsAttackPlayers", false);

            areaDamageHitsOtherPets = builder
                    .comment("Can tamed pets damage other tamed pets with area damage?")
                    .define("areaDamageHitsOtherPets", false);

            areaDamageHitsPlayers = builder
                    .comment("Can tamed pets damage players with area damage?")
                    .define("areaDamageHitsPlayers", false);

            allowUntaming = builder
                    .comment("Allow players to untame their pets?")
                    .define("allowUntaming", false);

            petLimitPerPlayer = builder // WORKING
                    .comment("Maximum number of pets one player can have. Set to -1 for unlimited.")
                    .defineInRange("petLimitPerPlayer", -1, -1, Integer.MAX_VALUE);

            allowRetaming = builder // WORKING
                    .comment("Allow players to re-tame pets that already belong to someone?")
                    .define("allowRetaming", false);

            showPetOwnershipName = builder // WORKING
                    .comment("Show the owner's name in the pet's custom name?")
                    .define("showPetOwnershipName", true);

            cleanupIntervalTicks = builder // WORKING
                    .comment("Interval in ticks between pet registry cleanups (20 ticks = 1 second, default 6000 = 5 minutes), setting this too low (especially below 100) may cause server lag.")
                    .defineInRange("cleanupIntervalTicks", 6000, 1, Integer.MAX_VALUE);

            builder.pop();
        }
    }
}
