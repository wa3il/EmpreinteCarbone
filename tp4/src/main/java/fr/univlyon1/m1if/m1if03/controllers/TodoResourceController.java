package fr.univlyon1.m1if.m1if03.controllers;

import fr.univlyon1.m1if.m1if03.dao.TodoDao;
import fr.univlyon1.m1if.m1if03.dto.todo.TodoDtoMapper;
import fr.univlyon1.m1if.m1if03.dto.todo.TodoRequestDto;
import fr.univlyon1.m1if.m1if03.dto.todo.TodoResponseDto;
import fr.univlyon1.m1if.m1if03.model.Todo;
import fr.univlyon1.m1if.m1if03.utils.TodosM1if03JwtHelper;
import fr.univlyon1.m1if.m1if03.utils.UrlUtils;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import javax.naming.InvalidNameException;
import javax.naming.NameNotFoundException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Contrôleur de ressources "users".<br>
 * Gère les CU liés aux opérations CRUD sur la collection d'utilisateurs :
 * <ul>
 * <li>Création / modification / suppression d'un utilisateur : POST, PUT,
 * DELETE</li>
 * <li>Récupération de la liste d'utilisateurs / d'un utilisateur / d'une
 * propriété d'un utilisateur : GET</li>
 * </ul>
 * Cette servlet fait appel à une <i>nested class</i> <code>UserResource</code>
 * qui se charge des appels au DAO.
 * Les opérations métier spécifiques (login &amp; logout) sont réalisées par la
 * servlet <a href=
 * "UserBusinessController.html"><code>UserBusinessController</code></a>.
 *
 * @author Lionel Médini
 */
@WebServlet(name = "TodoResourceController", urlPatterns = { "/todos", "/todos/*" })
public class TodoResourceController extends HttpServlet {
    private TodoDtoMapper todoMapper;
    private TodoResource todoResource;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        TodoDao todoDao = (TodoDao) config.getServletContext().getAttribute("todoDao");
        todoMapper = new TodoDtoMapper(config.getServletContext());
        todoResource = new TodoResource(todoDao);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] url = UrlUtils.getUrlParts(request);

        if (url.length == 1) {
            try {
            TodoRequestDto requestDto = (TodoRequestDto) request.getAttribute("dto");

                try {
                    int hash = todoResource.create(requestDto.getTitle(), requestDto.getCreator());
                    response.setHeader("Location", "todos/" + hash);
                    response.setStatus(HttpServletResponse.SC_CREATED);
                } catch (IllegalArgumentException ex) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                }
            } catch (IOException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Erreur lors de traitement de la requete.");
            } catch (UnsupportedOperationException e) {
                response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, e.getMessage());
            }

        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] url = UrlUtils.getUrlParts(request);

        if (url.length == 1) { // Renvoie la liste de tous les todos

            request.setAttribute("model", todoResource.readAll());
            request.setAttribute("view", "todos/todos");
            response.setStatus(HttpServletResponse.SC_OK);

            return;
        }
        try {
            Todo todo = todoResource.readOne(Integer.valueOf(url[1]));
            TodoResponseDto todoDto = todoMapper.toDto(todo);

            switch (url.length) {
                case 2 -> { 
                    request.setAttribute("model", todoDto);
                    request.setAttribute("view", "todos/todo");
                    if (todoDto.getAssignee() != null) {
                        response.addHeader("Link", "<users/" + todoDto.getAssignee() + ">; rel=\"users\"");
                    }
                    response.setStatus(HttpServletResponse.SC_OK);

                }
                case 3 -> { // Renvoie une propriété d'un todo
                    switch (url[2]) {
                        case "title" -> {
                            request.setAttribute("model",
                                    new TodoResponseDto(todoDto.getTitle(), todoDto.getHash(), null, null));
                            request.setAttribute("view", "/todos/todoProperty");
                        }
                        case "assignee" -> {
                            request.setAttribute("model",
                                    new TodoResponseDto(null, todoDto.getHash(), todoDto.getAssignee(), null));
                            request.setAttribute("view", "/todos/todoProperty");
                            if (todoDto.getAssignee() != null) {
                                response.addHeader("Link", "<users/" + todoDto.getAssignee() + ">; rel=\"users\"");
                            }
                            response.setStatus(HttpServletResponse.SC_OK);

                        }
                        case "status" -> {
                            request.setAttribute("model",
                                    new TodoResponseDto(null, todoDto.getHash(), null, todoDto.getCompleted()));
                            request.setAttribute("view", "/todos/todoProperty");
                            response.setStatus(HttpServletResponse.SC_OK);

                        }
                        default -> response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Propriété demandée erronée.");
                    }
                }
                default -> { // Redirige vers l'URL qui devrait correspondre à la sous-propriété demandée
                             // (qu'elle existe ou pas ne concerne pas ce contrôleur)
                    if (url[2].equals("assignee")) {
                        // Construction de la fin de l'URL vers laquelle rediriger
                        String urlEnd = UrlUtils.getUrlEnd(request, 3);
                        response.sendRedirect("users" + urlEnd);
                    } else {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Trop de paramètres dans l'URI.");
                    }
                }
            }
        } catch (IllegalArgumentException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        } catch (NameNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Le todo " + url[1] + " n'existe pas.");
        } catch (InvalidNameException ignored) {
            // Ne devrait pas arriver car les paramètres sont déjà des Strings
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] url = UrlUtils.getUrlParts(request);
        int hash = Integer.parseInt(url[1]);
        TodoRequestDto requestDto = (TodoRequestDto) request.getAttribute("dto");

        try {
            if (url.length == 2) {
                try {
                    todoResource.update(hash, requestDto.getTitle(), requestDto.getAssignee());
                    String token = (String) request.getAttribute("token");
                    String user = TodosM1if03JwtHelper.verifyToken(token, request);
                    List<Integer> ownedTodos = new ArrayList<>();
                    Collection<Todo> todos = todoResource.read();
                    for (Todo todo : todos) {
                        if (todo.getAssignee() != null && todo.getAssignee().equals(user)) {
                            ownedTodos.add(todo.hashCode());
                        }
                    }
                    token = TodosM1if03JwtHelper.generateToken(user, ownedTodos, request);
                    response.setHeader("Authorization", "Bearer " + token);
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } catch (IllegalArgumentException ex) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                } catch (NameNotFoundException e) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Le todo " + hash + " n'existe pas.");
                } catch (InvalidNameException ignored) {
                    // Ne devrait pas arriver car les paramètres sont déjà des Strings
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (IOException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Erreur lors de traitement de la requete.");
        } catch (UnsupportedOperationException e) {
            response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] url = UrlUtils.getUrlParts(request);
        Integer hash = Integer.parseInt(url[1]);
        if (url.length == 2) {
            try {
                todoResource.delete(hash);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } catch (IllegalArgumentException ex) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
            } catch (NameNotFoundException e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Le todo " + url[1] + " n'existe pas.");
            } catch (InvalidNameException ignored) {
                // Ne devrait pas arriver car les paramètres sont déjà des Strings
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private static class TodoResource {
        private final TodoDao todoDao;

        TodoResource(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        public Integer create(@NotNull String title, @NotNull String creator) throws IllegalArgumentException {
            if (title == null || title.isEmpty()) {
                throw new IllegalArgumentException("Le title ne doit pas être null ou vide.");
            }
            if (creator == null || creator.isEmpty()) {
                throw new IllegalArgumentException("Le login ne doit pas être null ou vide.");
            }
            Todo todo = new Todo(title, creator);
            todoDao.add(todo);
            return todo.hashCode();
        }

        public Collection<Integer> readAll() {
            // Implement this method to retrieve all todos
            return todoDao.findAll().stream().map(Todo::hashCode).toList();
        }
                public Collection<Todo> read() {
            // Implement this method to retrieve all todos
            return todoDao.findAll();
        }

        public Todo readOne(@NotNull int id)
                throws IllegalArgumentException, NameNotFoundException, InvalidNameException {
            return todoDao.findByHash(id);
        }

        public void update(@NotNull int id, String title, String assignee)
                throws IllegalArgumentException, NameNotFoundException, InvalidNameException {
            Todo todo = readOne(id);
            if (assignee != null && !assignee.isEmpty()) {
                todo.setAssignee(assignee);
            }
            if (title != null && !title.isEmpty()) {
                todo.setTitle(title);
            }
        }

        public void delete(@NotNull int id)
                throws IllegalArgumentException, NameNotFoundException, InvalidNameException {
            todoDao.deleteById(todoDao.getId(readOne(id)));
        }
    }
}
