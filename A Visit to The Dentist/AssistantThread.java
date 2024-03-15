import java.util.Random;

class AssistantThread extends Thread {
    private static final Object lock = new Object(); // Lock object for synchronization
    private static final int MAX_PRIORITY_INCREASE = 2; // Maximum priority increase for the assistant

    private static int currentAssistantIndex = 0; // Index of the current assistant
    private static volatile boolean isXRayInProgress = false; // Flag to indicate if an X-ray is in progress

    public AssistantThread(int id) {
        setName("Assistant-" + id); // Set the name of the assistant thread
    }

    @Override
    public void run() {
        while (true) {
            synchronized (lock) {
                if (!isXRayInProgress && WaitingRoom.hasWaitingPatients()) {
                    PatientThread patient = WaitingRoom.getNextPatient();
                    if (patient != null) {
                        takeInformation(patient); // Assist the patient by taking information
                        msg("is performing X-ray for " + patient.getName()); // Display a message
                        isXRayInProgress = true; // Set the X-ray in progress flag
                        setPriority(getPriority() + MAX_PRIORITY_INCREASE); // Increase the priority temporarily
                        try {
                            Thread.sleep(10); // Simulate the X-ray process
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        msg("X-ray is complete for " + patient.getName()); // Display a message
                        setPriority(getPriority() - MAX_PRIORITY_INCREASE); // Restore the original priority
                        isXRayInProgress = false; // Reset the X-ray in progress flag
                        DentistThread dentist = DentistThread.getNextDentist(); // Get the next available dentist
                        try {
                            dentist.interrupt(); // Interrupt the dentist thread to continue processing
                        } catch (SecurityException e) {
                            dentist.msg("has been interrupted by " + getName()); // Display a message if interrupted
                        }
                    }
                } else {
                    try {
                        Thread.sleep(1000); // If there is no patient or X-ray in progress, wait for 1 second
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

    public static void takeInformation(PatientThread patient) {
        patient.msg("is being assisted by " + getCurrentAssistant().getName()); // Display a message
        try {
            Thread.sleep(new Random().nextInt(3000)); // Simulate the process of taking information
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        patient.msg("is taken into the examination room"); // Display a message
    }

    public static void doXRay() {
        try {
            Thread.sleep(0); // Simulate a delay in the X-ray process
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (isXRayInProgress) {
            try {
                Thread.sleep(0); // If X-ray is already in progress, simulate another delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static AssistantThread getCurrentAssistant() {
        synchronized (lock) {
            int index = currentAssistantIndex;
            currentAssistantIndex = (currentAssistantIndex + 1) % Main.num_a; // Update the current assistant index
            return Main.assistants.get(index); // Return the current assistant
        }
    }

    public static boolean isAssistantAvailable() {
        synchronized (lock) {
            return !isXRayInProgress; // Check if the assistant is available (not performing an X-ray)
        }
    }
}import java.util.Random;

class AssistantThread extends Thread {
    private static final Object lock = new Object(); // Lock object for synchronization
    private static final int MAX_PRIORITY_INCREASE = 2; // Maximum priority increase for the assistant

    private static int currentAssistantIndex = 0; // Index of the current assistant
    private static volatile boolean isXRayInProgress = false; // Flag to indicate if an X-ray is in progress

    public AssistantThread(int id) {
        setName("Assistant-" + id); // Set the name of the assistant thread
    }

    @Override
    public void run() {
        while (true) {
            synchronized (lock) {
                if (!isXRayInProgress && WaitingRoom.hasWaitingPatients()) {
                    PatientThread patient = WaitingRoom.getNextPatient();
                    if (patient != null) {
                        takeInformation(patient); // Assist the patient by taking information
                        msg("is performing X-ray for " + patient.getName()); // Display a message
                        isXRayInProgress = true; // Set the X-ray in progress flag
                        setPriority(getPriority() + MAX_PRIORITY_INCREASE); // Increase the priority temporarily
                        try {
                            Thread.sleep(10); // Simulate the X-ray process
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        msg("X-ray is complete for " + patient.getName()); // Display a message
                        setPriority(getPriority() - MAX_PRIORITY_INCREASE); // Restore the original priority
                        isXRayInProgress = false; // Reset the X-ray in progress flag
                        DentistThread dentist = DentistThread.getNextDentist(); // Get the next available dentist
                        try {
                            dentist.interrupt(); // Interrupt the dentist thread to continue processing
                        } catch (SecurityException e) {
                            dentist.msg("has been interrupted by " + getName()); // Display a message if interrupted
                        }
                    }
                } else {
                    try {
                        Thread.sleep(1000); // If there is no patient or X-ray in progress, wait for 1 second
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

    public static void takeInformation(PatientThread patient) {
        patient.msg("is being assisted by " + getCurrentAssistant().getName()); // Display a message
        try {
            Thread.sleep(new Random().nextInt(3000)); // Simulate the process of taking information
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        patient.msg("is taken into the examination room"); // Display a message
    }

    public static void doXRay() {
        try {
            Thread.sleep(0); // Simulate a delay in the X-ray process
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (isXRayInProgress) {
            try {
                Thread.sleep(0); // If X-ray is already in progress, simulate another delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static AssistantThread getCurrentAssistant() {
        synchronized (lock) {
            int index = currentAssistantIndex;
            currentAssistantIndex = (currentAssistantIndex + 1) % Main.num_a; // Update the current assistant index
            return Main.assistants.get(index); // Return the current assistant
        }
    }

    public static boolean isAssistantAvailable() {
        synchronized (lock) {
            return !isXRayInProgress; // Check if the assistant is available (not performing an X-ray)
        }
    }
