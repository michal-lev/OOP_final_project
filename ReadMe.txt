================================================================================
                           COLLEGE MANAGEMENT SYSTEM
================================================================================

Team Details
------------
- Michal Lev (322812421)
- Liora Grinshpul (211666557)

--------------------------------------------------------------------------------

Our Software
------------
Name: College Management System

Description: A management system for colleges that enables handling of entities such as lecturers, committees, and departments.
The system provides tools for data analysis (salary averages), comparisons between objects (lecturers and committees),
and data persistence via backup file.

--------------------------------------------------------------------------------

Features
--------
- Lecturer Management: Add lecturers and assign them to departments.
- Committee Management: Add committees, manage committee members, and update the committee chair.
- Department Management: Add new departments to the college.
- Data Analysis: Calculate salary averages at the college and department levels.
- Sorting & Display: Interactive display of lecturers and committees sorted by multiple criteria chosen at runtime using Comparator-based sorting.
- Comparisons: Compare lecturers (by number of articles) and committees (by size/articles).
- Cloning: Create a clone of an existing committee.
- Data Persistence: Save and load data to/from the collegeBackup.bin file.

--------------------------------------------------------------------------------

Architecture & SOLID Design
---------------------------
To ensure scalability, maintainability, and clean code, the system strictly adheres to SOLID principles by enforcing a strict Separation of Concerns (SoC) across its layers:

Main.java: Acts purely as the application entry point. Its only responsibility is to initialize the application lifecycle by either instantiating a new College instance or restoring a previously saved state from the backup file.

College.java: The core engine of the application. It contains all the business logic, entity management, data analysis, and state processing. To respect the Single Responsibility Principle (SRP), it operates completely independently of the user interface and contains absolutely no user interaction.

CollegeUI.java: Handles the application's presentation layer. It manages the text-based menu flow and coordinates user interactions by bridging communication between the console and the backend business logic (calling functions in College.java).

ConsoleIO.java: A dedicated utility layer for input/output operations. It contains general, reusable helper functions (like getFromUser and ShowMssgToUsr) to capture and display data, completely isolated from any application-specific logic.

--------------------------------------------------------------------------------

Instructions
------------

Compilation & Execution:
- Compile all Java files.
- Run the Main class.

Usage:
- The program runs via a text-based menu (options 0–14).
- Follow the instructions in the console.

Notes:
- Data is automatically saved to collegeBackup.bin when exiting the program.
