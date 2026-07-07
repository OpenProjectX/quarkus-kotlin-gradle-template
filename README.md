# Quarkus Kotlin Gradle Template

A [Cookiecutter](https://cookiecutter.readthedocs.io/) template for creating multi-module **Quarkus extensions** in Kotlin with Gradle, ready for publication to Maven Central.

This is the Quarkus counterpart of the Spring Boot *starter + autoconfigure* template. The mapping:

| Spring Boot | Quarkus | Module here |
|---|---|---|
| `*-spring-boot-starter` (the dependency users add) | Extension **runtime** module | `runtime/` |
| `*-spring-boot-autoconfigure` (`@Configuration` + `@ConditionalOn…`) | Extension **deployment** module (`@BuildStep` processors) | `deployment/` |
| Runtime condition evaluation at every startup | **Build-time augmentation** (baked into bytecode) | — |
| `@ConfigurationProperties` | `@ConfigMapping` + `@ConfigRoot` | `runtime/…Config.kt` |

The key difference: Quarkus does the "auto-configuration" work once at **build time**, which is why it starts in milliseconds and compiles to native.

## Using this template

### Option A — Cookiecutter (recommended)

```bash
pipx install cookiecutter        # or: brew install cookiecutter / pip install cookiecutter

# From GitHub:
cookiecutter gh:OpenProjectX/quarkus-kotlin-gradle-template

# Or with the full URL:
cookiecutter https://github.com/OpenProjectX/quarkus-kotlin-gradle-template.git

# Pin to a released tag:
cookiecutter gh:OpenProjectX/quarkus-kotlin-gradle-template --checkout v1.0.0

# From a local clone:
cookiecutter path/to/quarkus-kotlin-gradle-template
```

Cookiecutter prompts for each variable, then generates a ready-to-build project.

### Option B — GitHub "Use this template" button

Copies the repo as-is (with `{{cookiecutter.*}}` placeholders) — you must then find-and-replace every placeholder and rename the placeholder-named directories/files. Option A is strongly recommended.

## Generated project structure

```
<project_slug>/
├── app/                                  # Example Quarkus app that consumes the extension
│   └── src/main/kotlin/…/app/…Resource.kt
├── core/                                 # Framework-agnostic Kotlin library (re-exported by runtime)
├── runtime/                              # Extension RUNTIME module = the "starter"
│   ├── <project_slug>.gradle.kts         #   -> artifactId: <project_slug>
│   └── src/main/kotlin/…/runtime/        #   …Config.kt (@ConfigMapping + @ConfigRoot), …Service.kt (@ApplicationScoped)
├── deployment/                           # Extension DEPLOYMENT module = the "autoconfigure"
│   ├── <project_slug>-deployment.gradle.kts  # -> artifactId: <project_slug>-deployment
│   └── src/main/kotlin/…/deployment/     #   …Processor.kt (@BuildStep methods)
├── buildSrc/                             # Gradle convention plugins
│   └── src/main/kotlin/
│       ├── kotlin-jvm.gradle.kts
│       └── quarkus-kotlin.gradle.kts     # applies the io.quarkus plugin to the app
├── gradle/libs.versions.toml             # Centralized dependency versions
├── build.gradle.kts                      # Maven Central publishing + GPG signing
├── settings.gradle.kts                   # Auto-discovers subprojects
└── gradle.properties
```

### Module naming rule

Quarkus requires the deployment artifact to be named `<runtime-artifactId>-deployment`. The `settings.gradle.kts` scanner names each project after its build file, so:

- `runtime/<project_slug>.gradle.kts` → project/artifact `<project_slug>`
- `deployment/<project_slug>-deployment.gradle.kts` → project/artifact `<project_slug>-deployment`

## What the generated sample does

The extension contributes a `…Service` bean (configured via a type-safe `@ConfigMapping` + `@ConfigRoot` interface) that the deployment module registers with an `AdditionalBeanBuildItem`. The example app injects it in a REST resource:

```bash
./gradlew build                 # builds core, runtime (extension), deployment, and the app
./gradlew :app:quarkusDev       # run the example app

curl localhost:8080/greeting/world
# -> Hello from <Project Name>, world!
```

At startup you'll see your extension listed in the Quarkus **Installed features** banner (from the `FeatureBuildItem`).

## Where to add your code

- **Runtime beans / config** → `runtime/src/main/kotlin/…/runtime/`
- **Build-time wiring** (`@BuildStep`) → `deployment/src/main/kotlin/…/deployment/…Processor.kt`
- **Pure logic with no Quarkus dependency** → `core/`
- **Extra runtime extensions** your extension builds on → `implementation(...)` in `runtime/<project_slug>.gradle.kts`; their `-deployment` counterparts in the deployment module.

## Template variables

| Variable          | Default              | Description                                  |
|-------------------|----------------------|----------------------------------------------|
| `project_name`    | My Quarkus Extension | Human-readable project name                  |
| `project_slug`    | _(derived)_          | Directory / runtime artifactId (hyphenated)  |
| `group_id`        | _(derived)_          | Maven group ID                               |
| `main_class_name` | _(derived)_          | Prefix for generated classes                 |
| `description`     | _(derived)_          | POM description                              |
| `github_username` | OpenProjectX         | GitHub org/user (SCM URLs)                    |
| `developer_*`     | OpenProjectX         | POM developer info                           |
| `kotlin_version`  | 2.4.0                | Kotlin version                               |
| `quarkus_version` | 3.37.1               | Quarkus version                              |
| `java_version`    | 17                   | Java toolchain version                       |

## Publishing to Maven Central

```bash
export OSSRH_USERNAME=...
export OSSRH_PASSWORD=...
export SIGNING_KEY_FILE=path/to/key.gpg
export SIGNING_KEY_PASSWORD=...

./gradlew release
```

## Tech stack

- Kotlin 2.4.0 · Quarkus 3.37.1 · Gradle 9.5.1 · Java 17 toolchain
