# Math Merge

An ad-free, chain-merge number game for little kids. Slide your finger across
touching tiles; the math shows up at the top. If the answer is on the level's
"ladder," the tiles merge into it.

**Levels (the ladder you build):**
- **Doubling:** 2, 4, 8, 16, 32 …
- **Squares:** 1, 4, 9, 16, 25 … (tiles show `3×3`, `4×4` …)
- **Cubes:** 1, 8, 27, 64 … (tiles show `2×2×2` …)
- **Tens:** 10, 100, 1000 … (adding = number bonds like `3 + 7 = 10`, `40 + 60 = 100`)

**Learn the shape (Squares & Cubes only):** besides the drag modes there are
three quiz modes: **See it** (dot grid / block cube → number), **Link it**
(number → picture → name like 16²) and **Name it** (find the 2 right names out
of 5). Pictures are shaded in blocks of 5. Each has 3 stages: warm-up
(1²–20², 1³–11³), stretch (21²–30², 12³–20³) and tens (30², 300², (3×10⁵)² …
where the zeros double or triple). Missed facts come back more often.

**No skipping:** a merge can never jump past the next goal on the ladder
("Too big a jump! Make 36 first"). Times modes follow the shape being taught:
Squares = 2 of the same (6 × 6), Cubes = 3 of the same (3 × 3 × 3, and the
finished square/cube is collected onto the ladder), Doubling/Tens = exactly
2 tiles (16 × 2, 10⁵ × 10).

**Doubling · Adding** uses the true doubling rule: two equal tiles, then each
next tile must equal the total so far (8 + 8 + 16 + 32).

Numbers are exact (BigInt) and keep going: big ones show as `10⁴²` or
`1.4×10⁴²`, the goal bar switches to orders of magnitude, every new "-illion"
(million … tredecillion … vigintillion) gets a celebration and is read out
loud, and reaching a **googol (10¹⁰⁰)** wins a trophy.

**Math (difficulty):**
- **Adding (Easy):** `4 + 5 = 9`
- **Take away (Medium):** `10 − 1 = 9`
- **Times (Hard):** `3 × 3 = 9`

Bright tiles are ladder numbers; grey tiles are helper numbers. There is no
game over: if the board gets stuck it shuffles itself. Hint shows a move.
The math is read out loud (the phone's text-to-speech), and each of the 9
level/math combos keeps its own saved game and best score.

## Building

Push to `main` and GitHub Actions builds the APK automatically:
- **Releases** (right side of the repo page) → newest `build-N` → tap
  `MathMerge.apk` on the phone to install.
- Or **Actions** tab → latest run → `MathMerge-apk` artifact (a zip).

**Web version (iPhones, any browser):** one-time, go to the repo's
Settings → Pages → Source: **GitHub Actions**. After that, every push also
publishes the game to `https://<your-username>.github.io/<repo-name>/`.
On an iPhone, open it in Safari → Share → **Add to Home Screen** for an app icon.

**Updating the app:** after a new build, open Math Merge and the menu shows a
green **"New version! Tap to update"** button (it checks this repo's Releases;
the repo must be public). Tap it, let the APK download, tap the downloaded file,
and choose **Update**. Saved games are kept. The build number is shown at the
bottom of the menu.

Updates only install over the old app if the new APK has a higher build number
and the same signing key, so:
- keep `app/mathmerge.keystore` in the repo and don't change it;
- keep building from this same repo (a new repo restarts build numbers at 1,
  and Android won't "downgrade").

`app/mathmerge.keystore` is the signing key. Keep it in the repo so new
builds install over old ones without losing saved games.

## Files
- `app/src/main/assets/index.html`: the whole game (HTML/JS). Open it in any
  browser to test it.
- `app/src/main/java/.../MainActivity.java`: Android wrapper (WebView,
  text-to-speech, vibration, back button).
