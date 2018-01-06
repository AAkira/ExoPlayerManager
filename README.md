# ExoPlayerManager

An android library that wraps the ExoPlayer and the IMA Android SDK which plays a video advertisement.  
This is written in Kotlin.

[![Platform](http://img.shields.io/badge/platform-android-brightgreen.svg?style=flat)](http://developer.android.com/index.html)
[![Language](http://img.shields.io/badge/language-kotlin-green.svg?style=flat)](https://kotlinlang.org)
[![License](http://img.shields.io/badge/license-apache2.0-lightgrey.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
[![Download PlayerManager](https://api.bintray.com/packages/aakira/maven/exoplayer-manager/images/download.svg)](https://bintray.com/aakira/maven/exoplayer-manager/_latestVersion)
[![Download IMA plugin](https://api.bintray.com/packages/aakira/maven/exoplayer-manager-ima/images/download.svg)](https://bintray.com/aakira/maven/exoplayer-manager-ima/_latestVersion)

## Preview

### Play a HLS video

![HLS_SAMPLE][hls_sample]

### Play a video advertisement

![IMA_SAMPLE][ima_sample] 

## Features

* Play a HLS video
* Play a video advertisement using the [IMA SDK v3](https://github.com/googleads/googleads-ima-android).
* Can limit the bitrate

## Requirements

* Kotlin 1.1.0 or above
* ExoPlayer 2.6 or above
* Android SDK version 4.1 or above (ExoPlayer requirements)

## Usage

### Play HLS

```Kotlin

// inject from xml
val simpleExoPlayerView: SimpleExoPlayerView by bindView(R.id.playerView)

val playerManager: ExoPlayerManager = ExoPlayerManager(context) 

// inject SimpleExoPlayerView
// https://github.com/google/ExoPlayer/blob/release-v2/library/src/main/java/com/google/android/exoplayer2/SimpleExoPlayer.java
playerManager.injectView(simpleExoPlayerView)

val dataSource = DataSourceCreator.UrlBuilder(
        url = HLS_SAMPLE_URL,
        userAgent = Util.getUserAgent(this, "UserAgent"),
        okHttpClient = your ok http client, // you can use your okhttp client if you want use it.
        dataSourceCreatorInterface = your data source // you can use your data source if you want use it.
)
playerManager.setHlsSource(dataSource.build())

// play
playerManager.play()

// pause
playerManager.pause()

// stop
playerManager.stop()

// release
playerManager.release()

// mute
playerManager.toMute()

// limit bitrate
playerManager.setMaxVideoBitrate((60 * 1000).toLong())

// change playback speed (speed, pitch)
playerManager.setPlaybackParameters(2f, 2f)

// state listener
playerManager.addOnStateChangedListener { playWhenReady: Boolean, playbackState: Int ->
}

// error listener
playerManager.addOnPlayerErrorListener {
}

```

```xml

<?xml version="1.0" encoding="UTF-8"?>
<RelativeLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:keepScreenOn="true">

    <com.google.android.exoplayer2.ui.SimpleExoPlayerView
        android:id="@+id/playerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:focusable="true"
        app:surface_type="texture_view"
        app:use_controller="false" />
</RelativeLayout>
```

### Play a video advertisement

```Kotlin

val adPlayerController: AdPlayerController = AdPlayerController.Builder(
        context = this,
        simpleExoPlayerView = simpleExoPlayerView,
        adUiContainer = adUiContainer,
        language = "us",
        userAgent = Util.getUserAgent(this, "UserAgent"),
        playerManager = playerManager)
        .create()

// call in Activity onResume()
adPlayerController.resume()

// call in Activity onPause()
adPlayerController.pause()

// call in Activity onDestroy()
adPlayerController.destroy()

// call in Activity detachedFromWindow()
adPlayerController.release()

```

## Setup

### Gradle

Add the dependency in your `build.gradle`

```groovy
buildscript {
	repositories {
		jcenter()
	}
}

dependencies {
	compile 'com.github.aakira:exoplayer-manager:0.0.8@aar'
	compile 'com.github.aakira:exoplayer-manager-ima:0.0.8@aar' // if you use an IMA SDK
}
```
## Using libraries

* [Exo Player r2.x](https://github.com/google/ExoPlayer)
* [googleads-ima-android(IMA Android SDK v3)](https://github.com/googleads/googleads-ima-android) (plugin)
* [kotterknife](https://github.com/JakeWharton/kotterknife) (only sample)
* [timber](https://github.com/JakeWharton/timber) (only sample)

reference : [exoplayer-textureview](https://github.com/satorufujiwara/exoplayer-textureview)

## Author

### Akira Aratani

* Twitter
 - [@_a_akira](https://twitter.com/_a_akira)
* Mail
 - developer.a.akira@gmail.com

## License

```
Copyright (C) 2017 A.Akira

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[hls_sample]: /art/hls_sample.gif
[ima_Sample]: /art/ima_sample.gif
