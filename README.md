# APIAssesment2App

Android app for NIT3213 final assignment: Login → Dashboard (RecyclerView) → Details using the `vu-nit3213-api`.

- **Name:** Geetansh  
- **Student ID:** 8103430  
- **Campus (auth path used):** `footscray`

---

## ✨ Features

- **Login Screen**
  - Username = first name, Password = 7–8 digit Student ID (e.g. `8103430`)
  - POST `/{campus}/auth` → receives `keypass`
  - Error handling + timeout retry (free Render servers can “cold start”)

- **Dashboard**
  - GET `/dashboard/{keypass}`
  - RecyclerView shows a **summary** for each entity (first two fields excluding `description`)
  - Each card is clickable:
    - Shows a **summary dialog** (title = asset type when available)
    - Button to open full **Details** screen

- **Details**
  - Displays **all fields**
  - `description` shown in a separate section

---

## 🧩 Tech Stack

- **Kotlin**, **AndroidX**, **Material**
- **Retrofit** + **OkHttp** (`logging-interceptor`)
- **Moshi** (`moshi-kotlin`) for Kotlin JSON
- **Hilt** for Dependency Injection
- Unit tests with **JUnit4**, **MockWebServer**, **Truth**

---

## 🌐 API

Base URL: `https://nit3213api.onrender.com/`

- **POST** `/{campus}/auth`
  ```json
  { "username": "Geetansh", "password": "8103430" }
  ```
  Response:
  ```json
  { "keypass": "forex" }
  ```

- **GET** `/dashboard/{keypass}`
  ```json
  {
    "entities": [ { /* entity */ }, ... ],
    "entityTotal": 7
  }
  ```
---

## 📁 Project Structure

```
app/src/main/java/com/example/apiassesment2app/
├─ App.kt
├─ di/NetworkModule.kt
├─ data/
│  ├─ ApiService.kt
│  ├─ Repository.kt
│  └─ model/
│     ├─ LoginRequest.kt
│     ├─ LoginResponse.kt
│     └─ DashboardResponse.kt
└─ ui/
   ├─ login/LoginPage.kt
   ├─ dashboard/
   │  ├─ DashboardActivity.kt
   │  └─ EntityAdapter.kt
   └─ details/DetailsActivity.kt
```

Layouts:  
`activity_login_page.xml`, `activity_dashboard.xml`, `item_entity.xml`, `activity_details.xml`  

Manifest:  
`AndroidManifest.xml` (Internet permission + exported flags)

---

## 🛠️ Setup & Run

1. **Open in Android Studio** (Hedgehog+ recommended).
2. **Gradle JDK**: 17  
   _Preferences → Build, Execution, Deployment → Build Tools → Gradle → **Gradle JDK = 17**_
3. **Sync Gradle**.
4. Run on an emulator or device.

> The API may “sleep” on Render. The first call can take ~10–30s.  
> We set OkHttp timeouts to 30s and retry the login once automatically.

---

## ✅ Unit Tests

Repository tests use MockWebServer to verify the auth and dashboard flows.

- Run: **Gradle panel → :app → verification → testDebugUnitTest**  
  or Right-click `RepositoryTest` → **Run**.
- Report: `app/build/reports/tests/testDebugUnitTest/index.html`

Dependencies in `app/build.gradle.kts`:
```kotlin
testImplementation("junit:junit:4.13.2")
testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
testImplementation("com.google.truth:truth:1.4.2")
```

---

## 🧪 Quick API check (optional)

Use curl to verify your campus:
```bash
curl -X POST https://nit3213api.onrender.com/footscray/auth   -H "Content-Type: application/json"   -d '{"username":"Geetansh","password":"8103430"}'
```

---

## 🩹 Troubleshooting

- **Timeout on login**: likely API cold start. Wait a few seconds and try again. We auto-retry once; OkHttp timeouts are 30s.
- **Manifest error (Android 12+)**: launcher activity must declare `android:exported="true"`.
- **JVM mismatch**: set **Gradle JDK = 17** and keep `compileOptions` + `kotlinOptions` at 17.

---

## 📚 Rubric Mapping

- **Project Completion**: all required features implemented (Login → Dashboard → Details, click handling).  
- **Code Organization/Cleanliness**: clear packages, single responsibility, DI separated.  
- **Dependency Injection**: Hilt with proper modules and injection points.  
- **Unit Testing**: repository tests using MockWebServer verify auth and dashboard parsing.

---

## 📸 Suggested screenshots for submission
- Login screen (with input + progress)
- Dashboard (list of cards with borders)
- Dialog summary on item click
- Details screen
- Test report (green)

---

## 🔧 Git steps

From the project root:

```bash
git init
curl -L https://www.toptal.com/developers/gitignore/api/android,androidstudio > .gitignore
git add .
git commit -m "chore: bootstrap project with Hilt + Retrofit + Moshi"
git remote add origin https://github.com/<your-username>/APIAssesment2App.git #<your username = github account username>
git branch -M main
git push -u origin main

git commit -m "feat(login): UI + validation + auth + error handling"
git commit -m "feat(dashboard): RecyclerView + card borders + item click dialog"
git commit -m "feat(details): show full entity info incl. description"
git commit -m "fix(network): increase OkHttp timeouts + retry on cold start"
git commit -m "test(repository): add MockWebServer tests for login & dashboard"
git commit -m "docs: add README with build/run/test instructions"
git push
```
