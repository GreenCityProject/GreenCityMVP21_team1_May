package greencity.service;

import greencity.annotations.RatingCalculationEnum;
import greencity.constant.ErrorMessage;
import greencity.dto.PageableDto;
import greencity.dto.econews.EcoNewsVO;
import greencity.dto.econewscomment.*;
import greencity.dto.notifications.CreateNotificationDto;
import greencity.dto.user.UserVO;
import greencity.entity.EcoNews;
import greencity.entity.EcoNewsComment;
import greencity.entity.User;
import greencity.enums.Role;
import greencity.exception.exceptions.BadRequestException;
import greencity.exception.exceptions.NotFoundException;
import greencity.exception.exceptions.UserHasNoPermissionToAccessException;
import greencity.repository.EcoNewsCommentRepo;
import greencity.repository.EcoNewsRepo;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import static greencity.constant.AppConstant.AUTHORIZATION;

@Service
@AllArgsConstructor
public class EcoNewsCommentServiceImpl implements EcoNewsCommentService {
    private EcoNewsCommentRepo ecoNewsCommentRepo;
    private EcoNewsService ecoNewsService;
    private NotificationService notificationService;
    private ModelMapper modelMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final greencity.rating.RatingCalculation ratingCalculation;
    private final HttpServletRequest httpServletRequest;
    private final EcoNewsRepo ecoNewsRepo;

    /**
     * Method to save {@link greencity.entity.EcoNewsComment}.
     *
     * @param econewsId                   id of {@link greencity.entity.EcoNews} to
     *                                    which we save comment.
     * @param addEcoNewsCommentDtoRequest dto with
     *                                    {@link greencity.entity.EcoNewsComment}
     *                                    text, parentCommentId.
     * @param userVO                      {@link User} that saves the comment.
     * @return {@link AddEcoNewsCommentDtoResponse} instance.
     */

    @Override
    public AddEcoNewsCommentDtoResponse save(Long econewsId, AddEcoNewsCommentDtoRequest addEcoNewsCommentDtoRequest,
        UserVO userVO) {
        EcoNewsVO ecoNewsVO = ecoNewsService.findById(econewsId);
        EcoNewsComment ecoNewsComment = modelMapper.map(addEcoNewsCommentDtoRequest, EcoNewsComment.class);
        ecoNewsComment.setUser(modelMapper.map(userVO, User.class));
        ecoNewsComment.setEcoNews(modelMapper.map(ecoNewsVO, EcoNews.class));
        if (addEcoNewsCommentDtoRequest.getParentCommentId() != 0) {
            EcoNewsComment parentComment =
                ecoNewsCommentRepo.findById(addEcoNewsCommentDtoRequest.getParentCommentId()).orElseThrow(
                    () -> new BadRequestException(ErrorMessage.COMMENT_NOT_FOUND_EXCEPTION));
            if (parentComment.getParentComment() == null) {
                ecoNewsComment.setParentComment(parentComment);
                sendNotification(userVO, parentComment);
            } else {
                throw new BadRequestException(ErrorMessage.CANNOT_REPLY_THE_REPLY);
            }
        }
        String accessToken = httpServletRequest.getHeader(AUTHORIZATION);
        CompletableFuture.runAsync(
            () -> ratingCalculation.ratingCalculation(RatingCalculationEnum.ADD_COMMENT, userVO, accessToken));
        return modelMapper.map(ecoNewsCommentRepo.save(ecoNewsComment), AddEcoNewsCommentDtoResponse.class);
    }

    private void sendNotification(UserVO currentUser, EcoNewsComment parentComment) {
        EcoNews ecoNews = ecoNewsRepo.findById(parentComment.getEcoNews().getId()).orElseThrow();
        CreateNotificationDto notificationDto = CreateNotificationDto.builder()
                .userId(parentComment.getUser().getId())
                .senderId(currentUser.getId())
                .section("GreenCity")
                .title("Your comment was replied")
                .message(String.format("%s replied to your comment on the news %s.", currentUser.getFirstName(), ecoNews.getTitle()))
                .build();
        notificationService.save(notificationDto);
    }

    /**
     * Method returns all comments to certain ecoNews specified by ecoNewsId.
     *
     * @param userVO    current {@link User}
     * @param ecoNewsId specifies {@link greencity.entity.EcoNews} to which we
     *                  search for comments
     * @return all comments to certain ecoNews specified by ecoNewsId.
     */
    @Override
    public PageableDto<EcoNewsCommentDto> findAllComments(Pageable pageable, UserVO userVO, Long ecoNewsId) {
        ecoNewsService.findById(ecoNewsId);
        Page<EcoNewsComment> pages = ecoNewsCommentRepo.findAllByParentCommentIsNullAndEcoNewsIdOrderByCreatedDateDesc(
            pageable, ecoNewsId);
        List<EcoNewsCommentDto> ecoNewsCommentDtos = pages
            .stream()
            .map(comment -> {
                comment.setCurrentUserLiked(comment.getUsersLiked().stream()
                    .anyMatch(u -> u.getId().equals(userVO.getId())));
                return comment;
            })
            .map(ecoNewsComment -> modelMapper.map(ecoNewsComment, EcoNewsCommentDto.class))
            .map(comment -> {
                comment.setReplies(ecoNewsCommentRepo.countByParentCommentId(comment.getId()));
                return comment;
            })
            .collect(Collectors.toList());

        return new PageableDto<>(
            ecoNewsCommentDtos,
            pages.getTotalElements(),
            pages.getPageable().getPageNumber(),
            pages.getTotalPages());
    }

    /**
     * Method returns all replies to certain comment specified by parentCommentId.
     *
     * @param parentCommentId specifies {@link greencity.entity.EcoNewsComment} to
     *                        which we search for replies
     * @param userVO          current {@link User}
     * @return all replies to certain comment specified by parentCommentId.
     */
    @Override
    public PageableDto<EcoNewsCommentDto> findAllReplies(Pageable pageable, Long parentCommentId, UserVO userVO) {
        Page<EcoNewsComment> pages = ecoNewsCommentRepo
            .findAllByParentCommentIdOrderByCreatedDateDesc(pageable, parentCommentId);
        List<EcoNewsCommentDto> ecoNewsCommentDtos = pages
            .stream()
            .map(comment -> {
                comment.setCurrentUserLiked(comment.getUsersLiked().stream()
                    .anyMatch(u -> u.getId().equals(userVO.getId())));
                return comment;
            })
            .map(ecoNewsComment -> modelMapper.map(ecoNewsComment, EcoNewsCommentDto.class))
            .collect(Collectors.toList());

        return new PageableDto<>(
            ecoNewsCommentDtos,
            pages.getTotalElements(),
            pages.getPageable().getPageNumber(),
            pages.getTotalPages());
    }

    /**
     * Method to mark {@link greencity.entity.EcoNewsComment} specified by id as
     * deleted.
     *
     * @param id     of {@link greencity.entity.EcoNewsComment} to delete.
     * @param userVO current {@link User} that wants to delete.
     */
    @Override
    public void deleteById(Long id, UserVO userVO) {
        EcoNewsComment comment = ecoNewsCommentRepo.findById(id)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.COMMENT_NOT_FOUND_EXCEPTION));

        if (userVO.getRole() != Role.ROLE_ADMIN && !userVO.getId().equals(comment.getUser().getId())) {
            throw new UserHasNoPermissionToAccessException(ErrorMessage.USER_HAS_NO_PERMISSION);
        }
        if (comment.getComments() != null) {
            comment.getComments().forEach(c -> c.setDeleted(true));
        }
        comment.setDeleted(true);
        String accessToken = httpServletRequest.getHeader(AUTHORIZATION);
        CompletableFuture.runAsync(
            () -> ratingCalculation.ratingCalculation(RatingCalculationEnum.DELETE_COMMENT, userVO, accessToken));
        ecoNewsCommentRepo.save(comment);
    }

    /**
     * Method to change the existing {@link greencity.entity.EcoNewsComment}.
     *
     * @param text   new text of {@link greencity.entity.EcoNewsComment}.
     * @param id     to specify {@link greencity.entity.EcoNewsComment} that user
     *               wants to change.
     * @param userVO current {@link User} that wants to change.
     */
    @Override
    @Transactional
    public void update(String text, Long id, UserVO userVO) {
        EcoNewsComment comment = ecoNewsCommentRepo.findById(id)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.COMMENT_NOT_FOUND_EXCEPTION));
        if (!userVO.getId().equals(comment.getUser().getId())) {
            throw new BadRequestException(ErrorMessage.NOT_A_CURRENT_USER);
        }
        comment.setText(text);
        ecoNewsCommentRepo.save(comment);
    }

    /**
     * Method to like or dislike {@link greencity.entity.EcoNewsComment} specified
     * by id.
     *
     * @param id     of {@link greencity.entity.EcoNewsComment} to like/dislike.
     * @param userVO current {@link User} that wants to like/dislike.
     */
    @Override
    public void like(Long id, UserVO userVO) {
        EcoNewsComment comment = ecoNewsCommentRepo.findById(id)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.COMMENT_NOT_FOUND_EXCEPTION));
        EcoNewsCommentVO ecoNewsCommentVO = modelMapper.map(comment, EcoNewsCommentVO.class);
        if (comment.getUsersLiked().stream()
            .anyMatch(user -> user.getId().equals(userVO.getId()))) {
            ecoNewsService.unlikeComment(userVO, ecoNewsCommentVO);
        } else {
            ecoNewsService.likeComment(userVO, ecoNewsCommentVO);
        }
        ecoNewsCommentRepo.save(modelMapper.map(ecoNewsCommentVO, EcoNewsComment.class));
    }

    /**
     * Method returns count of likes to certain
     * {@link greencity.entity.EcoNewsComment} specified by id.
     *
     * @param amountCommentLikesDto dto with id and count likes for comments.
     */
    @Override
    @Transactional
    public void countLikes(AmountCommentLikesDto amountCommentLikesDto) {
        EcoNewsComment comment = ecoNewsCommentRepo.findById(amountCommentLikesDto.getId()).orElseThrow(
            () -> new BadRequestException(ErrorMessage.COMMENT_NOT_FOUND_EXCEPTION));
        boolean isLiked = comment.getUsersLiked().stream().map(User::getId)
            .anyMatch(x -> x.equals(amountCommentLikesDto.getUserId()));
        amountCommentLikesDto.setLiked(isLiked);
        int size = comment.getUsersLiked().size();
        amountCommentLikesDto.setAmountLikes(size);
        messagingTemplate
            .convertAndSend("/topic/" + amountCommentLikesDto.getId() + "/comment", amountCommentLikesDto);
    }

    /**
     * Method to count replies to certain {@link greencity.entity.EcoNewsComment}.
     *
     * @param id specifies parent comment to all replies
     * @return amount of replies
     */
    @Override
    public int countReplies(Long id) {
        if (ecoNewsCommentRepo.findById(id).isEmpty()) {
            throw new NotFoundException(ErrorMessage.COMMENT_NOT_FOUND_EXCEPTION);
        }
        return ecoNewsCommentRepo.countByParentCommentId(id);
    }

    /**
     * Method to count not deleted comments to certain
     * {@link greencity.entity.EcoNews}.
     *
     * @param ecoNewsId to specify {@link greencity.entity.EcoNews}
     * @return amount of comments
     */
    @Override
    public int countOfComments(Long ecoNewsId) {
        // Retrieve the EcoNews object by its ID
        EcoNews ecoNews = ecoNewsRepo.findById(ecoNewsId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ECO_NEWS_NOT_FOUND_BY_ID + ecoNewsId));

        // Pass the ID of the EcoNews object to the repository method
        return ecoNewsCommentRepo.countEcoNewsCommentByEcoNews(ecoNews.getId());
    }

    /**
     * Method to get all active comments to {@link greencity.entity.EcoNews}
     * specified by ecoNewsId.
     *
     * @param pageable  page of news.
     * @param ecoNewsId specifies {@link greencity.entity.EcoNews} to which we
     *                  search for comments
     * @return all active comments to certain ecoNews specified by ecoNewsId.
     * @author Taras Dovganyuk
     */
    @Override
    public PageableDto<EcoNewsCommentDto> getAllActiveComments(Pageable pageable, UserVO userVO, Long ecoNewsId) {
        Page<EcoNewsComment> pages =
            ecoNewsCommentRepo
                .findAllByParentCommentIsNullAndDeletedFalseAndEcoNewsIdOrderByCreatedDateDesc(pageable, ecoNewsId);
        UserVO user = userVO == null ? UserVO.builder().build() : userVO;
        List<EcoNewsCommentDto> ecoNewsCommentDtos = pages
            .stream()
            .map(comment -> {
                comment.setCurrentUserLiked(comment.getUsersLiked().stream()
                    .anyMatch(u -> u.getId().equals(user.getId())));
                return comment;
            })
            .map(ecoNewsComment -> modelMapper.map(ecoNewsComment, EcoNewsCommentDto.class))
            .map(comment -> {
                comment.setReplies(ecoNewsCommentRepo.countByParentCommentId(comment.getId()));
                return comment;
            })
            .collect(Collectors.toList());

        return new PageableDto<>(
            ecoNewsCommentDtos,
            pages.getTotalElements(),
            pages.getPageable().getPageNumber(),
            pages.getTotalPages());
    }

    /**
     * Method returns all replies to certain comment specified by parentCommentId.
     *
     * @param parentCommentId specifies {@link greencity.entity.EcoNewsComment} to
     *                        which we search for replies
     * @param userVO          current {@link User}
     * @return all replies to certain comment specified by parentCommentId.
     * @author Taras Dovganyuk
     */
    @Override
    public PageableDto<EcoNewsCommentDto> findAllActiveReplies(Pageable pageable, Long parentCommentId, UserVO userVO) {
        Page<EcoNewsComment> pages = ecoNewsCommentRepo
            .findAllByParentCommentIdAndDeletedFalseOrderByCreatedDateDesc(pageable, parentCommentId);
        UserVO user = userVO == null ? UserVO.builder().build() : userVO;
        List<EcoNewsCommentDto> ecoNewsCommentDtos = pages
            .stream()
            .map(comment -> {
                comment.setCurrentUserLiked(comment.getUsersLiked().stream()
                    .anyMatch(u -> u.getId().equals(user.getId())));
                return comment;
            })
            .map(ecoNewsComment -> modelMapper.map(ecoNewsComment, EcoNewsCommentDto.class))
            .collect(Collectors.toList());

        return new PageableDto<>(
            ecoNewsCommentDtos,
            pages.getTotalElements(),
            pages.getPageable().getPageNumber(),
            pages.getTotalPages());
    }
}
