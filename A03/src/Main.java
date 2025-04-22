import javax.mail.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Main {
    private static final Scanner input = new Scanner(System.in);
    private static final Scanner choiceInput = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    public static void main(String[] args) {

        EmergencyAlertSystem vitalMonitoringSystem = new EmergencyAlertSystem();
        ChatSystem messagingSystem = new ChatSystem();
        AppointmentSystem schedulingSystem = new AppointmentSystem();
        VideoCallSystem telemedSystem = new VideoCallSystem();

        do {
            menu();
            int choice = choiceInput.nextInt();
            if(choice == 1) {
                vitalMonitoringSystem.checkVitals();
            }
            else if(choice == 2) {
                vitalMonitoringSystem.activatePanicButton();
            }
            else if(choice == 3) {
                messagingSystem.initiateChatSession();
            }
            else if(choice == 4) {
                telemedSystem.initiateVideoCall();
            }
            else if(choice == 5) {
                schedulingSystem.addAppointment();
            }
            else if(choice == 6) {
                schedulingSystem.viewAppointments();
            }
            else if(choice == 7) {
                schedulingSystem.editAppointment();
            }
            else if(choice == 8) {
                schedulingSystem.sendReminders();
            }
            else if(choice == 9) {
                System.out.println("Exiting System...");
                break;
            }
            else {
                System.out.println("Kindly enter an appropriate input!");
            }
        }while(true);
        input.close();
        choiceInput.close();
    }

    private static void menu() {
        System.out.println("\nPick An Option");
        System.out.println("1. View Vitals");
        System.out.println("2. Panic Button");
        System.out.println("3. Chat between Doctor and Patient");
        System.out.println("4. Initiate a Video Call");
        System.out.println("5. Add Appointment");
        System.out.println("6. View Appointments");
        System.out.println("7. Edit Appointment");
        System.out.println("8. Send Appointment Reminders");
        System.out.println("9. Exit");
        System.out.print("Choice: ");
    }

    private static LocalDateTime getDateTime() {
        while (true) {
            try {
                System.out.print("Enter date (dd/MM/yyyy): ");
                String dateString = input.nextLine().trim();
                LocalDate appointmentDate = LocalDate.parse(dateString, DATE_FORMAT);

                System.out.print("Enter time (HH:mm): ");
                String timeString = input.nextLine().trim();
                LocalTime appointmentTime = LocalTime.parse(timeString, TIME_FORMAT);

                return LocalDateTime.of(appointmentDate, appointmentTime);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date or time format. Please try again.");
            }
        }
    }
    private static double doubleInput(String msg, double min, double max) {
        while (true) {
            System.out.print(msg);
            try {
                double userValue = Double.parseDouble(input.nextLine());
                if (userValue >= min && userValue <= max) {
                    return userValue;
                } else {
                    System.out.println("Enter a value between " + min + " and " + max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter an appropriate input.");
            }
        }
    }
    private static int userChoice(int min, int max) {
        while (true) {
            try {
                int userChoice = Integer.parseInt(input.nextLine());
                if (userChoice >= min && userChoice <= max) {
                    return userChoice;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max);
                    System.out.print("Choice: ");
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number");
                System.out.print("Choice: ");
            }
        }
    }


    static class EmergencyAlertSystem {
        private final double CRITICAL_HEART_RATE_THRESHOLD = 120.0;
        private final double CRITICAL_BLOOD_PRESSURE_THRESHOLD = 180.0;
        private final double MIN_VALID_HEART_RATE = 30.0;
        private final double MAX_VALID_HEART_RATE = 220.0;
        private final double MIN_VALID_BLOOD_PRESSURE = 70.0;
        private final double MAX_VALID_BLOOD_PRESSURE = 250.0;

        public void checkVitals() {
            System.out.println("\nEmergency Alert System");
            double heartRate = doubleInput("Enter Heart Rate (bpm): ", MIN_VALID_HEART_RATE, MAX_VALID_HEART_RATE);
            double bloodPressure = doubleInput("Enter Blood Pressure (systolic): ", MIN_VALID_BLOOD_PRESSURE, MAX_VALID_BLOOD_PRESSURE);

            EmergencyAlert vitalAlert = new EmergencyAlert(heartRate, bloodPressure);
            vitalAlert.checkAndTriggerAlert();
        }

        public void activatePanicButton() {
            System.out.println("\n[Panic Button Activated!]");
            double patientHeartRate = doubleInput("Enter Heart Rate (bpm): ", MIN_VALID_HEART_RATE, MAX_VALID_HEART_RATE);
            double patientBloodPressure = doubleInput("Enter Blood Pressure (systolic): ", MIN_VALID_BLOOD_PRESSURE, MAX_VALID_BLOOD_PRESSURE);

            PanicButton emergencyButton = new PanicButton();
            emergencyButton.pressButton(patientHeartRate, patientBloodPressure);
        }
    }

    static class ChatSystem {
        private ChatServer messageServer = new ChatServer();
        private ChatClient doctorClient = new ChatClient("Dr. Asif", messageServer);
        private ChatClient patientClient = new ChatClient("Patient", messageServer);

        public void initiateChatSession() {
            boolean exitChatSession = false;

            while (!exitChatSession) {
                System.out.println("\nChat Module");
                System.out.println("1. Patient sends message");
                System.out.println("2. Doctor sends message");
                System.out.println("3. View messages");
                System.out.println("4. Back to menu");
                System.out.print("Choice: ");

                int chatMenuChoice = userChoice(1, 4);

                switch (chatMenuChoice) {
                    case 1:
                        System.out.print("Patient: ");
                        String patientMessage = input.nextLine();
                        patientClient.sendMessage(patientMessage);
                        break;
                    case 2:
                        System.out.print("Doctor: ");
                        String doctorMessage = input.nextLine();
                        doctorClient.sendMessage(doctorMessage);
                        break;
                    case 3:
                        System.out.println("\nChat History:");
                        patientClient.receiveMessages();
                        break;
                    case 4:
                        exitChatSession = true;
                        break;
                }
            }
        }
    }

    static class VideoCallSystem {
        public void initiateVideoCall() {
            System.out.println("\nVideo Call");
            System.out.print("Enter a video call meeting link (e.g., https://example.com/awrqr123): ");
            String videoCallLink = input.nextLine().trim();

            if (isValidUrl(videoCallLink)) {
                VideoCall telemedicineCall = new VideoCall(videoCallLink);
                telemedicineCall.startCall();
            } else {
                System.out.println("Provide a valid URL.");
            }
        }

        private boolean isValidUrl(String url) {
            return url != null && !url.isEmpty() &&
                    (url.startsWith("http://") || url.startsWith("https://"));
        }
    }

    static class AppointmentSystem {
        private ReminderService appointmentReminders = new ReminderService();
        public void addAppointment(){
            System.out.println("\nAdd Appointment");

            System.out.print("Enter patient name: ");
            String patientName = input.nextLine().trim();
            if (patientName.isEmpty()) {
                System.out.println("Patient name cannot be empty. Operation cancelled.");
                return;
            }
            LocalDateTime appointmentDateTime = getDateTime();

            System.out.print("Enter note: ");
            String appointmentNotes = input.nextLine().trim();

            System.out.print("Enter email: ");
            String contactEmail = input.nextLine().trim();
            System.out.print("Enter phone: ");
            String contactPhone = input.nextLine().trim();

            Appointment newAppointment = new Appointment(patientName, appointmentDateTime, appointmentNotes, contactEmail, contactPhone);
            appointmentReminders.addAppointment(newAppointment);
            System.out.println("Appointment added successfully!");
        }

        public void viewAppointments() {
            System.out.println("\nAppointments List");
            appointmentReminders.listAppointments();
        }

        public void editAppointment() {
            System.out.println("\nEdit Appointment");
            List<Appointment> scheduledAppointments = appointmentReminders.getAppointments();

            if (scheduledAppointments.isEmpty()) {
                System.out.println("No appointments exist.");
                return;
            }

            appointmentReminders.listAppointments();
            System.out.print("Enter appointment number to edit (0 to cancel): ");
            int appointmentIndex = userChoice(0, scheduledAppointments.size());

            if (appointmentIndex == 0) {
                System.out.println("Edit cancelled.");
                return;
            }

            LocalDateTime updatedDateTime = getDateTime();

            System.out.print("Enter new note: ");
            String updatedNotes = input.nextLine().trim();

            appointmentReminders.editAppointment(appointmentIndex - 1, updatedDateTime, updatedNotes);
        }

        public void sendReminders(){
            System.out.println("\nSend Appointment Reminders");
            List<Appointment> scheduledAppointments = appointmentReminders.getAppointments();

            if (scheduledAppointments.isEmpty()) {
                System.out.println("No appointments.");
                return;
            }

            System.out.println("Select an appointment to send a reminder:");
            for (int i = 0; i < scheduledAppointments.size(); i++) {
                System.out.println((i + 1) + ". " + scheduledAppointments.get(i).getFormattedDetails());
            }
            System.out.println((scheduledAppointments.size() + 1) + ". Send reminders for all appointments");
            System.out.println("0. Cancel");

            int reminderChoice = userChoice(0, scheduledAppointments.size() + 1);

            if (reminderChoice == 0) {
                System.out.println("Operation cancelled.");
                return;
            } else if (reminderChoice == scheduledAppointments.size() + 1) {
                for (Appointment appt : scheduledAppointments) {
                    appointmentReminders.sendAppointmentReminder(appt);
                }
                System.out.println("Reminders sent for all appointments.");
            } else {
                Appointment selectedAppointment = scheduledAppointments.get(reminderChoice - 1);
                appointmentReminders.sendAppointmentReminder(selectedAppointment);
                System.out.println("Reminder sent for appointment.");
            }
        }
    }
}

class EmergencyAlert {
    private double heartRate;
    private double bloodPressure;
    private static final double CRITICAL_HEART_RATE_THRESHOLD = 120.0;
    private static final double CRITICAL_BLOOD_PRESSURE_THRESHOLD = 180.0;

    public EmergencyAlert(double heartRate, double bloodPressure) {
        this.heartRate = heartRate;
        this.bloodPressure = bloodPressure;
    }

    public void checkAndTriggerAlert() {
        try {
            if (heartRate > CRITICAL_HEART_RATE_THRESHOLD || bloodPressure > CRITICAL_BLOOD_PRESSURE_THRESHOLD) {
                System.out.println("CRITICAL VITALS DETECTED! Triggering emergency alert.");
                String alertMessage = "EMERGENCY ALERT: Patient vitals are abnormal. Heart Rate: "
                        + heartRate + " bpm, Blood Pressure: " + bloodPressure + " mmHg";
                NotificationService.sendAlert(alertMessage);
            } else {
                System.out.println("Vitals are within normal ranges.");
            }
        } catch (Exception e) {
            System.err.println("Error triggering emergency alert: " + e.getMessage());
        }
    }
}
class NotificationService {
    public static void sendAlert(String alertMessage) {
        List<Notifiable> notificationChannels = new ArrayList<>();
        notificationChannels.add(new EmailNotification("doctor@example.com"));
        notificationChannels.add(new SMSNotification("9124714"));

        System.out.println("\n Sending notifications through all available channels:");
        for (Notifiable channel : notificationChannels) {
            channel.notifyUser(alertMessage);
        }
    }
}

class PanicButton {
    public void pressButton(double heartRate, double bloodPressure) {
        System.out.println("Panic button activated by the patient!");
        EmergencyAlert emergencyAlert = new EmergencyAlert(heartRate, bloodPressure);
        emergencyAlert.checkAndTriggerAlert();
    }
}
class Message {
    private String senderName;
    private String messageContent;
    private LocalDateTime sentTimestamp;

    public Message(String sender, String content) {
        this.senderName = sender;
        this.messageContent = content;
        this.sentTimestamp = LocalDateTime.now();
    }

    public String getSender() {
        return senderName;
    }

    public String getContent() {
        return messageContent;
    }

    public LocalDateTime getTimestamp() {
        return sentTimestamp;
    }

    @Override
    public String toString() {
        String formattedTime = sentTimestamp.format(DateTimeFormatter.ofPattern("hh:mm a"));
        return formattedTime + " " + senderName + ": " + messageContent;
    }
}

class ChatServer {
    private List<Message> messageHistory;

    public ChatServer() {
        messageHistory = new ArrayList<>();
    }

    public void addMessage(Message newMessage) {
        messageHistory.add(newMessage);
        System.out.println("New message received.");
    }

    public List<Message> getMessages() {
        return messageHistory;
    }
}

class ChatClient {
    private String userIdentity;
    private ChatServer messageServer;

    public ChatClient(String username, ChatServer server) {
        this.userIdentity = username;
        this.messageServer = server;
    }

    public void sendMessage(String messageContent) {
        if (messageContent == null || messageContent.trim().isEmpty()) {
            System.out.println("Cannot send empty message.");
            return;
        }

        Message newMessage = new Message(userIdentity, messageContent);
        messageServer.addMessage(newMessage);
    }

    public void receiveMessages() {
        List<Message> availableMessages = messageServer.getMessages();
        if (availableMessages.isEmpty()) {
            System.out.println("No messages available.");
            return;
        }

        for (Message msg : availableMessages) {
            System.out.println(msg);
        }
    }
}

class VideoCall {
    private String callMeetingLink;

    public VideoCall(String meetingLink) {
        this.callMeetingLink = meetingLink;
    }

    public void startCall() {
        System.out.println("Starting video call.");
        System.out.println("Please join the meeting: " + callMeetingLink);
        System.out.println("Waiting for others");
    }
}

interface Notifiable {
    void notifyUser(String message);
}

class EmailNotification implements Notifiable {
    private String recipientEmail;

    public EmailNotification(String email) {
        this.recipientEmail = email;
    }

    @Override
    public void notifyUser(String messageContent) {
        System.out.println("Sending Email to " + recipientEmail + ":\n" + messageContent);
    }
}


class SMSNotification implements Notifiable {
    private String recipientPhoneNumber;

    public SMSNotification(String phoneNumber) {
        this.recipientPhoneNumber = phoneNumber;
    }

    @Override
    public void notifyUser(String messageContent) {
        System.out.println("📱 Sending SMS to " + recipientPhoneNumber + ":\n" + messageContent);
    }
}

class ReminderService {
    private List<Appointment> scheduledAppointments;

    public ReminderService() {
        scheduledAppointments = new ArrayList<>();
    }

    public void addAppointment(Appointment newAppointment){
        scheduledAppointments.add(newAppointment);
        sendAppointmentReminder(newAppointment);
    }

    public void sendAppointmentReminder(Appointment appointment){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy 'at' hh:mm a");
        String formattedDateTime = appointment.getDateTime().format(formatter);

        String reminderMessage = "Appointment Reminder for " + appointment.getPatientName() + ":\n" +
                "Date: " + formattedDateTime + "\n" +
                "Note: " + appointment.getNote();
        SMSNotification sms = new SMSNotification(appointment.getPhone());
        sms.notifyUser(reminderMessage);
        EmailNotification email = new EmailNotification(appointment.getEmail());
        email.notifyUser(reminderMessage);
    }

    public void listAppointments() {
        if (scheduledAppointments.isEmpty()) {
            System.out.println("No appointments scheduled.");
        } else {
            System.out.println("Scheduled Appointments");
            int index = 1;
            for (Appointment appt : scheduledAppointments) {
                System.out.println(index + ". " + appt.getFormattedDetails());
                index++;
            }
        }
    }

    public void editAppointment(int appointmentIndex, LocalDateTime newDateTime, String newNote) {
        if (appointmentIndex >= 0 && appointmentIndex < scheduledAppointments.size()) {
            Appointment appointment = scheduledAppointments.get(appointmentIndex);
            appointment.setDateTime(newDateTime);
            appointment.setNote(newNote);
            System.out.println("Appointment updated successfully.");
        } else {
            System.out.println("Invalid appointment index.");
        }
    }

    public List<Appointment> getAppointments() {
        return scheduledAppointments;
    }
}
class Appointment {
    private String patientName;
    private LocalDateTime appointmentDateTime;
    private String appointmentNote;
    private String contactEmail;
    private String contactPhone;

    public Appointment(String patientName, LocalDateTime dateTime, String note, String email, String phone) {
        this.patientName = patientName;
        this.appointmentDateTime = dateTime;
        this.appointmentNote = note;
        this.contactEmail = email;
        this.contactPhone = phone;
    }

    public String getPatientName() {
        return patientName;
    }

    public LocalDateTime getDateTime() {
        return appointmentDateTime;
    }

    public String getNote() {
        return appointmentNote;
    }

    public String getEmail() {
        return contactEmail;
    }

    public String getPhone() {
        return contactPhone;
    }

    public void setDateTime(LocalDateTime newDateTime) {
        this.appointmentDateTime = newDateTime;
    }

    public void setNote(String newNote) {
        this.appointmentNote = newNote;
    }

    public String getFormattedDetails() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy 'at' hh:mm a");
        return  patientName + " | " + appointmentDateTime.format(formatter) + "\n" +
                "Note: " + appointmentNote + "\nEmail: " + contactEmail + " | Phone: " + contactPhone;
    }
}