import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;
import java.sql.*;
import java.util.*;

@WebServlet("/results")
public class ResultServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        List<Student> students = new ArrayList<>();

        String sql = "SELECT s.id, s.name, s.email, SUM(m.marks) AS total " +
                     "FROM students s JOIN marks m ON s.id = m.student_id " +
                     "GROUP BY s.id, s.name, s.email ORDER BY total DESC";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                students.add(new Student(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getInt("total")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        long passCount = students.stream().filter(s -> s.getStatus().equals("Pass")).count();
        long failCount = students.size() - passCount;
        double classAvg = students.stream().mapToDouble(Student::getAverage).average().orElse(0);

        res.setContentType("text/html; charset=UTF-8");
        res.setCharacterEncoding("UTF-8");
        PrintWriter out = res.getWriter();

        out.println("<!DOCTYPE html><html lang='en'><head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>Results | Student Result System</title>");
        out.println("<link href='https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap' rel='stylesheet'>");
        out.println("<style>");
        out.println("*{margin:0;padding:0;box-sizing:border-box;font-family:'Poppins',sans-serif;}");
        out.println("body{min-height:100vh;background:linear-gradient(135deg,#1a1a2e,#16213e,#0f3460);padding:30px 20px;}");

        // Header
        out.println(".header{text-align:center;margin-bottom:35px;}");
        out.println(".header h1{color:white;font-size:28px;font-weight:700;}");
        out.println(".header p{color:rgba(255,255,255,0.5);font-size:13px;margin-top:4px;}");

        // Stats cards
        out.println(".stats{display:grid;grid-template-columns:repeat(4,1fr);gap:15px;max-width:950px;margin:0 auto 25px;}");
        out.println(".stat-card{background:rgba(255,255,255,0.07);border:1px solid rgba(255,255,255,0.1);border-radius:16px;padding:20px;text-align:center;backdrop-filter:blur(10px);}");
        out.println(".stat-card .num{font-size:32px;font-weight:700;color:white;}");
        out.println(".stat-card .lbl{font-size:12px;color:rgba(255,255,255,0.5);margin-top:4px;text-transform:uppercase;letter-spacing:1px;}");
        out.println(".stat-card.blue .num{color:#60a5fa;}");
        out.println(".stat-card.green .num{color:#34d399;}");
        out.println(".stat-card.red .num{color:#f87171;}");
        out.println(".stat-card.yellow .num{color:#fbbf24;}");

        // Table card
        out.println(".card{background:rgba(255,255,255,0.05);border:1px solid rgba(255,255,255,0.1);border-radius:20px;overflow:hidden;max-width:950px;margin:0 auto;backdrop-filter:blur(10px);}");
        out.println(".card-top{display:flex;justify-content:space-between;align-items:center;padding:20px 25px;border-bottom:1px solid rgba(255,255,255,0.08);}");
        out.println(".card-top h2{color:white;font-size:16px;font-weight:600;}");
        out.println(".btn{display:inline-block;padding:10px 22px;border-radius:10px;font-size:13px;font-weight:600;text-decoration:none;cursor:pointer;border:none;font-family:inherit;transition:all 0.2s;}");
        out.println(".btn-primary{background:linear-gradient(135deg,#e94560,#c62a47);color:white;box-shadow:0 4px 15px rgba(233,69,96,0.4);}");
        out.println(".btn-primary:hover{transform:translateY(-2px);}");

        // Table
        out.println("table{width:100%;border-collapse:collapse;}");
        out.println("th{background:rgba(255,255,255,0.05);color:rgba(255,255,255,0.5);padding:12px 20px;text-align:left;font-size:11px;font-weight:600;text-transform:uppercase;letter-spacing:1px;}");
        out.println("td{padding:14px 20px;color:rgba(255,255,255,0.85);font-size:14px;border-bottom:1px solid rgba(255,255,255,0.05);}");
        out.println("tr:hover td{background:rgba(255,255,255,0.04);}");

        // Avatar
        out.println(".avatar{width:36px;height:36px;border-radius:50%;display:inline-flex;align-items:center;justify-content:center;font-weight:700;font-size:14px;margin-right:10px;}");
        out.println(".name-cell{display:flex;align-items:center;}");

        // Badges
        out.println(".badge{display:inline-block;padding:4px 14px;border-radius:20px;font-size:12px;font-weight:600;}");
        out.println(".badge-pass{background:rgba(52,211,153,0.15);color:#34d399;border:1px solid rgba(52,211,153,0.3);}");
        out.println(".badge-fail{background:rgba(248,113,113,0.15);color:#f87171;border:1px solid rgba(248,113,113,0.3);}");
        out.println(".grade-A{color:#fbbf24;font-weight:700;}.grade-B{color:#60a5fa;font-weight:700;}.grade-C{color:#a78bfa;font-weight:700;}.grade-D{color:#fb923c;font-weight:700;}.grade-F{color:#f87171;font-weight:700;}");

        // Progress bar
        out.println(".bar-bg{background:rgba(255,255,255,0.1);border-radius:10px;height:6px;width:100px;display:inline-block;vertical-align:middle;margin-left:8px;}");
        out.println(".bar-fill{height:6px;border-radius:10px;}");

        // Empty
        out.println(".empty{text-align:center;padding:60px;color:rgba(255,255,255,0.3);font-size:15px;}");
        out.println("</style></head><body>");

        // Header
        out.println("<div class='header'><h1>Student Result System</h1><p>Performance overview of all students</p></div>");

        // Stats
        out.printf("<div class='stats'>");
        out.printf("<div class='stat-card blue'><div class='num'>%d</div><div class='lbl'>Total Students</div></div>", students.size());
        out.printf("<div class='stat-card green'><div class='num'>%d</div><div class='lbl'>Passed</div></div>", passCount);
        out.printf("<div class='stat-card red'><div class='num'>%d</div><div class='lbl'>Failed</div></div>", failCount);
        out.printf("<div class='stat-card yellow'><div class='num'>%.1f</div><div class='lbl'>Class Average</div></div>", classAvg);
        out.println("</div>");

        // Card
        out.println("<div class='card'>");
        out.println("<div class='card-top'><h2>All Results</h2><a href='index.html' class='btn btn-primary'>+ Add Student</a></div>");

        if (students.isEmpty()) {
            out.println("<div class='empty'>No student records found. Add a student to get started.</div>");
        } else {
            out.println("<table>");
            out.println("<tr><th>#</th><th>Student</th><th>Email</th><th>Total</th><th>Average</th><th>Grade</th><th>Status</th></tr>");

            String[] colors = {"#e94560","#60a5fa","#34d399","#fbbf24","#a78bfa","#fb923c","#f472b6"};
            int i = 0;
            for (Student s : students) {
                String color = colors[i % colors.length];
                String initial = s.getName().substring(0, 1).toUpperCase();
                String gradeClass = "grade-" + (s.getGrade().equals("A+") ? "A" : s.getGrade());
                String statusBadge = s.getStatus().equals("Pass") ? "badge-pass" : "badge-fail";
                int pct = (int)((s.getAverage() / 100.0) * 100);
                String barColor = s.getAverage() >= 50 ? "#34d399" : "#f87171";

                out.printf("<tr>");
                out.printf("<td style='color:rgba(255,255,255,0.3);font-size:13px;'>%02d</td>", i + 1);
                out.printf("<td><div class='name-cell'><div class='avatar' style='background:rgba(255,255,255,0.08);color:%s;border:2px solid %s;'>%s</div>%s</div></td>",
                    color, color, initial, s.getName());
                out.printf("<td style='color:rgba(255,255,255,0.5);font-size:13px;'>%s</td>", s.getEmail());
                out.printf("<td><strong>%d</strong><span style='color:rgba(255,255,255,0.3);font-size:12px;'>/500</span></td>", s.getTotal());
                out.printf("<td>%.1f <div class='bar-bg'><div class='bar-fill' style='width:%d%%;background:%s;'></div></div></td>",
                    s.getAverage(), pct, barColor);
                out.printf("<td><span class='%s'>%s</span></td>", gradeClass, s.getGrade());
                out.printf("<td><span class='badge %s'>%s</span></td>", statusBadge, s.getStatus());
                out.println("</tr>");
                i++;
            }
            out.println("</table>");
        }

        out.println("</div></body></html>");
    }
}
