package school.report;

import java.util.ArrayList;

import grade.BasicEvaluation;
import grade.GradeEvaluation;
import grade.MajorEvaluation;
import school.School;
import school.Score;
import school.Student;
import school.Subject;

import utils.Define;

public class GenerateGradeReport {
	// 필드
	School school = School.getInstance(); // school 싱글톤 객체 생성

	public static final String TITLE = "수강생 학점 \t\t\n";
	public static final String HEADER = "이름 | 학번 | 필수과목 | 점수 \n";
	public static final String LINE = "=================================== \n";
	
	private StringBuffer buffer = new StringBuffer();
	//StringBuffer을 사용하는 이유 : String을 사용하면 새로운 객체를 생성해줄때마다 메모리 낭비가 심해서
	//StringBuffer를 사용하여 객체에 문자열을 쌓이게 한다.

	// 성적 산출 결과의 헤더, 몸통, 푸터를 합쳐준다.
	public String getReport() {
		ArrayList<Subject> subjectList = school.getSubjectList(); // 과목리스트 불러오기

		for (Subject subject : subjectList) { // 국어, 수학 총 두 번 실행
			makeHeader(subject);
			makeBody(subject);
			makeFooter();
		}

		return buffer.toString(); // buffer의 데이터를 String타입으로 바꾼다.

	}

	// 헤더를 만들어준다.
	public void makeHeader(Subject subject) {
		buffer.append(GenerateGradeReport.LINE);
		buffer.append("\t" + subject.getSubjectName());
		buffer.append(GenerateGradeReport.TITLE);
		buffer.append(GenerateGradeReport.HEADER);
		buffer.append(GenerateGradeReport.LINE);

	}

	// 몸통을 만든다.
	public void makeBody(Subject subject) {
		// 해당과목을 수강신청한 학생 리스트를 가져온다.
		ArrayList<Student> studentList = subject.getStudentList();

		for (int i = 0; i < studentList.size(); i++) {
			Student student = studentList.get(i);
			buffer.append(student.getStudentName()); // 학생이름
			buffer.append(" | ");
			buffer.append(student.getStudentId()); // 학번
			buffer.append(" | ");
			buffer.append(student.getMajorSubject().getSubjectName() + "\t"); // 학생필수과목
			buffer.append(" | ");

			// 학생별 수강과목의 점수 학점 계산
			getScoreGrade(student, subject.getSubjectId());
			buffer.append("\n");
			buffer.append(LINE);
		}
	}

	// 학생의 점수와 학점을 얻는 메소드
	public void getScoreGrade(Student student, int subjectId) {
		// 해당학생이 수강한 과목의 점수 리스트(국어, 수학)
		ArrayList<Score> scoreList = student.getScoreList();

		// 해당 학생의 필수과목의 과목코드
		int majorId = student.getMajorSubject().getSubjectId();

		//배열로 만듦.
		GradeEvaluation[] gradeEvaluations = { new BasicEvaluation(), new MajorEvaluation() };

		for (int i = 0; i < scoreList.size(); i++) { // 두번 돎
			Score score = scoreList.get(i); // 해당 과목의 점수를 받아온다. 지역변수 / 하나씩 뽑아오기

			// 과목 코드가 같은지 확인 subjectId 매개변수가 같은지 판단
			if (score.getSubject().getSubjectId() == subjectId) {
				String grade; // 학점

				//필수과목일 때 학점산출
				if (score.getSubject().getSubjectId() == majorId) {
					grade = gradeEvaluations[Define.SAB_TYPE].getGrade(score.getPoint());
					//grade = gradeEvaluations[인덱스 번호 1, 즉 new MajorEvaluation() 실행].getGrade(score.getPoint());
					
				} else { // 일반과목일 때 학점산출
					grade = gradeEvaluations[Define.AB_TYPE].getGrade(score.getPoint());
				}

				buffer.append(score.getPoint()); //해당과목의 점수
				buffer.append(":");
				buffer.append(grade); //해당과목의 학점
				buffer.append(" | ");
			}

		}
	}

	// 푸터를 만든다.
	public void makeFooter() {
		buffer.append("\n");
	}

}
