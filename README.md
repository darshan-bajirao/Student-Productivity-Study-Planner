# Student-Productivity-Study-Planner
A Java-based desktop application designed to help students organize their academic activities, manage daily tasks, plan study sessions, maintain notes, set reminders, and monitor their overall productivity through a centralized workspace.

Objectives
The main objectives of this project are:
- To develop a desktop-based student productivity application using Java.
- To provide a centralized platform for managing academic activities.
- To allow students to create and manage subjects.
- To create, update, and complete academic tasks.
- To organize study sessions and study plans.

Main Features
1. User Registration and Login
The application provides a registration and login system for users.
Users can create an account and access their personal workspace after successful authentication.
The application maintains user-specific information and provides a separate workspace for managing academic activities.
2. Dashboard
The dashboard provides an overview of the student's current academic activities.
It can display information such as:
- Total subjects
- Total tasks
- Completed tasks
- Pending tasks
- Study hours
- Productivity percentage
- Upcoming academic activities
This allows students to quickly understand their current progress.
3. Subject Management
The Subjects module allows students to manage the subjects they are studying.
Students can add subjects and use them while creating tasks and study sessions.
This provides better organization because academic activities can be associated with specific subjects.
4. Task Management
The Tasks module allows students to create and manage academic tasks.
Tasks can be used for activities such as:
- Assignments
- Homework
- Revision
- Practical work
- Project work
- Examination preparation
The system also provides task completion tracking so that students can distinguish between completed and pending work.
5. Study Planner
The Study Planner helps students organize dedicated study sessions.
The application includes a timer-based study mechanism that can be used for focused study sessions.
A Pomodoro-style study duration of approximately 25 minutes is used to encourage focused study periods.
6. Notes
The Notes module allows students to maintain important information related to their studies.
Students can create and manage notes instead of keeping academic information scattered across different applications.
7. Reminders
The Reminders module helps students identify important upcoming and overdue tasks.
The system can analyze task deadlines and display activities that require attention.
This helps students avoid missing important academic work.
8. Productivity Statistics
The Statistics module provides information about the student's productivity.
The application calculates productivity based on completed and total tasks.
The basic productivity calculation used is:
Productivity (%) = (Completed Tasks / Total Tasks) × 100
The system can also track study time based on recorded study sessions.
9. Local Data Storage
The application stores its information locally rather than requiring an online server.
Java object serialization and file handling are used for storing and retrieving application data.
The project uses a local data file for persistent storage.
