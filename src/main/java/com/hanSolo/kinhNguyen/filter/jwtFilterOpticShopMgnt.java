package com.hanSolo.kinhNguyen.filter;

import com.hanSolo.kinhNguyen.cacheCenter.CommonCache;
import com.hanSolo.kinhNguyen.models.Member;
import com.hanSolo.kinhNguyen.models.MemberRole;
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

public class jwtFilterOpticShopMgnt extends GenericFilterBean {


    @SuppressWarnings("unchecked")
    @Override
    public void doFilter(final ServletRequest req,
                         final ServletResponse res,
                         final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        //final HttpServletResponse response = (HttpServletResponse) res;

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("sheep ")) {
            throw new ServletException("Missing or invalid Authorization header.");
        }

        final String token = authHeader.substring(6); // The part after "sheep "

        try {
            final Claims claims = Jwts.parser()
                    .setSigningKey(Utility.SECRET_KEY.getBytes("UTF-8"))
                    .parseClaimsJws(token).getBody();

            if (!((List<String>) claims.get("roles")).contains(Utility.GODLIKE_ROLE)
            ) {
                throw new ServletException("Unauthorized action");
            }

            if(CommonCache.LOGIN_MEMBER_LIST.containsKey(claims.get("sub"))){
                Member currMem = CommonCache.LOGIN_MEMBER_LIST.getOrDefault(claims.get("sub"),null);
                if(currMem == null || !currMem.getStatus()){
                    throw new ServletException("USER_UNAVAILABLE");
                }
                boolean isGodLike = false;
                for (MemberRole role : currMem.getMemberRoles()) {
                    if(role.getRole().equals(Utility.GODLIKE_ROLE)){
                        isGodLike = true;
                        break;
                    }
                }
                if(!isGodLike){
                    throw new ServletException("USER_UNAVAILABLE");
                }

            }else{
                throw new ServletException("USER_NOT_EXIST");
            }

            request.setAttribute("claims", claims);
        } catch (final SignatureException e) {
            throw new ServletException("Invalid token.");
        }

        chain.doFilter(req, res);
    }

}