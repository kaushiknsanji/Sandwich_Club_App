# Sandwich Club Project

## Project Overview
In this project, you will complete the **Sandwich Club** app to
show the details of each sandwich once it is selected.

## Why this Project

Building a layout and populating its fields from data received as JSON
is a common task for Android Developers. Although JSON parsing is usually
done using libraries, writing the JSON parsing for  this project will
help you to better understand how it is processed.

## What Will I Learn?
Through this project, you will:
- Learn how to submit projects for review
- Practice JSON parsing to a model object
- Design an activity layout
- Populate all fields in the layout accordingly

## How Do I Complete this Project?
Download the [Sandwich Club app starter code.](https://github.com/udacity/sandwich-club-starter-code)

Design the layout for the detail activity so the different elements
display in a sensible way. Implement the JSON parsing in JsonUtils so it
produces a Sandwich Object that can be used to populate the UI that you designed.

## Implementation of the App

<!-- Video of the App -->
[![Video of Complete App Flow](https://i.ytimg.com/vi/b-BL7mM0tKM/maxresdefault.jpg)](https://youtu.be/b-BL7mM0tKM)

### Sample Screenshots

|Main Screen|Detail Screen|Detail Screen (Collapsed)|
|---|---|---|
|![main_screen](https://user-images.githubusercontent.com/26028981/41117978-24c8d334-6aac-11e8-9c14-66f10c9577e2.png)|![detail_screen](https://user-images.githubusercontent.com/26028981/41117980-27c3a1c2-6aac-11e8-9640-6669f11f76fd.png)|![detail_screen_collapsed](https://user-images.githubusercontent.com/26028981/41117984-2b27bed4-6aac-11e8-9fe9-bf0e18019eaa.png)|

### Things explored/developed in addition to the above defined Rubric

* Used [CoordinatorLayout](/app/src/main/res/layout/activity_detail.xml) for the [DetailActivity](/app/src/main/java/com/udacity/sandwichclub/DetailActivity.java), with Collapsible Toolbar for the Backdrop Image in Parallax.
* Applied Activity Transitions for [Enter](/app/src/main/res/transition-v21/detail_slide_enter.xml), Exit and Return.
* Added Coordinated Motion to the List items shown in the [layout](/app/src/main/res/layout/activity_main.xml) inflated by [MainActivity](/app/src/main/java/com/udacity/sandwichclub/MainActivity.java).
* Explored Palette API which is used to decorate the layout shown by DetailActivity.

### Information in general, on the entire app

* For the Shawarma item, which is having an image link that does not exist, a Sandwich [image](/app/src/main/res/drawable/detail_error_image.xml) will be displayed in such cases.
* Missing information such as no **"Also known as"** or **"Place of origin"** has been handled suitably, which is either displayed saying **"Unknown"** or totally hidden from the UI.

## Review from the Reviewer (Udacity)

![review](https://user-images.githubusercontent.com/26028981/41118001-38b689ae-6aac-11e8-9ff9-f3b51d92bc3d.PNG)
