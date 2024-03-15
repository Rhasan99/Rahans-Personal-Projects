import java.util.Objects;

class DentistThread extends Thread {
    private static final Object lock = new Object(); // Lock object for synchronization
    private static final int MAX_XRAY_TIME = 10; // Maximum time for X-ray consultation

    private static int currentDentistIndex = 0; // Index of the current dentist

    public DentistThread(int id) {
        setName("Dentist-" + id); // Set the name of the dentist thread
    }

    @Override
    public void run() {
        msg("is in their personal office"); // Display a message

        while (true) {
            synchronized (lock) {
                if (AssistantThread.isAssistantAvailable() && WaitingRoom.hasWaitingPatients()) {
                    AssistantThread.takeInformation(Objects.requireNonNull(WaitingRoom.getNextPatient())); // Take patient information with the help of an assistant
                    AssistantThread.doXRay(); // Simulate the X-ray process with the help of an assistant
                    try {
                        Thread.sleep(MAX_XRAY_TIME); // Simulate the consultation time
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    msg("consultation is complete"); // Display a message
                } else {
                    try {
                        Thread.sleep(1000); // If there are no available assistants or waiting patients, wait for 1 second
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - Main.time) + "] " + getName() + ": " + m); // Display a message with a timestamp
    }

    public static DentistThread getNextDentist() {
        synchronized (lock) {
            int index = currentDentistIndex;
            currentDentistIndex = (currentDentistIndex + 1) % Main.num_d; // Update the current dentist index
            return Main.dentists.get(index); // Return the current dentist
        }
    }
}
