package com.ryulth.timeline.apis.security;

import com.ryulth.timeline.account.service.JwtService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor implements HandlerInterceptor {
    private static final String HEADER_AUTH = "Authorization";

    private final JwtService jwtService;

    public TokenInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        try {
        final String token = request.getHeader(HEADER_AUTH).split("bearer ")[1];
            //TODO refactor
            if (token != null && jwtService.isUsable(token)) {
                String email = jwtService.getEmailFromAccessToken(token);
                request.getSession().setAttribute("email",email);
                return true;
            }
        }catch (ArrayIndexOutOfBoundsException e){
            response.sendError(401,"{ \"error\" : \"Authorization Format Invalid," +
                    " Must Be {TokenType} {AccessToken}\" }");
            response.setStatus(401);
            return false;
        }
        catch (NullPointerException e){
            response.sendError(401,"{ \"error\" : \"Need Access Token in Authorization Header\" }");
            response.setStatus(401);
            return false;
        }
        catch (UnauthorizedException e) {
            response.sendError(401,"{ \"error\" : \"" + e.getMessage() + "\" }");
            response.setStatus(401);
            return false;
        }
        response.sendError(401,"{ \"error\" : \"Unauthorized Access Token\" }");
        response.setStatus(401);
        return false;
    }
}
