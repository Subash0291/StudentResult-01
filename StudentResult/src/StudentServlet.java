import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;
import java.sql.*;

@WebServlet("/addStudent")
public class StudentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String name  = req.getParameter("name");
        String email = req.getParameter("email");

        int m1 = Integer.parseInt(req.getParameter("math"));
        int m2 = Integer.parseInt(req.getParameter("science"));
        int m3 = Integer.parseInt(req.getParameter("english"));
        int m4 = Integer.parseInt(req.getParameter("history"));
        int m5 = Integer.parseInt(req.getParameter("computer"));

        try (Connection con = DBConnection.getConnection()) {

            // Insert student
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO students (name, email) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, name);
            ps.setString(2, email);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int studentId = 0;
            if (rs.next()) studentId = rs.getInt(1);

            // Insert marks
            String[] subjects = {"Math", "Science", "English", "History", "Computer"};
            int[]    scores   = {m1, m2, m3, m4, m5};

            PreparedStatement ms = con.prepareStatement(
                "INSERT INTO marks (student_id, subject, marks) VALUES (?, ?, ?)"
            );
            for (int i = 0; i < subjects.length; i++) {
                ms.setInt(1, studentId);
                ms.setString(2, subjects[i]);
                ms.setInt(3, scores[i]);
                ms.addBatch();
            }
            ms.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        res.sendRedirect("results");
    }
}
