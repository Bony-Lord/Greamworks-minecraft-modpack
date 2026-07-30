# Changelog

Only stable progression baselines are listed here. Intermediate crash-fix and test builds are intentionally omitted.

## v232.106 - 2026-07-30

- Reduced the custom vanilla-spawner activation range from 48 to 20 blocks.
- Added 23 Create mechanical-mixer recipes mirroring the registered fluid-stone world interactions.
- Added Chemica and Silent Gear ore crushing routes with chance-based byproducts.
- Added additional Create milling recipes for modded stones, ores, and industrial byproducts.
- Added 11 Apothic Enchanting infusion recipes using Eterna, Quanta, and Arcana requirements.
- Added a hidden-items creative tab, Omni-Alloy and the guide book to the Greamworks tab, and Void Shard to Cataclysm.
- Added low-tier supplies, Stick n Stone equipment, Andesite Alloy, and Silent Gear templates to global chest loot.
- Reduced Stick n Stone wooden weapon durability to 20.
- Added `MODRINTH_UPLOAD_v232.106.json` metadata for manual Modrinth uploads.
- Did not add Create: Easy Stone Generators or another third-party stone generator.

## v232.105 - 2026-07-30

- Fixed all 15 world fluid reactions after v232.104 failed to trigger them.
- Kept NeoForge `FluidInteractionRegistry` as the primary path and added a narrow server-side neighbor-update fallback for modded fluid blocks.
- Changed adjacent-fluid matching from registry-name strings to resolved `FluidType` instances.
- Added the required NeoForge `@EmiEntrypoint`, allowing the recipes to appear under EMI World Interaction.
- Added startup diagnostics reporting registered and skipped fluid reactions.
- Did not add Create: Easy Stone Generators or any other third-party generator mod.

## v232.104 - 2026-07-30

- Updated the tracked baseline from v232.60 to v232.104 on Minecraft 1.21.1 and NeoForge 21.1.244.
- Attempted to replace the KubeJS fluid-world scanner with 15 native NeoForge `FluidInteractionRegistry` reactions; runtime testing later showed that they did not trigger.
- Added cross-mod stone formation using Chemica and TFMG fluids, including all raw Create stone families and calcite.
- Added an EMI plugin, but omitted NeoForge's required `@EmiEntrypoint`, so it was not discovered.
- Removed stale KubeJS information entries for the old oxygen and neighboring-stone behavior.
- Repaired 106 Field Manual recipe references and removed invalid trailing periods from item IDs across 60 icon pages.
- Retained greamworks-fixes 1.0.40, greamworks-tweaks 1.0.9, the Power Level double-application guard, and the conservative v232.102 performance pass.
- Cleaned obsolete local mrpack builds, audit snapshots, extracted logs, old source copies, and generated build caches.

## v232.60 - 2026-07-26

- Renamed the first custom harvest rank to Wooden.
- Added non-pickaxe harvest assignments for 1070 blocks.
- Materialized recipe files so EMI and the Field Manual share stable recipe IDs.
- Added 60 mechanism recipes, 23 machine recipes, Black Steel recipes, and final Azazel progression.
- Added Omni-Alloy and the Blackstone Pedestal sequence.
- Added the Ring of the Seven Deadly Sins to humanoid Azazel rewards.
- Removed CBC Advanced Technology 0.1.4c from the test pack due to a binary incompatibility with Create Big Cannons 5.11.7.
- Applied 17 conservative Modrinth updates for NeoForge 1.21.1.
