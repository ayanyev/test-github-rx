###### Introduction

This is a playground application. No serious UI design involved.
Used during hiring/interview processes.

### Hint for testers

1. Database is cleared each time app is started
2. In order to avoid GitHub API restriction for non-authorized requests, please,
   add your GitHub PAT token into :feature-repositories build.gradle file
```groovy
buildConfigField("String", "TOKEN", "\"<token goes here>\"")
```

### Requirements

###### Task 1 - Connect to the Github API

Connect to the Github API to retrieve the list of public repositories in <user> Github Account:
<https://api.github.com/users/<user>/repos>
This results in a list of public Repositories. Visualize the results in a list. You are free to
choose any meaningful subset of data to show in each row.

###### Task 2 - Create a detail page for the repository

Upon clicking an item on the repository list, redirect the user to a detail page. Retrieve
information about all the commits in the selected repository. Then render any relevant data into a
simple detail page. This can be done with the following call:
<https://api.github.com/repos/<user>/<repository>/commits>
Feel free to choose any meaningful subset of data to display.

###### Task 3 - Create a custom view to display commits in a month

On the detail page, a custom view with the following requirements should be shown.

- At the base of the view should be a text denoting the month.
- It should contain a rectangle, akin to the bar in a bar chart. The height of the bar should be in
  proportion to the number of commits in the given month vs the maximum commits in a month for that
  repository.
- Another text, aligned with the top of the bar should show the number of commits in that month. It
  should look similar to this. Feel free to choose your own color scheme and make your own design
  choices.

###### Task 4 - Cycle through all the months and trigger updates on the custom view.

While the user is on the detail page, the custom view needs to update continuously and should be
cycling through all the months. Create a mechanism that can update the custom view at an interval of
1.5 seconds. At each new interval, the height of the bar, the month name text and the number of
commits text should be updated. The user should be free to navigate back to the repository list and
open up a different repository’s detail page.

###### Bonus

Animate the height changes in the bar with each new update.

### Implementation description

###### Used libraries
* RxJava 3 + RxAndroid + RxKotlin
* Retrofit 2 + OkHttp + Moshi
* Room
* Hilt 
* Jetpack libraries (viewModel, fragment-ktx, constraintLayout)
###### Used solutions
* application is modularized feature-wise. Adding new feature module might lead to creation of
a common module for reusable code.
* dependency management done with the help of [gradle platform module](https://docs.gradle.org/current/userguide/java_platform_plugin.html)
* MVVM
* data-binding
* dependency injection
* clean architecture (domain/data/ui separation within feature module)
* custom chart view and list creation was done with the help of data-binding without subclassing View/ViewGroup
* shared view model used instead of passing repository object as fragment parameter
###### Not done but "must have" in real app
* proper exception handling
* pagination for repositories list
* proper navigation (usage of Navigation component)
* create base classes for view models, fragments.. Extract them into :common module
