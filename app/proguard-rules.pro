# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\flex 2\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# keep this class so that logging will show 'ACRA' and not a obfuscated name like 'a'.
# Note: if you are removing log messages elsewhere in this file then this isn't necessary
#-keep class com.google.**
#-dontwarn com.google.**

#-keep class com.google.**
#-dontnote libcore.icu.ICU
#-dontnote sun.misc.Unsafe

###################################################################################################
# Follwing rules are needed for ACRA
###################################################################################################

# keep all members of the class ACRA
-keep class org.acra.ACRA {
    *;
}

# keep this around for some enums that ACRA needs
-keep class org.acra.ReportingInteractionMode {
    *;
}

-keepnames class org.acra.sender.HttpSender$** {
    *;
}

-keepnames class org.acra.ReportField {
    *;
}

# keep this otherwise it is removed by ProGuard
-keep public class org.acra.ErrorReporter{
    public void addCustomData(java.lang.String,java.lang.String);
    public void putCustomData(java.lang.String,java.lang.String);
    public void removeCustomData(java.lang.String);
}

# keep this otherwise it is removed by ProGuard
-keep public class org.acra.ErrorReporter{
    public void handleSilentException(java.lang.Throwable);
}


###################################################################################################
# Follwing rules are needed for Google Task API
###################################################################################################

# Needed to keep generic types and @Key annotations accessed via reflection

-keepattributes Signature,RuntimeVisibleAnnotations,AnnotationDefault

-keepclassmembers class * {
  @com.google.api.client.util.Key <fields>;
}

# Needed by google-http-client-android when linking against an older platform version

-dontwarn com.google.api.client.extensions.android.**

# Needed by google-api-client-android when linking against an older platform version

-dontwarn com.google.api.client.googleapis.extensions.android.**

# Needed by google-play-services when linking against an older platform version

-dontwarn com.google.android.gms.**

# com.google.client.util.IOUtils references java.nio.file.Files when on Java 7+
-dontnote java.nio.file.Files, java.nio.file.Path

# Suppress notes on LicensingServices
-dontnote **.ILicensingService

# Suppress warnings on sun.misc.Unsafe
-dontnote sun.misc.Unsafe
-dontwarn sun.misc.Unsafe