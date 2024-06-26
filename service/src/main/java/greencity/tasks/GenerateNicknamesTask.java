package greencity.tasks;

import greencity.entity.NicknamesArchive;
import greencity.entity.ScheduledTask;
import greencity.entity.User;
import greencity.exception.exceptions.NotFoundException;
import greencity.repository.NicknamesArchiveRepo;
import greencity.repository.ScheduledTasksRepo;
import greencity.repository.UserRepo;
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

import static greencity.constant.AppConstant.SCHEDULED_TASK;

@Component
@AllArgsConstructor
@Slf4j
public class GenerateNicknamesTask implements SchedulingConfigurer {

    private TaskScheduler taskScheduler;

    private final UserRepo userRepo;
    private final ScheduledTasksRepo scheduledTasksRepo;
    private final NicknamesArchiveRepo nicknamesArchiveRepo;

    private Set<String> usedNicknames;

    private static final Random random = new Random();

    @Override
    @Transactional
    public void configureTasks(@NotNull ScheduledTaskRegistrar taskRegistrar) {
        taskScheduler.schedule(() -> {
            log.info("SCHEDULER RUNNING");
            generateNicknames();
            }, Instant.now());
    }

    @Transactional
    public void generateNicknames() {
        ScheduledTask scheduledTask = scheduledTasksRepo.findByTask(SCHEDULED_TASK).orElseThrow(() -> new NotFoundException("Task not found"));
        if(!scheduledTask.isDone()){
            log.info("TASK '" +  SCHEDULED_TASK + "' RUNNING");

            List<User> users = userRepo.findAll();

            usedNicknames.addAll(nicknamesArchiveRepo.findAll().stream().map(NicknamesArchive::getNickname).filter(Objects::nonNull).toList());

            for (User user : users) {
                if(user.getNickname() == null || user.getNickname().isEmpty()) {
                    String finalNickname = generateUniqueNickname(user.getName());
                    userRepo.updateUserNickname(user.getId(), finalNickname);
                    nicknamesArchiveRepo.save(NicknamesArchive.builder().activity(true).nickname(finalNickname).user(user).build());
                }
            }

            scheduledTasksRepo.updateScheduledTaskDone(scheduledTask.getId(), true);
        }
    }

    public String generateUniqueNickname(String username) {
        String baseNickname = username.toLowerCase().replaceAll("[^a-z0-9]", "");

        String candidateNickname = baseNickname + (random.nextInt(900) + 100);

        int suffix = 1;
        while (usedNicknames.contains(candidateNickname)) {
            candidateNickname = baseNickname + suffix;
            suffix++;
        }

        usedNicknames.add(candidateNickname);

        return candidateNickname;
    }
}
