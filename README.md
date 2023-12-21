# ENSF480-Term-Project 
## System Description:
System Initialization: Upon initialization, the Flight Reservation Application presents a Graphical User Interface (GUI) that serves as the entry point to the system, displaying the home page with navigation options.

Flight Reservation Process: The application allows all users to peruse the flight database, which is dynamically populated from the system's backend. However, to proceed with flight reservation, user authentication is required. This involves a registration process where users create a unique account tied to their personal information. Post-registration, users can authenticate themselves via the login module. Upon successful authentication, users can reserve a flight by selecting from the available options, choosing their preferred seat category (ordinary, comfort, or business-class), providing payment details, and finally confirming the booking. The system then generates a digital receipt, which is dispatched to the user's registered email address.

Registered User Interactions: Existing users, upon authentication, can access their personal dashboard which provides a consolidated view of their flight bookings. Additional features available to registered users include membership benefits and the option to apply for the airline's credit card.

Flight Attendant Interface: Flight attendants, after logging into their dedicated portal, can input necessary details, view their flight schedules, and access passenger manifests for their assigned flights.

Tourism Agent Access: Tourism agents, upon successful login, can perform actions similar to registered users. They can input required details, view flight schedules, and manage bookings on behalf of their clients.

Airline Agent Operations: Airline agents, post-login, can access passenger lists, manage flight bookings for registered users, and perform other customer service-related tasks.

Admin Control: Admin users have privileged access to the system. Upon login, they can manage various system entities including flight schedules, aircraft details, crew assignments, passenger lists, and destination data. They have full control over the database, allowing them to perform CRUD (Create, Read, Update, Delete) operations on all system records.
