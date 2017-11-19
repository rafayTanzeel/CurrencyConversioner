# CurrencyConversioner

## Purpose

App allows you to find all kind of currency exchange rates

## Implementation
* To prevent main thread from halting, most intensive actions(web request or database querying) are performed by an intent service which may spawn its own additional threads
* To avoid data loss due to configuration changes, SaveInstanceState bundle and realm database are used to store the values persistently
* The retrofit api is used to send a get request and parse it with gson
* The realm recycler view adapter is used to show the items as Grid on a recycler view for additional performance
* TextWacther is used to detect any changes in edit text and dynamically makes changes only on the viewable items of the grid on the screen, instead of changing every item on the entire grid, for additional performance. Recycler View provided the functionality to add these changes to the hidden items as they become viewable through scroll.
* The change is only performed on the UI, without any changes to database or resending webrequest.
* Web Request are only sent every 30 mintues by a intent service
* Intent service using pending intent with Alarm Manager to send itself an new intent every 30 minutes