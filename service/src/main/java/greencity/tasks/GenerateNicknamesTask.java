package greencity.tasks;

import greencity.entity.User;
import greencity.repository.UserRepo;
import greencity.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;

@Component
@AllArgsConstructor
@Slf4j
public class GenerateNicknamesTask implements SchedulingConfigurer {

    private TaskScheduler taskScheduler;

    private final UserRepo userRepo;

    private Set<String> usedNicknames;

    @Override
    @Transactional
    public void configureTasks(@NotNull ScheduledTaskRegistrar taskRegistrar) {
        taskScheduler.schedule(() -> {
            log.info("TASK RUNNING");
            generateNicknames();
            }, Instant.now());
    }

    @Transactional
    public void generateNicknames() {
        List<User> users = userRepo.findAll();

//        for (User user : users) {
//            userRepo.updateUserNickname(user.getId(), null);
//        }

        usedNicknames.addAll(users.stream().map(User::getNickname).filter(Objects::nonNull).toList());

        for (User user : users) {
            if(user.getNickname() == null || user.getNickname().isEmpty()) {
                userRepo.updateUserNickname(user.getId(), generateUniqueNickname(user.getName()));
            }
        }
    }

    public String generateUniqueNickname(String username) {
        String baseNickname = username.toLowerCase().replaceAll("[^a-z0-9]", "");

        // todo
        String candidateNickname = baseNickname + (new Random().nextInt(900) + 100);

        int suffix = 1;
        while (usedNicknames.contains(candidateNickname)) {
            candidateNickname = baseNickname + suffix;
            suffix++;
        }

        usedNicknames.add(candidateNickname);

        return candidateNickname;
    }
}
