package greencity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import greencity.constant.AppConstant;
import greencity.dto.PageableAdvancedDto;
import greencity.dto.PageableDto;
import greencity.dto.econews.AddEcoNewsDtoRequest;
import greencity.dto.econews.AddEcoNewsDtoResponse;
import greencity.dto.econews.EcoNewsDto;
import greencity.dto.econewscomment.AddEcoNewsCommentDtoRequest;
import greencity.dto.econewscomment.AddEcoNewsCommentDtoResponse;
import greencity.dto.econewscomment.EcoNewsCommentAuthorDto;
import greencity.dto.econewscomment.EcoNewsCommentDto;
import greencity.dto.event.*;
import greencity.dto.habit.*;
import greencity.dto.habitfact.*;
import greencity.dto.habitstatistic.AddHabitStatisticDto;
import greencity.dto.habitstatistic.UpdateHabitStatisticDto;
import greencity.dto.habittranslation.HabitTranslationDto;
import greencity.dto.language.LanguageDTO;
import greencity.dto.language.LanguageTranslationDTO;
import greencity.dto.language.LanguageVO;
import greencity.dto.shoppinglistitem.CustomShoppingListItemResponseDto;
import greencity.dto.shoppinglistitem.ShoppingListItemDto;
import greencity.dto.shoppinglistitem.ShoppingListItemPostDto;
import greencity.dto.shoppinglistitem.ShoppingListItemRequestDto;
import greencity.dto.tag.TagPostDto;
import greencity.dto.tag.TagTranslationVO;
import greencity.dto.tag.TagViewDto;
import greencity.dto.user.*;
import greencity.entity.*;
import greencity.enums.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.*;
import java.util.*;

public class ModelUtils {

    public static List<TagTranslationVO> getTagTranslationsVO() {
        return Arrays.asList(TagTranslationVO.builder().id(1L).name("Новини").build(),
            TagTranslationVO.builder().id(2L).name("News").build(),
            TagTranslationVO.builder().id(3L).name("Новины").build());
    }

    public static LanguageVO getLanguageVO() {
        return new LanguageVO(1L, AppConstant.DEFAULT_LANGUAGE_CODE);
    }

    public static TagPostDto getTagPostDto() {
        return new TagPostDto(TagType.ECO_NEWS, Collections.emptyList());
    }

    public static TagViewDto getTagViewDto() {
        return new TagViewDto("3", "ECO_NEWS", "News");
    }

    public static User getUser() {
        return User.builder()
            .id(1L)
            .email(TestConst.EMAIL)
            .name(TestConst.NAME)
            .role(Role.ROLE_USER)
            .lastActivityTime(LocalDateTime.now())
            .dateOfRegistration(LocalDateTime.now())
            .build();
    }

    public static UserVO getUserVO() {
        return UserVO.builder()
            .id(1L)
            .email(TestConst.EMAIL)
            .name(TestConst.NAME)
            .role(Role.ROLE_USER)
            .build();
    }

    public static HabitDto getHabitDto() {
        return HabitDto.builder()
            .id(1L)
            .image("someImageUrl")
            .complexity(2)
            .build();
    }

    public static ShoppingListItemDto getShoppingListItemDto(Long id, String text, String status) {
        return ShoppingListItemDto.builder()
                .id(id)
                .text(text)
                .status(status)
                .build();
    }

    public static EcoNewsAuthorDto getEcoNewsAuthorDto() {
        return new EcoNewsAuthorDto(1L, TestConst.NAME);
    }

    public static PageableDto<HabitDto> getPageableHabitDto(int totalElements, int currentPage, int totalPages) {
        List<HabitDto> habitList = List.of(getHabitDto());
        return new PageableDto<>(habitList, totalElements, currentPage, totalPages);
    }

    public static EcoNewsDto getEcoNewsDto() {
        return new EcoNewsDto(ZonedDateTime.of(2022, 12, 12, 12, 12, 12, 12, ZoneId.systemDefault()), null, 1L,
            "title", "text", "shortInfo", getEcoNewsAuthorDto(), null, null, 12, 12, 12);
    }

    public static AddEcoNewsDtoRequest getAddEcoNewsDtoRequest() {
        return new AddEcoNewsDtoRequest("title", "text",
            Collections.singletonList("tag"), null, null, "shortInfo");
    }

    public static AddEcoNewsDtoResponse getAddEcoNewsDtoResponse() {
        return new AddEcoNewsDtoResponse(1L, "title",
            "text", "shortInfo", EcoNewsAuthorDto.builder().id(1L).name(TestConst.NAME).build(),
            ZonedDateTime.now(), TestConst.SITE, null,
            Collections.singletonList("tag"));
    }

    public static MultipartFile getFile() {
        Path path = Paths.get("src/test/resources/test.jpg");
        String name = TestConst.IMG_NAME;
        String contentType = "photo/plain";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return new MockMultipartFile(name,
            name, contentType, content);
    }

    public static URL getUrl() throws MalformedURLException {
        return new URL(TestConst.SITE);
    }

    public static AddHabitStatisticDto addHabitStatisticDto() {
        return AddHabitStatisticDto.builder()
            .amountOfItems(5)
            .habitRate(HabitRate.DEFAULT)
            .createDate(ZonedDateTime.now())
            .build();
    }

    public static UpdateHabitStatisticDto getUpdateHabitStatisticDto() {
        return UpdateHabitStatisticDto.builder()
                .amountOfItems(5)
                .habitRate(HabitRate.DEFAULT)
                .build();
    }

    public static HabitStatistic getHabitStatistic() {
        return HabitStatistic.builder()
            .id(1L)
            .habitRate(HabitRate.DEFAULT)
            .createDate(ZonedDateTime.now())
            .amountOfItems(5)
            .habitAssign(HabitAssign.builder().id(13L).build())
            .build();
    }

    public static Category getCategory() {
        return Category.builder()
            .id(12L)
            .name("category")
            .build();
    }

    public static LocalTime getLocalTime() {
        return LocalTime.of(7, 20, 45, 342123342);
    }

    public static Specification getSpecification() {
        return Specification.builder()
            .id(1L)
            .name("specification")
            .build();
    }

    public static LanguageDTO getLanguageDTO() {
        return new LanguageDTO(1L, "en");
    }

    public static LanguageTranslationDTO getLanguageTranslationDTO() {
        return new LanguageTranslationDTO(getLanguageDTO(), "content");
    }

    public static EcoNewsComment getEcoNewsComment() {
        return EcoNewsComment.builder()
            .id(1L)
            .text("text")
            .createdDate(LocalDateTime.now())
            .modifiedDate(LocalDateTime.now())
            .user(getUser())
            .build();
    }

    public static AddEcoNewsCommentDtoRequest getAddEcoNewsCommentDtoRequest() {
        return new AddEcoNewsCommentDtoRequest("text", 0L);
    }

    public static AddEcoNewsCommentDtoResponse getAddEcoNewsCommentDtoResponse() {
        return AddEcoNewsCommentDtoResponse.builder()
            .id(getEcoNewsComment().getId())
            .author(getEcoNewsCommentAuthorDto())
            .text(getEcoNewsComment().getText())
            .modifiedDate(getEcoNewsComment().getModifiedDate())
            .build();
    }

    public static EcoNewsCommentAuthorDto getEcoNewsCommentAuthorDto() {
        return EcoNewsCommentAuthorDto.builder()
            .id(getUser().getId())
            .name(getUser().getName().trim())
            .userProfilePicturePath(getUser().getProfilePicturePath())
            .build();
    }

    public static EcoNewsCommentDto getEcoNewsCommentDto() {
        return EcoNewsCommentDto.builder()
            .id(1L)
            .modifiedDate(LocalDateTime.now())
            .author(getEcoNewsCommentAuthorDto())
            .text("text")
            .replies(0)
            .likes(0)
            .currentUserLiked(false)
            .status(CommentStatus.ORIGINAL)
            .build();
    }

    public static Principal getPrincipal() {
        return () -> "test@gmail.com";
    }

    public static UserProfilePictureDto getUserProfilePictureDto(Long id, String name, String profilePicturePath) {
        return UserProfilePictureDto.builder()
                .id(id)
                .name(name)
                .profilePicturePath(profilePicturePath)
                .build();
    }

    public static ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    public static HabitAssign getHabitAssign() {
        return HabitAssign.builder()
            .id(1L)
            .status(HabitAssignStatus.ACQUIRED)
            .createDate(ZonedDateTime.now())
            .habit(Habit.builder()
                .id(1L)
                .image("")
                .habitTranslations(Collections.singletonList(HabitTranslation.builder()
                    .id(1L)
                    .name("")
                    .description("")
                    .habitItem("")
                    .build()))
                .build())
            .user(getUser())
            .workingDays(0)
            .duration(0)
            .habitStreak(0)
            .lastEnrollmentDate(ZonedDateTime.now())
            .build();
    }

    public static HabitStatusCalendar getHabitStatusCalendar() {
        return HabitStatusCalendar.builder()
            .id(1L)
            .enrollDate(LocalDate.now()).build();
    }

    public static HabitFactVO getHabitFactVO() {
        return HabitFactVO.builder()
            .id(1L)
            .habit(HabitVO.builder()
                .id(1L)
                .image("string")
                .build())
            .translations(Collections.singletonList(HabitFactTranslationVO.builder()
                .id(1L)
                .content("content")
                .factOfDayStatus(FactOfDayStatus.POTENTIAL)
                .habitFact(null)
                .language(LanguageVO.builder()
                    .id(1L)
                    .code("ua")
                    .build())
                .build()))
            .build();
    }

    public static HabitFactPostDto getHabitFactPostDto() {
        return HabitFactPostDto.builder()
            .translations(List.of(getLanguageTranslationDTO()))
            .habit(new HabitIdRequestDto(1L))
            .build();
    }

    public static HabitFactUpdateDto getHabitFactUpdateDto() {
        return HabitFactUpdateDto.builder()
            .habit(new HabitIdRequestDto(1L))
            .translations(Collections.singletonList(
                new HabitFactTranslationUpdateDto(FactOfDayStatus.POTENTIAL, getLanguageDTO(), "")))
            .build();
    }

    public static List<LanguageTranslationDTO> getLanguageTranslationsDTOs() {
        return Arrays.asList(
            new LanguageTranslationDTO(new LanguageDTO(1L, "en"), "hello"),
            new LanguageTranslationDTO(new LanguageDTO(1L, "en"), "text"),
            new LanguageTranslationDTO(new LanguageDTO(1L, "en"), "smile"));
    }

    public static ShoppingListItemPostDto getShoppingListItemPostDto() {
        return new ShoppingListItemPostDto(getLanguageTranslationsDTOs(), new ShoppingListItemRequestDto(1L));
    }

    public static UserShoppingListItem getUserShoppingListItem() {
        return UserShoppingListItem.builder()
            .id(1L)
            .status(ShoppingListItemStatus.DONE)
            .habitAssign(HabitAssign.builder()
                .id(1L)
                .status(HabitAssignStatus.ACQUIRED)
                .habitStreak(10)
                .duration(300)
                .lastEnrollmentDate(ZonedDateTime.now())
                .workingDays(5)
                .build())
            .shoppingListItem(ShoppingListItem.builder()
                .id(1L)
                .build())
            .dateCompleted(LocalDateTime.of(2021, 2, 2, 14, 2))
            .build();
    }

    public static UserManagementDto getUserManagementDto() {
        return UserManagementDto.builder()
            .id(1L)
            .name("Username")
            .email("user@gmail.com")
            .userCredo("Credo")
            .role(Role.ROLE_ADMIN)
            .userStatus(UserStatus.ACTIVATED)
            .build();
    }

    public static HabitAssignPropertiesDto getHabitAssignPropertiesDto() {
        return HabitAssignPropertiesDto.builder()
            .defaultShoppingListItems(List.of(1L, 2L))
            .duration(20)
            .build();
    }

    public static HabitAssignCustomPropertiesDto getHabitAssignCustomPropertiesDto() {
        return HabitAssignCustomPropertiesDto.builder()
            .habitAssignPropertiesDto(getHabitAssignPropertiesDto())
            .friendsIdsList(List.of(1L, 2L))
            .build();
    }

    public static UpdateUserShoppingListDto getUpdateUserShoppingListDto() {
        return UpdateUserShoppingListDto.builder()
            .userShoppingListItemId(1L)
            .habitAssignId(1L)
            .userShoppingListAdvanceDto(List.of(UserShoppingListItemAdvanceDto.builder()
                .id(1L)
                .shoppingListItemId(1L)
                .status(ShoppingListItemStatus.INPROGRESS)
                .build()))
            .build();
    }

    public static CustomShoppingListItemResponseDto getCustomShoppingListItemResponseDto() {
        return CustomShoppingListItemResponseDto.builder()
            .id(1L)
            .status(ShoppingListItemStatus.ACTIVE)
            .text("text")
            .build();
    }

    public static UserShoppingListItemResponseDto getUserShoppingListItemResponseDto() {
        return UserShoppingListItemResponseDto.builder()
            .id(1L)
            .status(ShoppingListItemStatus.ACTIVE)
            .text("text")
            .build();
    }

    public static UserShoppingAndCustomShoppingListsDto getUserShoppingAndCustomShoppingListsDto() {
        return UserShoppingAndCustomShoppingListsDto.builder()
            .userShoppingListItemDto(List.of(getUserShoppingListItemResponseDto()))
            .customShoppingListItemDto(List.of(getCustomShoppingListItemResponseDto()))
            .build();
    }

    public static AddCustomHabitDtoRequest getAddCustomHabitDtoRequest() {
        return AddCustomHabitDtoRequest.builder()
            .complexity(2)
            .customShoppingListItemDto(List.of(
                CustomShoppingListItemResponseDto.builder()
                    .id(1L)
                    .status(ShoppingListItemStatus.ACTIVE)
                    .text("buy a shopper")
                    .build()))
            .defaultDuration(7)
            .habitTranslations(
                List.of(HabitTranslationDto.builder()
                    .description("Description")
                    .habitItem("Item")
                    .languageCode("en")
                    .name("use shopper")
                    .build()))
            .image("https://csb10032000a548f571.blob.core.windows.net/allfiles/photo_2021-06-01_15-39-56.jpg")
            .tagIds(Set.of(20L))
            .build();
    }

    public static EventCreateDtoResponse getEventCreateDtoResponse(int datesNumber, int imagesNumber) {
        var organiser = UserForEventDtoResponse.builder()
                .id(1L)
                .name("username")
                .organizerRating(1.0)
                .email("email@booble.com")
                .build();
        var createdTimestamp = LocalDateTime.of(2050, 12, 31, 9, 59);
        var address = AddressDtoResponse.builder()
                .id(1L)
                .latitude(BigDecimal.valueOf(51.523788))
                .longitude(BigDecimal.valueOf(-0.158611))
                .countryEn("United Kingdom")
                .countryUa("Сполучене Королівство")
                .regionEn("London")
                .regionUa("Лондон")
                .cityEn("London")
                .cityUa("Лондон")
                .streetEn("Baker Street")
                .streetUa("вулиця Бейкер")
                .houseNumber("221B")
                .formattedAddressEn("221B Baker St, London, UK")
                .formattedAddressUa("вулиця Бейкер, 221B, Лондон, Сполучене Королівство")
                .build();

        List<EventDateLocationDtoResponse> dates = new ArrayList<>();
        for (int i = 1; i <= datesNumber; i++) {
            var eventDateLocation = EventDateLocationDtoResponse.builder()
                    .id((long) i)
                    .eventId(1L)
                    .startTime(createdTimestamp.plusDays(i))
                    .endTime(createdTimestamp.plusDays(i).plusHours(8))
                    .onlineLink("https://example.com/online-event")
                    .address(address)
                    .build();
            dates.add(eventDateLocation);
        }

        List<AdditionalImageForEventDtoResponse> images = new ArrayList<>();
        for (int i = 1; i <= imagesNumber; i++) {
            AdditionalImageForEventDtoResponse image = AdditionalImageForEventDtoResponse.builder()
                    .id((long) i)
                    .data("https://csb14a548fF" + (i + 154) + ".blob.core.windows.net/allfiles/photo_2021-06-01_15-39-5" + (i + 11) + ".jpg")
                    .build();
            images.add(image);
        }

        EventCreateDtoResponse mainDto = EventCreateDtoResponse.builder()
                .id(1L)
                .timestamp(createdTimestamp)
                .title("New Event Title Example")
                .dates(dates)
                .organizer(organiser)
                .description("<p>Опис події, що містить кілька абзаців для перевірки.</p><p>Другий абзац з описом події.</p>")
                .isOpen(true)
                .titleImage("https://csb10032000a548f571.blob.core.windows.net/allfiles/photo_2021-06-01_15-39-56.jpg")
                .additionalImages(images)
                .build();

        return mainDto;
    }

    public static EventCreateDtoRequest getEventCreateDtoRequest(int datesNumber) {
        var createdTimestamp = LocalDateTime.of(2050, 12, 31, 9, 59);
        var address = AddressDtoRequest.builder()
                .latitude(BigDecimal.valueOf(51.523788))
                .longitude(BigDecimal.valueOf(-0.158611))
                .countryEn("United Kingdom")
                .countryUa("Сполучене Королівство")
                .regionEn("London")
                .regionUa("Лондон")
                .cityEn("London")
                .cityUa("Лондон")
                .streetEn("Baker Street")
                .streetUa("вулиця Бейкер")
                .houseNumber("221B")
                .formattedAddressEn("221B Baker St, London, UK")
                .formattedAddressUa("вулиця Бейкер, 221B, Лондон, Сполучене Королівство")
                .build();

        List<EventDateLocationDtoRequest> dates = new ArrayList<>();
        for (int i = 1; i <= datesNumber; i++) {
            var eventDateLocation = EventDateLocationDtoRequest.builder()
                    .startTime(createdTimestamp.plusDays(i))
                    .endTime(createdTimestamp.plusDays(i).plusHours(8))
                    .onlineLink("https://example.com/online-event")
                    .address(address)
                    .build();
            dates.add(eventDateLocation);
        }

        EventCreateDtoRequest mainDto = EventCreateDtoRequest.builder()
                .title("New Event Title Example")
                .dates(dates)
                .description("<p>Опис події, що містить кілька абзаців для перевірки.</p><p>Другий абзац з описом події.</p>")
                .isOpen(true)
                .build();

        return mainDto;
    }

    public static PageableAdvancedDto<EventCreateDtoResponse> getPageableAdvancedDtoForEventCreateDtoResponse(
            int datesNumber,
            int imagesNumber,
            long totalElements,
            int currentPage,
            int totalPages,
            int number,
            boolean hasPrevious,
            boolean hasNext,
            boolean first,
            boolean last) {
        return new PageableAdvancedDto<>(
                Collections.singletonList(getEventCreateDtoResponse(datesNumber, imagesNumber)),
                totalElements,
                currentPage,
                totalPages,
                number,
                hasPrevious,
                hasNext,
                first,
                last);
    }
}