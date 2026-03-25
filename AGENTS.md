# AGENTS.md

## Language policy

- 默认使用简体中文回答。
- 除非我明确要求英文，否则不要切换英文叙述。
- 代码、命令、报错、API 名称保持原文，不要强行翻译。
- 提问澄清时也使用中文。

## Project overview

- Repository: `kaleidoscopecookery`
- Platform: Minecraft Forge 1.20.1 mod
- Java version: 17 (`build.gradle`, `gradle.properties`)
- Gradle wrapper: 8.14.3 (`gradle/wrapper/gradle-wrapper.properties`)
- Mod ID: `kaleidoscope_cookery`
- Main package: `com.github.ysbbbbbb.kaleidoscopecookery`
- CI currently builds with `./gradlew build` in `.github/workflows/gradle-publish-1.20.1.yml`

## Instruction file inventory

- No existing root `AGENTS.md` was present before this file was added.
- No `.cursorrules` file was found.
- No `.cursor/rules/` directory was found.
- No `.github/copilot-instructions.md` file was found.
- No extra agent-specific instruction files were discovered in this repo.
- If new instruction files are added later, treat deeper/local files as more specific than this root guide.

## Environment and tooling

- Always use the Gradle wrapper, not a system Gradle install.
- Windows commands: `./gradlew.bat <task>`
- Unix/macOS commands: `./gradlew <task>`
- The build disables the Gradle daemon in `gradle.properties`; expect one-shot daemon startup messages.
- Source encoding is UTF-8 (`tasks.withType(JavaCompile).configureEach`).
- ForgeGradle, Parchment mappings, and Sponge Mixin are configured in `build.gradle`.
- The project includes generated resources from `src/generated/resources`.
- The default Forge run directories are under `run/`, which is gitignored.

## Repository layout

- `src/main/java/` — main Java sources
- `src/main/resources/` — handwritten resources, mixin config, `mods.toml`, assets, data
- `src/generated/resources/` — generated resources included in the main resource set
- `src/test/` — not present at the time of writing
- `src/gameTest/` — not present at the time of writing
- `.github/workflows/gradle-publish-1.20.1.yml` — CI build workflow
- `build.gradle` — source of truth for runs, dependencies, mappings, mixins, and resource handling

## Verified commands

These commands were checked directly in this repository.

### Core build commands

- List all tasks: `./gradlew.bat tasks --all`
- Build the mod: `./gradlew.bat build`
- Clean outputs: `./gradlew.bat clean`
- Build help: `./gradlew.bat help --task build`

Notes:

- `build` completed successfully in this repo.
- `build` currently runs `test`, but `test` resolves to `NO-SOURCE` because no committed test sources were found.
- `assemble` exists and is narrower than `build` if you only need packaging.

### Forge run tasks

- Client run: `./gradlew.bat runClient`
- Second client profile: `./gradlew.bat runClient2`
- Dedicated server run: `./gradlew.bat runServer`
- Data generation: `./gradlew.bat runData`
- GameTest server task: `./gradlew.bat runGameTestServer`

These task names were verified with `help --task` and `tasks --all`.

### Test commands and single-test guidance

- Gradle exposes a `test` task: `./gradlew.bat test`
- Gradle also exposes single-test filtering syntax: `./gradlew.bat test --tests "com.example.MyTest"`
- However, this repo currently has no committed `src/test/java` tree, no discovered JUnit test classes, and `test` currently reports `NO-SOURCE`.
- Practical meaning: there is no usable single-test workflow today because there are no committed tests to target.
- If a proper JVM test suite is added later, prefer `--tests` for single-class or single-method runs.

### Command selection rules

- For general validation, use `build`.
- For content/datagen changes, run `runData` and review the generated diff.
- For client-only behavior, rendering, tooltips, or visual behavior, run `runClient`.
- For common/server-side logic, registries, recipes, loot, entities, or worldgen, prefer `runServer` or `build`.
- Use `runGameTestServer` only if real GameTests are added; none were found during this analysis.
- If you are unsure what exists, start with `tasks --all`.

## Current validation reality

- No dedicated lint task was found.
- No Checkstyle, Spotless, or EditorConfig file was found.
- No committed unit test source tree was found.
- No committed GameTest source tree was found.
- CI currently verifies `build`; do not claim stronger automated coverage than that.
- The build does emit existing compiler warnings; do not treat those warnings as newly introduced unless your change causes them.

## Code organization patterns

- The mod entrypoint is `KaleidoscopeCookery` and wires registries onto the Forge mod event bus.
- Registry-heavy code lives under `init/` with classes like `ModItems`, `ModBlocks`, `ModEffects`, and `ModRecipes`.
- Event handlers are placed under `event/` and commonly use `@Mod.EventBusSubscriber` plus static `@SubscribeEvent` methods.
- Datagen code lives under `datagen/`, with providers registered from `DataGenerators`.
- Mixin classes live under `mixin/` and must stay synchronized with `src/main/resources/kaleidoscope_cookery.mixins.json`.
- Utilities are collected under `util/` and tend to be small, static helper methods.

## Code style guidelines

### Formatting

- Use 4-space indentation.
- Keep opening braces on the same line.
- Match the spacing style already present in the touched file instead of reformatting unrelated code.
- Keep chained builder calls readable; split long chains only when the surrounding file already does so.
- Do not perform broad style-only rewrites in large registry files.

### Imports

- Prefer the import style already used in the local file.
- Most files use explicit imports.
- Wildcard imports do exist in some high-fanout files such as `KaleidoscopeCookery.java` and `ModItems.java`.
- Do not normalize wildcard imports to explicit imports unless you are already editing that file for a concrete reason.
- Keep `java.*`, third-party, Minecraft, and mod-local imports grouped consistently with the file’s existing order.

### Types and nullability

- Target Java 17 language features only.
- Several packages use package-level `@ParametersAreNonnullByDefault` and `@MethodsReturnNonnullByDefault` via `package-info.java`; preserve those defaults.
- When a nullable API is required, use the existing annotation style (`org.jetbrains.annotations.Nullable` appears in datagen code).
- Prefer concrete Forge/Minecraft types over generic `Object` unless the API genuinely requires flexibility.
- Do not introduce `as any`-style thinking in Java by weakening types unnecessarily.

### Naming

- Classes, enums, and records: `PascalCase`
- Methods and local variables: `camelCase`
- Constants and registry fields: `UPPER_SNAKE_CASE`
- Resource IDs, registry names, texture/model/data file names: `lower_snake_case`
- Keep package names lowercase.
- New registry entries should follow existing naming patterns exactly so IDs, textures, lang keys, and recipes stay aligned.

### Control flow and error handling

- Prefer guard clauses and early returns for invalid or irrelevant states.
- Avoid empty catch blocks.
- Avoid introducing defensive exception handling unless the surrounding API actually throws recoverable exceptions.
- Follow existing null-handling patterns such as `Objects.requireNonNull(...)` when a registry lookup must exist.
- When interacting with Forge registries, be explicit about the expected nullability.

### Logging and comments

- The repo defines a shared logger in `KaleidoscopeCookery.LOGGER`.
- Use logging sparingly for actual diagnostics, not routine control flow.
- Existing comments are a mix of Chinese and English.
- Preserve the language convention already used in the file you touch; do not rewrite comment language wholesale.

## Datagen and generated resources

- `src/generated/resources` is part of the main resources set and is committed output, not throwaway scratch data.
- `build.gradle` excludes only `.cache/**` from that generated tree.
- If you change datagen providers, recipe builders, tag generators, or model/state generators, run `./gradlew.bat runData`.
- Review generated JSON/model/tag/lang output before finishing.
- Avoid hand-editing generated files unless there is a clear repo-specific reason and no generator owns the file.
- Keep handwritten assets in `src/main/resources` and generated assets in `src/generated/resources` separated.

## Change strategy for agents

- Make narrow, local changes that match the surrounding subsystem.
- In registry-heavy files, add or adjust the smallest necessary entry instead of refactoring the whole file.
- For new content, update all linked surfaces together: Java registration, models/textures, recipes/tags/loot, lang keys, and any datagen provider.
- For mixin changes, update both the Java mixin class and the mixin config if required.
- For event logic, prefer the established static subscriber pattern unless the existing subsystem uses a different one.

## Verification checklist

- Run the narrowest relevant verified command before finishing.
- Minimum default check: `./gradlew.bat build`
- Datagen change: `./gradlew.bat runData`
- Client-visible change: `./gradlew.bat runClient` when practical
- Server/common gameplay change: `./gradlew.bat runServer` or `build`
- If you add JVM tests in the future, use `./gradlew.bat test --tests "..."` for focused reruns.
- State clearly when no automated test coverage exists for the touched area.

## What not to assume

- Do not assume a lint pipeline exists.
- Do not assume committed unit tests or GameTests exist.
- Do not assume generated resources can be skipped after datagen changes.
- Do not assume wildcard imports are forbidden; follow the local file.
- Do not assume comments must be English-only.
