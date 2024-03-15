import java.util.ArrayList;
import java.util.List;

class WaitingRoom {
    private static final Object lock = new Object(); // Lock object for synchronization
    private static final int SCREENING_CAPACITY = 3; // Capacity of the screening room
    private static final List<PatientThread> waitingPatients = new ArrayList<>(); // List of patients waiting for screening
    private static int currentScreeningIndex = 0; // Index of the current screening

    public static void waitForScreening(PatieintThread patient) {
        synchronized (lock) {
            waitingPatients.add(patient); // Add the patient to the waiting list
            msg(patient.getName() + " is waiting for screening"); // Display a message
            if (waitingPatients.size() >= SCREENING_CAPACITY) {
                startScreening(); // If the screening room is full, start the screening process
            }
        }
    }

    public static void leaveAfterScreening(PatientThread patient) {
        synchronized (lock) {
            waitingPatients.remove(patient); // Remove the patient from the waiting list
            msg(patient.getName() + " is leaving after screening"); // Display a message
            if (waitingPatients.size() == 0) {
                startScreening(); // If there are no more patients waiting, start the next screening
            }
        }
    }

    public static boolean hasWaitingPatients() {
        synchronized (lock) {
            return !waitingPatients.isEmpty(); // Check if there are any patients waiting
        }
    }

    public static PatientThread getNextPatient() {
        synchronized (lock) {
            if (hasWaitingPatients()) {
                return waitingPatients.get(0); // Get the next patient in the waiting list
            }
            return null; // If there are no patients waiting, return null
        }
    }

    private static void startScreening() {
        synchronized (lock) {
            if (waitingPatients.size() >= SCREENING_CAPACITY) {
                int screeningIndex = ++currentScreeningIndex; // Increment the screening index
                StringBuilder message = new StringBuilder();
                message.append("Screening ").append(screeningIndex).append(": ");
                for (int i = 0; i < SCREENING_CAPACITY; i++) {
                    PatientThread patient = waitingPatients.get(i);
                    message.append(patient.getName()).append(" "); // Append the names of patients in the current screening
                }
                msg(message.toString()); // Display the screening message
                try {
                    Thread.sleep(2000); // Simulate the screening process
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < SCREENING_CAPACITY; i++) {
                    PatientThread patient = waitingPatients.get(i);
                    try {
                        patient.join(); // Wait for each patient in the screening to finish their processing
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                waitingPatients.subList(0, SCREENING_CAPACITY).clear(); // Remove the screened patients from the waiting list
                if (hasWaitingPatients()) {
                    startScreening(); // If there are still patients waiting, start the next screening
                }
            }
        }
    }

    public static void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - Main.time) + "] Waiting Room: " + m); // Display a message with a timestamp
    }
}
