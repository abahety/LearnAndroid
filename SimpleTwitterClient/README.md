# Project 2 - *Simple Twitter Client*

**Simple Twitter Client** is an android app that allows a user to view his Twitter timeline and post a new tweet. The app utilizes [Twitter REST API](https://dev.twitter.com/rest/public).

Time spent: **10** hours spent in total (so far..still working)

## User Stories

The following **required** functionality is completed:

* [x] User can **sign in to Twitter** using OAuth login
* [x] User can **view tweets from their home timeline**
  * [x] User is displayed the username, name, and body for each tweet
  * [x] User is displayed the [relative timestamp](https://gist.github.com/nesquena/f786232f5ef72f6e10a7) for each tweet "8m", "7h"
  * [x] User can view more tweets as they scroll with [infinite pagination](http://guides.codepath.com/android/Endless-Scrolling-with-AdapterViews-and-RecyclerView). Number of tweets is unlimited.
    However there are [Twitter Api Rate Limits](https://dev.twitter.com/rest/public/rate-limiting) in place.
* [x] User can **compose and post a new tweet**
  * [x] User can click a “Compose” icon in the Action Bar on the top right
  * [x] User can then enter a new tweet and post this to twitter
  * [x] User is taken back to home timeline with **new tweet visible** in timeline

The following **optional** features are implemented:

* [x] User can **see a counter with total number of characters left for tweet** on compose tweet page
* [x] User can **click a link within a tweet body** on tweet details view. The click will launch the web browser with relevant page opened.
* [x] User can **pull down to refresh tweets timeline**
* [ ] User can **open the twitter app offline and see last loaded tweets**. Persisted in SQLite tweets are refreshed on every application launch. While "live data" is displayed when app can get it from Twitter API, it is also saved for use in offline mode.
* [ ] User can tap a tweet to **open a detailed tweet view**
* [ ] User can **select "reply" from detail view to respond to a tweet**
* [x] Improve the user interface and theme the app to feel "twitter branded"

The following **bonus** features are implemented:

* [ ] User can see embedded image media within the tweet detail view
* [ ] Compose tweet functionality is build using modal overlay

The following **additional** features are implemented:

* [x] List anything else that you can get done to improve the app functionality!
  * User can see soft input keyboard open directly when he tries to compose a tweet with focus to edit text view
  * User is taken back to timeline with hiding of keyboard once tweet is successfully posted
  * User cannot click the tweet button untill he has some data entered into edit text field
  * Added media view (photo only as of now) in the timeline activity itself


## Video Walkthrough 

Here's a walkthrough of implemented user stories:

<img src='https://github.com/abahety/LearnAndroid/blob/master/SimpleTwitterClient/Demo.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

Media view inside timeline demo:

<img src='https://github.com/abahety/LearnAndroid/blob/master/SimpleTwitterClient/DemoWithMedia.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

Describe any challenges encountered while building the app.
* Implement infinite pagination
* getting better UI

## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android
- [android-rest-client-template](https://github.com/codepath/android-rest-client-template) - Simple skeleton to work with OAuth api against a identity provider


## License

    Copyright [2015] [abahety]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.