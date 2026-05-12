package ra.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ra.model.entity.*;
import ra.model.enum_status.BorrowingStatus;
import ra.model.enum_status.SessionStatus;
import ra.repository.admin.EquipmentRepository;
import ra.repository.auth.UserRepository;
import ra.repository.client.DepartmentRepository;
import ra.repository.client.MentoringSessionRepository;
import ra.repository.lecturer.*;
import ra.service.UserService;
import ra.util.PasswordUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DataSeeding implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private LabRoomRepository labRoomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private MentoringSessionRepository mentoringSessionRepository;

    @Autowired
    private AcademicEvaluationRepository academicEvaluationRepository;

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    @Autowired
    private BorrowingDetailRepository borrowingDetailRepository;


    @Override
    public void run(String... args) throws Exception {
        seedAdmin();
        seedDepartments();
        seedLabRooms();
        seedSampleData();
    }

    private void seedAdmin() {
        if (!userService.existsByRole("admin")) {
            User user = new User();
            UserProfile profile = new UserProfile();

            user.setUsername("admin");
            user.setPassword(PasswordUtil.hash("admin123"));
            user.setRole("admin");
            user.setBan(false);

            user.setProfile(profile);
            profile.setUser(user);
            profile.setFullName("Administrator");

            userService.save(user);
        }
    }

    private void seedDepartments() {
        if (departmentRepository.count() == 0) {
            List<Department> departments = Arrays.asList(
                    new Department(null, "Công nghệ phần mềm", null),
                    new Department(null, "Khoa học máy tính", null),
                    new Department(null, "Hệ thống thông tin", null),
                    new Department(null, "An toàn thông tin", null)
            );

            departmentRepository.saveAll(departments);
        }
    }

    private void seedLabRooms() {
        if (labRoomRepository.count() == 0) {
            List<LabRoom> labs = Arrays.asList(
                    new LabRoom(null, "Phòng Lab 101 - AI & Robotics", null),
                    new LabRoom(null, "Phòng Lab 102 - Chuyên đề Phần mềm", null),
                    new LabRoom(null, "Phòng Lab 201 - Mạng máy tính", null),
                    new LabRoom(null, "Phòng Lab 305 - IoT & Embedded System", null)
            );

            labRoomRepository.saveAll(labs);
        }
    }

    private void seedSampleData() {
        if (userRepository.findByUsername("student01") != null) {
            return;
        }

        List<Department> departments = departmentRepository.findAll();
        List<LabRoom> labRooms = labRoomRepository.findAll();

        if (departments.isEmpty() || labRooms.isEmpty()) {
            return;
        }

        List<Equipment> equipments = Arrays.asList(
                new Equipment(null, "Arduino Uno R3", 35, "Vi điều khiển", 25, false, null),
                new Equipment(null, "Laptop Dell Vostro", 10, "Thiết bị", 7, false, null),
                new Equipment(null, "Cảm biến nhiệt độ DHT11", 50, "Cảm biến", 40, false, null),
                new Equipment(null, "Module ESP8266", 30, "Vi điều khiển", 18, false, null),
                new Equipment(null, "Breadboard 830 lỗ", 45, "Linh kiện", 33, false, null)
        );
        equipmentRepository.saveAll(equipments);

        List<User> students = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            User student = new User();
            UserProfile profile = new UserProfile();

            student.setUsername("student0" + i);
            student.setPassword(PasswordUtil.hash("123456"));
            student.setRole("user");
            student.setBan(false);

            profile.setFullName("Sinh viên mẫu " + i);
            profile.setUser(student);
            student.setProfile(profile);

            students.add(userRepository.save(student));
        }

        List<User> lecturerUsers = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            User lecturerUser = new User();
            UserProfile profile = new UserProfile();

            lecturerUser.setUsername("lecturer0" + i);
            lecturerUser.setPassword(PasswordUtil.hash("123456"));
            lecturerUser.setRole("lecturer");
            lecturerUser.setBan(false);

            profile.setFullName("Giảng viên mẫu " + i);
            profile.setUser(lecturerUser);
            lecturerUser.setProfile(profile);

            User savedLecturerUser = userRepository.save(lecturerUser);
            lecturerUsers.add(savedLecturerUser);

            Lecturer lecturer = new Lecturer();
            lecturer.setUser(savedLecturerUser);
            lecturer.setDepartment(departments.get((i - 1) % departments.size()));
            lecturerRepository.save(lecturer);
        }

        for (int i = 0; i < 5; i++) {
            MentoringSession session = new MentoringSession();
            session.setStudent(students.get(i));
            session.setLecturer(lecturerUsers.get(i));
            session.setSessionDate(LocalDate.now().plusDays(i + 1));
            session.setStartTime(LocalTime.of(8 + i, 0));
            session.setEndTime(LocalTime.of(9 + i, 0));
            session.setNote("Nội dung tư vấn mẫu " + (i + 1));

            if (i < 2) {
                session.setStatus(SessionStatus.PENDING);
            } else {
                session.setStatus(SessionStatus.COMPLETED);
            }

            MentoringSession savedSession = mentoringSessionRepository.save(session);

            if (savedSession.getStatus() == SessionStatus.COMPLETED) {
                AcademicEvaluation evaluation = new AcademicEvaluation();
                evaluation.setSession(savedSession);
                evaluation.setLabRoom(labRooms.get(i % labRooms.size()));
                evaluation.setComment("Đánh giá năng lực mẫu " + (i + 1));
                evaluation.setPerformance(i == 2 ? "Khá" : i == 3 ? "Giỏi" : "Xuất sắc");
                academicEvaluationRepository.save(evaluation);

                BorrowingRecord record = new BorrowingRecord();
                record.setSession(savedSession);
                record.setStatus(i == 2 ? BorrowingStatus.PENDING : BorrowingStatus.COMPLETED);
                record.setDetails(new ArrayList<>());

                BorrowingRecord savedRecord = borrowingRecordRepository.save(record);

                BorrowingDetail detail = new BorrowingDetail();
                detail.setBorrowingRecord(savedRecord);
                detail.setEquipment(equipments.get(i));
                detail.setQuantity(i + 1);
                borrowingDetailRepository.save(detail);
            }
        }

        for (int i = 0; i < 2; i++) {
            BorrowingRecord record = new BorrowingRecord();
            record.setSession(mentoringSessionRepository.findAll().get(i));
            record.setStatus(BorrowingStatus.PENDING);
            record.setDetails(new ArrayList<>());

            BorrowingRecord savedRecord = borrowingRecordRepository.save(record);

            BorrowingDetail detail = new BorrowingDetail();
            detail.setBorrowingRecord(savedRecord);
            detail.setEquipment(equipments.get(i));
            detail.setQuantity(i + 2);
            borrowingDetailRepository.save(detail);
        }
    }

}