package greencity.config;

import greencity.security.filters.AccessTokenAuthenticationFilter;
import greencity.security.jwt.JwtTool;
import greencity.security.providers.JwtAuthenticationProvider;
import greencity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import java.util.Arrays;
import java.util.Collections;
import static greencity.constant.AppConstant.*;
import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Config for security.
 *
 * @author Nazar Stasyuk && Yurii Koval
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
@EnableMethodSecurity
public class SecurityConfig {
    private static final String ECONEWS_COMMENTS = "/econews/comments";
    private static final String USER_CUSTOM_SHOPPING_LIST_ITEMS = "/user/{userId}/custom-shopping-list-items";
    private static final String CUSTOM_SHOPPING_LIST = "/custom/shopping-list-items/{userId}";
    private static final String CUSTOM_SHOPPING_LIST_URL = "/custom/shopping-list-items/{userId}/"
            + "custom-shopping-list-items";
    private static final String CUSTOM_SHOPPING_LIST_ITEMS = "/{userId}/custom-shopping-list-items";
    private static final String HABIT_ASSIGN_ID = "/habit/assign/{habitId}";
    private static final String USER_SHOPPING_LIST = "/user/shopping-list-items";
    private final JwtTool jwtTool;
    private final UserService userService;
    private final AuthenticationConfiguration authenticationConfiguration;

    /**
     * Constructor.
     */
    @Autowired
    public SecurityConfig(JwtTool jwtTool, UserService userService,
                          AuthenticationConfiguration authenticationConfiguration) {
        this.jwtTool = jwtTool;
        this.userService = userService;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    /**
     * Bean {@link PasswordEncoder} that uses in coding password.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Method for configure security.
     *
     * @param http {@link HttpSecurity}
     */
    @Bean
    public SecurityFilterChain applicationSecurity(HttpSecurity http) throws Exception {
        http.cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
            config.setAllowedOrigins(Collections.singletonList("http://localhost:4205"));
            config.setAllowedMethods(
                            Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"));
            config.setAllowedHeaders(
                            Arrays.asList("Access-Control-Allow-Origin", "Access-Control-Allow-Headers",
                                    "X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization"));
            config.setAllowCredentials(true);
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setMaxAge(3600L);
            return config;
        }))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .addFilterBefore(
                        new AccessTokenAuthenticationFilter(jwtTool, authenticationManager(), userService),
                        UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception.authenticationEntryPoint((req, resp, exc) -> resp
                                .sendError(SC_UNAUTHORIZED, "Authorize first."))
                        .accessDeniedHandler((req, resp, exc) ->
                                resp.sendError(SC_FORBIDDEN, "You don't have authorities.")))
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/", "/management/", "/management/login").permitAll()
                        .requestMatchers("/v2/api-docs/**", "/v3/api-docs/**", "/swagger.json",
                                "/swagger-ui.html").permitAll()
                        .requestMatchers("/swagger-resources/**", "/webjars/**", "/swagger-ui/**").permitAll()
                        .requestMatchers("/management/**",
                                "/econews/comments/replies/{parentCommentId}")
                        .hasAnyRole(ADMIN)
                        .requestMatchers("/css/**",
                                "/img/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, ECONEWS_COMMENTS)
                        .hasAnyRole(ADMIN)
                        .requestMatchers(HttpMethod.GET,
                                "/ownSecurity/verifyEmail",
                                "/ownSecurity/updateAccessToken",
                                "/ownSecurity/restorePassword",
                                "/factoftheday/",
                                "/factoftheday/all",
                                "/factoftheday/find",
                                "/factoftheday/languages",
                                "/category",
                                "/habit",
                                "/habit/{id}",
                                "/habit/{id}/shopping-list",
                                "/tags/search",
                                "/tags/v2/search",
                                "/habit/tags/all",
                                "/habit/statistic/{habitId}",
                                "/habit/statistic/assign/{habitAssignId}",
                                "/habit/statistic/todayStatisticsForAllHabitItems",
                                "/specification",
                                "/econews",
                                "/econews/newest",
                                "/econews/tags",
                                "/econews/tags/all",
                                "/econews/recommended",
                                "/econews/{id}",
                                "/econews/countLikes/{econewsId}",
                                "/econews/comments/count/comments/{ecoNewsId}",
                                "/econews/comments/count/replies/{parentCommentId}",
                                "/econews/comments/count/likes",
                                "/econews/comments/replies/active/{parentCommentId}",
                                "/econews/comments/active",
                                "/language",
                                "/search",
                                "/search/econews",
                                "/user/emailNotifications",
                                "/user/activatedUsersAmount",
                                "/user/{userId}/habit/assign",
                                "/token")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/ownSecurity/signUp",
                                "/ownSecurity/signIn",
                                "/ownSecurity/changePassword")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/event",
                                "/event/{id}",
                                "/event/attenders/{eventId}",
                                "/achievements",
                                CUSTOM_SHOPPING_LIST_ITEMS,
                                CUSTOM_SHOPPING_LIST,
                                CUSTOM_SHOPPING_LIST_URL,
                                "/custom/shopping-list-items/{userId}/{habitId}",
                                "/econews/count",
                                "/shopping-list-items",
                                "/habit/assign/allForCurrentUser",
                                "/habit/assign/active/{date}",
                                "/habit/assign/{habitAssignId}/more",
                                "/habit/assign/activity/{from}/to/{to}",
                                HABIT_ASSIGN_ID + "/active",
                                HABIT_ASSIGN_ID,
                                HABIT_ASSIGN_ID + "/all",
                                "/habit/statistic/acquired/count",
                                "/habit/statistic/in-progress/count",
                                "/facts",
                                "/facts/random/{habitId}",
                                "/facts/dayFact/{languageId}",
                                "/newsSubscriber/unsubscribe",
                                "/social-networks/image",
                                "/user",
                                "/user/shopping-list-items/habits/{habitId}/shopping-list",
                                USER_CUSTOM_SHOPPING_LIST_ITEMS,
                                "/user/{userId}/custom-shopping-list-items/available",
                                "/user/{userId}/profile/",
                                "/user/isOnline/{userId}/",
                                "/user/{userId}/profileStatistics/",
                                "/factoftheday/",
                                "/factoftheday/all",
                                "/user/shopping-list-items/{userId}/get-all-inprogress",
                                "/habit/assign/{habitAssignId}/allUserAndCustomList",
                                "/habit/assign/allUserAndCustomShoppingListsInprogress",
                                "/habit/assign/{habitAssignId}",
                                "/habit/tags/search",
                                "/habit/search",
                                "/habit/{habitId}/friends/profile-pictures",
                                "/notifications/**")
                        .hasAnyRole(USER, ADMIN, MODERATOR, UBS_EMPLOYEE)
                        .requestMatchers(HttpMethod.POST,
                                "/category",
                                "/econews",
                                "/econews/like",
                                "/econews/dislike",
                                "/econews/comments/{econewsId}",
                                "/econews/comments/like",
                                CUSTOM_SHOPPING_LIST_ITEMS,
                                "/files/image",
                                "/files/convert",
                                HABIT_ASSIGN_ID,
                                HABIT_ASSIGN_ID + "/custom",
                                "/habit/assign/{habitAssignId}/enroll/**",
                                "/habit/assign/{habitAssignId}/unenroll/{date}",
                                "/habit/statistic/{habitId}",
                                "/newsSubscriber",
                                USER_CUSTOM_SHOPPING_LIST_ITEMS,
                                USER_SHOPPING_LIST,
                                "/user/{userId}/habit",
                                "/habit/custom",
                                "/custom/shopping-list-items/{userId}/{habitId}/custom-shopping-list-items",
                                "/event",
                                "/event/attenders/{eventId}",
                                "/notifications/**")
                        .hasAnyRole(USER, ADMIN, MODERATOR, UBS_EMPLOYEE)
                        .requestMatchers(HttpMethod.PUT,
                                "/event/update",
                                "/habit/statistic/{id}",
                                "/econews/update",
                                "/ownSecurity",
                                "/user/profile",
                                HABIT_ASSIGN_ID + "/update-habit-duration",
                                "/habit/assign/{habitAssignId}/updateProgressNotificationHasDisplayed",
                                HABIT_ASSIGN_ID + "/allUserAndCustomList")
                        .hasAnyRole(USER, ADMIN, MODERATOR, UBS_EMPLOYEE)
                        .requestMatchers(HttpMethod.PATCH,
                                ECONEWS_COMMENTS,
                                CUSTOM_SHOPPING_LIST_ITEMS,
                                CUSTOM_SHOPPING_LIST_URL,
                                HABIT_ASSIGN_ID,
                                "/shopping-list-items/shoppingList/{userId}",
                                HABIT_ASSIGN_ID,
                                "/habit/assign/cancel/{habitId}",
                                USER_CUSTOM_SHOPPING_LIST_ITEMS,
                                USER_SHOPPING_LIST + "/{shoppingListItemId}/status/{status}",
                                USER_SHOPPING_LIST + "/{userShoppingListItemId}",
                                "/user/profilePicture",
                                "/user/deleteProfilePicture",
                                "/notifications/**")
                        .hasAnyRole(USER, ADMIN, MODERATOR, UBS_EMPLOYEE)
                        .requestMatchers(HttpMethod.DELETE,
                                ECONEWS_COMMENTS,
                                "/events/comments/{eventCommentId}",
                                "/event/attenders/{eventId}",
                                "/event/{eventId}",
                                "/econews/{econewsId}",
                                CUSTOM_SHOPPING_LIST_ITEMS,
                                CUSTOM_SHOPPING_LIST_URL,
                                "/favorite_place/{placeId}",
                                "/social-networks",
                                USER_CUSTOM_SHOPPING_LIST_ITEMS,
                                USER_SHOPPING_LIST + "/user-shopping-list-items")
                        .hasAnyRole(USER, ADMIN, MODERATOR, UBS_EMPLOYEE)
                        .requestMatchers(HttpMethod.GET,
                                "/newsSubscriber",
                                "/comments",
                                "/comments/{id}",
                                "/user/all",
                                "/user/roles")
                        .hasAnyRole(ADMIN, MODERATOR)
                        .requestMatchers(HttpMethod.POST,
                                "/place/filter/predicate")
                        .hasAnyRole(ADMIN, MODERATOR)
                        .requestMatchers(HttpMethod.PUT,
                                "/place/update/")
                        .hasAnyRole(ADMIN, MODERATOR)
                        .requestMatchers(HttpMethod.POST,
                                "/facts",
                                "/user/filter")
                        .hasAnyRole(ADMIN)
                        .requestMatchers(HttpMethod.PUT,
                                "/facts/{factId}")
                        .hasAnyRole(ADMIN)
                        .requestMatchers(HttpMethod.PATCH,
                                "/user",
                                "/user/status",
                                "/user/role",
                                "/user/update/role")
                        .hasAnyRole(ADMIN)
                        .requestMatchers(HttpMethod.DELETE,
                                "/facts/{factId}",
                                "/comments")
                        .hasAnyRole(ADMIN)
                        .anyRequest().hasAnyRole(ADMIN))
                .logout(logout -> logout.logoutUrl("/logout")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/management/logout", "GET"))
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .deleteCookies("accessToken")
                        .logoutSuccessUrl("/"));
        return http.build();
    }

    /**
     * Method for configure type of authentication provider.
     *
     * @param auth {@link AuthenticationManagerBuilder}
     */
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(new JwtAuthenticationProvider(jwtTool));
    }

    /**
     * Provides AuthenticationManager.
     *
     * @return {@link AuthenticationManager}
     */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
