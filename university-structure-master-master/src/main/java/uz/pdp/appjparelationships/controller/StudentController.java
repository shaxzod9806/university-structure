package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.entity.Student;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.payload.StudentDto;
import uz.pdp.appjparelationships.repository.AddressRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;
import uz.pdp.appjparelationships.repository.StudentRepository;
import uz.pdp.appjparelationships.repository.SubjectRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    SubjectRepository subjectRepository;

    //1. VAZIRLIK
    @GetMapping("/forMinistry")
    public Page<Student> getStudentListForMinistry(@RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 2);
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage;
    }

    //2. UNIVERSITY
    @GetMapping("/forUniversity/{universityId}")
    public Page<Student> getStudentListForUniversity(@PathVariable Integer universityId,
                                                     @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 2);
        Page<Student> studentPage = studentRepository.findAllByGroup_Faculty_UniversityId(universityId, pageable);
        return studentPage;
    }

    //3. FACULTY DEKANAT
    @GetMapping("/forFaculty/{facultyId}")
    public Page<Student> getStudentListForFaculty(@PathVariable Integer facultyId,
                                                  @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 2);
        Page<Student> studentPage = studentRepository.findAllByGroup_FacultyId(facultyId, pageable);
        return studentPage;
    }

    //4. GROUP OWNER
    @GetMapping("/forGroupOwner/{groupId}")
    public Page<Student> getStudentListForGroup(@PathVariable Integer groupId,
                                                @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 2);
        Page<Student> studentPage = studentRepository.findAllByGroup_FacultyId(groupId, pageable);
        return studentPage;
    }

    @PostMapping
    public String add(@RequestBody StudentDto studentDto) {

        Optional<Address> optionalAddress = addressRepository.findById(studentDto.getAddressId());
        if (optionalAddress.isEmpty())
            return "Address not found";


        Optional<Group> optionalGroup = groupRepository.findById(studentDto.getGroupId());
        if (optionalGroup.isEmpty())
            return "Group not found";


        List<Subject> subjects = new ArrayList<>();
        for (Integer subjectId : studentDto.getSubjects()) {
            Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);
            if (optionalSubject.isEmpty())
                return "Subject not found";

            subjects.add(optionalSubject.get());
        }

        Student student = new Student();
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setGroup(optionalGroup.get());
        student.setAddress(optionalAddress.get());
        student.setSubjects(subjects);
        student.setSubjects(subjects);
        studentRepository.save(student);

        return "Student added";
    }

    @DeleteMapping(value = "/{id}")
    public String delete(@PathVariable Integer id) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isEmpty()) {
            return "Student not found";
        }

        studentRepository.deleteById(id);
        return "Student deleted";
    }

    @PutMapping(value = "/{id}")
    public String edit(@PathVariable Integer id, @RequestBody StudentDto studentDto) {
        Optional<Student> optionalStudent = studentRepository.findById(id);

        if (optionalStudent.isPresent()) {


            Optional<Address> optionalAddress = addressRepository.findById(studentDto.getAddressId());
            if (!optionalAddress.isPresent()) {
                return "Address not found";
            }

            Optional<Group> optionalGroup = groupRepository.findById(studentDto.getGroupId());
            if (!optionalGroup.isPresent()) {
                return "Group not found";
            }

            List<Subject> subjects = new ArrayList<>();
            for (Integer subjectId : studentDto.getSubjects()) {
                Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);
                if (!optionalSubject.isPresent()) {
                    return "Subject not found";
                }
                subjects.add(optionalSubject.get());
            }

            Student student = optionalStudent.get();
            student.setFirstName(studentDto.getFirstName());
            student.setLastName(studentDto.getLastName());
            student.setAddress(optionalAddress.get());
            student.setGroup(optionalGroup.get());
            student.setSubjects(subjects);
            student.setSubjects(subjects);
            studentRepository.save(student);

            return "Student edited";
        }
        return "Student not found";
    }
}


