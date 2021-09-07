package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.repository.SubjectRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/subject")
public class SubjectController {
    @Autowired
    SubjectRepository subjectRepository;

    @PostMapping
    public String add(@RequestBody Subject subject) {
        boolean existsByName = subjectRepository.existsByName(subject.getName());
        if (existsByName)
            return "This subject already exist";
        subjectRepository.save(subject);
        return "Subject added";
    }


    @GetMapping
    public List<Subject> getAll() {
        List<Subject> subjectList = subjectRepository.findAll();
        return subjectList;
    }

    @GetMapping(value = "/{id}")
    public Subject getById(@PathVariable Integer id) {
        Optional<Subject> optionalSubject = subjectRepository.findById(id);
        if (optionalSubject.isPresent()) {
            Subject subject = optionalSubject.get();
            return subject;
        } else {
            return new Subject();
        }
    }

    @PutMapping(value = "/{id}")
    public String edit(@PathVariable Integer id, @RequestBody Subject comingSubject) {
        Optional<Subject> optionalSubject = subjectRepository.findById(id);
        if (optionalSubject.isPresent()) {
            Subject subject = optionalSubject.get();

            subject.setName(comingSubject.getName());
            subjectRepository.save(subject);
            return "Subject saved";
        } else {
            return "Subject not found";
        }
    }

    @DeleteMapping(value = "/{id}")
    public String delete(@PathVariable Integer id) {
        Optional<Subject> optionalSubject = subjectRepository.findById(id);
        if (optionalSubject.isPresent()) {
            subjectRepository.deleteById(id);
            return "Subject deleted";
        } else {
            return "Subject not found";
        }
    }


}
