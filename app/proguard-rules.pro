# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep all Room entities and DAOs
-keep class com.dailyreminder.data.local.database.entity.** { *; }
-keep class com.dailyreminder.data.local.database.dao.** { *; }

# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# Keep Gson classes
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }

# Keep kotlinx.serialization classes
-keep class kotlinx.serialization.** { *; }
-keep class kotlinx.datetime.serializers.** { *; }
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# Keep data classes used for serialization
-keep class com.dailyreminder.data.model.** { *; }

# Keep encryption utilities
-keep class com.dailyreminder.data.util.EncryptionUtils { *; }

# Keep notification service
-keep class com.dailyreminder.presentation.service.** { *; }

# Optimize but don't obfuscate for debugging
-dontobfuscate