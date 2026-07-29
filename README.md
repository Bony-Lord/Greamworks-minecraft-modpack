# Greamworks Minecraft Modpack

NeoForge 1.21.1 modpack project for the Greamworks progression pack.

## Current baseline

- Pack version: **v232.104**
- Minecraft: **1.21.1**
- NeoForge: **21.1.244**
- Focus: Create-centered progression, Cult of Azazel endgame, custom harvest tiers, EMI-visible recipes, native cross-mod fluid interactions, and conservative performance tuning.
- Release artifact: `Greamworks_v232.104_EMI_Fluid_Interactions_Book_Repair.mrpack`
- Core pack modules: `greamworks-fixes` 1.0.40, `greamworks-tweaks` 1.0.9, and `greamworks-void-dimension` 1.1.1.
- Third-party JARs and resource packs remain unmodified. Pack behavior is implemented with KubeJS, datapack overrides, configuration, and separate compatibility mods.

## v232.104 highlights

- 15 authoritative fluid-collision recipes implemented through NeoForge `FluidInteractionRegistry`.
- Matching EMI recipes in the vanilla **World Interaction** category, including calcite formation.
- Removed the old KubeJS world scanner and stale item-information entries.
- Repaired 106 Field Manual recipe references and invalid item IDs across 60 icon pages.
- Retained the v232.102 conservative performance pass and Power Level guard fixes.
- Cleaned local release history so only the current v232.104 artifact and the explicit v229 backup remain outside source control.

Runtime logs, worlds, caches, crash reports, local exports, staging directories, and release archives are intentionally excluded from version control.
