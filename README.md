# AGSkeletonLoading

## Introduction
*AG Skeleton Loading* is a library to provide a easy way to include skeleton loading.

![GIF](https://github.com/AgnaldoNP/AGSkeletonLoading/blob/master/screenshots/sample01.gif?raw=true)

## Install

**Step 1**. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:
```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
**Step 2.** Add the dependency
```
dependencies {
  implementation 'com.github.AgnaldoNP:AGSkeletonLoading:1.0'
}
```
[![](https://jitpack.io/v/AgnaldoNP/AGSkeletonLoading.svg)](https://jitpack.io/#AgnaldoNP/AGSkeletonLoading)


## Usage
You can use skeleton loading by replace a commom view or view group layout for it correspondent on this library.
Below is an example by using a SkeletonRelativeLayout

![GIF](https://github.com/AgnaldoNP/AGSkeletonLoading/blob/master/screenshots/sample01.gif?raw=true)

```xml
<aglibs.loading.skeleton.layout.SkeletonRelativeLayout
    android:id="@+id/skeletonLayout"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginBottom="20dp"
    app:clipToText="false">

    <ImageView
        android:id="@+id/img001"
        android:layout_width="50dp"
        android:layout_height="50dp"
        android:src="@drawable/ic_email" />

    <TextView
        android:id="@+id/text001"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentEnd="true"
        android:layout_marginStart="8dp"
        android:layout_marginTop="2dp"
        android:layout_toEndOf="@+id/img001"
        android:text="Lorem Ipsum is simply dummy text" />

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="8dp"
        android:layout_marginTop="8dp"
        android:layout_below="@+id/text001"
        android:layout_toEndOf="@+id/img001"
        android:text="Lorem Ipsum Text" />

</aglibs.loading.skeleton.layout.SkeletonRelativeLayout>
```

Any skeleton layout, View or ViewGroup, provided by this library has the following methods to start loading, stop loading or check loading status
```
skeletonLayout.isLoading()
skeletonLayout.startLoading()
skeletonLayout.stopLoading()
```

### Skeleton Available loading views
 - SkeletonLinearLayout
 - SkeletonRelativeLayout
 - SkeletonFrameLayout
 - SkeletonGridLayout
 - SkeletonConstraintLayout
 - SkeletonTextView
 - SkeletonButton
 - SkeletonImageView

### Options
| Property                 |               Value / Type              |   Default   |
|--------------------------|:---------------------------------------:|:-----------:|
| autoStart                |                 boolean                 |     true    |
| enableDevelopPreview     |                 boolean                 |     true    |
| splitSkeletonTextByLines |                 boolean                 |     true    |
| clipToText               |                 drawable                |     true    |
| skeletonColor            |                  color                  |   #E6E6E6   |
| shimmerStrokeWidth       |                dimension                |     30dp    |
| shimmerBlurWidth         |                dimension                |     25dp    |
| shimmerLightenFactor     |                  floar                  |     0.2     |
| skeletonCornerRadius     |                dimension                |     5dp     |
| duration                 | enum - shortCycle mediumCycle longCycle | mediumCycle |
| customDuration           |                 integer                 |      -      |

These configs also can be changes at runtime by calling their set methods

## Contributions and Support
Contributions are welcome. Create a new pull request in order to submit your fixes and they shall be merged after moderation. In case of any issues, bugs or any suggestions, either create a new issue or post comments in already active relevant issues