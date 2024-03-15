import java.util.ArrayList;
import java.util.List;


public class Main {
    public static long time = System.currentTimeMillis();

    static final int num_p = 20; // Number of patients
    static final int num_d = 2;  // Number of dentists
    static final int num_a = 3;  // Number of assistants

    static List<PatientThread> patients = new ArrayList<>();
    static List<DentistThread> dentists = new ArrayList<>();
    static List<AssistantThread> assistants = new ArrayList<>();

    public static void main(String[] args) {

        // Create patient threads
        for (int i = 0; i < num_p; i++) {
            patients.add(new PatientThread(i + 1));
        }

        // Create dentist threads
        for (int i = 0; i < num_d; i++) {
            dentists.add(new DentistThread(i + 1));
        }

        // Create assistant threads
        for (int i = 0; i < num_a; i++) {
            assistants.add(new AssistantThread(i + 1));
        }

        // Start patient threads
        for (PatientThread patient : patients) {
            patient.start();
        }

        // Start dentist threads
        for (DentistThread dentist : dentists) {
            dentist.start();
        }

        // Start assistant threads
        for (AssistantThread assistant : assistants) {
            assistant.start();
        }
    }
}