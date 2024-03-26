package fr.univlyon1.m1if.m1if03.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.naming.InvalidNameException;
import javax.naming.NameNotFoundException;
import fr.univlyon1.m1if.m1if03.dao.UserDao;
import fr.univlyon1.m1if.m1if03.dto.todo.TodoRequestDto;
import fr.univlyon1.m1if.m1if03.dto.user.UserDtoMapper;
import fr.univlyon1.m1if.m1if03.dto.user.UserResponseDto;
import fr.univlyon1.m1if.m1if03.model.User;
import fr.univlyon1.m1if.m1if03.utils.UrlUtils;

/**
 * Filtre d'autorisation.
 * <ul>
 * <li>Vérifie que l'utilisateur authentifié a le droit d'accéder à certaines
 * ressources. Renvoie un code 403 (Forbidden) sinon.</li>
 * <li>Vérifie si l'utilisateur authentifié a le droit d'afficher complètement
 * certaines ressources. Positionne un attribut de requête en conséquence.</li>
 * </ul>
 *
 * @author Lionel Médini
 */
@WebFilter
public class CacheFilter extends HttpFilter {
    private Map<Integer, Date> todos = new HashMap<>();
    private boolean isModified = true;

    private UserDao userDao;
    private UserDtoMapper userMapper;

    public void init(FilterConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        if (req.getMethod().equals("GET")) {
            String[] url = UrlUtils.getUrlParts(req);
            switch (url[0]) {
                case "users" -> {
                    if (url.length > 1) {
                        String ifnonematch = req.getHeader("If-None-Match");
                        ServletContext context = req.getServletContext();
                        try {
                            userDao = (UserDao) context.getAttribute("userDao");
                            userMapper = new UserDtoMapper(context);
                            User user = userDao.findOne(url[1]);
                            UserResponseDto userDto = userMapper.toDto(user);
                            String etag = getTag(userDto);

                            if (ifnonematch != null && etag.equals(ifnonematch)) {
                                res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                                return;
                            }

                            res.setHeader("ETag", etag);
                            chain.doFilter(req, res);

                        } catch (NameNotFoundException | InvalidNameException e) {
                            e.printStackTrace();
                        }
                    } else {
                        chain.doFilter(req, res);
                        return;
                    }
                }
                case "todos" -> {
                    if (url.length > 1) {
                        url = UrlUtils.getUrlParts(req);
                        long ifModifiedSince = req.getDateHeader("If-Modified-Since");
                        if (ifModifiedSince != -1) {
                            Date lastModified = todos.get(Integer.parseInt(url[1]));
                            if (ifModifiedSince >= lastModified.getTime()) {
                                res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                                return;
                            }
                        }
                        res.setDateHeader("Last-Modified", new Date().getTime()+1000);
                        chain.doFilter(req, res);
                        return;
                    } else {
                        if (url.length == 1) {
                            long ifModifiedSince = req.getDateHeader("If-Modified-Since");
                            if (ifModifiedSince != -1) {

                                if (!todos.isEmpty()) {
                                    int i = 0; // Check if the "todos" list is not empty before iterating
                                    for (Map.Entry<Integer, Date> entry : todos.entrySet()) {
                                        if (entry.getValue().getTime() >= ifModifiedSince) {
                                            i = 1;
                                        }
                                    }
                                    if (i == 1) {
                                        res.setDateHeader("Last-Modified", new Date().getTime());
                                        isModified = true;
                                        chain.doFilter(req, res);
                                        return;
                                    } else {
                                        res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                                        return;
                                    }

                                } else {
                                    if (isModified) {
                                        res.setDateHeader("Last-Modified", new Date().getTime());
                                        isModified = false;
                                        chain.doFilter(req, res);
                                        return;
                                    } else {
                                        res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                                        return;
                                    }

                                }

                            }

                            // Set "Last-Modified" header and proceed with the request even if the list is
                            // empty
                            res.setDateHeader("Last-Modified", new Date().getTime());
                            if (todos.isEmpty() && isModified) {
                                isModified = false;

                            }

                            try {
                                chain.doFilter(req, res);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (ServletException e) {
                                e.printStackTrace();
                            }
                            return;
                        }

                    }

                }
                default -> chain.doFilter(req, res);
            }

        } else if (req.getMethod().equals("POST") || req.getMethod().equals("PUT")) {
            String[] url = UrlUtils.getUrlParts(req);

            switch (url[0]) {

                case "todos" -> {
                    TodoRequestDto requestDto = (TodoRequestDto) req.getAttribute("dto");
                    if (url.length > 1 && url[1].equals("toggleStatus")) {
                        todos.put(requestDto.getHash(), new Date());
                    } else {
                        if (req.getMethod().equals("PUT")) {
                            todos.put(Integer.parseInt(url[1]), new Date());
                        } else {
                            chain.doFilter(req, res);
                            String headerValue = res.getHeader("Location"); // Get the header value
                            String hash = headerValue.substring("todos/".length());
                            todos.put(Integer.parseInt(hash), new Date());
                            return;
                        }
                    }
                    chain.doFilter(req, res);
                    return;
                }
                default -> chain.doFilter(req, res);
            }
        } else if (req.getMethod().equals("DELETE")) {
            String[] url = UrlUtils.getUrlParts(req);
            switch (url[0]) {
                case "todos" -> {
                    chain.doFilter(req, res);
                    todos.remove(Integer.parseInt(url[1]));
                }
                default -> chain.doFilter(req, res);
            }
        } else {
            chain.doFilter(req, res);
        }
    }

    private String getTag(UserResponseDto user) {
        // Calculate the ETag based on the data
        String joiner = "";
        if (user.getLogin() != null) {
            joiner = joiner + user.getLogin().hashCode();
        }
        if (user.getName() != null) {
            joiner = joiner + user.getName().hashCode();
        }
        if (user.getAssignedTodos() != null) {
            joiner = joiner + user.getAssignedTodos().toString().hashCode();
        }

        // Hash the concatenated string to generate the ETag
        return joiner;
    }

}
