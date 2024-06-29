package encyclopediaApp.Aspect;

import encyclopediaApp.Exception.CustomException;
import encyclopediaApp.Service.BannedUserService;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CheckBannedAspect {

    @Autowired
    private BannedUserService bannedUserService;

    @Before("@annotation(encyclopediaApp.Annotation.CheckBanned)")
    public void checkIfUserIsBanned(JoinPoint joinPoint) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (bannedUserService.isUserBanned(username)) {
            throw new CustomException("User is banned");
        }
    }
}