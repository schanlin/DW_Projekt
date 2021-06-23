package backend;

import java.sql.Date;
import java.sql.SQLException;

import javax.annotation.PostConstruct;

import backend.subject.Subject;
import backend.subject.SubjectDao;
import backend.test.Test;
import backend.test.TestDao;
import backend.test_result.TestResult;
import backend.test_result.TestResultDao;
import backend.user.Student;
import backend.user.StudentDao;
import backend.user.User;
import backend.user.UserDao;
import org.springframework.stereotype.Component;

import backend.klasse.Klasse;
import backend.klasse.KlasseDao;

@Component
public class DemoDataInitializer {
	private final KlasseDao klasseDao;
	private final UserDao userDao;
	private final StudentDao studentDao;
	private final SubjectDao subjectDao;
	private final TestDao testDao;
	private final TestResultDao testResultDao;

	public DemoDataInitializer(KlasseDao klasseDao, UserDao userDao, StudentDao studentDao, SubjectDao subjectDao,
							   TestDao testDao, TestResultDao testResultDao) {
		this.klasseDao = klasseDao;
		this.userDao = userDao;
		this.studentDao = studentDao;
		this.subjectDao = subjectDao;
		this.testDao = testDao;
		this.testResultDao = testResultDao;
	}
	
	@PostConstruct
	public void init() throws SQLException {
		KlasseDao.createTable();
		if(klasseDao.count() > 0) {
			System.out.println("Data exists.");
			return;
		}
		System.out.println("Create Demo Data...");
		 klasseDao.insert(new Klasse("2016a"));
		 klasseDao.insert(new Klasse("2016b"));

		 UserDao.createTable();
		 userDao.insert(new User("admin", "PASSWORT", "Admini", "Strator", "Admin"));
		 userDao.insert(new User("storm", "PASSWORT", "Zoya", "Nazyalenski", "Lehrende"));
		 userDao.insert(new User("hunter", "PASSWORT", "Jarl", "Brum", "Lehrende"));
		 userDao.insert(new User("tailor", "PASSWORT", "Genya", "Safin", "Lehrende"));

		 studentDao.insert(new Student("demjin", "PASSWORT", "Kaz", "Brekker", "Lernende", 1));
		 studentDao.insert(new Student("phantom", "PASSWORT", "Inej", "Ghaza", "Lernende", 1));
		 studentDao.insert(new Student("sharpshooter", "PASSWORT", "Jesper Llewellyn", "Fahey", "Lernende", 1));
		 studentDao.insert(new Student("merchling", "PASSWORT", "Wylan", "Van Eck", "Lernende", 1));
		 studentDao.insert(new Student("redbird", "PASSWORT", "Nina", "Zenik", "Lernende", 2));
		 studentDao.insert(new Student("trassel", "PASSWORT", "Matthias Benedik", "Helvar", "Lernende", 2));
		 studentDao.insert(new Student("sunny", "PASSWORT", "Alina", "Starkov", "Lernende", 2));
		 studentDao.insert(new Student("brief", "PASSWORT", "Malyen", "Oretsev", "Lernende", 2));
		 userDao.insert(new Student("phoenix", "PASSWORT", "Kuwei", "Yul-Bo", "Lernende"));

		 SubjectDao.createTable();
		 subjectDao.insert(new Subject("Deutsch", 1, 2, false));
		 subjectDao.insert(new Subject("Mathematik", 1, 4, false));
		 subjectDao.insert(new Subject("Biologie", 1, 4, false));
		 subjectDao.insert(new Subject("Deutsch", 2, 2, false));
		 subjectDao.insert(new Subject("Mathematik", 2, 4, false));
		 subjectDao.insert(new Subject("Biologie", 2, 4, false));

		TestDao.createTable();
		testDao.insert(new Test("Grammatik", new Date(1483225200000L), 1));
		testDao.insert(new Test("Gedichtinterpretation", new Date(1483225200000L), 1));
		testDao.insert(new Test("Essay", new Date(1483225200000L), 1));
		testDao.insert(new Test("Pflanzen", new Date(1483225200000L), 6));
		testDao.insert(new Test("Evolutionstheorie", new Date(1483225200000L), 6));
		testDao.insert(new Test("Sexualkunde", new Date(1483225200000L), 6));

		TestResultDao.createTable();
		testResultDao.insert(new TestResult(1, 5, 1));
		testResultDao.insert(new TestResult(1, 6, 4));
		testResultDao.insert(new TestResult(1, 7, 3));
		testResultDao.insert(new TestResult(1, 8, 1));
		testResultDao.insert(new TestResult(2, 5, 2));
		testResultDao.insert(new TestResult(2, 6, 2));
		testResultDao.insert(new TestResult(2, 7, 4));
		testResultDao.insert(new TestResult(2, 8, 3));
		testResultDao.insert(new TestResult(3, 5, 1));
		testResultDao.insert(new TestResult(3, 6, 3));
		testResultDao.insert(new TestResult(3, 7, 2));
		testResultDao.insert(new TestResult(3, 8, 4));
		testResultDao.insert(new TestResult(4, 9, 3));
		testResultDao.insert(new TestResult(4, 10, 2));
		testResultDao.insert(new TestResult(4, 11, 1));
		testResultDao.insert(new TestResult(4, 12, 4));
		testResultDao.insert(new TestResult(5, 9, 4));
		testResultDao.insert(new TestResult(5, 10, 2));
		testResultDao.insert(new TestResult(5, 11, 4));
		testResultDao.insert(new TestResult(5, 12, 2));
		testResultDao.insert(new TestResult(6, 9, 1));
		testResultDao.insert(new TestResult(6, 10, 3));
		testResultDao.insert(new TestResult(6, 11, 1));
		testResultDao.insert(new TestResult(6, 12, 2));
	}
	
}
