import java.util.*;

class Student {
    String regNo;
    String name;

    Student(String regNo, String name) {
        this.regNo = regNo;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Student)) return false;
        Student other = (Student) obj;
        return Objects.equals(regNo, other.regNo);
    }

    // Deliberately no hashCode override in this first version.
    @Override
    public String toString() { return regNo + " " + name; }
}

final class CorrectStudent {
    private final String regNo;
    private final String name;

    CorrectStudent(String regNo, String name) {
        this.regNo = regNo;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CorrectStudent)) return false;
        CorrectStudent other = (CorrectStudent) obj;
        return Objects.equals(regNo, other.regNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regNo);
    }

    @Override
    public String toString() { return regNo + " " + name; }
}

public class HashSetContractDemo {
    public static void main(String[] args) {
        System.out.println("TEST 1: same object inserted twice");
        Student a = new Student("R101", "Arun");
        Set<Student> sameObject = new HashSet<>();
        sameObject.add(a);
        sameObject.add(a);
        System.out.println("Expected size: 1; observed: " + sameObject.size());

        System.out.println("\\nTEST 2: separate objects with same registration number");
        Set<Student> duplicates = new HashSet<>();
        duplicates.add(new Student("R102", "Divya"));
        duplicates.add(new Student("R102", "Divya"));
        System.out.println("Expected with broken contract: commonly 2; observed: " + duplicates.size());
        System.out.println("The exact outcome depends on hash bucket collision; equals is checked within a bucket.");

        System.out.println("\\nTEST 3: mutate equality field after insertion");
        Student mutable = new Student("R103", "Kumar");
        Set<Student> changed = new HashSet<>();
        changed.add(mutable);
        System.out.println("Contains before change: " + changed.contains(mutable));
        mutable.regNo = "R999";
        System.out.println("Contains after change: " + changed.contains(mutable)
                + " (may be false because the object is in its old bucket)");

        System.out.println("\\nCORRECTED VERSION");
        Set<CorrectStudent> fixed = new HashSet<>();
        fixed.add(new CorrectStudent("R101", "Arun"));
        fixed.add(new CorrectStudent("R102", "Divya"));
        fixed.add(new CorrectStudent("R101", "Arun"));
        System.out.println("Unique students: " + fixed.size());
    }
}
