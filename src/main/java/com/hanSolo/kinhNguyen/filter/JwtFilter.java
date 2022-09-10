package com.hanSolo.kinhNguyen.filter;

import com.hanSolo.kinhNguyen.utility.Utility;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public class JwtFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("sheep ")) {
            throw new ServletException("Missing or invalid Authorization header.");
        }

        final String token = authHeader.substring(6); // The part after "sheep "

        try {
            final Claims claims = Jwts.parser().setSigningKey(Utility.SECRET_KEY.getBytes("UTF-8"))
                    .parseClaimsJws(token).getBody();

            if(!((List<String>) claims.get("roles")).contains(Utility.MEMBER_ROLE)){
                throw new ServletException("Unauthorized action");
            }

            request.setAttribute("claims", claims);
        }
        catch (final SignatureException e) {
            throw new ServletException("Invalid token.");
        }

        filterChain.doFilter(req, res);
    }
}
