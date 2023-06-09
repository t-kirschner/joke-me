package com.sqs.jokeapplication;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

// connection to database. Saves and fetches jokes to and from h2 database.
@Service
public class JokeDatabase {
    private static final Logger logger = LoggingManager.getLogger();

    @Autowired
    private JokeRepository repository;
    JokeApi api = new JokeApi();

    // triggered when application starts. Initially loads 20 jokes in database.
    @PostConstruct
    private void initJokes() {
        loadJokesFromApi(Language.ENGLISH);
        loadJokesFromApi(Language.GERMAN);
    }

    // loads 10 jokes with specific language from joke api in database when needed
    public boolean loadJokesFromApi(Language language) {
        ArrayList<Joke> jokeArrayList = new ArrayList<>();
        String response;

        for (int i = 0; i < 10; i++) {
            response = api.getApiResponse(language);
            if (response == null) {
                return false;
            } else {
                jokeArrayList.add(new Joke(response, language));
            }
        }

        try {
            repository.saveAll(jokeArrayList);
            System.out.println("reloaded jokes");
        } catch (Exception e) {
            logger.log(Level.WARNING, "Joke Database Error", e);
            return false;
        }

        return true;
    }

    // fetches a requested joke from database
    public String getJokeFromDatabase(String userInput) {
        Language language;
        if (userInput.contains("1")) {
            language = Language.ENGLISH;
        } else {
            language = Language.GERMAN;
        }

        Joke joke = repository.findFirstByLanguage(language.toString());
        if (joke == null) {
            return null;
        }

        try {
            repository.delete(joke);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Joke Database Error", e);
            return null;
        }

        return joke.getJoke();
    }

    // counts how many jokes from specific language remain in database
    public long getCountByLanguage(Language language) {
        return repository.countByLanguage(language.toString());
    }

    public void setRepository(JokeRepository repository) {
        this.repository = repository;
    }

    public void setApi(JokeApi api) {
        this.api = api;
    }
}


