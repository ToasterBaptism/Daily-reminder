#!/bin/bash

# Daily Reminder - Build APK Script
# This script builds the Android APK for the Daily Reminder app

set -e  # Exit on any error

echo "🏗️  Building Daily Reminder APK..."

# Check if we're in the right directory
if [ ! -f "gradlew" ]; then
    echo "❌ Error: gradlew not found. Please run this script from the project root directory."
    exit 1
fi

# Make gradlew executable
chmod +x gradlew

echo "📦 Cleaning previous builds..."
./gradlew clean

echo "🔧 Building debug APK..."
./gradlew assembleDebug

echo "🔧 Building release APK..."
./gradlew assembleRelease

# Check if APKs were created
DEBUG_APK="app/build/outputs/apk/debug/app-debug.apk"
RELEASE_APK="app/build/outputs/apk/release/app-release-unsigned.apk"

if [ -f "$DEBUG_APK" ]; then
    echo "✅ Debug APK created: $DEBUG_APK"
    ls -lh "$DEBUG_APK"
else
    echo "❌ Debug APK not found!"
fi

if [ -f "$RELEASE_APK" ]; then
    echo "✅ Release APK created: $RELEASE_APK"
    ls -lh "$RELEASE_APK"
else
    echo "❌ Release APK not found!"
fi

echo "🎉 Build completed!"
echo ""
echo "📱 To install the debug APK on a connected device:"
echo "   adb install $DEBUG_APK"
echo ""
echo "📋 APK Information:"
echo "   Debug APK: $DEBUG_APK"
echo "   Release APK: $RELEASE_APK"
echo ""
echo "⚠️  Note: The release APK is unsigned. For production, you'll need to sign it."