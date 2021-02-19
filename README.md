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
  implementation 'com.github.AgnaldoNP:AGSkeletonLoading:1.2'
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

### Available Skeleton loading views
 - [SkeletonRecyclerView](https://github.com/AgnaldoNP/AGSkeletonLoading#skeletonlistview-and-skeletonrecyclerview)
 - [SkeletonListView](https://github.com/AgnaldoNP/AGSkeletonLoading#skeletonlistview-and-skeletonrecyclerview)
 - SkeletonLinearLayout
 - SkeletonRelativeLayout
 - SkeletonFrameLayout
 - SkeletonGridLayout
 - SkeletonConstraintLayout
 - SkeletonTextView
 - SkeletonButton
 - SkeletonImageView

### Options
| Property                   |               Value / Type              |   Default   |
|----------------------------|:---------------------------------------:|:-----------:|
| autoStart                  |                 boolean                 |     true    |
| enableDevelopPreview       |                 boolean                 |     true    |
| splitSkeletonTextByLines   |                 boolean                 |     true    |
| clipToText                 |                 drawable                |     true    |
| skeletonColor              |                  color                  |   #E6E6E6   |
| shimmerStrokeWidth         |                dimension                |     30dp    |
| shimmerBlurWidth           |                dimension                |     25dp    |
| shimmerLightenFactor       |                  floar                  |     0.2     |
| skeletonCornerRadius       |                dimension                |     5dp     |
| duration                   | enum - shortCycle mediumCycle longCycle | mediumCycle |
| customDuration             |                 integer                 |      -      |
| skeletonViewHolderItem     |                 reference               |      -      |
| skeletonViewHolderAmount   |                 integer                 |      -      |
| skeletonViewHolderTruncate |                 boolean                 |    false    |
>All these properties can be referenced in any skeleton layout but can make no effect depending on layout used

### Commom properties
The Properties bellow has effect for all skeleton views loading
 - **autoStart**
   - Define if the layout automatic start loading as soon as it is inflated
 - **enableDevelopPreview**
   - Define if it shold be shown on Layout Editor while developing. This can not work pretty well with skeleton ViewGroups
 - **splitSkeletonTextByLines**
   - If the layout or some inner layout is an EditText, it defines if the skeleton considers each line to be drawn or if its drawn as only one rectangle.
   > If true, lineSpacingExtra should be setted to separate the rectangles.

   ```xml
    <aglibs.loading.skeleton.view.SkeletonTextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginBottom="20dp"
        android:lineSpacingExtra="4dp"
        android:text="Lorem Ipsum is simply dummy text of the printing and typesetting industry."
        app:splitSkeletonTextByLines="true" />

    <aglibs.loading.skeleton.view.SkeletonTextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:lineSpacingExtra="4dp"
        android:text="Lorem Ipsum is simply dummy text of the printing and typesetting industry."
        app:splitSkeletonTextByLines="false" />
    ```
   ![GIF](https://github.com/AgnaldoNP/AGSkeletonLoading/blob/master/screenshots/sample02.gif?raw=true)

 - **clipToText**
   - If the layout or some inner layout is an EditText, it defines if the skeleton should be drawn restricted to text and not to the entire layout
   ```xml
   <aglibs.loading.skeleton.view.SkeletonTextView
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:layout_marginBottom="20dp"
       android:lineSpacingExtra="4dp"
       android:text="Lorem Ipsum is simply dummy text of the printing and typesetting industry."
       app:clipToText="true" />


   <aglibs.loading.skeleton.view.SkeletonTextView
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:lineSpacingExtra="4dp"
       android:text="Lorem Ipsum is simply dummy text of the printing and typesetting industry."
       app:clipToText="false" />
   ```
   ![GIF](https://github.com/AgnaldoNP/AGSkeletonLoading/blob/master/screenshots/sample03.gif?raw=true)

 - **skeletonColor**
   - The color used to draw the skeleton rectangles
   ```xml
   <aglibs.loading.skeleton.view.SkeletonTextView
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:layout_marginBottom="20dp"
       android:lineSpacingExtra="4dp"
       android:text="Lorem Ipsum is simply dummy text of the printing and typesetting industry."
       app:skeletonColor="#FF9696" />


   <aglibs.loading.skeleton.view.SkeletonTextView
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:lineSpacingExtra="4dp"
       android:text="Lorem Ipsum is simply dummy text of the printing and typesetting industry."
       app:skeletonColor="#96E7FF"/>
   ```
   ![GIF](https://github.com/AgnaldoNP/AGSkeletonLoading/blob/master/screenshots/sample04.gif?raw=true)

 - **shimmerStrokeWidth**
   - Width of the shimmer effect. It works together with _shimmerBlurWidth_
   ```xml
   <aglibs.loading.skeleton.view.SkeletonTextView
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:lineSpacingExtra="4dp"
       android:text="Lorem Ipsum is simply dummy text of the printing and typesetting industry."
       app:shimmerStrokeWidth="100dp" />
   ```
   ![GIF](https://github.com/AgnaldoNP/AGSkeletonLoading/blob/master/screenshots/sample05.gif?raw=true)

 - **shimmerBlurWidth**
   - Width of the blur effect used as mask on _shimmerStrokeWidth_
   ```xml
    <aglibs.loading.skeleton.view.SkeletonTextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:lineSpacingExtra="4dp"
        android:text="Lorem Ipsum is simply dummy text of the printing and typesetting industry."
        app:shimmerBlurWidth="10dp" />
   ```
   ![GIF](https://github.com/AgnaldoNP/AGSkeletonLoading/blob/master/screenshots/sample06.gif?raw=true)

 - **shimmerLightenFactor**
   - Lighten factor applied on _skeletonColor_ to be used on shimmer effect, tipically it is from 0 to 1 depending on the _skeletonColor_
   ```xml
   <aglibs.loading.skeleton.view.SkeletonTextView
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:layout_marginBottom="20dp"
       android:lineSpacingExtra="4dp"
       android:text="Lorem Ipsum is simply dummy text of the printing and typesetting industry."
       app:shimmerLightenFactor="0.9"
       app:skeletonColor="#F00" />

   <aglibs.loading.skeleton.view.SkeletonTextView
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:lineSpacingExtra="4dp"
       android:text="Lorem Ipsum is simply dummy text of the printing and typesetting industry."
       app:shimmerLightenFactor="0.2"
       app:skeletonColor="#00F" />
   ```
   ![GIF](https://github.com/AgnaldoNP/AGSkeletonLoading/blob/master/screenshots/sample07.gif?raw=true)

 - **skeletonCornerRadius**
   - Size of the skeleton rectangle corners radius
   ```xml
    <aglibs.loading.skeleton.view.SkeletonTextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginBottom="20dp"
        android:lineSpacingExtra="4dp"
        android:text="Lorem Ipsum is simply dummy text of the printing and typesetting industry."
        app:skeletonCornerRadius="0dp" />

    <aglibs.loading.skeleton.view.SkeletonTextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Lorem Ipsum is simply dummy text of the printing and typesetting industry."
        app:splitSkeletonTextByLines="false"
        app:skeletonCornerRadius="20dp" />
   ```
   ![GIF](https://github.com/AgnaldoNP/AGSkeletonLoading/blob/master/screenshots/sample08.gif?raw=true)

 - **duration**
   - Time to shimmer effect be applied from the start of layout to end
 - **customDuration**
   - Customized _duration_
   ```xml
   <aglibs.loading.skeleton.view.SkeletonTextView
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:layout_marginBottom="20dp"
       android:lineSpacingExtra="4dp"
       android:text="Lorem Ipsum is simply dummy text of the printing and typesetting industry."
       app:duration="shortCycle" />

   <aglibs.loading.skeleton.view.SkeletonTextView
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:layout_marginBottom="20dp"
       android:lineSpacingExtra="4dp"
       android:text="Lorem Ipsum is simply dummy text of the printing and typesetting industry."
       app:customDuration="4000" />
   ```
   ![GIF](https://github.com/AgnaldoNP/AGSkeletonLoading/blob/master/screenshots/sample09.gif?raw=true)


### SkeletonListView and SkeletonRecyclerView
If you want to build a list using ListView or RecyclerView with a skeleton loading you need to create a layout that represents the item list.
Once the layout is created need to reference it by adding *skeletonViewHolderItem* property, as you can see int the snippet bellow.

#### *list_item.xml*
```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:paddingStart="2dp"
    android:paddingTop="10dp"
    android:paddingEnd="2dp"
    android:paddingBottom="10dp"
    tools:context=".MainActivity">

    <ImageView
        android:id="@+id/img017"
        android:layout_width="50dp"
        android:layout_height="50dp"
        android:src="@drawable/ic_email" />

    <TextView
        android:id="@+id/text016"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentEnd="true"
        android:layout_marginStart="8dp"
        android:layout_marginTop="2dp"
        android:layout_toEndOf="@+id/img017"
        android:text="Lorem Ipsum is simply dummy text" />

    <TextView
        android:id="@+id/text015"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_below="@+id/text016"
        android:layout_marginStart="8dp"
        android:layout_marginTop="8dp"
        android:layout_toEndOf="@+id/img017"
        android:text="Lorem Ipsum Text" />

</RelativeLayout>
```
> It's important that the layout has some content to be possible calculate the rects the represents the skeletons

#### Usage
```xml
<aglibs.loading.skeleton.layout.SkeletonListView
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:skeletonViewHolderItem="@layout/item_list"/>

<aglibs.loading.skeleton.layout.SkeletonRecyclerView
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:skeletonViewHolderItem="@layout/item_list"/>
```
![GIF](https://github.com/AgnaldoNP/AGSkeletonLoading/blob/master/screenshots/sample10.gif?raw=true)

#### SkeletonListView and SkeletonRecyclerView properties
 - **skeletonViewHolderItem**
   - Reference to layout to be used as view holder to skeleton
 - **skeletonViewHolderAmount**
   - If setted, is the number of skeletons to be drawn, otherwise it will be calculated at runtime
 - **skeletonViewHolderTruncate**
   - If true and _skeletonViewHolderAmount_ is not setted this property defines if the last item can be incomplete drawn


## Contributions and Support
Contributions are welcome. Create a new pull request in order to submit your fixes and they shall be merged after moderation. In case of any issues, bugs or any suggestions, either create a new issue or post comments in already active relevant issues

## Please consider supporting me
Bitcoin URI: bitcoin:BC1Q4RT2KNSX28CA4H5YA08VF0SXMG3JPHKS6GWDXV?label=Consider%20support%20me

Bitcoin Address: bc1q4rt2knsx28ca4h5ya08vf0sxmg3jphks6gwdxv



