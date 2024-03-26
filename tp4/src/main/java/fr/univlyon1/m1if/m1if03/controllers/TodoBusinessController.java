package fr.univlyon1.m1if.m1if03.controllers;

import fr.univlyon1.m1if.m1if03.dao.TodoDao;
import fr.univlyon1.m1if.m1if03.dto.todo.TodoRequestDto;
import fr.univlyon1.m1if.m1if03.model.Todo;
import fr.univlyon1.m1if.m1if03.utils.TodosM1if03JwtHelper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;

import java.io.IOException;

import javax.naming.InvalidNameException;
import javax.naming.NameNotFoundException;

/**
 * Contrôleur d'opérations métier "users".<br>
 * Concrètement : gère les opérations de login et de logout.
 *
 * @author Lionel Médini
 */
@WebServlet(name = "TodoBusinessController", urlPatterns = { "/todos/toggleStatus" })
public class TodoBusinessController extends HttpServlet {
    private TodoBusiness todoBusiness;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        TodoDao todoDao = (TodoDao) config.getServletContext().getAttribute("todoDao");
        todoBusiness = new TodoBusiness(todoDao);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getRequestURI().endsWith("toggleStatus")) {
            try {
                TodoRequestDto requestDto = (TodoRequestDto) request.getAttribute("dto");

                int hash = requestDto.getHash();

                try {

                    if (todoBusiness.toggleStatus(hash, request)) {
                        response.sendError(HttpServletResponse.SC_NO_CONTENT);
                    } else {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    }
                    
                } catch (IllegalArgumentException ex) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage()+ hash);
                } catch (NameNotFoundException e) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Le todo " + hash + " n'existe pas.");
                } catch (InvalidNameException ignored) {
                    // Ne doit pas arriver car les logins des utilisateurs sont des Strings
                }
            } catch (IOException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Erreur lors de traitement de la requete.");
            } catch (UnsupportedOperationException e) {
                response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, e.getMessage());
            }
        } else {
            // Ne devrait pas arriver mais sait-on jamais...
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private static class TodoBusiness {
        private final TodoDao todoDao;

        TodoBusiness(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        public boolean toggleStatus(@NotNull int hash, HttpServletRequest request)
                throws IllegalArgumentException, InvalidNameException, NameNotFoundException {
            Todo todo = todoDao.findByHash(hash);
            if (TodosM1if03JwtHelper.isAssigned((String) request.getAttribute("token"), hash)) {
                todo.setCompleted(!todo.isCompleted());
                return true;
            } else {
                return false;
            }
        }

    }
}
