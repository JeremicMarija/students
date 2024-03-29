package com.marija.students.controller;

import com.marija.students.dto.FakultetDto;
import com.marija.students.dto.StudentDto;
import com.marija.students.model.Fakultet;
import com.marija.students.model.Student;
import com.marija.students.service.StudentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/student")
public class StudentRestController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private StudentService studentService;

    public StudentRestController(ModelMapper modelMapper, StudentService studentService) {
        this.modelMapper = modelMapper;
        this.studentService = studentService;
    }


    @GetMapping("/all")
    public @ResponseBody ResponseEntity<List<Student>>findAll(){

        List<Student> studenti = studentService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(studenti);
    }

    @GetMapping("/get/{studentId}")
    public @ResponseBody ResponseEntity<StudentDto>getStudent(@PathVariable String studentId){
        studentId = studentId.replace('-','/');
//        System.out.println(studentId);

        Student student = studentService.getStudentById(studentId);
        StudentDto studentResponse = modelMapper.map(student, StudentDto.class);
        List<Fakultet> fakultetList = student.getFakulteti();
        List<String>fakultetIdsList = new ArrayList<>();
        for (int i = 0; i < fakultetList.size(); i++){
            fakultetIdsList.add(fakultetList.get(i).getMaticniBroj());
//            System.out.println(fakultetList.get(i).getMaticniBroj());
//            System.out.println(fakultetIdsList);
        }
        studentResponse.setFakultetIds(fakultetIdsList);
        return ResponseEntity.status(HttpStatus.OK).body(studentResponse);

    }

    @PostMapping("/save")
    public ResponseEntity<Student>createStudent(@Valid @RequestBody StudentDto studentDto){

        Student student = studentService.createStudent(studentDto);
//        StudentDto studentResponse = modelMapper.map(student, StudentDto.class);
//        studentResponse.setFakultetId(student.getFakulteti().get(0).getMaticniBroj());
        return new ResponseEntity<Student>(student, HttpStatus.CREATED);
    }


    @PutMapping("/update")
    public ResponseEntity<Student> updateStudent(@Valid @RequestBody StudentDto studentDto){
        Student student = studentService.updateStudent(studentDto);
        return  new ResponseEntity<Student>(student, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{brojIndeksa}")
    public @ResponseBody ResponseEntity<String>delete(@PathVariable String brojIndeksa){
        //replace(-, /)
        brojIndeksa = brojIndeksa.replace('-','/');
        studentService.delete(brojIndeksa);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted");
    }

}
