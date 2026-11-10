# Math Merge

An ad-free, chain-merge number game for little kids. Slide your finger across
touching tiles; the math shows up at the top. If the answer is on the level's
"ladder," the tiles merge into it.

**Levels (the ladder you build):**
- **Doubling:** 2, 4, 8, 16, 32 …
- **Squares:** 1, 4, 9, 16, 25 … (tiles show `3×3`, `4×4` …)
- **Cubes:** 1, 8, 27, 64 … (tiles show `2×2×2` …)
- **Tens:** 10, 100, 1000 … (adding = number bonds like `3 + 7 = 10`, `40 + 60 = 100`)

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

`app/mathmerge.keystore` is the signing key. Keep it in the repo so new
builds install over old ones without losing saved games.

## Files
- `app/src/main/assets/index.html`: the whole game (HTML/JS). Open it in any
  browser to test it.
- `app/src/main/java/.../MainActivity.java`: Android wrapper (WebView,
  text-to-speech, vibration, back button).
