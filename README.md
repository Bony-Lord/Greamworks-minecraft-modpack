# Greamworks Minecraft Modpack

NeoForge 1.21.1 modpack project for the Greamworks progression pack.

## Current baseline

- Pack version: **v232.106**
- Minecraft: **1.21.1**
- NeoForge: **21.1.244**
- Focus: Create-centered progression, Cult of Azazel endgame, custom harvest tiers, EMI-visible recipes, native cross-mod fluid interactions, and conservative performance tuning.
- Release artifact: `Greamworks_v232.106_Processing_Loot_CreativeTabs.mrpack`
- Core pack modules: `greamworks-fixes` 1.0.40, `greamworks-tweaks` 1.0.10, and `greamworks-void-dimension` 1.1.1.
- Third-party JARs and resource packs remain unmodified. Pack behavior is implemented with KubeJS, datapack overrides, configuration, and separate compatibility mods.

## v232.106 highlights

- Reduced vanilla mob-spawner activation range from 48 to 20 blocks.
- Added 23 EMI-visible Create mechanical-mixer alternatives for the fluid-stone interactions.
- Added chance-based Chemica, Silent Gear, and TFMG crushing routes and more Create milling routes.
- Added Apothic Enchanting infusion recipes using Eterna, Quanta, and Arcana.
- Added a hidden-items creative tab, Omni-Alloy and the guide book to Greamworks, and Void Shard to Cataclysm.
- Added low-tier supplies, Stick n Stone gear, Andesite Alloy, and Silent Gear templates to chest loot.
- Reduced Stick n Stone wooden weapon durability to 20.
- Create: Easy Stone Generators was not added.

- Fixed the v232.105 fluid reactions with direct `FluidType` matching and a server-side neighbor fallback.
- Registered matching EMI recipes through NeoForge's required `@EmiEntrypoint`.
- Added explicit registered/skipped reaction diagnostics.
- Removed the old KubeJS world scanner and stale item-information entries.
- Repaired 106 Field Manual recipe references and invalid item IDs across 60 icon pages.
- Retained the v232.102 conservative performance pass and Power Level guard fixes.
- Added the custom `greamworks-tweaks` 1.0.11 sources and the current `.mrpack` to this repository.

Runtime logs, worlds, caches, crash reports, local exports, staging directories, and release archives are intentionally excluded from version control.
