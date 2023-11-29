package fr.polytech.annotation;


import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("(hasRole('client_candidate') or hasRole('client_admin')) or hasRole('client_recruiter') and @offerService.isRecruiterInCompany( #id, #token)")
public @interface IsCandidateOrIsAdminOrIsRecruiterAndInOfferCompany {
}
