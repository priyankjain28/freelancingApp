# FreeLancing App - MVVM, Firebase-Storage & Messaging, Databinding, Google Map
A prototype application for marketplace which connects freelance labor (service experts) with consumers to do tasks.
consumer makes a request for cleaning; cleaner gets a request and accepts or rejects it.
 
## Features
* Sign in / Sign up (w/ email) for users. 
* 2 different modes: Worker Mode and Consumer Mode. 
* Worker Mode
    * Sees the list of all incoming tasks (inbox)
    * Gets the notification about the task. For every task he can accept or reject it.
    * Once the task is complete, I can mark it as a complete. 
    * Once the task is complete, worker can rate the consumer on scale from 1 to 5.
    * List of all my tasks: history of completed tasks, tasks which I accepted, but haven’t completed it 
* Consumer Mode
    * Create a task. Each task should have name, description, category (from the list of categories), location (taken automatically), current date & time (automatic). 
    * List of my tasks: tasks of completed tasks requested by me 
    * Once the task is completed by the worker, I can rate him on scale from 1 to 5.
    
    
## Technical Feature
This repository contains a freelancing app that implements MVVM architecture using Databinding, Firebase Database, Push Notification, LiveData, Google Maps, Glide

## The app has following packages:
* data: It contains all the data accessing and manipulating components.
* ui: View classes along with their corresponding ViewModel.
* init: Initialize instances and maintain user session
* util: Utility classes.

##### Classes have been designed in such a way that it could be inherited and maximize the code reuse.

## Library Reference:
* JetPack Architecture 
* Firebase - Storage & Messaging
* Google Maps
* Glide

## Special Feature
* Splash Screen - Handled User Session
* Login/Signup  - Used TextInputLayout to more interactive also handled validation
* User Screen 
    * used Map to locate worker nearby user, 
    * create task which contain following information like task title, type, location, date time
    * user can viewed the create task in the bottom of screen using horizontal recycler view.
    * it can be deleted and also give the rating only in case of completed/rated

* Worker Screen 
    * tasklist shown on the screen sorted by distance by default
    * task lifecycle - Open | Accepted/Rejected | In Progress | Completed | Rating
    * rating card open only in case of task complete.
    * using static google map api to denote task location on map
    
* Logout        - Logout with the user session

