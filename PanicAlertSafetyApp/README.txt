Panic Alert Safety App
------------------------------------------------------------
A modern Android safety application built using Kotlin, Jetpack Compose, and Material 3.
The app provides emergency alert, location sharing, and contact management features to enhance personal safety in critical situations.

------------------------------------------------------------
PROJECT OVERVIEW
------------------------------------------------------------
PanicAlertSafetyApp allows users to:
• Send emergency SMS alerts to predefined contacts.
• Automatically share their live GPS location.
• Manage trusted contacts and locations.
• Review alert history and sent notifications.
• Operate through a modern Jetpack Compose UI with Material 3 design principles.

------------------------------------------------------------
KEY FEATURES
------------------------------------------------------------
1. PANIC BUTTON
   - Instantly sends SOS messages with current location.

2. CONTACTS MANAGER
   - Add, update, or remove emergency contacts.

3. LOCATION TRACKER
   - Uses Google’s Fused Location Provider API for accurate GPS updates.

4. ALERT HISTORY
   - Displays previous alerts sent to contacts.

5. PERMISSION HANDLER
   - Handles all required runtime permissions (SMS, Location, Internet).

6. MODERN UI
   - Built completely with Jetpack Compose and Material 3 for dynamic themes.

------------------------------------------------------------
TECH STACK
------------------------------------------------------------
Language: Kotlin
UI Framework: Jetpack Compose (Material 3)
Architecture: MVVM (ViewModel + LiveData)
Navigation: Compose Navigation
Permissions: Accompanist Permissions Library
Location Services: Google Play Services Location API
Asynchronous Tasks: Kotlin Coroutines
Data Handling: Kotlinx Serialization (JSON)

------------------------------------------------------------
PROJECT STRUCTURE
------------------------------------------------------------
app/
 ┣ java/com/example/panicalertsafetyapp/
 ┃ ┣ ui/
 ┃ ┃ ┣ components/       → Reusable UI elements (AppBottomBar, PermissionHandler)
 ┃ ┃ ┣ screens/          → Screens like Home, Contacts, Locations, Alerts
 ┃ ┃ ┣ navigation/       → Navigation routes and NavHost setup
 ┃ ┃ ┣ theme/            → Color schemes and typography
 ┃ ┃ ┗ viewmodel/        → PanicViewModel managing app logic
 ┃ ┗ MainActivity.kt     → Entry point using Compose
 ┣ res/values/           → colors.xml, strings.xml, themes.xml
 ┣ res/mipmap/           → App icons
 ┗ AndroidManifest.xml

------------------------------------------------------------
PERMISSIONS REQUIRED
------------------------------------------------------------
ACCESS_FINE_LOCATION  → Fetches accurate GPS location.
SEND_SMS              → Sends emergency text messages.
INTERNET              → Supports network and map-based functionality.

------------------------------------------------------------
SETUP INSTRUCTIONS
------------------------------------------------------------
1. Clone the repository
   git clone https://github.com/your-username/PanicAlertSafetyApp.git
   cd PanicAlertSafetyApp

2. Open the project in Android Studio (Giraffe or newer).

3. Let Gradle sync automatically.

4. Connect an Android device or emulator.

5. Run the app using the “Run ▶” button.

------------------------------------------------------------
MINIMUM REQUIREMENTS
------------------------------------------------------------
Minimum SDK Version: 26
Target SDK Version: 34
Kotlin Version: 1.9.22
Compose Compiler Plugin: org.jetbrains.kotlin.plugin.compose
Android Studio Version: Giraffe or newer

------------------------------------------------------------
DEPENDENCIES USED
------------------------------------------------------------
androidx.compose.material3:material3
androidx.navigation:navigation-compose:2.7.7
androidx.lifecycle:lifecycle-runtime-ktx:2.7.0
androidx.activity:activity-compose:1.8.2
com.google.android.gms:play-services-location:21.0.1
com.google.accompanist:accompanist-permissions:0.34.0
org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3
org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0
androidx.compose.material:material-icons-extended
com.google.android.material:material:1.12.0

------------------------------------------------------------
DEVELOPER NOTES
------------------------------------------------------------
• This app uses the latest Compose Material 3 UI toolkit.
• The AppBottomBar component provides smooth navigation between screens.
• The PermissionHandler composable ensures runtime permission safety.
• Built with MVVM pattern for clean, scalable architecture.

------------------------------------------------------------
LICENSE
------------------------------------------------------------
MIT License
Copyright (c) 2025 Parth Bhatt

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

------------------------------------------------------------
AUTHOR
------------------------------------------------------------
Developed by: Parth Bhatt
Institution: Nanhi Pari Seemant Engineering College, Pithoragarh
Focus Areas: Android Development, AI Integration, and Innovation
Year: 2025
