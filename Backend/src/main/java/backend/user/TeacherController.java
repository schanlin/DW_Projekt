package backend.user;

import java.util.List;

import backend.subject.Subject;
import backend.subject.SubjectDao;
import backend.test.Test;
import backend.test.TestDao;
import backend.test_result.AverageByStudent;
import backend.test_result.TestResult;
import backend.test_result.TestResultDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TeacherController {
	private final TeacherDao teacherDao;
	private final SubjectDao subjectDao;
	private final TestDao testDao;
	private final TestResultDao testResultDao;

	public TeacherController(TeacherDao teacherDao, SubjectDao subjectDao, TestDao testDao, TestResultDao testResultDao) {
		this.teacherDao = teacherDao;
		this.subjectDao = subjectDao;
		this.testDao = testDao;
		this.testResultDao = testResultDao;
	}
	
	@GetMapping("/teacher")
	public List<Teacher> getAllTeachers() {
		List<Teacher> teachers = teacherDao.findAll();
		return teachers;
	}
	
	@GetMapping("/teacher/{id}")
	public Teacher getTeacherById(@PathVariable int id) {
		Teacher teacher = teacherDao.findById(id);
		if (teacher==null){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return teacher;
	}

	@GetMapping("/teacher/{id}/subjects")
	public List<Subject> getSubjectsByTeacher(@PathVariable int id) {
		return subjectDao.findByTeacher(id);
	}


	@GetMapping("/teacher/{teacherId}/subjects/{subjectId}/overview")
	public List<AverageByStudent> getAverageByStudent(@PathVariable int subjectId) {
		return testResultDao.findAllAverageByStudentAndSubject(subjectId);
	}

	@GetMapping("/teacher/{teacherId}/subjects/{subjectId}/tests")
	public List<Test> getTestsByTeacherAndSubject(@PathVariable int subjectId) {
		return testDao.findBySubject(subjectId);
	}

	@GetMapping("/teacher/{teacherId}/subjects/{subjectId}/tests/{testId}/results")
	public List<TestResult> getResultsByTest(@PathVariable int testId) {
		return testResultDao.findByTest(testId);
	}

	@PostMapping("/teacher/{teacherId}/subjects/{subjectId}/tests/{testId}/results")
	public ResponseEntity postNewMark(@RequestBody TestResult testResult) {
		int status = testResultDao.insertOrUpdate(testResult);
		if (status==1) {
			return new ResponseEntity<>(HttpStatus.CREATED);
		} else if (status==2) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			throw new IllegalStateException();
		}
	}

}
