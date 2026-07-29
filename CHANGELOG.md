# Changelog

Only stable progression baselines are listed here. Intermediate crash-fix and test builds are intentionally omitted.

## v232.104 - 2026-07-30

- Updated the tracked baseline from v232.60 to v232.104 on Minecraft 1.21.1 and NeoForge 21.1.244.
- Replaced the KubeJS fluid-world scanner with 15 native NeoForge `FluidInteractionRegistry` reactions.
- Added cross-mod stone formation using Chemica and TFMG fluids, including all raw Create stone families and calcite.
- Added an EMI plugin that mirrors the authoritative Java table in the World Interaction category.
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
