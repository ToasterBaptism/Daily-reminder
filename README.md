# 📱 Daily Reminder - Offline Meal Planning & Task Management App

A fully autonomous, offline-capable Android application for organizing daily meals, tasks, and appointments through an intuitive calendar interface with real-time notifications.

## 🎯 Project Overview

Daily Reminder is a complete, self-contained Android app that enables users to organize their daily activities without requiring any internet connection. All data is stored locally with encryption, ensuring complete privacy and offline functionality.

## ✨ Key Features

### 📅 Interactive Calendar System
- Month, week, and day view options
- Gesture navigation between time periods
- Current day highlighting with visual indicators
- Multi-day event visualization
- Holiday and weekend differentiation

### 🍽️ Meal Planning Module
- Time-specific meal scheduling (breakfast, lunch, dinner, snacks)
- Recipe integration with preparation steps
- Ingredient tracking and grocery list generation
- Nutritional information display
- Meal type categorization (vegetarian, vegan, keto, etc.)
- Portion size customization
- Cooking time estimation

### ✅ Task Management System
- Custom task creation with 11 categories:
  - Work/Professional
  - Personal Care
  - Health & Medical
  - Shopping & Errands
  - Household Chores
  - Education & Learning
  - Finance & Banking
  - Travel & Transportation
  - Social & Recreation
  - Hobbies & Interests
  - Other
- Task priority levels (Low, Medium, High, Urgent)
- Task duration estimation
- Recurring task scheduling (daily, weekly, monthly)
- Task completion tracking with progress indicators
- Subtask support
- Tag system for organization

### 🔔 Notification & Alert Engine
- Customizable toast notifications
- Alarm-style reminders with sound/vibration
- Notification scheduling with lead times
- Snooze functionality with multiple options
- Notification grouping by category
- Do-not-disturb integration
- Notification history with action tracking

### 💾 Data Management Features
- Local SQLite database with Room persistence
- Automatic daily backups with 30-day retention
- Manual backup/restore functionality
- Data export in multiple formats (JSON, encrypted)
- Import capability for existing schedules
- AES-256 data encryption for privacy protection
- Storage optimization with compression
- Conflict resolution for sync scenarios

## 🏗️ Technical Architecture

### Frontend
- **UI Framework**: Jetpack Compose 1.5+ with Material Design 3
- **Navigation**: Navigation Compose with bottom navigation
- **State Management**: ViewModel with StateFlow/LiveData
- **Theming**: Dark/Light theme support with accessibility options

### Backend Logic
- **Language**: Kotlin 1.9+
- **Architecture**: Clean Architecture with MVVM pattern
- **Dependency Injection**: Hilt for dependency management
- **Async Operations**: Coroutines and Flow
- **Background Tasks**: WorkManager for scheduled operations

### Data Layer
- **Database**: Room Persistence Library with SQLite
- **Encryption**: AES-256 for sensitive data
- **Backup Format**: Encrypted JSON with compression
- **Storage**: Internal app storage only (no external dependencies)

### Notification System
- **Manager**: Android Notification Manager with custom channels
- **Scheduling**: AlarmManager for precise timing
- **Sounds**: Custom notification sounds and vibration patterns
- **Actions**: Dismiss, snooze, and quick action support

## 📁 Project Structure

```
daily-reminder/
├── app/
│   ├── src/main/java/com/dailyreminder/
│   │   ├── MainActivity.kt
│   │   ├── application/
│   │   │   ├── DailyReminderApplication.kt
│   │   │   └── di/ (Hilt modules)
│   │   ├── data/
│   │   │   ├── local/ (Database, DAOs, Entities)
│   │   │   ├── model/ (Data models)
│   │   │   └── util/ (Utilities)
│   │   ├── domain/
│   │   │   └── usecase/ (Business logic)
│   │   └── presentation/
│   │       ├── ui/ (Compose screens & components)
│   │       ├── viewmodel/ (ViewModels)
│   │       └── service/ (Background services)
│   └── src/main/res/ (Resources, themes, strings)
├── scripts/ (Build and utility scripts)
└── docs/ (Documentation)
```

## 🚀 Getting Started

### Prerequisites
- Android Studio Iguana (2023.2.1) or newer
- Android SDK 34 (Android 14)
- OpenJDK 17
- Minimum device: Android 8.0+ (API 26)

### Building the App

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd Daily-reminder
   ```

2. **Build using the script**:
   ```bash
   ./scripts/build-apk.sh
   ```

3. **Or build manually**:
   ```bash
   ./gradlew assembleDebug
   ./gradlew assembleRelease
   ```

### Installation

1. **Install debug APK**:
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

2. **Or install via Android Studio**:
   - Open project in Android Studio
   - Connect device or start emulator
   - Click "Run" button

## 🔒 Privacy & Security

### Zero Internet Dependency
- **No Network Permissions**: App cannot access internet
- **Offline-Only**: All functionality works without connectivity
- **Local Processing**: All operations performed on-device
- **No Telemetry**: Zero data collection or analytics

### Data Protection
- **AES-256 Encryption**: All sensitive data encrypted
- **Local Storage**: Data never leaves the device
- **Secure Backups**: Password-protected export files
- **Access Control**: Device-level authentication integration

### Compliance
- **GDPR Compliant**: No personal data collection
- **CCPA Compliant**: No data sharing or selling
- **Privacy by Design**: Built with privacy as core principle

## 🎨 Accessibility Features

### Visual Accessibility
- **High Contrast Mode**: Enhanced contrast for better visibility
- **Large Text Support**: Scalable fonts up to 200%
- **Colorblind-Friendly**: Accessible color palette
- **Dark/Light Themes**: Automatic or manual theme switching

### Motor Accessibility
- **Large Touch Targets**: Minimum 48dp touch areas
- **Gesture Support**: Swipe navigation throughout app
- **Voice Feedback**: Screen reader compatibility
- **Haptic Responses**: Tactile feedback for confirmations

### Cognitive Accessibility
- **Simple Navigation**: Intuitive bottom navigation
- **Clear Labels**: Descriptive content descriptions
- **Consistent Layout**: Predictable interface patterns
- **Help System**: Contextual guidance and tutorials

## 📊 Performance Specifications

### Resource Usage
- **APK Size**: <25MB total
- **RAM Usage**: <150MB peak usage
- **CPU Usage**: <20% average during active use
- **Battery Drain**: <5%/hour during active use
- **Database Response**: <100ms average query time

### Compatibility
- **Android Versions**: 8.0+ (API 26)
- **Architectures**: arm64-v8a, armeabi-v7a, x86_64
- **Screen Sizes**: Phone and tablet support
- **Languages**: English (primary), with localization support

## 🧪 Testing

### Automated Testing
```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Run all tests
./gradlew check
```

### Manual Testing Scenarios
1. **First-time installation and setup**
2. **Daily task scheduling and notifications**
3. **7-day continuous usage period**
4. **Backup/restore functionality**
5. **Performance under low memory conditions**
6. **Accessibility with TalkBack enabled**

## 📦 Build Outputs

### Debug Build
- **File**: `app/build/outputs/apk/debug/app-debug.apk`
- **Purpose**: Development and testing
- **Features**: Debug logging, development tools

### Release Build
- **File**: `app/build/outputs/apk/release/app-release-unsigned.apk`
- **Purpose**: Production deployment
- **Features**: Optimized, obfuscated, unsigned

## 🔧 Configuration

### Build Variants
- **Debug**: Development build with logging
- **Release**: Production build with optimizations

### Signing Configuration
For production releases, configure signing in `app/build.gradle`:
```gradle
android {
    signingConfigs {
        release {
            storeFile file('path/to/keystore.jks')
            storePassword 'store_password'
            keyAlias 'key_alias'
            keyPassword 'key_password'
        }
    }
}
```

## 📚 Documentation

- **User Manual**: Complete user guide with screenshots
- **Technical Specification**: Detailed architecture documentation
- **API Documentation**: Internal API reference
- **Privacy Policy**: Data handling and privacy practices

## 🤝 Contributing

This is a complete, self-contained application built to specification. For modifications or enhancements:

1. Follow the existing architecture patterns
2. Maintain offline-only functionality
3. Ensure accessibility compliance
4. Add comprehensive tests for new features
5. Update documentation accordingly

## 📄 License

This project is built as a complete implementation of the Daily Reminder specification. All code is original and follows Android development best practices.

## 🆘 Support

### Common Issues
1. **Build Failures**: Ensure Android SDK 34 is installed
2. **Permission Errors**: Grant notification permissions in device settings
3. **Database Issues**: Clear app data to reset database
4. **Backup Problems**: Check device storage permissions

### Troubleshooting
- Check Android Studio version compatibility
- Verify Gradle and build tools versions
- Ensure device meets minimum requirements
- Review logcat output for detailed error messages

---

**Daily Reminder** - Your complete offline planning companion! 📱✨