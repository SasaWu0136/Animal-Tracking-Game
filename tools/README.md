# Java update utility

Run `java tools/AnimalTrackingGameUpdate.java --check` from the repository root to validate, or omit `--check` to install the embedded Wild Trails edition.

Requires JDK 17 or newer. No Python, external packages, network access or separate compilation is required.

The utility checks original Git blob hashes and embedded package integrity before writing. Custom files cause a stop without changes. The original game directory is moved to `archive/original/Animal Tracking Game`; maintained files go into `src/`, `web/`, `docs/`, `scripts/` and `tests/`.

Matching existing installations are preserved. The tool does not create commits or push to GitHub. Its embedded package is a fixed snapshot; editing game source does not automatically rebuild it. Use Git for normal maintenance of an installed checkout.

## Night-expedition interface

`java tools/AnimalTrackingNightUpdate.java` applies the dark sector-map interface to the original Wild Trails web edition. It checks all three frontend files, backs up replaced versions, and preserves custom changes. Add `--check` to validate without writing. Game rules and Java source are unchanged.
