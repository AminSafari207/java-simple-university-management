import com.jsum.model.person.Student;
import com.jsum.service.*;
import com.jsum.utils.PrintUtils;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("postgresql");

        StudentService studentService = new StudentService(emf);
        GradeService gradeService = new GradeService(emf);
        QueryTestService queryTestService = new QueryTestService(emf);
        StudentEnrollmentService studentEnrollmentService = new StudentEnrollmentService(emf);
        ProfessorEnrollmentService professorEnrollmentService = new ProfessorEnrollmentService(emf);

        Long s1 = queryTestService.addStudent("Sara", "sara@uni.test", "Software");
        Long s2 = queryTestService.addStudent("Mehdi", "mehdi@uni.test", "IT");
        Long s3 = queryTestService.addStudent("Neda", "neda@uni.test", "CS");

        Long p1 = queryTestService.addProfessor("Dr. Ada", "ada@uni.test", 120_000.0);
        Long p2 = queryTestService.addProfessor("Dr. Turing", "turing@uni.test", 130_000.0);

        Long c1 = queryTestService.addCourse("Algorithms", 4);
        Long c2 = queryTestService.addCourse("Databases", 3);
        Long c3 = queryTestService.addCourse("Networks", 3);
        Long c4 = queryTestService.addCourse("Operating Systems", 4);

        studentEnrollmentService.enroll(s1, c1, "Fall 2025");
        studentEnrollmentService.enroll(s1, c2, "Fall 2025");
        studentEnrollmentService.enroll(s2, c1, "Fall 2025");
        studentEnrollmentService.enroll(s2, c3, "Fall 2025");
        studentEnrollmentService.enroll(s3, c2, "Fall 2025");

        professorEnrollmentService.enrollProfessor(c1, p1);
        professorEnrollmentService.enrollProfessor(c2, p1);
        professorEnrollmentService.enrollProfessor(c3, p1);
        professorEnrollmentService.enrollProfessor(c4, p1);
        professorEnrollmentService.enrollProfessor(c1, p2);

        gradeService.saveNumeric(s1, c1, "Fall 2025", 18.0);
        gradeService.saveNumeric(s1, c2, "Fall 2025", 9.0);
        gradeService.saveNumeric(s2, c1, "Fall 2025", 12.0);
        gradeService.saveLetter(s2, c3, "Fall 2025", "B");
        gradeService.saveLetter(s3, c2, "Fall 2025", "C-");

        Student mehdi = studentService.getStudentWithComputed(s2, "Fall 2025");
        PrintUtils.printList("Computed Student", List.of(mehdi));

        List<Student> failedStudents = queryTestService.getStudentsWithFails();
        PrintUtils.printList("Students Fails", failedStudents);

        double avgGpa = queryTestService.averageGpaAllStudents();
        PrintUtils.printList("Average GPA For All Students", List.of(avgGpa));
    }
}
