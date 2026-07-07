// ===========================================================================
// Independent frontend module: a Vite + React single-page app.
//
// Gradle doesn't compile anything here — the `app` module's Quinoa extension
// installs the npm dependencies and runs the Vite build
// (see `quarkus.quinoa.ui-dir = ../webui` in the app's application.yaml), then
// serves the produced `dist/` from the Quarkus application.
//
// The `base` plugin just gives this module the standard lifecycle tasks
// (build / clean / assemble) so it participates cleanly in the Gradle build.
//
// To iterate on the UI standalone:  cd webui && npm install && npm run dev
// ===========================================================================
plugins {
    base
}
