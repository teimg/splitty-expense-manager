# Splitty
## Description

_Splitty_ is an expense splitting calculator to be used by groups of friends. Developed by all 6 collaborators.

**Product Demo Video**: https://drive.google.com/file/d/1v8WW1kmvlGN8wucCtA3rQiQ3hAuYWuU5/view?usp=sharing 

## How to run

The server can be run through the gradle task `gradle bootRun`.

The client can be run through the gradle task `gradle run`.

## Configuration

The client can be configured in the file
`client/src/main/resources/client/config/custom.properties`
which is generated upon running the client app for the first time.

The user's email credentials and a default address for testing can be configured in the file
`client/src/main/resources/client/config/mail.properties`.

## Technology

The application contains an implementation of live updating through long polling in the method
`EventController::checkForUpdates`
along with the class
`EventChangeService`
on the server and in the class
`LongPollingEventUpdateProvider`
on the client.
This implementation is used in the client on the scenes
`EventOverview`, `StatisticsScreen` and `OpenDebts`.

The application contains an implementation of live updating through websockets in the class
`EventChangeService`
on the server and multiple methods in the class
`EventCommunicator`
on the client.
This implementation is used in the client on the scene
`AdminScreen`.

## HCI features

The client has a number of usability features implemented.

Icons are included in the scenes
`StartScreen` (for deleting a recent event),
`EventOverview` (for editing or deleting an expense) and
`OpenDebts` (to show the availability for reminder emails and bank account details).

The application has keyboard shortcuts in most scenes.
The shortcut guide is included in this `README` as an appendix
but can also be downloaded from the client application through the settings menu.

The client features popups for confirmation or to show an error or information message, or to give feedback for an action.

## Functionality

The client application begins on startup in the `StartScreen`.
From here, you can visit recent events,
create a new event with a title of choice
or join an event using an invite code.

All of these options lead to the `EventOverview`.
From here, you can rename an event,
send email invites to people you'd like to add,
manage the event's participants
and register, edit or delete an expense.
You can also view your event's statistics
and an overview of the open debts in your event.

To edit or remove a participant,
you need to select the participant in the dropdown and then press the 'Edit' or 'Remove' button.
To filter expenses by a participant,
you first need to select the participant in the dropdown
and then press one of the sorting options 'All', 'From' or 'Including'.

In the `OpenDebts` screen you can mark debts as paid,
send a reminder email if the debtors have their email addresses configured,
view the payer's bank details if they are configured,
and see an overview of all open balances.

The reminder email is sent from the email address you have configured in the email configuration file,
and it is CC'd to the default testing address, also configured there.
If you want to receive a copy of all emails sent from your address through the application,
you can configure the default address to be your own address.
You can send a default email to test whether your credentials are working from the settings screen.
This email is sent to the configured default address, and CC'd to your own address.

The client also features an administrative overview.
The `AdminScreen` can be accessed through the settings menu.
Upon entering the `AdminScreen` for the first time, a password is required.
This password is randomly generated and printed to the console when starting the server.
Here you can find an overview of all registered events,
delete events,
and import and export a JSON dump of an event.
Note that you can only import an event after you have deleted it,
otherwise the event already exists.

## Appendix: Shortcut guide

Below are the KeyBoard guide for all the scenes: (Admin screens do not support any shortcuts - Use ENTER to exit Popups on recommended value). For drop downs, use ALT + ARROWDOWN to open them and then navigate through them using the arrows (press enter to select the currently selected item)

**Start Screen:**
They all follow from CTRL + "CHARACTER"
-  C: Create event
- J: Join Event
- E: Enter event currently selected on the recently viewed events list. You can select these by continuously pressed TAB allowing the small blue box to outline the event name that you want to enter.

**Event Overview:**
They all follow from CTRL + "CHARACTER"
-  A: Add expense
- B: Go Back
- O: Open Debts Screen
- S: Statistics Screen
- P: Add Participant
- I: Go to invites screen
- E: Enter expense currently selected on the expense list. You can select these by continuously pressed TAB allowing the small blue box to outline the expense EDIT button that you want to edit.

**Add/Edit Expense:**
They all follow from CTRL + "CHARACTER"
- B: Go Back
- ENTER: Submit creation of expense

**Contact Info (Participant add screen):**
They all follow from CTRL + "CHARACTER"
- B: Go Back
- ENTER: Submit creation of participant

**Invitation Screen:**
They all follow from CTRL + "CHARACTER"
- B: Go Back
- ENTER: Submit send emails

**Open Debts:**
They all follow from CTRL + "CHARACTER"
- B: Go Back

**Statistics:**
They all follow from CTRL + "CHARACTER"
- B: Go Back

**Tag Screen:**
They all follow from CTRL + "CHARACTER"
- B: Go Back
- ENTER: Submit creation of tag

