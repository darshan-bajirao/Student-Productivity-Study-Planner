# Student Productivity & Study Planner

A polished Java Swing desktop application for a college Java mini-project.

## Tech stack
- Java 17+ (JDK 21 recommended)
- Java Swing / AWT
- Java Collections
- SHA-256 password hashing for local demo accounts
- Java Serialization for local persistence
- Swing Timer / ScheduledExecutorService for animations and Pomodoro

## No external libraries
There is no Maven, Gradle, MySQL, JDBC driver, or internet dependency.

## Main features
- Modern dark tech UI
- Animated login background
- Login / Register / Logout
- User-specific local workspace
- Dashboard
- Subjects CRUD
- Tasks CRUD + status + priority + deadlines
- Weekly study planner
- 25-minute Pomodoro focus timer
- Notes CRUD
- Productivity dashboard and subject-wise chart
- Deadline reminders
- Automatic local saving

## Run
### VS Code
1. Install JDK 21.
2. Install VS Code and Extension Pack for Java.
3. Open this project folder (the folder containing `src`).
4. Open `src/Main.java`.
5. Click Run.

### Windows
Double-click `RUN_WINDOWS.bat`.

Data is stored in `data/student_planner.dat`.
