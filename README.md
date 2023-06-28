
# Id Verifier

Android application designed as a virtual ID Verifier specifi- cally tailored for hostel identification. This application harnesses the power of an android application to provide hostels with a secure and efficient solution for managing resident identification and enhancing overall security measures.

This emphasizes the importance of robust identification systems in hostel environments to ensure the safety and well-being of residents. It highlights the limitations of traditional paper-based identification methods and introduces the virtual ID Verifier as a modern alternative that optimizes the hostel management process.

## Features

- QR Based Authentication
- Exit Permits
- Faster and Safer Authentication

## Workflow

The system follows the following workflow:

1. The Admin registers a new student, generates a unique QR code, and stores the student details and QR code in the database.
2. Students present their QR codes to Security personnel.
3. The Security personnel scan and validate the QR codes.
4. If the QR code is valid, the system retrieves the student details from the database.
5. If the student is found in the database, access is granted, and the entry/exit time is recorded.
6. If the QR code or student details are invalid or not found, an error message is displayed.

## Requirements

- Java Development Kit (JDK)
- Android Studio
- Android SDK
- Firebase (Backend As a Service)

## Installation

1. Clone or download the project from the repository.
2. Open the project in Android Studio.
3. Build and run the project on an Android emulator or physical device.

## Configuration

- Update the database connection settings in the project's configuration file to match your database credentials and configuration.
