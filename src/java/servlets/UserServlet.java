
package servlets;

import models.Role;
import models.User;
import services.UserService;
import java.util.List;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author RJ
 */

public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserService us = new UserService();
        
        String action = request.getParameter("action");
        String email = request.getParameter("email");

        try {
            switch (action) {
                case "edit":
                    User user = us.get(email);
                    String fname = user.getFirstName();
                    String lname = user.getLastName();
                    String em = user.getEmail();
                    int id = user.getRole().getRoleId();
                    request.setAttribute("fname", fname);
                    request.setAttribute("lname", lname);
                    request.setAttribute("em", em);
                    request.setAttribute("id", id);
                    break;
                case "delete":
                    us.delete(email);
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            if (action != null) {
                request.setAttribute("message", "Could not perform action");
            }
        }
        
        try {
            List<User> users = us.getAll();
            
            request.setAttribute("users", users);
        } catch(Exception e)  {
            request.setAttribute("message", "No users found");  
        }
        
        getServletContext().getRequestDispatcher("/WEB-INF/users.jsp").forward(request, response);
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String email = request.getParameter("email");
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String password = request.getParameter("password");
        int roleID = Integer.parseInt(request.getParameter("role"));
        Boolean active = false;
        Role role = null;
        
        switch (roleID) {
            case 1:
                role = new Role(1, "System Admin");
            break;
            case 2:
                role = new Role(2, "Regular User");
            break;
            case 3:
                role = new Role(3, "Company Admin");
            break;
            default:
                role = new Role(2, "Regular User");
        }
        
        UserService us = new UserService();

        try {
            switch (action) {
                case "add":
                    us.insert(email, active, firstname, lastname, password, role);
                    break;
                case "update":
                    us.update(email, active, firstname, lastname, password, role);
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            request.setAttribute("message", "Could not perform action");
        }
        
         try {
            List<User> users = us.getAll();

            request.setAttribute("users", users);
        } catch(Exception e)  {
            request.setAttribute("message", "No users found");  
        }

        getServletContext().getRequestDispatcher("/WEB-INF/users.jsp").forward(request, response);
    }

}
