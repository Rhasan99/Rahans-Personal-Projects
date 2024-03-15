import java.util.Random;

class PatientThread extends Thread {
    private final int id; // Patient ID
    private static final Random random = new Random(); // Random number generator

    public PatientThread(int id) {
        this.id = id; // Set the patient ID
        setName("Patient-" + id); // Set the name of the patient thread
    }

    @Override
    public void run() {
        msg("is going to the dentist's office"); // Display a message
        try {
            Thread.sleep(random.nextInt(5000)); // Simulate the time it takes for the patient to travel to the dentist's office
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        msg("has arrived at the dentist's office"); // Display a message

        WaitingRoom.waitForScreening(this); // Wait for the patient to be screened by the waiting room

        msg("is being consulted by the dentist"); // Display a message
        try {
            Thread.sleep(2000); // Simulate the consultation time
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        msg("is leaving the dentist's office"); // Display a message
        WaitingRoom.leaveAfterScreening(this); // Notify the waiting room that the patient has left after the screening
    }

    public int getPatientId() {
        return id; // Get the patient ID
    }

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - Main.time) + "] " + getName() + ": " + m); // Display a message with a timestamp
    }
}
