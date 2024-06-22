package greencity.validator;

import greencity.annotations.ValidEventCommentRequest;
import greencity.dto.eventcomment.AddEventCommentDtoRequest;
import greencity.dto.eventcomment.UpdateEventCommentDtoRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

@Slf4j
public class ValidEventCommentDtoRequest implements ConstraintValidator<ValidEventCommentRequest, Object> {

    private boolean isAddEventCommentDtoRequest(Object obj) {
        return obj instanceof AddEventCommentDtoRequest;
    }

    private boolean isUpdateEventCommentDtoRequest(Object obj) {
        return obj instanceof UpdateEventCommentDtoRequest;
    }

    static List<String> badWordsList = new ArrayList<>();
    static List<String> partsOfNormalWords = new ArrayList<>();

    @Override
    public void initialize(ValidEventCommentRequest constraintAnnotation) {
        try {
            String uri = "https://docs.google.com/spreadsheets/d/1IeOyqg48XGBVv7TP9tZU5asO2xT2JKQdpFCJZ3WDhvU/export?format=csv";
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URI(uri).toURL().openConnection().getInputStream()));
            String line;
            while((line = reader.readLine()) != null) {
                processLine(line);
            }
        } catch (IOException | URISyntaxException e ) {
            log.error("Error while reading file", e);
        }
    }

    private void processLine(String line) {
        try {
            String[] content;
            content = line.split(",");
            String word = content[0];
            badWordsList.add(word);

            if (content.length > 1) {
                String exclusionWord = content[1];
                partsOfNormalWords.add(exclusionWord);
            }
        } catch (Exception e) {
            log.error("Error while reading file", e);
        }
    }

    public static List<String> badWordsFound(String input) {
        input = replaceSymbols(input);

        ArrayList<String> badWordsFound = new ArrayList<>();
        input = input.toLowerCase().replaceAll("[^a-z\\s]", "");

        String[] words = input.split("\\s+");

        for (String word : words) {
            if (!word.trim().isEmpty()) {
                for(String badWord : badWordsList) {
                    if (word.contains(badWord) && !isPartOfNormalWord(word, badWord)) {
                        badWordsFound.add(word);
                    }
                }
            }
        }

        return badWordsFound;
    }

    private static boolean isPartOfNormalWord(String word, String badWord) {
        for(String partOfNormalWord : partsOfNormalWords) {
            if(partOfNormalWord.equals(badWord) && word.length() > badWord.length()) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean isValid(Object request, ConstraintValidatorContext constraintValidatorContext) {
        if (isAddEventCommentDtoRequest(request)) {
            AddEventCommentDtoRequest addRequest = (AddEventCommentDtoRequest) request;
            return badWordsFound(addRequest.getText()).isEmpty();

        } else if (isUpdateEventCommentDtoRequest(request)) {
            UpdateEventCommentDtoRequest updateRequest = (UpdateEventCommentDtoRequest) request;
            return badWordsFound(updateRequest.getText()).isEmpty();
        }

        return false;
    }

    private static String replaceSymbols(String input) {
        input = input.replace("1","i");
        input = input.replace("!","i");
        input = input.replace("3","e");
        input = input.replace("4","a");
        input = input.replace("@","a");
        input = input.replace("5","s");
        input = input.replace("$","s");
        input = input.replace("7","t");
        input = input.replace("0","o");
        input = input.replace("9","g");

        return input;
    }
}
