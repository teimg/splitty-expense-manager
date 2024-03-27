| Key          | Value                                                                                    |
|--------------|------------------------------------------------------------------------------------------|
| Date:        | 26/03/2024                                                                               |
| Time:        | 15:45-16:45                                                                              |
| Location:    | Drebbelweg cubicle 14                                                                    |
| Chair        | Feiyang Liu                                                                              |
| Minute Taker | Jona Bedaux                                                                             |
| Attendees:   | Teim Giesen, Nick de Boer, Defne Kösecioğlu, Feiyang Liu, Jona Bedaux and Makar Kuleshov |

**Agenda Items**:

- Opening by chair (1 min)

- Check-in: How is everyone doing? (1 min)

Everyone is doing well!

- Announcements by the team (? min)

No announcements.

- Summarization/Review of Agenda (1 min)

- Approval of the agenda - Does anyone have any additions? (1 min)

Agenda is approved as is.

- Approval of last minutes - Did everyone read the minutes from the previous meeting? (1 min)

A minor mistake was caught in last weeks planning, but good aside from that.

- Announcements by the TA (? min)

Thursday evening is the deadline for the product pitch (formative).
Monday is the deadline for the implemented features feedback (formative).

- Presentation of the current app to TA  (2 min)

Improvements over last week:
- recent events
- invite people screen is hooked up, except email functionality
- progress for the settle debts screen
- admin screen with websocket powered synchronization
- implementation of model-view architecture
- progress on long polling
- LLS template download


- **Agenda - item 1**: Review of previous meetings action points (2 min)

Last weeks action points:

Client side testing: Nick
- implemented model-view architecture for some scenes
- implemented scenes 100% tested

Navigation and recent events: Jona
- recent events done
- stuck on expense editing for quite long
- Nick possibly has a fix for latest bug

Admin page and websockets: Teim, Makar
- admin page login
- implemented statistics screen backend
- websockets mostly working but issues with database cause instability

Long polling for event overview on front end: Feiyang
- the basics for long polling are implemented
- not fully working yet

Email notifications: Defne
- progress but possibly not fully tested
- email not fully done yet


- **Agenda - item 2**: Discussion on last week's work - Feedback: what went well and what didn't go well? (10 min)

See last point.

- **Agenda - item 3**: Our next steps: what functionality should we focus on next? (5 min)

Testing percentage from rubric is percentage shown on GitLab.
80% is only to get from 'good' to 'excellent', maybe inefficient to focus too much on that.

Long polling and client testing continues.
Edit expense issue carries over.
Admin page will continue work.

Open debt implemented incorrectly, will fix this week.
Passing commons through scenes causes desyncs. Maybe work only with ID's?

Changes need to be visible, it is not needed to sync absolutely everything.

Long polling broken for unknown reason.
Long polling on a per scene basis or transcending scenes?

- **Agenda - item 4**: Division of tasks (10 min)

Nick will take some server side issues.
Backend testing
Admin page
Sorting
JSON dump: client _probably_ needs to be the same on import/export.
Nick will take admin page password.
Database does not save all changes.

Code example by Makar:
The database seems to save incorrectly interlinked objects without problems.
When adding a new Participant, the event is updated correctly
BUT the participant in question still has a reference to the outdated event as well.

no known fix for now...

General deadline on tuesday afternoon.

- Summarize action points: Who, what, when? (3 min)

	- Nick: admin password and frontend testing + database fix
	- Makar: websockets and frontend testing + database fix
    - Jona: slides or something for product pitch + database fix and issues when they arise 
    - Defne: model-view and testing
    - Feiyang: create product pitch google slides + popups and testing and long polling 
    - Teim: email functionality and issues when they arise

- Feedback round: What went well and what can be improved next time? (2 min)

Tasks were divided well.
Meeting content was important.
Well managed meeting.

- Planned meeting duration != actual duration? Where/why did you mis-estimate? (? min)

Meeting was roughly within time.

- Question round: Does anyone have anything to add before the meeting closes? (1 min)

No questions.

- Closure (1 min)

Meeting closed.